package com.github.luchici.cityservice.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CITIES")
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "city_id")
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z -]+$")
    @Size(min = 2, max = 50)
    private String cityName;
    @Pattern(regexp = "^[a-zA-Z -]+$")
    @Size(min = 2, max = 50)
    private String country;
    @OneToMany(cascade = ALL, fetch = LAZY, orphanRemoval = true, mappedBy = "city")
    private Set<ImageData> images;
    @OneToMany(cascade = ALL, fetch = LAZY, mappedBy = "city")
    @JsonManagedReference
    private Set<Attraction> attractions;

    public City(String cityName, String country) {
        this.cityName = cityName;
        this.country = country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(cityName, city.cityName) &&
                Objects.equals(country, city.country);
    }

    @PrePersist
    private void setCityToAttractions() {
        if (attractions != null) {
            attractions.iterator().forEachRemaining(att -> att.setCity(this));
        }
    }
}
