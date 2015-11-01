(ns mongitor.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [mongitor.handlers]
            [mongitor.subs]
            [mongitor.views :as views]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init! []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
