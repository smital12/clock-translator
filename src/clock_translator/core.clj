(ns clock-translator.core
  (require [clojure.string :as string]))

(def minute_words
  ["" "one" "two" "three" "four" "five"
   "six" "seven" "eight" "nine" "ten"
   "eleven" "twelve" "thirteen" "fourteen" "fifteen"
   "sixteen" "seventeen" "eighteen" "nineteen"])

(def tens_words
  ["o'" "" "twenty" "thirty" "fourty" "fifty"])

(def hour_words
  ["" "one" "two" "three" "four" "five"
   "six" "seven" "eight" "nine" "ten"
   "eleven" "twelve"])

(defn translator
  [hh mm]
  (let [ones_digit (rem mm 10)]
    {:hours (cond (and (= hh 0) (= mm 0)) "midnight"
                  (and (= hh 12) (= mm 0)) "noon"
                  :else (hour_words (inc (rem (dec hh) 12))))
     :tens (cond
             (= mm 0) ""
             (or (>= mm 20) (< mm 10)) (tens_words (quot mm 10))
             :else "")
     :minutes (cond
                (= ones_digit 0) ""
                (>= mm 20) (minute_words ones_digit)
                (< mm 20) (minute_words mm)
                :else "")
     :ampm (cond
             (= hh 0) ""
             (< hh 12) "am"
             (>= hh 12) "pm")}))

(defn clock-translator
  "Translates input time in the HH:MM format to a sentance"
  [hhmm]

  (let [[hh mm] (map #(Integer/parseInt %) (re-seq #"\d+" hhmm))]
    (let [timeMap (translator hh mm)]
      (str "It's "
           (string/join " " (remove string/blank? [(:hours timeMap)
                                                   (:tens timeMap)
                                                   (:minutes timeMap)
                                                   (:ampm timeMap)]))
           "."))))

(pmap clock-translator ["00:00" "01:30" "12:05" "14:01" "20:29" "21:00" "02:20"])
;(print (get (pmap clock-translator ["00:00" "21:00" ]) :hours))
;(clojure.pprint/pprint (clock-translator "10:12"))
;(clock-translator "10:12")

