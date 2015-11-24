(ns purchases-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))

(defn -main [& args]
  (println "Type a category")
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
    (spit "filtered_purchases.edn"
          (pr-str purchses))))
