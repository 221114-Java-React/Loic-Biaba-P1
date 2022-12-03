package com.revature.resproject.dtos.requests;

public class UpdateTicketRequest {
    private int id;

    public UpdateTicketRequest() {
        super();
    }

    public UpdateTicketRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UpdateTicketRequest{" +
                "id=" + id +
                '}';
    }
}
