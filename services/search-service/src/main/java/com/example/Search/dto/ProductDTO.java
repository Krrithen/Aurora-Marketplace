package com.example.Search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ProductDTO {

    private String productId;
    private String productName;
}

