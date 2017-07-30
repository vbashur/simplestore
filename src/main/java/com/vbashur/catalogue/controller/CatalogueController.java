package com.vbashur.catalogue.controller;

import com.vbashur.catalogue.model.Category;
import com.vbashur.catalogue.model.Product;
import com.vbashur.catalogue.repository.CategoryJpaRepository;
import com.vbashur.catalogue.repository.ProductJpaRepository;
import com.vbashur.catalogue.service.CurrencyExchangeService;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue/")
public class CatalogueController {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public void saveProduct(@RequestBody Product product) {
        Product newProduct = new Product();
        newProduct.setCategory(product.getCategory());
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setCurrency(product.getCurrency());
        productJpaRepository.save(newProduct);
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Iterable<Product> listProducts(@RequestParam(required = false) String currency) {
        if (StringUtils.isNotEmpty(currency)) {
            return convertProductPrices(productJpaRepository.findAll(), currency);
        }
        return productJpaRepository.findAll();
    }

    @RequestMapping(value = "/product/name/{name}", method = RequestMethod.GET)
    public Iterable<Product> getProductByName(@PathVariable String name,
                                              @RequestParam(required = false) String currency) {
        if (StringUtils.isNotEmpty(currency)) {
            return convertProductPrices(productJpaRepository.findByName(name), currency);
        }
        return productJpaRepository.findByName(name);
    }

    @RequestMapping(value = "/product/{uuid}", method = RequestMethod.GET)
    public Product getProductByUuid(@PathVariable UUID uuid,
                                    @RequestParam(required = false) String currency) {
        if (StringUtils.isNotEmpty(currency)) {
            return convertProductPrice(productJpaRepository.findOne(uuid), currency);
        }
        return productJpaRepository.findOne(uuid);
    }

    @RequestMapping(value = "/product/category/name/{categoryName}", method = RequestMethod.GET)
    public Iterable<Product> getProductsByCategoryName(@PathVariable String categoryName,
                                                       @RequestParam(required = false) String currency) {
        Category category = categoryJpaRepository.findByName(categoryName);
        if (StringUtils.isNotEmpty(currency)) {
            return convertProductPrices(productJpaRepository.findByCategory(category), currency);
        }
        return productJpaRepository.findByCategory(category);
    }

    @RequestMapping(value = "/product/category/{categoryUuid}", method = RequestMethod.GET)
    public Iterable<Product> getProductsByCategoryUuid(@PathVariable UUID categoryUuid,
                                                       @RequestParam(required = false) String currency) {
        Category category = categoryJpaRepository.findOne(categoryUuid);
        if (StringUtils.isNotEmpty(currency)) {
            return convertProductPrices(productJpaRepository.findByCategory(category), currency);
        }
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
        return categoryJpaRepository.findOne(uuid);
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

    private Iterable<Product> convertProductPrices(Iterable<Product> products, String targetCurrency) {
        List<Product> convertedProducts = new LinkedList<>();
        for (Product product : products) {
            Product convertedProduct = convertProductPrice(product, targetCurrency);
            convertedProducts.add(convertedProduct);
        }
        return convertedProducts;
    }

    private Product convertProductPrice(Product product, String targetCurrency) {
        Double convertedPrice = currencyExchangeService.getRate(product.getPrice(), product.getCurrency(), targetCurrency);
        Product convertedProduct = SerializationUtils.clone(product);
        convertedProduct.setPrice(convertedPrice);
        convertedProduct.setCurrency(targetCurrency);
        return convertedProduct;
    }
}
