package com.wallet_service.repository;

import com.wallet_service.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByUserId(String userId);

    boolean existsByUserId(String userId);
}