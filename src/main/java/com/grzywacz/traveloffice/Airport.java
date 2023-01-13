package com.grzywacz.traveloffice;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString
public class Airport extends BasicEntity {

    private String name;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
