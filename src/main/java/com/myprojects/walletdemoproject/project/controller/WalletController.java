package com.myprojects.walletdemoproject.project.controller;

import com.myprojects.walletdemoproject.project.dto.RequestDto;
import com.myprojects.walletdemoproject.project.model.Wallet;
import com.myprojects.walletdemoproject.project.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Кошелек", description = "Контроллер для работы с кошельком пользователя")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Retryable(value = { ResponseStatusException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    @PostMapping(value = "/wallet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallet> postWalletOperation(@RequestBody RequestDto requestBody) {
//        log.info("Received request: {}", requestBody);

        try {
            switch (requestBody.getOperationType()) {
                case "DEPOSIT":
                    BigDecimal depositAmount = new BigDecimal(requestBody.getAmount());
                    return ResponseEntity.ok(walletService.deposit(requestBody.getUuid().toString(), depositAmount));
                case "WITHDRAW":
                    BigDecimal withdrawAmount = new BigDecimal(requestBody.getAmount());
                    return ResponseEntity.ok(walletService.withdraw(requestBody.getUuid().toString(), withdrawAmount));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (ResponseStatusException e) {
//            log.error("Error processing request: ", e);
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (NumberFormatException e) {
//            log.error("Invalid amount format: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
//            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/wallets/{walletUuid}")
    public ResponseEntity<Wallet> getWalletByUuid(@PathVariable String walletUuid) {
        try {
            Wallet wallet = walletService.getWalletByUuid(walletUuid);
            return ResponseEntity.ok(wallet);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
//            log.error("Error retrieving wallet: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}