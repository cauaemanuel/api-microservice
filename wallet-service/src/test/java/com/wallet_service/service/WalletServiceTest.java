package com.wallet_service.service;

import com.wallet_service.client.UserClient;
import com.wallet_service.model.entity.Wallet;
import com.wallet_service.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    private WalletRepository walletRepository;
    private UserClient userClient;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        userClient = mock(UserClient.class);
        walletService = new WalletService(walletRepository, userClient);
    }

    @Test
    void testCreateWalletDelegatesToUseCase() {
        when(userClient.exists("user1")).thenReturn(true);
        when(walletRepository.existsByUserId("user1")).thenReturn(false);

        walletService.createWallet("user1");

        verify(walletRepository, times(1)).existsByUserId("user1");
    }

    @Test
    void testGetWalletBalanceSuccess() {
        Wallet wallet = new Wallet();
        wallet.setUserId("user1");
        wallet.setBalance(100.0);

        when(walletRepository.findByUserId("user1")).thenReturn(Optional.of(wallet));

        Double balance = walletService.getGetWalletBalance("user1");
        assertEquals(100.0, balance);
    }

    @Test
    void testGetWalletBalanceThrowsWhenNotFound() {
        when(walletRepository.findByUserId("user1")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> walletService.getGetWalletBalance("user1"));
    }

    @Test
    void testUpdateWalletBalanceSuccess() {
        Wallet wallet = new Wallet();
        wallet.setUserId("user1");
        wallet.setBalance(50.0);

        when(walletRepository.findByUserId("user1")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        walletService.updateWalletBalance("user1", 25.0);

        assertEquals(75.0, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testUpdateWalletBalanceThrowsForNegative() {
        assertThrows(IllegalArgumentException.class, () -> walletService.updateWalletBalance("user1", -10.0));
    }
}