package com.revature.resproject.services;

import com.revature.resproject.daos.ReimbursementDAO;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.dtos.requests.NewReimbursementRequest;
import com.revature.resproject.models.*;
import com.revature.resproject.utils.Sequence;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.US);
       // dateFormat.format(LocalDateTime.now())
        try {
             createdTicket = new Reimbursement(Sequence.nextIdValue(), Double.parseDouble(req.getAmount()), LocalDateTime.now(), LocalDateTime.of(2000, 02, 20, 00, 02), req.getDescription(),
                     "", 1 + rand.nextInt(200), principal.getId(), 0, Status.PENDING, Rtype.OTHER);
            logger.info(" " + createdTicket);
             reimbDAO.save(createdTicket);
         } catch (RuntimeException e) {
            logger.info("Problem with image");
             e.printStackTrace();
         }
        return createdTicket;

    }

    public List<Reimbursement> getAllTickets() {
            return reimbDAO.findAll();
    }
}
