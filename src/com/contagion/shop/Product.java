package com.contagion.shop;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final String companyName;
    private final LocalDate bbdate;
    private final String from;
    private final StringProperty to = new SimpleStringProperty();
    private final StringProperty deliveredBy = new SimpleStringProperty();
    private final StringProperty boughtBy = new SimpleStringProperty();
    private final BooleanProperty exists = new SimpleBooleanProperty(true);
    private final BooleanProperty disposed = new SimpleBooleanProperty(false);

    public Product(String name, String companyName, LocalDate bbdate, String from){
        this.id = UUID.randomUUID();
        this.name = name;
        this.companyName = companyName;
        this.bbdate = bbdate;
        this.from = from;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", bbdate=" + bbdate +
                '}';
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBbdate() {
        return bbdate.toString();
    }

    public String getFrom() {
        return from;
    }

    public StringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public StringProperty deliveredByProperty() {
        return deliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy.set(deliveredBy);
    }

    public StringProperty boughtByProperty() {
        return boughtBy;
    }

    public void setBoughtBy(String boughtBy) {
        this.boughtBy.set(boughtBy);
    }

    public BooleanProperty existsProperty() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists.set(exists);
    }

    public BooleanProperty disposedProperty() {
        return disposed;
    }

    public void setDisposed(boolean disposed) {
        this.disposed.set(disposed);
    }
}
