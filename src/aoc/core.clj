(ns aoc.core
  (:require [clojure.string :as string])
  (:import java.awt.datatransfer.StringSelection
           java.awt.Toolkit))

(defn day-no-zeroes [day-str]
  (string/replace day-str "0" ""))

;; Based on the core/time macro
;; https://github.com/clojure/clojure/blob/master/src/clj/clojure/core.clj#L3884
(defn solution [func]
  (let [start (. System (nanoTime))
        result (func)]
    {:time (- (. System (nanoTime)) start)
     :result result}))

(defn print-solution [{:keys [time result]}]
  (if (nil? result)
      (println "No result; presumed not implemented")
      (do
        (println "Duration:" (/ (double time) 1000000.0) "milliseconds")
        (println "Result:" result))))

(defn copy-result-to-clipboard [{:keys [result]}]
  (if (nil? result)
      false
      (let [selection (StringSelection. (str result))]
        (.setContents (.getSystemClipboard (Toolkit/getDefaultToolkit))
                      selection
                      selection)
        true)))

(defn -main [year day]
  (let [puzzle-ns (str "aoc.puzzle-" year "-" (day-no-zeroes day))]
    (require [(symbol puzzle-ns) :as 'puzzle])
    (println "Advent of Code" year "Day" day)
    (println "==========================")
    (println)
    (println "Part One")
    (println "--------")
    (let [part-one (list (symbol "puzzle/part-one"))
          solution-one (solution #(eval part-one))]
      (print-solution solution-one)
      (copy-result-to-clipboard solution-one))
    (println)
    (println "Part Two")
    (println "--------")
    (let [part-two (list (symbol "puzzle/part-two"))
          solution-two (solution #(eval part-two))]
      (print-solution solution-two)
      (copy-result-to-clipboard solution-two))))
