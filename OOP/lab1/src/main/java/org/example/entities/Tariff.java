package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Tariff {
    @Id
    @GeneratedValue
    protected long id;

    protected String name;

    @Column(name = "price_per_month")
    protected int pricePerMonth;


    public Tariff(String name, int pricePerMonth) {
        this.name = name;
        this.pricePerMonth = pricePerMonth;
    }

    public Tariff() {

    }
}
