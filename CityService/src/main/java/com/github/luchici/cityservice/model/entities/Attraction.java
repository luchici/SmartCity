package com.github.luchici.cityservice.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Table(name = "ATTRACTIONS")
public class Attraction {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "attraction_id")
    private Integer id;
    private String name;
    @ManyToOne(cascade = {DETACH, REFRESH, MERGE, PERSIST}, fetch = LAZY, optional = false)
    // @JoinTable(
    //         name = "city_images",
    //         joinColumns = @JoinColumn(name = "image_id"),
    //         inverseJoinColumns = @JoinColumn(name = "city_id"))
    @JoinColumn(name = "city_id")
    @JsonBackReference
    private City city;

    public Attraction(String name, City city) {
        this.name = name;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, cityName=" + city.getCityName() +
                '}';
    }
}
