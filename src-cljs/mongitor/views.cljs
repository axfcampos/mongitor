(ns mongitor.views
    (:require [re-frame.core :as re-frame]))

(def view-refresh-button
  [:button {:on-click #(re-frame/dispatch [:get-ops])} "Refresh"])

(defn kill-op? [opid]
  (let [kill? (js/confirm)]
    (if kill?
      (re-frame/dispatch [:kill-op opid]))))

(defn kill-op-button
  [opid]
  [:button {:on-click #(kill-op? opid)} "Kill Op"])

(defn- p-last-n-pairs
  [n s]
  (for [[k v] (take n (into (sorted-map-by >) s))]
    ^{:key k} [:p (str k " -> " v)]))

(defn- p-current-ops
  [current-ops]
  (enable-console-print!)
  (for [op current-ops]
    ^{:key (:opid op)} [:p (kill-op-button (:opid op)) (str op)]))

(defn main-panel []
  (let [current-ops (re-frame/subscribe [:current-ops])
        event-log (re-frame/subscribe [:event-log])]
    (fn []
      [:div
       [:div view-refresh-button]
       [:div {:class "event-log"} (p-last-n-pairs 30 @event-log)]
       [:div (p-current-ops @current-ops)]])))
