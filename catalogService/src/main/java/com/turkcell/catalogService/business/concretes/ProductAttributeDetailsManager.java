package com.turkcell.catalogService.business.concretes;

import com.turkcell.catalogService.business.abstacts.ProductAttributeDetailsService;
import com.turkcell.catalogService.business.dtos.request.create.CreatedProductAttributeDetailsRequest;
import com.turkcell.catalogService.business.dtos.request.update.UpdatedProductAttributeDetailsRequest;
import com.turkcell.catalogService.business.dtos.response.create.CreatedProductAttributeDetailsResponse;
import com.turkcell.catalogService.business.dtos.response.get.GetProductAttributeDetailsResponse;
import com.turkcell.catalogService.business.dtos.response.update.UpdatedProductAttributeDetailsResponse;
import com.turkcell.catalogService.dataAccess.abstracts.AttributeRepository;
import com.turkcell.catalogService.dataAccess.abstracts.ProductAttributeDetailsRepository;
import com.turkcell.catalogService.entities.concretes.Attribute;
import com.turkcell.catalogService.entities.concretes.ProductAttributeDetails;
import com.turkcell.corepackage.utils.mappers.ModelMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductAttributeDetailsManager implements ProductAttributeDetailsService {

    private final ModelMapperService modelMapperService;
    private final ProductAttributeDetailsRepository productAttributeDetailsRepository;
    private final AttributeRepository attributeRepository;

    @Override
    public CreatedProductAttributeDetailsResponse add(CreatedProductAttributeDetailsRequest createdProductAttributeDetailsRequest) {
        Optional<Attribute> attribute2 = attributeRepository.findById(createdProductAttributeDetailsRequest.getAttributeDTO().getId());
        Attribute attribute = this.modelMapperService.forRequest().map(createdProductAttributeDetailsRequest.getAttributeDTO(), Attribute.class);
        ProductAttributeDetails productAttributeDetails = this.modelMapperService.forRequest().map(createdProductAttributeDetailsRequest,ProductAttributeDetails.class);
        productAttributeDetails.setAttribute(attribute);
        productAttributeDetailsRepository.save(productAttributeDetails);
        CreatedProductAttributeDetailsResponse createdProductAttributeDetailsResponse = this.modelMapperService.forResponse().map(productAttributeDetails,CreatedProductAttributeDetailsResponse.class);
        createdProductAttributeDetailsResponse.setAttributeName(attribute2.get().getAttributeName());
        return createdProductAttributeDetailsResponse;

    }

    @Override
    public UpdatedProductAttributeDetailsResponse update(UpdatedProductAttributeDetailsRequest updatedProductAttributeDetailsRequest) {
        ProductAttributeDetails productAttributeDetails = this.modelMapperService.forRequest().map(updatedProductAttributeDetailsRequest,ProductAttributeDetails.class);
        productAttributeDetailsRepository.save(productAttributeDetails);
        return this.modelMapperService.forResponse().map(productAttributeDetails,UpdatedProductAttributeDetailsResponse.class);
    }

    @Override
    public GetProductAttributeDetailsResponse getById(int id) {
        Optional<ProductAttributeDetails> productAttributeDetails = productAttributeDetailsRepository.findById(id);
        return this.modelMapperService.forResponse().map(productAttributeDetails.get(),GetProductAttributeDetailsResponse.class);
    }
}
