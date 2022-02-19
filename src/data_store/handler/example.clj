(ns data-store.handler.example
  (:require
   [integrant.core :as ig]
   [reitit.ring :as ring]
   [muuntaja.core :as m]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [ring.util.response :as res]))

(comment
  (res/status {})
  
  (res/created {:body "created"}) 
  (res/response {:body "ok"})
  (res/content-type {} "application/json")
  )

(defn ok [_]
  (res/response "ok"))

(defn json [_]
  (-> {:created "2022-1-1", :d1 1, :d2 2}
      (res/response))  
  )

(defn created [_]
  (res/created "" "created"))

(defn nocontent [_]
  {:status 204
   :body "no content"})

(defmethod ig/init-key :data-store.handler/example [_ options]
  (ring/ring-handler
   (ring/router
    [["/" ok]
     ["/channels" {:get ok
                   :post created}]
     ["/channels/:channel-id/data" {:get json`
                                    :post created
                                    :delete nocontent}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))
