package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="customer_mobile_tariff",
            joinColumns=  @JoinColumn(name="customer_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="mobile_tariff_id", referencedColumnName="id"))
    private Set<MobileTariff> mobileTariffs = new HashSet<MobileTariff>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="customer_home_tariff",
            joinColumns=  @JoinColumn(name="customer_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="home_tariff_id", referencedColumnName="id"))
    private Set<HomeTariff> homeTariffs = new HashSet<HomeTariff>();

    public Customer() {

    }

    public void addMobileTariff(MobileTariff mobileTariff) {
        if (mobileTariff != null) {
            mobileTariffs.add(mobileTariff);
            mobileTariff.getCustomers().add(this);
        }
    }

    public void removeMobileTariff(MobileTariff mobileTariff) {
        if (mobileTariff != null && mobileTariffs.contains(mobileTariff)) {
            mobileTariffs.remove(mobileTariff);
            mobileTariff.getCustomers().remove(this);
        }
    }

    public void addHomeTariff(HomeTariff homeTariff) {
        if (homeTariff != null) {
            homeTariffs.add(homeTariff);
            homeTariff.getCustomers().add(this);
        }
    }

    public void removeHomeTariff(HomeTariff homeTariff) {
        if (homeTariff != null && homeTariffs.contains(homeTariff)) {
            homeTariffs.remove(homeTariff);
            homeTariff.getCustomers().remove(this);
        }
    }
}