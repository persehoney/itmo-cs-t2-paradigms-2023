;------------HW 10------------

(defn constant [const]
  (fn [_]
    const))

(defn variable [var]
  (fn [args]
    (get args var)))

(defn unaryOperation [f]
  (fn [operation]
    (fn [args]
      (f (operation args)))))

(defn binaryOperation [f]
  (fn [operation1 operation2]
    (fn [args]
      (f (operation1 args) (operation2 args)))))

(def add (binaryOperation +))
(def subtract (binaryOperation -))
(def multiply (binaryOperation *))
(def divide (binaryOperation (fn [x y] (/ (double x) (double y)))))
(def negate (unaryOperation -))
(def exp (unaryOperation (fn [x] (Math/exp(double x)))))
(def ln (unaryOperation (fn [x] (Math/log (abs(double x))))))
(def arcTan (unaryOperation (fn [x] (Math/atan(double x)))))
(def arcTan2 (binaryOperation (fn [x y] (Math/atan2(double x) (double y)))))

(def functionalOperations
  {'+ add,
   '- subtract,
   '* multiply,
   '/ divide,
   'negate negate,
   'exp exp,
   'ln ln,
   'atan arcTan,
   'atan2 arcTan2
   })

;------------HW 11------------

(defn evaluate [expression args] ((.evaluate expression) args))
(defn toString [expression] (.toStr expression))

(definterface IOperation
  (evaluate [])
  (toStr []))

(deftype ConstantType [const]
  IOperation
  (evaluate [_] (fn [_] const))
  (toStr [_] (format "%.1f" const)))

(defn Constant [const] (ConstantType. const))

(deftype VariableType [var]
  IOperation
  (evaluate [_] (fn [args] (get args var)))
  (toStr [_] var))

(defn Variable [var] (VariableType. var))

(deftype Operation [operator operation operations]
  IOperation
  (evaluate [_]
    (fn [args] (apply operation (mapv (fn [operand] (evaluate operand args)) operations))))
  (toStr [_]
    (str "(" operator " " (clojure.string/join " " (mapv toString operations)) ")")))

(defn createOperation [operator operation]
  (fn [& operations] (Operation. operator operation operations)))

(def Add (createOperation '+ +))
(def Subtract (createOperation '- -))
(def Multiply (createOperation '* *))
(def Divide (createOperation '/ (fn [x y] (/ (double x) (double y)))))
(def Negate (createOperation 'negate -))
(def Sin
  (createOperation 'sin (fn [x] (Math/sin (double x)))))
(def Cos
  (createOperation 'cos (fn [x] (Math/cos (double x)))))
(def Sinh
  (createOperation 'sinh (fn [x] (Math/sinh (double x)))))
(def Cosh
  (createOperation 'cosh (fn [x] (Math/cosh (double x)))))

(def objectOperations
  {'+      Add,
   '-      Subtract,
   '*      Multiply,
   '/      Divide,
   'negate Negate,
   'sin Sin,
   'cos Cos,
   'sinh Sinh,
   'cosh Cosh
   })

(defn parse [map const variable]
  (letfn
    [(parser [expression]
       (cond
         (string? expression) (parser (read-string expression))
         (seq? expression) (apply (get map (first expression)) (mapv parser (rest expression)))
         (number? expression) (const expression)
         (symbol? expression) (variable (str expression))))]
    (fn [expression] (parser expression))))

(def parseFunction (parse functionalOperations constant variable))
(def parseObject (parse objectOperations Constant Variable))
