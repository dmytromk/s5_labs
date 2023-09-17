package org.example.entities;

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

    @ManyToMany(mappedBy = "mobileTariffs")
    private Set<Customer> customers = new HashSet<Customer>();


    public MobileTariff(long id, int pricePerMonth, int minutes, int sms) {
        super(id, pricePerMonth);
        this.minutes = minutes;
        this.sms = sms;
    }

    public MobileTariff() {
        super();
    }

    @Override
    public String toString() {
        return "MobileTariff{" +
                "minutes=" + this.minutes +
                ", sms=" + this.sms +
                ", id=" + this.id +
                ", pricePerMonth=" + this.pricePerMonth +
                '}';
    }
}
