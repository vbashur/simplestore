package com.vbashur.catalogue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbashur.catalogue.CatalogueApplication;
import com.vbashur.catalogue.model.Category;
import com.vbashur.catalogue.model.Product;
import com.vbashur.catalogue.repository.CategoryJpaRepository;
import com.vbashur.catalogue.repository.ProductJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogueApplication.class)
@WebAppConfiguration
public class CatalogueControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MockMvc mvc;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        categoryJpaRepository.deleteAllInBatch();
        productJpaRepository.deleteAllInBatch();
    }

    @Test
    public void testAddGetDeleteCategory() throws Exception {
        String newCategoryName = UUID.randomUUID().toString();
        Category category = new Category();
        category.setName(newCategoryName);

        // Add category
        mvc.perform(post("/api/catalogue/category")
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        // List all categories
        MvcResult result = mvc.perform(get("/api/catalogue/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(category.getName())))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Category[] categories = mapper.readValue(result.getResponse().getContentAsString().getBytes(), Category[].class);

        // Read the specific category by UUID
        mvc.perform(get("/api/catalogue/category/" + categories[0].getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(category.getName())));

        // Read the specific category by name
        mvc.perform(get("/api/catalogue/category/name/" + category.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(category.getName())));

        // Delete the category
        mvc.perform(delete("/api/catalogue/category/" + categories[0].getUuid())
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        // Check that there are no categories
        mvc.perform(get("/api/catalogue/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));

    }



    @Test (expected = NestedServletException.class)
    public void testCategoryNotFoundURLRequest() throws Exception {
        String newCategoryName = UUID.randomUUID().toString();
        Category category = new Category();
        category.setName(newCategoryName);

        // Add category
        mvc.perform(post("/api/catalogue/category")
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        // Attempt to fetch unregistered category by uuid
        mvc.perform(get("/api/catalogue/category/" + UUID.randomUUID().toString())
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Attempt to fetch unregistered category by name
        mvc.perform(get("/api/catalogue/category/name/" + UUID.randomUUID().toString())
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Attempt to delete unregistered category by name
        mvc.perform(delete("/api/catalogue/category/" + UUID.randomUUID().toString())
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testInvalidURLRequest() throws Exception {
        String newCategoryName = UUID.randomUUID().toString();
        Category category = new Category();
        category.setName(newCategoryName);

        // Add category
        mvc.perform(post("/api/catalogue/category" + UUID.randomUUID().toString())
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());


    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }


}
