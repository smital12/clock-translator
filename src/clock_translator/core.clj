(ns clock-translator.core)

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

(defn clock-translator
  "Translates input time in the HH:MM format to a sentance"
  [hhmm]
  (def timeInWords {:hours ""
                    :tens ""
                    :minutes ""})
  (let [[hh mm] (map #(Integer/parseInt %) (re-seq #"\d+" hhmm))]
    (str "It is "
         (if (and (= hh 0) (= mm 0))
           "midnight"
           (str (hour_words (inc (rem (dec hh) 12)))
                (cond
                  (= mm 0) ""
                  (< mm 10) " o'"
                  (> mm 20) (str " " (tens_words (quot mm 10))))
                (cond
                  (= mm 0) ""
                  (< mm 20) (minute_words mm)
                  (> mm 20) (if (not= (rem mm 10) 0)
                              (str " " (minute_words (rem mm 10)))))
                (if (< hh 12)
                  " am"
                  " pm"))))))

(map #'clock-translator '("00:00" "01:30" "12:05" "14:01" "20:29" "21:00"))
