package domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Medicine extends Entity {
    private String name;
    private String producer;
    private BigDecimal price;
    private boolean isOTC;
    private int stock;

    public Medicine() { }

    public Medicine(int idEntity, String name, String producer, BigDecimal price, boolean isOTC, int stock) {
        super(idEntity);
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.isOTC = isOTC;
        this.stock = stock;
    }

    public Medicine(String name, String producer, BigDecimal price, boolean isOTC, int stock) {
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.isOTC = isOTC;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isOTC() {
        return isOTC;
    }

    public void setOTC(boolean OTC) {
        isOTC = OTC;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + getIdEntity() +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", price=" + price +
                ", isOTC=" + isOTC +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medicine medicine = (Medicine) o;
        return isOTC == medicine.isOTC &&
                stock == medicine.stock &&
                Objects.equals(name, medicine.name) &&
                Objects.equals(producer, medicine.producer) &&
                Objects.equals(price, medicine.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, producer, price, isOTC, stock);
    }
}
