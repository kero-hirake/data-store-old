(ns data-store.handler.example
  (:require
   [integrant.core :as ig]
   [reitit.ring :as ring]
   [muuntaja.core :as m]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [ring.util.response :as res]
   [reitit.ring.middleware.parameters :as params]))

(comment
  (res/status {})

  (res/created {:body "created"})
  (res/response {:body "ok"})
  (res/content-type {} "application/json"))

(def data-list
  (atom
   [{:created "2022-1-1"
     :d1 1
     :d2 2}
    {:created "2022-1-2"
     :d1 2
     :d2 4}]))

(defn set-data [data]
  (swap! data-list conj data))

(defn get-data-by-date [date]
  (first (filter #(= (:created %) date) @data-list))
  )
(defn get-data []
  @data-list)

(comment
  (get-data)
  (set-data {:created "2022-1-3", :d1 4, :2 3})
  (get-data-by-date "2022-1-3")
  )

(defn ok [_]
  (res/response "ok")) 

(defn json [{{:strs [date]} :query-params}]
  (-> (get-data-by-date date)
      (res/response)))

(comment
  data-list
  (json {})
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
     ["/channels/:channel-id/data" {:get  json
                                    :post created
                                    :delete nocontent}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware
                         params/parameters-middleware]}})))
