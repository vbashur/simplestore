package com.vbashur.catalogue.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product extends EntityBase {

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private Double price;

    @Column
    private String currency;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) { this.currency = currency; }

}
