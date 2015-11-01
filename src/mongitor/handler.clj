(ns mongitor.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [mongitor.layout :refer [error-page]]
            [mongitor.routes.home :refer [home-routes]]
            [mongitor.routes.ops :refer [ops-routes]]
            [mongitor.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     (if (env :dev) :trace :info)
     :appenders {:rotor (rotor/rotor-appender
                          {:path "mongitor.log"
                           :max-size (* 512 1024)
                           :backlog 10})}})

  (if (env :dev) (parser/cache-off!))
  (timbre/info (str
                 "\n-=[mongitor started successfully"
                 (when (env :dev) " using the development profile")
                 "]=-")))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "mongitor is shutting down...")
  (timbre/info "shutdown complete!"))

(def app-routes
  (routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
;    home-routes ; **NO CSRF PROTECTION**
;    (wrap-routes #'ops-routes middleware/wrap-csrf)
    ops-routes
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
