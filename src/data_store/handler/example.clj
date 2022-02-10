(ns data-store.handler.example
  (:require
   [integrant.core :as ig]
   [reitit.ring :as ring]
   [muuntaja.core :as m]
   [reitit.ring.middleware.muuntaja :as muuntaja]))

(defn ok [_]
  {:status 200
   :body "ok"})

(defmethod ig/init-key :data-store.handler/example [_ options]
  (ring/ring-handler
   (ring/router
    ["/"
     ["example" ok]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}}))
  )
