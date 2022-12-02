package com.revature.resproject.dtos.requests;

public class NewReimbursementRequest {
    private String amount;
    private String description;

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

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "Amount=" + amount +
                ", Ticket description='" + description + '\'' +
                '}';
    }
}
