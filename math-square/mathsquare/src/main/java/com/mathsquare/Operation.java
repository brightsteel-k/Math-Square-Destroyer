package com.mathsquare;

import com.mathsquare.exceptions.InvalidOperationException;

public enum Operation {
    ADD(0),
    SUB(1),
    MUL(2),
    DIV(3);

    protected static String[] NAMES = { "Addition", "Subtraction", "Multiplication", "Division"};
    protected static String[] SYMBOLS = { "+", "-", "x", "/"};
    protected int code;

    Operation(int code) {
        this.code = code;
    }

    public String getName() {
        return NAMES[code];
    }

    public String getSymbol() {
        return SYMBOLS[code];
    }

    @Override
    public String toString() {
        return SYMBOLS[code];
    }

    public static Operation parseOperation(char symbol) throws InvalidOperationException {
        switch (symbol) {
            case '+':
                return ADD;
            case '-':
                return SUB;
            case 'x':
                return MUL;
            case '*':
                return MUL;
            case '/':
                return DIV;
            default:
                throw new InvalidOperationException(symbol);
        }
    }
}