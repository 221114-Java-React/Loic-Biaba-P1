package com.revature.resproject.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewReimbursementRequest;
import com.revature.resproject.dtos.requests.UpdateTicketRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Reimbursement;
import com.revature.resproject.models.Role;
import com.revature.resproject.models.Status;
import com.revature.resproject.models.User;
import com.revature.resproject.services.ReimbursementService;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
import com.revature.resproject.utils.custom_exceptions.InvalidReimbException;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ReimbursementHandler {
    private final ReimbursementService reimbursementService;
    private final TokenService tokenService;
    private final ObjectMapper mapper;
    private final static Logger logger = LoggerFactory.getLogger(User.class);

    public ReimbursementHandler(ReimbursementService reimbursementService, TokenService tokenService, ObjectMapper mapper) {
        this.reimbursementService = reimbursementService;
        this.tokenService = tokenService;
        this.mapper = mapper;
    }

    public void register(Context ctx) throws IOException {

        NewReimbursementRequest req = mapper.readValue(ctx.req.getInputStream(), NewReimbursementRequest.class);

        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            //  logger.info(token);
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");

            Reimbursement ticket = null;

            if(!reimbursementService.isEmpty(req.getAmount())) {
                if(!reimbursementService.isEmpty(req.getDescription())) {
                     reimbursementService.isNumeric(req.getAmount());
                        ticket = reimbursementService.makeTicket(req, principal);
                } else throw new InvalidReimbException("Please enter a description");
            } else throw new InvalidReimbException("Please enter an amount");

            ctx.status(201); // CREATED
            ctx.json(ticket);
        } catch (InvalidReimbException | InvalidAuthException e) {
            ctx.status(403);
            ctx.json(e);
        }catch (NumberFormatException e) {
            ctx.status(403);
            ctx.result("Amount must be digits only");
        }
    }
    public void getAllTickets(Context ctx) {
        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals(Role.ADMIN) && !principal.getRole().equals(Role.MANAGER)) throw new InvalidAuthException("You are not authorized to do this");

            // logger.info("Principal: " + principal.toString());
            // logger.info("Principal: " + tokenService.extractDetails(token));

            List<Reimbursement> tickets = reimbursementService.getAllTickets();
            ctx.json(tickets);
        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            ctx.json(e);
        }
    }
    public void getAllTicketsByStatus(Context ctx) {
        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token. Use a valid one");
            if (!principal.getRole().equals(Role.ADMIN) && !principal.getRole().equals(Role.MANAGER)) throw new InvalidAuthException("You are not authorized to do this");

            String status = ctx.req.getParameter("ticketStatus");
            Status paramStatus = reimbursementService.showStatus(status);
            List<Reimbursement> tickets = reimbursementService.getAllTicketByStatus(paramStatus);

            if (tickets.isEmpty()) {ctx.result("No ticket has been found!"); return;}

            ctx.json(tickets);
        } catch (InvalidAuthException | InvalidReimbException e) {
            ctx.status(401);
            ctx.json(e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            ctx.json(e);
        }
    }


    public void processTicket(Context ctx) throws IOException {
        UpdateTicketRequest req = mapper.readValue(ctx.req.getInputStream(), UpdateTicketRequest.class);
        try {
            Reimbursement processedTicket = null;
            if (reimbursementService.isDuplicateId(req.getId())) {
                List<Reimbursement> tickets = reimbursementService.getAllTicketbyId(req.getId());
                for (Reimbursement ticket : tickets) {
                    processedTicket = reimbursementService.processTicket(req);
                }
            }
        } catch (InvalidReimbException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        }
    }

}
