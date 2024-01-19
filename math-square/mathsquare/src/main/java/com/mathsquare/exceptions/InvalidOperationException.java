package com.mathsquare.exceptions;

public class InvalidOperationException extends RuntimeException {
    private char operation;

    public InvalidOperationException(char operation) {
        this.operation = operation;
    }

    public char getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "[Invalid Operation Exception]: Caused by character \'" + operation +"\'.";
    }
}
