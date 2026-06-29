package com.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseInventoryCommand {

    private String sagaId;

    private String orderId;

    private String product;

    private int quantity;

}