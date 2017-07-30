package com.vbashur.catalogue.repository;

import com.vbashur.catalogue.model.Category;
import com.vbashur.catalogue.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public interface ProductJpaRepository extends JpaRepository<Product, UUID> {

    public Iterable<Product> findByName(String name);

    public Product findByUuid(UUID uuid);

    public Iterable<Product> findByCategory(Category category);

}
