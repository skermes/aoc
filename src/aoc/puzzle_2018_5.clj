(ns aoc.puzzle-2018-5
  (:require [clojure.string :as string]))

(defn input [] (string/trim (slurp "inputs/2018_5.txt")))
(defn input-lines [] (string/split-lines (input)))

(defn reactants? [c1 c2]
  (and (not (nil? c1))
       (= (string/upper-case c1) (string/upper-case c2))
       (not (= c1 c2))))

;; It's important to always return a vector here so that we have fast
;; last access and subvectoring.
(defn append-unit [units new-unit]
  (let [last-unit (get units (- (count units) 1))]
    (if (reactants? last-unit new-unit)
        (subvec units 0 (- (count units) 1))
        (conj units new-unit))))

(defn react-units [units]
  (reduce append-unit [] units))

(def units [
  #{\A \a}
  #{\B \b}
  #{\C \c}
  #{\D \d}
  #{\E \e}
  #{\F \f}
  #{\G \g}
  #{\H \h}
  #{\I \i}
  #{\J \j}
  #{\K \k}
  #{\L \l}
  #{\M \m}
  #{\N \n}
  #{\O \o}
  #{\P \p}
  #{\Q \q}
  #{\R \s}
  #{\S \s}
  #{\T \t}
  #{\U \u}
  #{\V \v}
  #{\W \w}
  #{\X \x}
  #{\Y \y}
  #{\Z \z}])

(defn part-one []
  (count (react-units (input))))

(defn part-two []
  (let [polymer (react-units (input))
        filtered (map #(remove % polymer) units)]
    (->> filtered
         (map react-units)
         (map count)
         (apply min))))
