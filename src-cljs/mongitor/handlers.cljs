(ns mongitor.handlers
    (:require [re-frame.core :as re-frame]
              [ajax.core :as ajx]
              [mongitor.db :as db]))

(defn kill-op [opid]
  (ajx/ajax-request {:uri (str "/ops/" opid)
                     :method "DELETE"
                     :handler #(re-frame/dispatch [:kill-op-ok %1])
                     :error-handler #(re-frame/dispatch [:kill-op-not-ok %1])
                     :format (ajx/json-request-format)
                     :response-format (ajx/json-response-format {:keywords? true})}))

(defn get-ops []
  (ajx/ajax-request {:uri "/ops"
                     :method "GET"
                     :handler #(re-frame/dispatch [:get-ops-ok %1])
                     :error-handler #(re-frame/dispatch [:get-ops-not-ok %1])
                     :format (ajx/json-request-format)
                     :response-format (ajx/json-response-format {:keywords? true})}))

(defn handle-get-ops
  [app-state _]
  (get-ops)
  app-state)

(defn handle-kill-op
  [app-state [_ opid]]
  (kill-op opid)
  app-state)

(defn handle-log-get-ops-ok
  [app-state [_ timestamp]]
  (assoc app-state :event-log
         (assoc (:event-log app-state) timestamp "get ops ok!")))

(defn handle-current-ops
  [app-state [_ ops]]
  (assoc app-state :current-ops ops))

(defn handle-get-ops-ok
  [app-state [_ [_ response]]]
  (re-frame/dispatch [:current-ops (:current-ops response)])
  (re-frame/dispatch [:log-get-ops-ok (:timestamp response)])
  app-state)

(defn handle-get-ops-not-ok
  [app-state [_ response]]
  (assoc app-state :status-message "Failed to retrieve ops."))

(defn handle-kill-op-ok
  [app-state [_ [_ response]]]
  (re-frame/dispatch
    [:log-kill-op-ok
     (first (drop-while #(not (= (:opid %) (:opid response))) (:current-ops app-state)))
     (:timestamp response)])
  (re-frame/dispatch [:get-ops])
  app-state)

(defn handle-log-kill-op-ok
  [app-state [_ op timestamp]]
  (assoc app-state :event-log
         (assoc (:event-log app-state) timestamp (str "OK kill op: " op))))

(re-frame/register-handler
  :kill-op-ok
  handle-kill-op-ok)

(re-frame/register-handler
  :kill-op-not-ok
  (fn [app-state _]
    (re-frame/dispatch [:get-ops])
    (assoc app-state :status-message "Failed to kill op.")))

(re-frame/register-handler
  :log-kill-op-ok
  handle-log-kill-op-ok)

(re-frame/register-handler
  :kill-op
  handle-kill-op)

(re-frame/register-handler
  :log-get-ops-ok
  handle-log-get-ops-ok)

(re-frame/register-handler
  :current-ops
  handle-current-ops)

(re-frame/register-handler
  :get-ops-ok
  handle-get-ops-ok)

(re-frame/register-handler
  :get-ops-not-ok
  handle-get-ops-not-ok)

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/app-db))

(re-frame/register-handler
  :get-ops
  handle-get-ops)
