(ns aoc.puzzle-2018-1
  (:require [clojure.string :as string]))

(defn input [] (slurp "inputs/2018_1.txt"))
(defn input-lines [] (string/split-lines (input)))

(defn changes [lines]
  (map #(Integer/parseInt %) lines))

(defn net-change [changes]
  (reduce + changes))

(defn frequencies [changes]
  (reductions + changes))

(defn first-repeated
  ([values] (first-repeated values #{}))
  ([[head & tail] already-seen]
    (if (contains? already-seen head)
        head
        (recur tail (conj already-seen head)))))

(defn part-one []
  (->> (input-lines)
       changes
       net-change))

(defn part-two []
  (->> (input-lines)
       changes
       cycle
       frequencies
       first-repeated))
