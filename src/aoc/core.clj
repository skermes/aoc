(ns aoc.core
  (:require [clojure.string :as string]))

(defn day-no-zeroes [day-str]
  (string/replace day-str "0" ""))

(defn -main [year day]
  (let [puzzle-ns (str "aoc.puzzle-" year "-" (day-no-zeroes day))]
    (require [(symbol puzzle-ns) :as 'puzzle])
    (println (eval (list (symbol "puzzle/part-one"))))
    (println)
    (println (eval (list (symbol "puzzle/part-two"))))))
