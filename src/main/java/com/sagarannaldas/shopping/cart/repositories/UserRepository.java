package com.sagarannaldas.shopping.cart.repositories;

import com.sagarannaldas.shopping.cart.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
