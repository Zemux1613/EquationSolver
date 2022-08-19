package de.tom.equationSolver;

public enum Operation {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLIKATION("*"),
    DIVISION("/");

    private String symbol;

    Operation(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
