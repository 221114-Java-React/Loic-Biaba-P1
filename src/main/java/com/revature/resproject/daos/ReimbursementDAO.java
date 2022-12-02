package com.revature.resproject.daos;

import com.revature.resproject.models.Reimbursement;
import com.revature.resproject.utils.ConnectionFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReimbursementDAO implements CrudDAO<Reimbursement>{

    @Override
    public void save(Reimbursement obj) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursement_t  (reimbid, amount, submitted, resolved, description, receipt, paymentid, authorid, resolverid, statusid, typeid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, obj.getId());
            ps.setDouble(2, Math.round(obj.owedMoney() * 100.0) / 100.0);
            ps.setTimestamp(3, obj.resolveSubDate());
            ps.setTimestamp(4, obj.resolveResDate());
            ps.setString(5, obj.getDescription());
            ps.setBytes(6, obj.getReceipt());
            ps.setInt(7, obj.getPaymentId());
            ps.setInt(8, obj.getAuthorId());
            ps.setInt(9, obj.getResolverId());
            ps.setInt(10, obj.getStatus().ordinal());
            ps.setInt(11, obj.getRtype().ordinal());
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
        return null;
    }
}
