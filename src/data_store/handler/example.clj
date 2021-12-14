(ns data-store.handler.example
  (:require [compojure.core :refer :all]
            [integrant.core :as ig]))

(defmethod ig/init-key :data-store.handler/example [_ options]
  (context "/example" []
    (GET "/" []
      {:body {:example "data"}})))
