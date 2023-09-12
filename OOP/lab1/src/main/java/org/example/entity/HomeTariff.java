package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class HomeTariff extends Tariff {
    @Column(name = "data_allowance_mb")
    private int dataAllowanceMb;

    @Column(name = "speed_mbps")
    private int speedMbps;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="customer_home_tariff",
            joinColumns=  @JoinColumn(name="home_tariff_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="customer_id", referencedColumnName="id"))
    private Set<Customer> customers = new HashSet<Customer>();


    public HomeTariff(long id, int pricePerMonth, int dataAllowanceMb, int speedMbps) {
        super(id, pricePerMonth);
        this.dataAllowanceMb = dataAllowanceMb;
        this.speedMbps = speedMbps;
    }

    public HomeTariff() {

    }
}
