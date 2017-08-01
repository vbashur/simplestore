package com.vbashur.catalogue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import java.util.List;

@Entity
public class Category extends EntityBase {

    @ManyToMany(mappedBy="category")
    List<Product> products;

    @Column(unique = true, nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PreRemove
    private void removeCategoryFromUsers() {
        for (Product p : products) {
            p.getCategory().remove(this);
        }
    }

}
