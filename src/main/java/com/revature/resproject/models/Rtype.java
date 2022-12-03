package com.revature.resproject.models;

public enum Rtype {
    LODGING(0), TRAVEL(1), FOOD(2), OTHER(3);

    private int i;
    Rtype(int i) {
        this.i = i;
    }

    public int getvalue() {
        return i;
    }
}
