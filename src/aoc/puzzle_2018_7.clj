(ns aoc.puzzle-2018-7
  (:require [clojure.string :as string]))

(defn input [] (string/trim (slurp "inputs/2018_7_sample.txt")))
(defn input-lines [] (string/split-lines (input)))

(defn line->edge [line]
  [(nth line 5) (nth line 36)])

(defn add-edge-to-graph [graph [from to]]
  (assoc graph from (conj (get graph from #{}) to)))

(defn build-graph [edges]
  (reduce add-edge-to-graph {} edges))

(defn add-inverted-edge-to-graph [graph [to from]]
  (assoc graph from (conj (get graph from #{}) to)))

(defn build-inverted-graph [edges]
  (reduce add-inverted-edge-to-graph {} edges))

(defn invert-graph [graph]
  (reduce ))

(defn step-to-perform [graph inverted-graph]
  (->> (keys graph)
       (remove (set (keys inverted-graph)))
       sort
       first))

(defn part-one []
  (->> (input-lines)
       (map line->edge)
       build-inverted-graph))

(defn part-two []
  nil)
