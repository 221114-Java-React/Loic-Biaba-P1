package com.revature.resproject.models;

public enum Rtype {
    LODGING(1), TRAVEL(2), FOOD(3), OTHER(4);

    private int i;
    Rtype(int i) {
        this.i = i;
    }
}
