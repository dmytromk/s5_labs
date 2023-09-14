package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class MobileTariff extends Tariff {
    private int minutes;

    private int sms;

    @ManyToMany(mappedBy = "customer_mobile_tariff")
    private Set<Customer> customers = new HashSet<Customer>();


    public MobileTariff(long id, int pricePerMonth, int minutes, int sms) {
        super(id, pricePerMonth);
        this.minutes = minutes;
        this.sms = sms;
    }

    public MobileTariff() {
        super();
    }
}
