(defn operation [f] (fn [& elements]
                      (if (every? number? elements)
                        (apply f elements)
                        (apply mapv (operation f) elements))))

(def v+ (operation +))
(def v- (operation -))
(def v* (operation *))
(def vd (operation /))

(def s+ (operation +))
(def s- (operation -))
(def s* (operation *))
(def sd (operation /))

(defn scalar [& vectors]
  (apply + (apply mapv * vectors)))

(defn vect [& vectors]
  (apply (fn [v1 v2]
           (let [[x1 y1 z1] v1 [x2 y2 z2] v2]
             [(- (* y1 z2) (* y2 z1))
              (- (* z1 x2) (* z2 x1))
              (- (* x1 y2) (* x2 y1))]))
         vectors))

(defn v*s [v & s]
  (mapv (fn [x] (apply * x s)) v))

(def m+ (operation v+))
(def m- (operation v-))
(def m* (operation v*))
(def md (operation vd))

(defn m*s [m & s]
  (mapv (fn [v] (apply v*s v s)) m))

(defn m*v [m & v]
  (mapv (fn [x] (apply scalar x v)) m))

(defn transpose [m]
  (apply mapv vector m))

(defn m*m [& ms]
  (apply (fn [m1 m2]
           (mapv (fn [v] (m*v (transpose m2) v)) m1))
         ms))