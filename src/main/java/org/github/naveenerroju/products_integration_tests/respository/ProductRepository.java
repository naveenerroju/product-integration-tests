package org.github.naveenerroju.products_integration_tests.respository;

import org.github.naveenerroju.products_integration_tests.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
