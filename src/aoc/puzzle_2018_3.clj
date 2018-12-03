(ns aoc.puzzle-2018-3
  (:require [clojure.string :as string]))

(defn input [] (slurp "inputs/2018_3.txt"))
(defn input-lines [] (string/split-lines (input)))

(def rect-re #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

(defn line->rect [line]
  (let [[_ id x y width height] (re-matches rect-re line)]
    {:id id
     :x (Integer/parseInt x)
     :y (Integer/parseInt y)
     :width (Integer/parseInt width)
     :height (Integer/parseInt height)}))

(defn rect-points [{:keys [x y width height]}]
  (for [dx (range width)
        dy (range height)]
    {:x (+ x dx) :y (+ y dy)}))

(defn repeated-points [grouped-points]
  (->> grouped-points
       (filter (fn [[k v]] (> (count v) 1)))
       (map (fn [[k v]] k))))

(defn add-point-to-counts [counts point]
  (assoc counts point (+ 1 (get counts point 0))))

(defn count-points [points]
  (reduce add-point-to-counts {} points))

(defn is-overlapping? [counts rect]
  (->> rect
       rect-points
       (map counts)
       (apply max)
       (< 1)))

(defn find-non-overlapping-rect [counts rects]
  (->> rects
       (drop-while #(is-overlapping? counts %))
       first))

(defn part-one []
  (->> (input-lines)
       (map line->rect)
       (mapcat rect-points)
       (group-by identity)
       repeated-points
       count))

(defn part-two []
  (let [rects (map line->rect (input-lines))
        counts (count-points (mapcat rect-points rects))]
    (:id (find-non-overlapping-rect counts rects))))
