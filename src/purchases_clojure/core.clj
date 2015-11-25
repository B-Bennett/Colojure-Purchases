(ns purchases-clojure.core
(:require [clojure.string :as str]
          [clojure.walk :as walk]
          [clojure.pprint :as pprint]
          [ring.adapter.jetty :as j]
          [hiccup.core :as h])
(:gen-class))
(defn read-purchases []
 #_ (println "Type a category")
(let [input (read-line)
      purchses (slurp "purchases.csv")
      purchses (str/split-lines purchses)
      purchses (map (fn [line]
                      (str/split line #","))
                    purchses)
      header (first purchses)
      purchses (rest purchses)
      purchses (map (fn [line]
                      (interleave header line))
                    purchses)
      purchses (map (fn [line]
                      (apply hash-map line))
                    purchses)
      purchses (walk/keywordize-keys purchses)
      purchses (filter (fn [line]
                         (= (:category line) input))
                       purchses)]
  #_(spit (format "filtered_%s.edn" input)
          (with-out-str (pprint/pprint purchses)))
  purchses))

(defn purchase-html []
  (let [purchases (read-purchases)]
    (map (fn [line]
         [:p
          (str (:category line)
               "  "
               (:date line))])
       purchases)))

(defn handler [request]
  {:status  200
   :headers {"Content-type" "text/html"}
   :body    (h/html [:html
                     [:body
                      [:h3 "Type a Category"
                       [:br]
                       [:div
                        [:input.form-control
                         {:type "text"
                          :placeholder "Type here"}]]
                       [:br]
                       [:button]
                       [:br]
                     (purchase-html)]]])})
(defn -main [& args]
  (j/run-jetty #'handler {:port 4000 :join? false}))
