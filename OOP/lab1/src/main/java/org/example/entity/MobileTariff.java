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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="customer_mobile_tariff",
            joinColumns=  @JoinColumn(name="mobile_tariff_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="customer_id", referencedColumnName="id"))
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
