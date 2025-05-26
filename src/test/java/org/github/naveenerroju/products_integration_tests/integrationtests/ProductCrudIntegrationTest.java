package org.github.naveenerroju.products_integration_tests.integrationtests;

import org.github.naveenerroju.products_integration_tests.model.Product;
import org.github.naveenerroju.products_integration_tests.respository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductCrudIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository repository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/products";
    }

    private static String createdProductId;

    @Test
    @Order(1)
    void testCreateProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(999.99);
        product.setDescription("A high-end gaming laptop");

        ResponseEntity<Product> response = restTemplate.postForEntity(getBaseUrl(), product, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        createdProductId = response.getBody().getId();

        Product dbProduct = repository.findById(createdProductId).orElse(null);
        assertThat(dbProduct).isNotNull();
        assertThat(dbProduct.getName()).isEqualTo("Laptop");
    }

    @Test
    @Order(2)
    void testGetProductById() {
        ResponseEntity<Product> response = restTemplate.getForEntity(getBaseUrl() + "/" + createdProductId, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdProductId);
    }

    @Test
    @Order(3)
    void testUpdateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Laptop");
        updatedProduct.setPrice(899.99);
        updatedProduct.setDescription("Updated description");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> requestUpdate = new HttpEntity<>(updatedProduct, headers);

        ResponseEntity<Product> response = restTemplate.exchange(getBaseUrl() + "/" + createdProductId,
                HttpMethod.PUT, requestUpdate, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Laptop");

        Product dbProduct = repository.findById(createdProductId).orElse(null);
        assertThat(dbProduct).isNotNull();
        assertThat(dbProduct.getPrice()).isEqualTo(899.99);
    }

    @Test
    @Order(4)
    void testGetAllProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity(getBaseUrl(), Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(5)
    void testDeleteProduct() {
        restTemplate.delete(getBaseUrl() + "/" + createdProductId);

        boolean exists = repository.findById(createdProductId).isPresent();
        assertThat(exists).isFalse();
    }
}