package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "mobile_tariff")
@Getter
@Setter
public class MobileTariff extends Tariff {
    private int minutes;

    private int sms;

    @ManyToMany(mappedBy = "mobileTariffs", fetch = FetchType.EAGER)
    private Set<Customer> customers = new HashSet<Customer>();


    public MobileTariff(String name, int pricePerMonth, int minutes, int sms) {
        super(name, pricePerMonth);
        this.minutes = minutes;
        this.sms = sms;
    }

    public MobileTariff() {
        super();
    }

    @Override
    public String toString() {
        return "MobileTariff = {" +
                "id=" + this.id +
                ", name=" + this.name +
                ", pricePerMonth=" + this.pricePerMonth +
                ", minutes=" + this.minutes +
                ", sms=" + this.sms +
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

        if (!(o instanceof MobileTariff))
            return false;

        MobileTariff other = (MobileTariff) o;

        return Objects.equals(this.id, other.getId());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
