package com.vbashur.catalogue.controller;

import com.vbashur.catalogue.model.Category;
import com.vbashur.catalogue.model.Product;
import com.vbashur.catalogue.repository.CategoryJpaRepository;
import com.vbashur.catalogue.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue/")
public class CatalogueController {


    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;


//    @RequestMapping(path = "/repo", method = RequestMethod.GET)
//    public Iterable<Product> findByRepo() throws IOException {
//
//        Category category = new Category();
//        category.setName("categoryTest");
//
//        Product product = new Product();
//        product.setName("productName");
//        product.setPrice(12.4);
//        product.setCategory(Arrays.asList(category));
//        productJpaRepository.saveAndFlush(product);
//        return productJpaRepository.findAll();
//    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public void saveProduct(@RequestBody Product product) {
        Product newProduct = new Product();
        newProduct.setCategory(product.getCategory());
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        productJpaRepository.save(newProduct);
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Iterable<Product> listProducts() {
        return productJpaRepository.findAll();
    }

    @RequestMapping(value = "/product/name/{name}", method = RequestMethod.GET)
    public Iterable<Product> getProductByName(@PathVariable String name) {
        return productJpaRepository.findByName(name);
    }

    @RequestMapping(value = "/product/{uuid}", method = RequestMethod.GET)
    public Product getProductByUuid(@PathVariable UUID uuid) {
        return productJpaRepository.findByUuid(uuid);
    }

    @RequestMapping(value = "/product/category/name/{categoryName}", method = RequestMethod.GET)
    public Iterable<Product> getProductsByCategoryName(@PathVariable String categoryName) {
        Category category = categoryJpaRepository.findByName(categoryName);
        return productJpaRepository.findByCategory(category);
    }

    @RequestMapping(value = "/product/category/{categoryUuid}", method = RequestMethod.GET)
    public Iterable<Product> getProductsByCategoryUuid(@PathVariable UUID categoryUuid) {
        Category category = categoryJpaRepository.findByUuid(categoryUuid);
        return productJpaRepository.findByCategory(category);
    }

    @RequestMapping(value = "/product/{uuid}", method = RequestMethod.DELETE)
    public void deleteProductByUuid(@PathVariable UUID uuid) {
        productJpaRepository.delete(uuid);
    }

    @RequestMapping(value = "/product/{uuid}", method = RequestMethod.PATCH)
    public Iterable<Product> updateProductByUuid(@PathVariable UUID uuid, @RequestBody Product product) {
        product.setUuid(uuid);
        return productJpaRepository.save(Arrays.asList(product));
    }

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public void saveCategory(@RequestBody Category category) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        categoryJpaRepository.save(newCategory);
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public Iterable<Category> listCategories() {
        return categoryJpaRepository.findAll();
    }

    @RequestMapping(value = "/category/name/{name}", method = RequestMethod.GET)
    public Category getCategoryByName(@PathVariable String name) {
        return categoryJpaRepository.findByName(name);
    }

    @RequestMapping(value = "/category/{uuid}", method = RequestMethod.GET)
    public Category getCategoryByUuid(@PathVariable UUID uuid) {
        return categoryJpaRepository.findByUuid(uuid);
    }

    @RequestMapping(value = "/category/{uuid}", method = RequestMethod.DELETE)
    public void deleteCategoryByUuid(@PathVariable UUID uuid) {
        categoryJpaRepository.delete(uuid);
    }

    @RequestMapping(value = "/category/{uuid}", method = RequestMethod.PATCH)
    public Iterable<Category> updateCategoryByUuid(@PathVariable UUID uuid, @RequestBody Category category) {
        category.setUuid(uuid);
        return categoryJpaRepository.save(Arrays.asList(category));
    }
}
