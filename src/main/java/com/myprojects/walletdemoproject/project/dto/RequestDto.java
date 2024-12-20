package com.myprojects.walletdemoproject.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class RequestDto {

    private UUID uuid;
    private String operationType;
    private Long amount;

    public RequestDto(UUID uuid, String operationType, Long amount) {
        this.uuid = uuid;
        this.operationType = operationType;
        this.amount = amount;
    }

    public RequestDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
