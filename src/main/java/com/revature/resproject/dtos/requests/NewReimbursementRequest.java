package com.revature.resproject.dtos.requests;

import com.revature.resproject.models.Rtype;

public class NewReimbursementRequest {
    private String amount;
    private String description;
    private String type;

    public NewReimbursementRequest() {
        super();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rtype getType() {
        String string = type.toLowerCase();
        switch (string) {
            case "lodging":
                return Rtype.LODGING;
            case "travel":
                return Rtype.TRAVEL;
            case "food":
                return Rtype.FOOD;
            default:
                return Rtype.OTHER;
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
