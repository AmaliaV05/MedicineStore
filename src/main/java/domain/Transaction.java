package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction extends Entity {
    private int clientCardNumber;
    private int quantity;
    private LocalDateTime dateTime;
    private int medicineId;

    public Transaction() { }

    public Transaction(int idEntity, int clientCardNumber, int quantity, LocalDateTime dateTime, int drugId) {
        super(idEntity);
        this.clientCardNumber = clientCardNumber;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.medicineId = drugId;
    }

    public Transaction(int clientCardNumber, int quantity, LocalDateTime dateTime, int drugId) {
        this.clientCardNumber = clientCardNumber;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.medicineId = drugId;
    }

    public Transaction(int clientCardNumber, int quantity, int medicineId) {
        this.clientCardNumber = clientCardNumber;
        this.quantity = quantity;
        this.medicineId = medicineId;
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

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getIdEntity() +
                ", clientCardNumber=" + clientCardNumber +
                ", quantity=" + quantity +
                ", dateTime=" + dateTime +
                ", medicineId=" + medicineId +
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
                medicineId == that.medicineId &&
                Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientCardNumber, quantity, dateTime, medicineId);
    }
}
