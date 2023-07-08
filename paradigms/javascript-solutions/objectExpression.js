"use strict";

class Source {
    whiteSpaces = new Set([' ', '\n', '\t', '\r'])

    constructor(data) {
        this.data = data;
        this.pos = -1;
        this.END = '\0';
        this.ch = 0xffff;
        this.take();
    }

    toString() {
        return this.data;
    }

    hasNext() {
        return this.pos < this.data.length;
    }

    next() {
        this.pos++;
        return this.data.charAt(this.pos);
    }

    countBrackets(ch) {
        if (ch === '(')
            bracketCounter++;
        else if (ch === ')')
            bracketCounter--;
    }

    take() {
        let result = this.ch;
        this.countBrackets(result);
        this.ch = this.hasNext() ? this.next() : this.END;
        return result;
    }

    isWhitespace() {
        return this.whiteSpaces.has(this.ch);
    }

    skipWhitespace() {
        while (this.isWhitespace()) {
            this.take();
        }
    }

    test(expected) {
        return this.ch === expected;
    }

    eof() {
        return this.pos >= this.data.length;
    }

    isNumber() {
        return this.isNumberByPos(this.pos - 1);
    }

    nextIsNumber() {
        return this.isNumberByPos(this.pos);
    }

    isNumberByPos(pos) {
        return /^\d+$/.test(this.data.charAt(pos + 1));
    }

    buildNumber() {
        const start = this.pos;
        if (this.isNumber() || this.ch === '-' & this.nextIsNumber()) {
            this.take();
        }
        while (this.isNumber()) {
            this.take();
        }
        return this.data.slice(start, this.pos);
    }

    expectChar(c) {
        if (this.ch !== c) {
            throw new ParsingError(`illegal symbol. expected: "${c}", actual: "${this.ch}"`);
        }
        this.take();
    }

    expect(string) {
        Array.from(string).forEach((c) => this.expectChar(c));
    }

    isArgument() {
        return this.isNumber() || this.test('-') || this.test('x') || this.test('y') ||
            this.test('z') || this.test('(');
    }

    takeOperation() {
        const start = this.pos;
        let pos = this.pos;
        while (this.data[pos] !== " " && pos < this.data.length && this.data[pos] !== ')') {
            pos++;
            this.take();
            if (this.data.slice(start, pos) === 'atan' && this.data[pos] === '2') {
                pos++;
            }
            if (operationSet.has(this.data.slice(start, pos))) {
                if (this.data.slice(start, pos) === '-' && this.isNumberByPos(pos - 1)) {
                    return this.buildNumber();
                } else {
                    break;
                }
            }
        }
        return this.data.slice(start, pos)
    }
}

const variableMap = new Map([['x', 0], ['y', 1], ['z', 2]]);
const operationSet = new Set(['(', ')', 'x', 'y', 'z']);
const multiDimOperations = new Set(["meansq", "rms", "sumrec", "hmean", "avg"]);
const operationMap = {};
const dimensionMap = {};

function Operation(...functions) {
    this.functions = functions;
}

Operation.prototype = {
    getOp: function () {
        if (this.operator === "sumrec" || this.operator === "hmean") {
            return this.operator.concat(this.functions.length);
        }
        return this.operator;
    },
    toString: function () {
        return this.functions.map((f) => f.toString()).join(" ").concat(` ${this.getOp()}`);
    },
    prefix: function () {
        return `(${this.operator} `.concat(this.functions.map((f) => f.prefix()).join(" ").concat(')'));
    },
    postfix: function () {
        return `(`.concat(this.functions.map((f) => f.postfix()).join(" ").concat(` ${this.operator})`));
    },
    evaluate: function (...args) {
        if (multiDimOperations.has(this.operator)) {
            return this.evalRule(this.functions).evaluate(...args);
        }
        return this.evalRule(...this.functions.map(f => f.evaluate(...args)));
    },
    diff: function (variable) {
        if (multiDimOperations.has(this.operator)) {
            return this.evalRule(this.functions).diff(variable);
        }
        return this.diffRule(this.functions.map(f => f.diff(variable)), this.functions);
    }
}

function createOperation(dim, operator, evalRule, diffRule) {
    const f = function (...functions) {
        return Operation.apply(this, functions);
    }
    f.prototype = Object.create(Operation.prototype);
    f.prototype.operator = operator;
    f.prototype.evalRule = evalRule;
    f.prototype.diffRule = diffRule;
    f.prototype.constructor = f;
    operationMap[operator] = f;
    operationSet.add(operator);
    dimensionMap[operator] = dim;
    return f;
}

function Const(constant) {
    this.constant = parseInt(constant);
    this.toString = () => this.constant.toString();
    this.prefix = () => this.toString();
    this.postfix = () => this.toString();
    this.evaluate = () => this.constant;
    this.diff = () => new Const(0);
}

function Variable(variable) {
    this.variable = variable;
    this.toString = () => this.variable;
    this.prefix = () => this.toString();
    this.postfix = () => this.toString();
    this.evaluate = (...args) => args[variableMap.get(this.variable)];
    this.diff = (variable) => variable === this.variable ? new Const(1) : new Const(0);
}

const Negate = createOperation(1, "negate", (arg) => -arg, ([diff]) => new Negate(diff));

const Add = createOperation(2, '+',
    (...args) => args.reduce((sum, arg) => sum + arg, 0), (dx) => new Add(...dx)
);

const Subtract = createOperation(2, '-', (a, b) => a - b, (dx) => new Subtract(dx[0], dx[1]));

const Multiply = createOperation(2, '*', (a, b) => a * b,
    (dx, args) => new Add(
        new Multiply(dx[0], args[1]),
        new Multiply(args[0], dx[1])
    )
);

const Divide = createOperation(2, '/', (a, b) => a / b,
    (dx, args) => new Divide(
        new Subtract(
            new Multiply(dx[0], args[1]),
            new Multiply(args[0], dx[1])
        ), new Multiply(args[1], args[1])
    )
);

const Root = createOperation(1, "sqrt", a => Math.sqrt(a), ([dx], [arg]) =>
    new Divide(dx, new Multiply(
        new Const(2),
        new Root(arg)))
);

const Meansq = createOperation("any", "meansq",
    (functions) => new Divide(
        new Add(...functions.map((f) => new Multiply(f, f))),
        new Const(functions.length))
);

const RMS = createOperation("any", "rms", (functions) => new Root(new Meansq(...functions)));

const SumrecN = createOperation(undefined, "sumrec",
    (functions) => new Add(...functions.map((f) => new Divide(new Const(1), f)))
);

const HMeanN = createOperation(undefined, "hmean",
    (functions) => new Divide(new Const(functions.length), new SumrecN(...functions))
);

const Sum = createOperation("any", "sum",
    (...args) => args.reduce((sum, arg) => sum + arg, 0), (dx) => new Add(...dx)
);

const Avg = createOperation("any", "avg",
    (functions) => new Divide(new Sum(...functions), new Const(functions.length))
);
const ArcTan = createOperation(1, "atan", (f) => Math.atan(f))
const ArcTan2 = createOperation(undefined, "atan2", (f1, f2) => Math.atan2(f1, f2))

let Sumrec2 = SumrecN;
let Sumrec3 = SumrecN;
let Sumrec4 = SumrecN;
let Sumrec5 = SumrecN;
let HMean2 = HMeanN;
let HMean3 = HMeanN;
let HMean4 = HMeanN;
let HMean5 = HMeanN;

function ParsingError(message) {
    this.message = message;
    this.name = "ParsingError";
}

ParsingError.prototype = Object.create(Error.prototype);
ParsingError.prototype.constructor = ParsingError;

let bracketCounter = 0;
let mode;

function parseArgument(struct, argument) {
    if (struct instanceof Array)
        struct.push(argument);
    return argument;
}

function parseArguments(functions, source, struct) {
    while (source.isArgument()) {
        functions.push(parseElement(source, struct));
    }
    return functions;
}

function parseOperation(source, struct, f, dimension) {
    if (dimension === undefined) {
        dimension = parseInt(source.take());
    }
    source.skipWhitespace();

    let functions = [];
    let operation;
    if (!(struct instanceof Array)) {
        functions = parseArguments(functions, source, struct);
        operation = new f(...functions);
    } else {
        if (!source.test(')') && mode !== "polish") {
            throw new ParsingError(`illegal symbol. expected: ")", got: "${source.take()}"`);
        }
        if (dimension !== "any") {
            functions = struct.splice(-dimension, dimension);
        } else {
            functions = struct.splice(-struct.length, struct.length);
        }
        operation = new f(...functions)
        struct.push(operation);
    }
    if (Number.isInteger(dimension) && functions.length !== dimension) {
        throw new ParsingError(`invalid operation: "${source.toString()}"`);
    }
    return operation;
}

function parseNewOperation(stopFunc, source, struct) {
    let result;
    const stack = [];
    while (!stopFunc()) {
        result = parseElement(source, stack);
    }
    if (struct !== undefined) {
        struct.push(stack.pop());
    } else {
        stack.pop();
    }
    if (stack.length !== 0) {
        throw new ParsingError(`bark invalid operation: "(${stack.map((e) => e.toString()).join(" ")
            .concat(" ").concat(result)})"`);
    }
    source.take();
    return result;
}

function parseOpenBracket(source, struct) {
    let result;
    if (struct instanceof Array) {
        result = parseNewOperation(() => source.test(')'), source, struct);
    } else {
        result = parseElement(source, struct);
        source.expect(')');
    }
    if (result instanceof Variable || result instanceof Const)
        throw new ParsingError(`meow invalid operation: "(${result.toString()})"`)
    return result;
}

function parseElement(source, struct) {
    source.skipWhitespace();
    const parsedResult = parseValue(source, struct);
    source.skipWhitespace();
    return parsedResult;
}

function parseValue(source, struct) {
    if (source.isNumber() || source.test('-') && source.nextIsNumber()) {
        return parseArgument(struct, new Const(source.buildNumber()));
    }
    const operation = source.takeOperation();
    if (operation === ')') {
        return struct;
    } else if (operation === '(') {
        return parseOpenBracket(source, struct);
    } else if (operation === 'x' || operation === 'y' || operation === 'z') {
        return parseArgument(struct, new Variable(operation));
    } else if (operationMap[operation] !== undefined) {
        return parseOperation(source, struct, operationMap[operation], dimensionMap[operation]);
    } else {
        throw new ParsingError(`illegal symbol: "${source.take()}"`)
    }
}

function check(result) {
    if (bracketCounter !== 0) {
        throw new ParsingError("incorrect bracket sequence");
    }
    if (result === undefined) {
        throw new ParsingError("empty input")
    }
}

function parserUtils(input) {
    bracketCounter = 0;
    return new Source(input);
}

function parse(input) {
    mode = "polish";
    return parsePostfix(input);
}

function parsePrefix(input) {
    const source = parserUtils(input);
    let result = undefined;
    result = parseElement(source, result);
    if (!source.eof()) {
        throw new ParsingError("end of input expected");
    }
    check(result);
    return result;
}

function parsePostfix(input) {
    const source = parserUtils(input);
    const result = parseNewOperation(() => source.eof(), source);
    check(result);
    return result;
}
