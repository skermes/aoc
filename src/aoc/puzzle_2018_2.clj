(ns aoc.puzzle-2018-2
  (:require [clojure.string :as string]))

(defn input [] (slurp "inputs/2018_2.txt"))
(defn input-lines [] (string/split-lines (input)))

(defn map-vals [f m]
  (reduce-kv (fn [acc k v] (assoc acc k (f v))) {} m ))

(defn letter-counts [s]
  (->> s
       (group-by identity)
       (map-vals count)))

(defn has-pair? [counts]
  (some #{2} (vals counts)))

(defn has-triple? [counts]
  (some #{3} (vals counts)))

(defn pair-triple-counts [lines]
  (reduce (fn [{:keys [pairs triples]} counts]
              {:pairs (+ pairs (if (has-pair? counts) 1 0))
               :triples (+ triples (if (has-triple? counts) 1 0))})
          {:pairs 0 :triples 0}
          (map letter-counts lines)))

(defn mismatch-indices [line1 line2]
  (->> (map vector line1 line2)
       (map-indexed (fn [i [l1 l2]] (if (= l1 l2) nil i)))
       (remove nil?)))

(defn pairs-with-mismatches [lines]
  (for [line1 lines
        line2 lines]
    {:line1 line1 :line2 line2 :mismatches (mismatch-indices line1 line2)}))

(defn one-mismatch? [pair]
  (= 1 (count (:mismatches pair))))

(defn str-without-index [s i]
  (->> (drop (+ 1 i) s)
       (concat (take i s))
       (apply str)))

(defn part-one []
  (let [{:keys [pairs triples]} (pair-triple-counts (input-lines))]
    (* pairs triples)))

(defn part-two []
  (->> (input-lines)
       pairs-with-mismatches
       (drop-while (comp not one-mismatch?))
       first
       ((fn [{:keys [line1 mismatches]}]
         (str-without-index line1 (first mismatches))))))
