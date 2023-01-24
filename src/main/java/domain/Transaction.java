package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction extends Entity {
    private int clientCardNumber;
    private int quantity;
    private LocalDateTime dateTime;
    private int drugId;

    public Transaction() { }

    public Transaction(int idEntity, int clientCardNumber, int quantity, LocalDateTime dateTime, int drugId) {
        super(idEntity);
        this.clientCardNumber = clientCardNumber;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.drugId = drugId;
    }

    public Transaction(int clientCardNumber, int quantity, LocalDateTime dateTime, int drugId) {
        this.clientCardNumber = clientCardNumber;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.drugId = drugId;
    }

    public int getClientCardNumber() {
        return clientCardNumber;
    }

    public void setClientCardNumber(int clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getIdEntity() +
                ", clientCardNumber=" + clientCardNumber +
                ", quantity=" + quantity +
                ", dateTime=" + dateTime +
                ", drugId=" + drugId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transaction that = (Transaction) o;
        return clientCardNumber == that.clientCardNumber &&
                quantity == that.quantity &&
                drugId == that.drugId &&
                Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientCardNumber, quantity, dateTime, drugId);
    }
}
