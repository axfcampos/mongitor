(ns mongitor.routes.ops
  (:require [compojure.core :refer :all]
            [clj-time.core :as t]
            [mongitor.db.mongo :as mongo]))

(defn ops []
  (println "GET: /ops")
  {:body
   {:time (str (t/now))
    :current-ops (mongo/get-current-ops)}})


(defmulti del-op-resp #((if (contains? %2 :info) :info :err)))
(defmethod del-op-resp :info
  [id result]
  {:status 200
   :body {:time (str (t/now))
          :message (:info result)
          :opid (read-string id)}})
(defmethod del-op-resp :err
  [id result]
  {:status 400
   :body {:time (str (t/now))
          :message (:err result)
          :opid (read-string id)}})

(defn del-op [id]
  (println (str "DETELE: /ops/"id))
  (del-op-resp id (mongo/kill-op id)))

(defroutes ops-routes
  (DELETE "/ops/:id" [id] (del-op id))
  (GET "/ops" [] (ops)))
