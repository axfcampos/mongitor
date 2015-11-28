(ns mongitor.views
    (:require [re-frame.core :as re-frame]))

(def view-refresh-button
  [:button {:class "refresh-button" :on-click #(re-frame/dispatch [:get-ops])} "Retrieve Current Operations"])

(defn kill-op? [opid]
  (let [kill? (js/confirm)]
    (if kill?
      (re-frame/dispatch [:kill-op opid]))))

(defn kill-op-button
  [opid]
  [:button {:class "kill-button" :on-click #(kill-op? opid)} "Kill?"])

(defn- event-table [size elements]
  [:table {:class "event-table"}
   (for [[k v] (take size (into (sorted-map-by >) elements))]
     ^{:key k} [:tr {:class "event-tr"}
                [:td {:class "time-cell"} k]
                (if (= (:result v) :ok)
                  [:td {:class "ok-result-cell"} (:message v)]
                  [:td {:class "error-result-cell"} (:message v)])])])

(defn- ops-table [ops]
  [:table {:class "ops-table"}
   (for [op ops]
     ^{:key (:opid op)} [:tr {:class "op-tr"}
                         [:td {:class "cell"} (kill-op-button (:opid op))]
                         [:td {:class "cell"} (str op)]])])

(defn main-panel []
  (let [current-ops (re-frame/subscribe [:current-ops])
        event-log (re-frame/subscribe [:event-log])]
    (fn []
      [:div
       [:div {:id "refresher"} view-refresh-button]
       [:div {:id "events"} (event-table 30 @event-log)]
       [:div {:id "seperator"}]
       [:div {:id "ops"} (ops-table @current-ops)]])))
