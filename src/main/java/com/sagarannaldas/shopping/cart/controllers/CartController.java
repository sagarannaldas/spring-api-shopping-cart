package com.sagarannaldas.shopping.cart.controllers;


import com.sagarannaldas.shopping.cart.dtos.AddItemToCartRequest;
import com.sagarannaldas.shopping.cart.dtos.CartDto;
import com.sagarannaldas.shopping.cart.dtos.CartItemDto;
import com.sagarannaldas.shopping.cart.dtos.UpdateCartItemRequest;
import com.sagarannaldas.shopping.cart.entities.Cart;
import com.sagarannaldas.shopping.cart.entities.CartItem;
import com.sagarannaldas.shopping.cart.mappers.CartMapper;
import com.sagarannaldas.shopping.cart.repositories.CartRepository;
import com.sagarannaldas.shopping.cart.repositories.ProductRepository;
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
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addItemToCart(
            @PathVariable(name = "id") UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build();

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable(name = "cartId") UUID cartId
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();

        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found")
            );

        var cartItem = cart.getItem(productId);
        
        if (cartItem == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart")
            );

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }
}