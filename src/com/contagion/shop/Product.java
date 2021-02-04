package com.contagion.shop;

import java.time.LocalDate;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final String companyName;
    private final LocalDate bbdate;

    public Product(String name, String companyName, LocalDate bbdate){
        this.id = UUID.randomUUID();
        this.name = name;
        this.companyName = companyName;
        this.bbdate = bbdate;
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

    public UUID getId() {
        return id;
    }
}
