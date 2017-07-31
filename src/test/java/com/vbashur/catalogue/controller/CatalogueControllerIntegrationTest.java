package com.vbashur.catalogue.controller;

import com.vbashur.catalogue.CatalogueApplication;
import com.vbashur.catalogue.model.Category;
import com.vbashur.catalogue.repository.CategoryJpaRepository;
import com.vbashur.catalogue.repository.ProductJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testAddNewCategory() throws Exception {
        String newCategoryName = UUID.randomUUID().toString();
        Category category = new Category();
        category.setName(newCategoryName);
        mvc.perform(post("/api/catalogue/category")
                .content(this.json(category))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        ).andExpect(status().isOk());

    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
