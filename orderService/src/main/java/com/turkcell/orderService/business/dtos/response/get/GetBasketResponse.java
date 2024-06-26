package com.turkcell.orderService.business.dtos.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetBasketResponse {
    private String id;
    private String accountId;
    private Double totalPrice;
    private List<GetOrderItemResponse> basketItems;
}
