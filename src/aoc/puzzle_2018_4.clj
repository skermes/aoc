(ns aoc.puzzle-2018-4
  (:require [clojure.string :as string]))

(defn input [] (slurp "inputs/2018_4.txt"))
(defn input-lines [] (string/split-lines (input)))

(def event-re #"\[\d+-(\d+)-(\d+) (\d+):(\d+)\] (wakes up|falls asleep|Guard #(\d+) begins shift)")

(defn line->event [line]
  (let [[_ month day hour minute action guard-id] (re-matches event-re line)
        event {:month (Integer/parseInt month)
               :day (Integer/parseInt day)
               :hour (Integer/parseInt hour)
               :minute (Integer/parseInt minute)}]
    (condp #(string/starts-with? %2 %1) action
      "wakes" (assoc event :action :wake-up)
      "falls" (assoc event :action :fall-asleep)
      "Guard" (assoc event :action :start-shift
                           :guard-id (Integer/parseInt guard-id)))))

(defmulti record-guard-event (fn [record event] (:action event)))

(defmethod record-guard-event :start-shift [record {:keys [guard-id]}]
  (assoc record :active-guard guard-id))

(defmethod record-guard-event :fall-asleep [{:keys [active-guard] :as record}
                                            {:keys [minute]}]
  (let [guard-record (get record active-guard {})]
    (assoc record active-guard (assoc guard-record :asleep-at minute))))

(defn count-minutes [minute-count new-minutes]
  (reduce #(assoc %1 %2 (+ 1 (get minute-count %2 0)))
          minute-count
          new-minutes))

(defmethod record-guard-event :wake-up [{:keys [active-guard] :as record}
                                         {:keys [minute]}]
  (let [{:keys [asleep-at] :as guard-record} (record active-guard)
        sleep-minutes (range asleep-at minute)]
    (assoc record active-guard
      (assoc guard-record :asleep-at nil
                          :total-sleep (+ (count sleep-minutes)
                                          (get guard-record :total-sleep 0))
                          :minute-count (count-minutes
                                          (get guard-record :minute-count {})
                                          sleep-minutes)))))

(defn sleepiest-guard [record]
  (apply max-key #(:total-sleep (record %)) (keys record)))

(defn sleepiest-minute [{:keys [minute-count]}]
  (apply max-key minute-count (keys minute-count)))

(defn sleep-times [{:keys [minute-count]} minute]
  (minute-count minute))

(defn consistent-sleeper [record]
  (apply max-key #(sleep-times (record %) (sleepiest-minute (record %)))
                 (keys record)))

(defn part-one []
  (let [record (->> (input-lines)
                    sort
                    (map line->event)
                    (reduce record-guard-event {})
                    (#(dissoc % :active-guard)))
        sleepy (sleepiest-guard record)]
    (* sleepy (sleepiest-minute (record sleepy)))))

(defn part-two []
  (let [record (->> (input-lines)
                    sort
                    (map line->event)
                    (reduce record-guard-event {})
                    (#(dissoc % :active-guard)))
        sleepy (consistent-sleeper record)]
    (* sleepy (sleepiest-minute (record sleepy)))))
