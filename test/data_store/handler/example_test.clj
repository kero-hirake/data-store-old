(ns data-store.handler.example-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [ring.mock.request :as mock]
            [data-store.handler.example :as example]))

(deftest smoke-test
  (testing "data get"
    (let [handler  (ig/init-key :data-store.handler/example {})
          response (handler (mock/request :get "/channels/1/data"))]
      (is (= 200 (:status response)) "status code")
      (is (= "{\"created\":\"2022-1-1\",\"d1\":1,\"d2\":2}"  (slurp (:body response))) "body" )))
  (testing "data post"
    (let [handler (ig/init-key  :data-store.handler/example {})
          response (handler (mock/request :post "/channels/1/data"))]
      (is (= 201 (:status response)) "status code") 
      (is (= "created" (:body response)) "body")
      )))

