package com.sagarannaldas.shopping.cart.mappers;

import com.sagarannaldas.shopping.cart.dtos.ProductDto;
import com.sagarannaldas.shopping.cart.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);

    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductDto productDto , @MappingTarget Product product);
}