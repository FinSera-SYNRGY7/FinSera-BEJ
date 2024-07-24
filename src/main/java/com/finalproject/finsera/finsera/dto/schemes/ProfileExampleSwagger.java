package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.model.entity.Customers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileExampleSwagger {
    private Customers data;
}
