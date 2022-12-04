package com.revature.resproject.dtos.requests;

public class ProcessTicketRequest {
     private int ticket_id;
     private String status;

    public ProcessTicketRequest() {
        super();
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProcessTicketRequest{" +
                "ticket_id=" + ticket_id +
                ", status='" + status + '\'' +
                '}';
    }
}
