package dev.thesarfo.springgraphql;

import dev.thesarfo.springgraphql.entity.Product;
import dev.thesarfo.springgraphql.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringGraphqlApplication {

    private final ProductRepository productRepository;

    public SpringGraphqlApplication(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void initDB() {
        List<Product> products = Stream.of(
                new Product("Laptop", "Electronics", 1200.00f, 10),
                new Product("SmartPhone", "Electronics", 1200.00f, 10),
                new Product("Office Chair", "Furniture", 1200.00f, 10),
                new Product("Water Bottle", "Accessories", 1200.00f, 10)
        ).toList();

        productRepository.saveAll(products);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringGraphqlApplication.class, args);
    }

}
