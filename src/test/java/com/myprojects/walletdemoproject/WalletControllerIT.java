package com.myprojects.walletdemoproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.walletdemoproject.project.controller.WalletController;
import com.myprojects.walletdemoproject.project.dto.RequestDto;
import com.myprojects.walletdemoproject.project.model.Wallet;
import com.myprojects.walletdemoproject.project.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
@ExtendWith(MockitoExtension.class)
public class WalletControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDepositSuccess() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(1000));

        when(walletService.deposit(any(String.class), any(BigDecimal.class))).thenReturn(wallet);

        RequestDto request = new RequestDto();
        request.setUuid(UUID.randomUUID());
        request.setOperationType("DEPOSIT");
        request.setAmount(1000L);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(500));

        when(walletService.withdraw(any(String.class), any(BigDecimal.class))).thenReturn(wallet);

        RequestDto request = new RequestDto();
        request.setUuid(UUID.randomUUID());
        request.setOperationType("WITHDRAW");
        request.setAmount(500L);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500));
    }

    @Test
    public void testBadRequestInvalidOperationType() throws Exception {
        RequestDto request = new RequestDto();
        request.setUuid(UUID.randomUUID());
        request.setOperationType("INVALID_OPERATION");
        request.setAmount(1000L);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestInvalidAmount() throws Exception {
        RequestDto request = new RequestDto();
        request.setUuid(UUID.randomUUID());
        request.setOperationType("WITHDRAW");
        request.setAmount(-500L); // Негативная сумма

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
