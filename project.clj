(defproject trivia "1.0.0-SNAPSHOT"
  :description "Some trivia stuff in Clojure/Script"
  :url "http://mike-trivia.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [aleph "0.4.6"]
                 [cheshire "5.10.0"]
                 [compojure "1.6.2"]
                 [environ "1.2.0"]
                 [ring/ring-anti-forgery "1.0.1"]
                 [ring/ring-defaults "0.2.3"]
                 [selmer "1.12.40"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "trivia.jar"
  :profiles {:production {:env {:production true}}})
