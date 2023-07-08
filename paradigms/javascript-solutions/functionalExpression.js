"use strict";

const variable = (v) => (...args) => {
    if (v === "x")
        return args[0]
    else if (v === "y")
        return args[1]
    return args[2]
}
const cnst = (...args) => () => args[0];
const one = cnst(1)
const two = cnst(2)
const unaryOperation = (f) => (f1) => (...args) => f(f1(...args))
const binaryOperation = (f) => (f1, f2) => (...args) => f(f1(...args), f2(...args))
const add = binaryOperation((f1, f2) => f1 + f2)
const subtract = binaryOperation((f1, f2) => f1 - f2)
const multiply = binaryOperation((f1, f2) => f1 * f2)
const divide = binaryOperation((f1, f2) => f1 / f2)
const negate = unaryOperation(f => -f)
const sin = unaryOperation(f => Math.sin(f))
const cos = unaryOperation(f => Math.cos(f))
const sinh = unaryOperation(f => Math.sinh(f))
const cosh = unaryOperation(f => Math.cosh(f))
