package com.sagarannaldas.shopping.cart.mappers;

import com.sagarannaldas.shopping.cart.dtos.CartDto;
import com.sagarannaldas.shopping.cart.dtos.CartItemDto;
import com.sagarannaldas.shopping.cart.entities.Cart;
import com.sagarannaldas.shopping.cart.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}