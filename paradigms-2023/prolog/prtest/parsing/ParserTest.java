package prtest.parsing;

import base.Selector;

import static jstest.expression.Operations.ARITH;

/**
 * Tests for
 * <a href="https://www.kgeorgiy.info/courses/paradigms/homeworks.html#prolog-expression-parsing">Prolog Expression Parser</a>
 * homework of <a href="https://www.kgeorgiy.info/courses/paradigms">Programming Paradigms</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ParserTest {
    private static final Selector SELECTOR = ParserTester.builder()
            .variant("Base", ARITH)
            .selector();

    private ParserTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
