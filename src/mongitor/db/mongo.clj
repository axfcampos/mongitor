(ns mongitor.db.mongo
  (:require [monger.core :as mg]
            [monger.conversion :as mcv]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [environ.core :refer [env]]))

(defonce db
  (delay
    (let [uri (:database-uri env)
          {:keys [conn db]} (mg/connect-via-uri uri)]
      db)))

(defonce ops-collection "$cmd.sys.inprog")
(defonce kill-ops-collection "$cmd.sys.killop")

(defn get-current-ops []
  (:inprog (mcv/from-db-object (mc/find-one @db ops-collection {}) true)))

(defn kill-op [id]
  (mcv/from-db-object (mc/find-one @db kill-ops-collection {:op id}) true))
