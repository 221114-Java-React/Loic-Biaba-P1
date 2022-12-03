package com.revature.resproject.models;

public enum Status {
    PENDING(0), APPROVED(1), DENIED(2);
    private int i;
    Status(int i) {
        this.i = i;
    }
    public int getvalue() {
        return i;
    }
}
