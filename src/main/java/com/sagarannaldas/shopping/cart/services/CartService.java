package com.sagarannaldas.shopping.cart.services;

import com.sagarannaldas.shopping.cart.dtos.CartDto;
import com.sagarannaldas.shopping.cart.dtos.CartItemDto;
import com.sagarannaldas.shopping.cart.entities.Cart;
import com.sagarannaldas.shopping.cart.exceptions.CartNotFoundException;
import com.sagarannaldas.shopping.cart.exceptions.ProductNotFoundException;
import com.sagarannaldas.shopping.cart.mappers.CartMapper;
import com.sagarannaldas.shopping.cart.repositories.CartRepository;
import com.sagarannaldas.shopping.cart.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(
            UUID cartId,
            Long productId,
            Integer quantity
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        var cartItem = cart.getItem(productId);

        if (cartItem == null)
            throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.clear();

        cartRepository.save(cart);
    }
}
