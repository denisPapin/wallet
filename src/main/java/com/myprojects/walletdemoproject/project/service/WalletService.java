package com.myprojects.walletdemoproject.project.service;

import com.myprojects.walletdemoproject.project.model.Wallet;
import com.myprojects.walletdemoproject.project.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Wallet update(Wallet wallet) {
        try {
            return repository.save(wallet);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Кошелек был изменён другим пользователем. Пожалуйста, обновите данные и попробуйте ещё раз.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка обновления кошелька", e);
        }
    }

    @Transactional(readOnly = true)
    public Wallet getOne(String uuid) {
        return repository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Кошелек не найден"));
    }

    @Transactional
    public Wallet create(Wallet wallet) {
        wallet.setCreatedWhen(LocalDateTime.now());
        return repository.save(wallet);
    }

    @Transactional
    public void delete(String walletUuid) {
        if (!repository.existsById(UUID.fromString(walletUuid))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Кошелек не найден");
        }
        repository.deleteById(UUID.fromString(walletUuid));
    }

    @Transactional(readOnly = true)
    public Wallet getWalletByUuid(String walletUuid) {
        return repository.findById(UUID.fromString(walletUuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Кошелек не найден"));
    }

    @Transactional
    public Wallet deposit(String walletUuid, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = getOne(walletUuid);
        wallet.setBalance(wallet.getBalance().add(amount));
        return update(wallet);
    }

    @Transactional
    public Wallet withdraw(String walletUuid, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = getOne(walletUuid);
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Недостаточно средств на счету");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        return update(wallet);
    }


    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сумма должна быть положительной");
        }
    }
}
