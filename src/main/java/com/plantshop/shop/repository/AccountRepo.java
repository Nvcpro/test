package com.plantshop.shop.repository;

import com.plantshop.shop.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
	Optional<Account> findByEmail(String email);
}
