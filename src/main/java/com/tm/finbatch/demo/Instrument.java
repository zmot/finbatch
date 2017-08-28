package com.tm.finbatch.demo;

import java.util.Date;

public class Instrument {
    String type;
    Date createdDate;
    double amount;

    public Instrument() {
    }

    public Instrument(String type, Date createdDate, double amount) {
        this.type = type;
        this.createdDate = createdDate;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instrument that = (Instrument) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return createdDate != null ? createdDate.equals(that.createdDate) : that.createdDate == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type != null ? type.hashCode() : 0;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "type='" + type + '\'' +
                ", createdDate=" + createdDate +
                ", amount=" + amount +
                '}';
    }
}
