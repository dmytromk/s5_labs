package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
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

    @ManyToMany(mappedBy = "homeTariffs", fetch = FetchType.EAGER)
    private Set<Customer> customers = new HashSet<Customer>();


    public HomeTariff(String name, int pricePerMonth, int dataAllowanceMb, int speedMbps) {
        super(name, pricePerMonth);
        this.dataAllowanceMb = dataAllowanceMb;
        this.speedMbps = speedMbps;
    }

    public HomeTariff() {

    }

    @Override
    public String toString() {
        return "HomeTariff{" +
                "id=" + this.id +
                ", pricePerMonth=" + this.pricePerMonth +
                ", dataAllowanceMb=" + this.dataAllowanceMb +
                ", speedMbps=" + this.speedMbps +
                '}';
    }

    public void addCustomer(Customer customer){
        this.customers.add(customer);
    }

    public void removeCustomer(Customer customer){
        this.customers.remove(customer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof HomeTariff))
            return false;

        HomeTariff other = (HomeTariff) o;

        return Objects.equals(this.id, other.getId());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
