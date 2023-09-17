package org.example.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @CreationTimestamp
    @Column(name = "created_on")
    private Instant createdOn;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="customer_mobile_tariff",
            joinColumns=  @JoinColumn(name="customer_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="mobile_tariff_id", referencedColumnName="id"))
    private Set<MobileTariff> mobileTariffs = new HashSet<MobileTariff>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "customer_home_tariff",
            joinColumns = @JoinColumn(name="customer_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="home_tariff_id", referencedColumnName="id"))
    private Set<HomeTariff> homeTariffs = new HashSet<HomeTariff>();

    public Customer() {

    }

    public void addMobileTariff(MobileTariff mobileTariff) {
        if (mobileTariff != null) {
            this.mobileTariffs.add(mobileTariff);
            mobileTariff.addCustomer(this);
        }
    }

    public void removeMobileTariff(MobileTariff mobileTariff) {
        if (mobileTariff != null) {
            this.mobileTariffs.remove(mobileTariff);
            mobileTariff.removeCustomer(this);
        }
    }

    public void addHomeTariff(HomeTariff homeTariff) {
        if (homeTariff != null) {
            this.homeTariffs.add(homeTariff);
            homeTariff.addCustomer(this);
        }
    }

    public void removeHomeTariff(HomeTariff homeTariff) {
        if (homeTariff != null) {
            this.homeTariffs.remove(homeTariff);
            homeTariff.removeCustomer(this);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", createdOn=" + this.createdOn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Customer))
            return false;

        Customer other = (Customer) o;

        return Objects.equals(this.id, other.getId());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}