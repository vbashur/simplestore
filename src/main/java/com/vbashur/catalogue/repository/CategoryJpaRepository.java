package com.vbashur.catalogue.repository;

import com.vbashur.catalogue.model.Category;
import com.vbashur.catalogue.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public interface CategoryJpaRepository extends JpaRepository<Category, UUID> {

    public Category findByName(String name);

}
