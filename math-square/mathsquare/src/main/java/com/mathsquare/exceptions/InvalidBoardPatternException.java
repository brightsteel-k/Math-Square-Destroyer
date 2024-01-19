package com.mathsquare.exceptions;

public class InvalidBoardPatternException extends RuntimeException {
    
    private String reason;
    private String row;

    public InvalidBoardPatternException(String reason) {
        this(reason, "/");
    }

    public InvalidBoardPatternException(String reason, String row) {
        this.reason = reason;
        this.row = row;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        String message = "[Invalid Board Pattern Exception]: Failed to parse board from JSON pattern. Reason: \'" + reason +"\'.";
        if (!row.equals("/")) {
            message += "\nCAUSED BY ROW: [\'" + row + "\']";
        }
        return message;
    }
}
