package com.sagarannaldas.shopping.cart.mappers;

import com.sagarannaldas.shopping.cart.dtos.UserDto;
import com.sagarannaldas.shopping.cart.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
}
