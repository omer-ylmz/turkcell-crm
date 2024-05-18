package com.turkcell.categoryService.business.dtos.response.create;

import com.turkcell.categoryService.business.dtos.dto.AttributeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatedProductAttributeDetailsResponse {
    private int id;
    private AttributeDTO attributeDTO;
    private String attributeValue;
}