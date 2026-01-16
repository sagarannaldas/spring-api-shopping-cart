package com.sagarannaldas.shopping.cart.mappers;

import com.sagarannaldas.shopping.cart.dtos.RegisterUserRequest;
import com.sagarannaldas.shopping.cart.dtos.UpdateUserRequest;
import com.sagarannaldas.shopping.cart.dtos.UserDto;
import com.sagarannaldas.shopping.cart.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
