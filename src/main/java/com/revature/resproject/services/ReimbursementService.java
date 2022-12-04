package com.revature.resproject.services;

import com.revature.resproject.daos.ReimbursementDAO;
import com.revature.resproject.dtos.requests.UpdateTicketRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.dtos.requests.NewReimbursementRequest;
import com.revature.resproject.models.*;
import com.revature.resproject.utils.Sequence;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.resproject.utils.custom_exceptions.InvalidReimbException;
import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReimbursementService {
    private static final SecureRandom rand = new SecureRandom();
    private final ReimbursementDAO reimbDAO;
    private final static Logger logger = LoggerFactory.getLogger(Reimbursement.class);

    public ReimbursementService(ReimbursementDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public boolean isEmpty(String string) {
        if (string == null || string.equals(""))
            return true;

        return false;
    }
    public void isNumeric(String string) {
        double value;
        try {
            value = Double.parseDouble(string);
            // return true;
        } catch (NumberFormatException e) {
            throw e;
        }

    }

    public Reimbursement makeTicket(NewReimbursementRequest req, Principal principal) {
        Reimbursement createdTicket = null;

        try {
             createdTicket = new Reimbursement(Sequence.nextIdValue(), Double.parseDouble(req.getAmount()), LocalDateTime.now(), LocalDateTime.of(2000, 02, 20, 00, 02), req.getDescription(),
                     "", 1 + rand.nextInt(200), principal.getId(), 0, Status.PENDING, req.getType());
             reimbDAO.save(createdTicket);
         } catch (RuntimeException e) {
             e.printStackTrace();
         }
        return createdTicket;

    }

    public List<Reimbursement> getAllTickets() {
            return reimbDAO.findAll();
    }

    public boolean isDuplicateId(int id) {
            List<Integer> ticketids = reimbDAO.findAllTicketId();
        return ticketids.contains(id);
    }

    public List<Reimbursement> getAllTicketbyId(int id) {
        return reimbDAO.getAllTicketByID(id);

    }

    public Reimbursement updateTicket(UpdateTicketRequest req) {
        Reimbursement ticket = new Reimbursement();
        return ticket;
    }

    public List<Reimbursement> getAllTicketByStatus(Status status) {

        return reimbDAO.getAllTicketsbyStatus(status);
    }
    public Status showStatus(String status) {
        String string = status.toLowerCase();
        switch (string) {
            case "pending":
                return Status.PENDING;
            case "approved":
                return Status.APPROVED;
            case "denied":
                return Status.DENIED;
            default:
                throw new InvalidReimbException("Invalid Status. Please input a valid status");
        }
    }
    public Status checkStatus(String status) {
        String string = status.toLowerCase();
        switch (string) {
            case "approve":
                return Status.APPROVED;
            case "deny":
                return Status.DENIED;
            default:
                throw new InvalidReimbException("Invalid action. Please input approve or deny");
        }
    }

    public Reimbursement processStatus(int ticket_id, Status status, int id) throws SQLException {
        Reimbursement processedTicket = reimbDAO.processedTicket(ticket_id, status, id);
        if (processedTicket == null) throw new InvalidReimbException("Unable to process ticket");
        return processedTicket;
    }
}
