package com.sagarannaldas.shopping.cart.controllers;


import com.sagarannaldas.shopping.cart.dtos.AddItemToCartRequest;
import com.sagarannaldas.shopping.cart.dtos.CartDto;
import com.sagarannaldas.shopping.cart.dtos.CartItemDto;
import com.sagarannaldas.shopping.cart.dtos.UpdateCartItemRequest;
import com.sagarannaldas.shopping.cart.exceptions.CartNotFoundException;
import com.sagarannaldas.shopping.cart.exceptions.ProductNotFoundException;
import com.sagarannaldas.shopping.cart.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var cartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable(name = "id") UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request
    ) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(
            @PathVariable(name = "cartId") UUID cartId
    ) {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItem(
                cartId, productId, request.getQuantity()
        );
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId
    ) {
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable(name = "cartId") UUID cartId
    ) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product not found in the cart."));
    }
}