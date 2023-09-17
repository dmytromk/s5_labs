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

    @Column(name = "price_per_month")
    protected int pricePerMonth;


    public Tariff(long id, int pricePerMonth) {
        this.id = id;
        this.pricePerMonth = pricePerMonth;
    }

    public Tariff() {

    }
}
