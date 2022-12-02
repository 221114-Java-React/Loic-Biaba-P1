package com.revature.resproject.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Reimbursement {
    private int id;
    private double amount;
    private LocalDateTime submitDate;
    private LocalDateTime resolveDate;
    private String description;
    private String receipt;

    private int paymentId;
    private int authorId;
    private int resolverId;
    private Status status;
    private Rtype rtype;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.US);

    public Reimbursement() {
        super();
    }

    public Reimbursement(int id, double amount, LocalDateTime submitDate, LocalDateTime resolveDate, String description, String receipt, int paymentId, int authorId, int resolverId, Status status, Rtype rtype) {
        this.id = id;
        this.amount = amount;
        this.submitDate = submitDate;
        this.resolveDate = resolveDate;
        this.description = description;
        this.receipt = receipt;
        this.paymentId = paymentId;
        this.authorId = authorId;
        this.resolverId = resolverId;
        this.status = status;
        this.rtype = rtype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return "US$ " + amount;
    }
    public double owedMoney() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSubmitDate() {
        String time =  dateFormat.format(submitDate);
        return time;
    }

    public Timestamp resolveSubDate() {
        return Timestamp.valueOf(submitDate);
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public Timestamp resolveResDate() {
        return Timestamp.valueOf(resolveDate);
    }

    public String getResolveDate() {
        String time = dateFormat.format(resolveDate);
       return time;
    }

    public void setResolveDate(LocalDateTime resolveDate) {
        this.resolveDate = resolveDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] showReceipt() throws IOException {
        byte[] image;

        if (receipt == null || receipt.equals("")) {
            image = new byte[]{1, 0, 0, 1, 0, 0, 1};
            return image;
        }

        image = Files.readAllBytes(Paths.get(receipt));
        return image;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }


    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getResolverId() {
        return resolverId;
    }

    public void setResolverId(int resolverId) {
        this.resolverId = resolverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Rtype getRtype() {
        return rtype;
    }

    public void setRtype(Rtype rtype) {
        this.rtype = rtype;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "Reimbursement Id=" + id +
                ", Amount=" + amount +
                ", Submited Date/Time=" + dateFormat.format(submitDate) +
                ", Resolved Date/Time=" + dateFormat.format(resolveDate) +
                ", Ticket description='" + description + '\'' +
                ", Ticket receipt='" + receipt + '\'' +
                ", Payment Id=" + paymentId +
                ", Author Id=" + authorId +
                ", Resolver Id=" + resolverId +
                ", Ticket status=" + status +
                ", Ticket type=" + rtype +
                '}';
    }
}
