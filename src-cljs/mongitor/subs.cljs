(ns mongitor.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
  :event-log
  (fn [db]
    (reaction (:event-log @db))))

(re-frame/register-sub
 :current-ops
 (fn [db]
   (reaction (:current-ops @db))))
