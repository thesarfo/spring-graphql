package dev.thesarfo.springgraphql.service;

import dev.thesarfo.springgraphql.entity.Product;
import dev.thesarfo.springgraphql.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // sales tea; update the stock of product in the inventory
    public Product updateStock(int id, int stock) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setStock(stock);
        return productRepository.save(existingProduct);
    }

    // warehouse: receive new shipment
    public Product receiveNewShipment(int id, int stock) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setStock(existingProduct.getStock() + stock);

        return productRepository.save(existingProduct);
    }
}
