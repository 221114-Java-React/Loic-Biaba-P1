package com.revature.resproject.daos;

import com.revature.resproject.dtos.requests.UpdateTicketRequest;
import com.revature.resproject.models.*;
import com.revature.resproject.utils.ConnectionFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReimbursementDAO implements CrudDAO<Reimbursement> {
    private final static Logger logger = LoggerFactory.getLogger(User.class);

    @Override
    public void save(Reimbursement obj) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursement_t  (reimbid, amount, submitted, resolved, description, receipt, paymentid, authorid, resolverid, statusid, typeid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, obj.getId());
            ps.setDouble(2, Math.round(obj.owedMoney() * 100.0) / 100.0);
            ps.setTimestamp(3, obj.resolveSubDate());
            ps.setTimestamp(4, obj.resolveResDate());
            ps.setString(5, obj.getDescription());
            ps.setBytes(6, obj.showReceipt());
            ps.setInt(7, obj.getPaymentId());
            ps.setInt(8, obj.getAuthorId());
            ps.setInt(9, obj.getResolverId());
            ps.setInt(10, obj.getStatus().getvalue());
            ps.setInt(11, obj.getRtype().getvalue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
         */
    }

    @Override
    public void delete(Reimbursement obj) {

    }

    @Override
    public void update(Reimbursement obj) {

    }

    @Override
    public Reimbursement findById() {
        return null;
    }

    @Override
    public List<Reimbursement> findAll() {
        List<Reimbursement> tickets = new ArrayList<>();
        Status status[] = Status.values();
        Rtype rtype[] = Rtype.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * from reimbursement_t");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reimbursement currentTicket = new Reimbursement(rs.getInt("reimbid"), rs.getDouble("amount"), rs.getTimestamp("submitted").toLocalDateTime(), rs.getTimestamp("resolved").toLocalDateTime(),
                        rs.getString("description"), rs.getBytes("receipt").toString(), rs.getInt("paymentid"), rs.getInt("authorid"), rs.getInt("resolverid"), status[rs.getInt("statusid")], rtype[rs.getInt("typeid")]);
                tickets.add(currentTicket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public List<Integer> findAllTicketId() {
        List<Integer> ticketsIds = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (reimbid) from reimbursement_t");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int currentId = rs.getInt("reimbid");
                ticketsIds.add(currentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketsIds;
    }

    public List<Reimbursement> getAllTicketsbyStatus(Status status) {
        List<Reimbursement> tickets = new ArrayList<>();
        Status statuses[] = Status.values();
        Rtype rtype[] = Rtype.values();
        // logger.info("Tickets status DAO = " + status.getvalue());

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_t WHERE statusid = ?");
            ps.setInt(1, status.getvalue());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reimbursement currentTicket = new Reimbursement(rs.getInt("reimbid"), rs.getDouble("amount"), rs.getTimestamp("submitted").toLocalDateTime(), rs.getTimestamp("resolved").toLocalDateTime(),
                        rs.getString("description"), rs.getBytes("receipt").toString(), rs.getInt("paymentid"), rs.getInt("authorid"), rs.getInt("resolverid"), statuses[rs.getInt("statusid")], rtype[rs.getInt("typeid")]);

                tickets.add(currentTicket);
            }
            // logger.info("Tickets status DAO = "
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public Reimbursement processedTicket(int ticket_id, Status status, int id) throws SQLException {
        Reimbursement ticket = null;
        Status statuses[] = Status.values();
        Rtype rtype[] = Rtype.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pd = con.prepareStatement("UPDATE reimbursement_t SET resolved = ?, resolverid = ?, statusid = ? WHERE reimbid = ?");
            pd.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pd.setInt(2, id);
            pd.setInt(3, status.getvalue());
            pd.setInt(4, ticket_id);
            int result = pd.executeUpdate();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_t WHERE reimbid = ?");
            ps.setInt(1, ticket_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ticket = new Reimbursement(rs.getInt("reimbid"), rs.getDouble("amount"), rs.getTimestamp("submitted").toLocalDateTime(), rs.getTimestamp("resolved").toLocalDateTime(),
                        rs.getString("description"), rs.getBytes("receipt").toString(), rs.getInt("paymentid"), rs.getInt("authorid"), rs.getInt("resolverid"), statuses[rs.getInt("statusid")], rtype[rs.getInt("typeid")]);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public List<Reimbursement> getAllTicketByID(int id) {
        List<Reimbursement> tickets = new ArrayList<>();
        Status statuses[] = Status.values();
        Rtype rtype[] = Rtype.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_t WHERE reimbid = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reimbursement currentTicket = new Reimbursement(rs.getInt("reimbid"), rs.getDouble("amount"), rs.getTimestamp("submitted").toLocalDateTime(), rs.getTimestamp("resolved").toLocalDateTime(),
                        rs.getString("description"), rs.getBytes("receipt").toString(), rs.getInt("paymentid"), rs.getInt("authorid"), rs.getInt("resolverid"), statuses[rs.getInt("statusid")], rtype[rs.getInt("typeid")]);

                tickets.add(currentTicket);
            }
            // logger.info("Tickets status DAO = "
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public Reimbursement updateTicket(UpdateTicketRequest req) {
        Reimbursement ticket = null;
        Status statuses[] = Status.values();
        Rtype rtype[] = Rtype.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pd = con.prepareStatement("UPDATE reimbursement_t SET amount = ?, description = ?, typeid = ?, submitted = ? WHERE reimbid = ?");
            pd.setDouble(1, Math.round(Double.parseDouble(req.getAmount()) * 100.0) / 100.0);
            pd.setString(2, req.getDescription());
            pd.setInt(3, req.getType().getvalue());
            pd.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pd.setInt(5, Integer.parseInt(req.getId()));

            int result = pd.executeUpdate();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_t WHERE reimbid = ?");
            ps.setInt(1, Integer.parseInt(req.getId()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ticket = new Reimbursement(rs.getInt("reimbid"), rs.getDouble("amount"), rs.getTimestamp("submitted").toLocalDateTime(), rs.getTimestamp("resolved").toLocalDateTime(),
                        rs.getString("description"), rs.getBytes("receipt").toString(), rs.getInt("paymentid"), rs.getInt("authorid"), rs.getInt("resolverid"), statuses[rs.getInt("statusid")], rtype[rs.getInt("typeid")]);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }
}