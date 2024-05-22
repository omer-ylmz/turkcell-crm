package com.turkcell.catalogService.business.dtos.request.create;

import com.turkcell.catalogService.business.dtos.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatedProductRequest {
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private CategoryDTO categoryDTO;
    private List<ProductFeatureRequest> productFeatures;
}

