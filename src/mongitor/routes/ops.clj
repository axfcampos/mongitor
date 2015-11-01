(ns mongitor.routes.ops
  (:require [compojure.core :refer :all]
            [clj-time.core :as t]
            [mongitor.db.mongo :as mongo]))

(defn ops []
  (println "GET: /ops")
  {:body
   {:timestamp (str (t/now))
    :current-ops (mongo/get-current-ops)}})

(defn del-op [id]
  (println (str "DETELE: /ops/"id))
  (mongo/kill-op id)
  {:body
   {:timestamp (str (t/now))}})

(defroutes ops-routes
  (DELETE "/ops/:id" [id] (del-op id))
  (GET "/ops" [] (ops)))
