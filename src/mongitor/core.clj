(ns mongitor.core
  (:require [mongitor.handler :refer [app init destroy]]
            [aleph.http :as http]
            [clojure.tools.nrepl.server :as nrepl]
            [taoensso.timbre :as timbre]
            [cheshire.generate :refer [add-encoder encode-str]]
            [environ.core :refer [env]])
  (:gen-class))

(add-encoder org.bson.types.ObjectId encode-str)
(add-encoder org.bson.types.BSONTimestamp encode-str)

(defonce nrepl-server (atom nil))

(defn parse-port [port]
  (when port
    (cond
      (string? port) (Integer/parseInt port)
      (number? port) port
      :else          (throw (Exception. (str "invalid port value: " port))))))

(defn stop-nrepl []
  (when-let [server @nrepl-server]
    (nrepl/stop-server server)))

(defn start-nrepl
  "Start a network repl for debugging when the :nrepl-port is set in the environment."
  []
  (if @nrepl-server
    (timbre/error "nREPL is already running!")
    (when-let [port (env :nrepl-port)]
      (try
        (->> port
             (parse-port)
             (nrepl/start-server :port)
             (reset! nrepl-server))
        (timbre/info "nREPL server started on port" port)
        (catch Throwable t
          (timbre/error t "failed to start nREPL"))))))

(defn http-port [port]
  (parse-port (or port (env :port) 3000)))

(defn stop-app []
  (stop-nrepl)
  (destroy)
  (shutdown-agents))

(defn start-app
  "e.g. lein run 3000"
  [[port]]
  (let [port (http-port port)]
    (try
      (init)
      (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app))
      (start-nrepl)
      (http/start-server
        app
        {:port port})
      (timbre/info "server started on port:" port)
      (catch Throwable t
        (timbre/error t (str "server failed to start on port: " port))))))

(defn -main [& args]
  (start-app args))
