(ns trivia.handler
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [selmer.parser :as selmer]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [trivia.jservice :as jservice]))

(defn wrap-logging
  [handler]
  (fn [{:keys [uri method] :as request}]
    (let [label (str method " \"" uri "\"")]
      (try
        (println label)
        (let [{:keys [status] :as response} (handler request)]
          (println (str label " -> " status))
          response)
        (catch Exception e
          (println e label)
          {:status 500
           :body (selmer/render-file "templates/error.html" {})})))))

(defroutes app
  (GET "/" []
       (selmer/render-file "templates/index.html" {}))

  (GET "/error" []
       (throw (ex-info "A test error." {})))

  (GET "/random-clue" []
       (let [{:keys [question answer value category]} (jservice/random-clue)
             {category-title :title} category]
         (selmer/render-file "templates/random-clue.html" {:category category-title
                                                 :value value
                                                 :question question
                                                 :answer answer})))

  (ANY "*" []
       (route/not-found (selmer/render-file "templates/not-found.html" {}))))

(def handler (-> #'app
                 (wrap-resource "public")
                 (wrap-defaults site-defaults)
                 (wrap-logging)))
