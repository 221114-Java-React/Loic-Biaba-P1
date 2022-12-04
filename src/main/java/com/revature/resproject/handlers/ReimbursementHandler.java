package com.revature.resproject.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewReimbursementRequest;
import com.revature.resproject.dtos.requests.NewUpdateUserRequest;
import com.revature.resproject.dtos.requests.ProcessTicketRequest;
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

import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
            if (!principal.getRole().equals(Role.DEFAULT)) throw new InvalidAuthException("You are not authorized to do this");
            if (!principal.isActive()) throw new InvalidAuthException("You do not have permission to do this");
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
            if (principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

            List<Reimbursement> tickets = reimbursementService.getAllTickets();
            if (tickets.isEmpty()) {ctx.result("No ticket has been found!"); return;}

            if (!principal.getRole().equals(Role.MANAGER)) {
                List<Reimbursement> userTickets = new ArrayList<>();
                for (Reimbursement candidate : tickets) {
                    if (candidate.getAuthorId() == principal.getId())
                        userTickets.add(candidate);
                }
                if (userTickets.isEmpty()) {ctx.result("No ticket has been found!"); return;}
                ctx.json(userTickets);
            }
            else
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
            if (principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

            String status = ctx.req.getParameter("ticketStatus");
            Status paramStatus = reimbursementService.showStatus(status);
            List<Reimbursement> tickets = reimbursementService.getAllTicketByStatus(paramStatus);
            if (tickets.isEmpty()) {ctx.result("No ticket has been found!"); return;}

            if (!principal.getRole().equals(Role.MANAGER)) {
                List<Reimbursement> userTickets = new ArrayList<>();
                for (Reimbursement candidate : tickets) {
                    if (candidate.getAuthorId() == principal.getId())
                       userTickets.add(candidate);
                }
                if (userTickets.isEmpty()) {ctx.result("No ticket has been found!"); return;}
                ctx.json(userTickets);
            }
            else
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
        ProcessTicketRequest req = mapper.readValue(ctx.req.getInputStream(), ProcessTicketRequest.class);

        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token. Use a valid one");
            if (!principal.getRole().equals(Role.MANAGER)) throw new InvalidAuthException("You are not authorized to do this");

            Reimbursement processedTicket = null;
            if (reimbursementService.isDuplicateId(req.getTicket_id())) {
                Status status = reimbursementService.checkStatus(req.getStatus());
                List<Reimbursement> tickets = reimbursementService.getAllTicketbyId(req.getTicket_id());
                for (Reimbursement candidate: tickets) {
                    if(candidate.getAuthorId() == principal.getId()) throw new InvalidReimbException("A manager cannot resolve his own ticket");
                    if(candidate.getStatus() == Status.APPROVED || candidate.getStatus() == Status.DENIED) throw new InvalidReimbException("Ticket has already been resolved");
                }
                    processedTicket = reimbursementService.processStatus(req.getTicket_id(), status, principal.getId());

            } else throw new InvalidReimbException("Ticket doesn't exist");
            ctx.json(processedTicket);
            ctx.status(202); // ACCEPTED
        } catch (InvalidReimbException | InvalidAuthException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateTicket(Context ctx) throws IOException {
        UpdateTicketRequest req = mapper.readValue(ctx.req.getInputStream(), UpdateTicketRequest.class);
        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            //  logger.info(token);
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals(Role.DEFAULT)) throw new InvalidAuthException("You are not authorized to do this");

            Reimbursement updatedTicket = null;
            if (reimbursementService.isDuplicateId(Integer.parseInt(req.getId()))) {
                if(!reimbursementService.isEmpty(req.getAmount())) {
                    if(!reimbursementService.isEmpty(req.getDescription())) {
                        if(!reimbursementService.isEmpty(req.getId())) {
                            reimbursementService.isNumeric(req.getAmount());
                            List<Reimbursement> tickets = reimbursementService.getAllTicketbyId(Integer.parseInt(req.getId()));
                            for (Reimbursement candidate: tickets) {
                                if(candidate.getStatus() == Status.APPROVED || candidate.getStatus() == Status.DENIED) throw new InvalidReimbException("Ticket has already been resolved. Resolved ticket cannot be updated");
                            }
                            updatedTicket = reimbursementService.updateTicket(req);
                        } else throw new InvalidReimbException("Please enter a ticket ID");
                    } else throw new InvalidReimbException("Please enter a description");
                } else throw new InvalidReimbException("Please enter an amount");
            } else throw new InvalidReimbException("Ticket doesn't exist");

        } catch (InvalidReimbException | InvalidAuthException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        }
    }

}
