package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.customer.DetailCustomerResponse;
import com.finalproject.finsera.finsera.dto.customer.UpdateMpinRequest;
import com.finalproject.finsera.finsera.dto.qris.QrisResponseDto;
import com.finalproject.finsera.finsera.dto.responseMsg.ResponseConstant;
import com.finalproject.finsera.finsera.dto.schemes.InfoSaldoExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ProfileExampleSwagger;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profiling Controller", description = "API untuk operasi detail user")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping(value = {"/", ""})
    @Operation(summary = "Detail user (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ProfileExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<?> userDetail(@RequestHeader(name = "Authorization") String token) {
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        Map<String, Object> data = new HashMap<>();
        Optional<Customers> customer = customerRepository.findByUsername(username);
        DetailCustomerResponse detailCustomerResponse = new DetailCustomerResponse();
        detailCustomerResponse.setAddress(customer.get().getAddress());
        detailCustomerResponse.setIdCustomer(customer.get().getIdCustomers());
        detailCustomerResponse.setName(customer.get().getName());
        detailCustomerResponse.setEmail(customer.get().getEmail());
        detailCustomerResponse.setGender(customer.get().getGender());
        detailCustomerResponse.setPhone(customer.get().getPhoneNumber());
        detailCustomerResponse.setUsername(customer.get().getUsername());

        if (customer != null) {
            data.put("userDetails", detailCustomerResponse);
            response.put("data", data);
            return ResponseEntity.ok(BaseResponse.success(response, "success"));
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/update-mpin")
    @Operation(summary = "Update PIN AppLock (done) ", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = InfoSaldoExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<BaseResponse<Void>> updateMpin(@RequestHeader(name = "Authorization") String token, @RequestBody UpdateMpinRequest mpin) {
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        try{
            Customers updatedCustomer =  customerService.updateMpin(username, mpin.getMpinAuth());
            return ResponseEntity.ok(BaseResponse.success(null, ResponseConstant.UPDATE_SUCCESS));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.failure(400, e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, e.getMessage()));
        }
    }


}
