package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "home_tariff")
@Getter
@Setter
public class HomeTariff extends Tariff {
    @Column(name = "data_allowance_mb")
    private int dataAllowanceMb;

    @Column(name = "speed_mbps")
    private int speedMbps;

    @ManyToMany(mappedBy = "homeTariffs")
    private Set<Customer> customers = new HashSet<Customer>();


    public HomeTariff(long id, int pricePerMonth, int dataAllowanceMb, int speedMbps) {
        super(id, pricePerMonth);
        this.dataAllowanceMb = dataAllowanceMb;
        this.speedMbps = speedMbps;
    }

    public HomeTariff() {

    }

    @Override
    public String toString() {
        return "HomeTariff{" +
                "id =" + this.id +
                ", pricePerMonth=" + this.pricePerMonth +
                ", dataAllowanceMb=" + this.dataAllowanceMb +
                ", speedMbps=" + this.speedMbps +
                '}';
    }
}
