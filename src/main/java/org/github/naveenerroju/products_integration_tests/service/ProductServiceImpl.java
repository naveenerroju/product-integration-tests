package org.github.naveenerroju.products_integration_tests.service;

import org.github.naveenerroju.products_integration_tests.model.Product;
import org.github.naveenerroju.products_integration_tests.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product updateProduct(String id, Product product) {
        Product existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setDescription(product.getDescription());
        return repository.save(existing);
    }

    @Override
    public void deleteProduct(String id) {
        repository.deleteById(id);
    }
}