package com.sagarannaldas.shopping.cart.controllers;

import com.sagarannaldas.shopping.cart.dtos.ProductDto;
import com.sagarannaldas.shopping.cart.entities.Product;
import com.sagarannaldas.shopping.cart.mappers.ProductMapper;
import com.sagarannaldas.shopping.cart.repositories.CategoryRepository;
import com.sagarannaldas.shopping.cart.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.getProductsByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }

        var productsDto = products.stream()
                .map(productMapper::toProductDto)
                .toList();
        return ResponseEntity.ok(productsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable Long id
    ) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        product = productRepository.save(product);
        var productDtoNew = productMapper.toProductDto(product);
        var uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productDtoNew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productMapper.updateProduct(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}