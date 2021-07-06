(ns trivia.jservice
  (:require [aleph.http :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

(defn execute-request [base-url request]
  (let [{:keys [as path query-params method body throw debug]
         :or {as :json-from-snake throw true method "GET" debug true}} request
        url (str base-url path)]
    (when debug
      (println "Executing" (str/upper-case method) "request:" url query-params))
    (try
      (let [response @(http/request {:url (str base-url path)
                                     :method method
                                     :query-params query-params
                                     :body body})
            body (slurp (:body response))]
        (if (= as :json-from-snake)
          (json/parse-string body (fn [k] (keyword (str/replace k #"_" "-"))))
          body))
      (catch Exception ex
        (if throw
          (throw (ex-info (str "Failed to execute " (str/upper-case method) " request to " url ": " (ex-message ex))
                          (or (ex-data ex) {})
                          ex))
          (ex-data ex))))))

(def jservice-base-url "https://jservice.io/api")

(def execute-jservice-request (partial execute-request jservice-base-url))

(defn random-clues [count]
  (execute-jservice-request {:path "/random"
                             :query-params {"count" count}}))

(defn random-clue [] (first (random-clues 1)))

(comment
  (random-clue)
  )
