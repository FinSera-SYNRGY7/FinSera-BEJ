package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.qris.QrisMerchantRequestDto;
import com.finalproject.finsera.finsera.dto.qris.QrisResponseDto;
import com.finalproject.finsera.finsera.dto.responseMsg.ResponseConstant;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.service.QrisService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/qris")
@Tag(name = "Qris Controller", description = "API untuk pembayaran Qris")
public class QrisController {


    @Autowired
    CustomerService customerService;

    @Autowired
    QrisService qrisService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping({"/", ""})
    @Operation(summary = "Qris (done) ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BaseResponse<QrisResponseDto>> generateQris(@RequestHeader(name = "Authorization") String token) {
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        try{
            QrisResponseDto responseDto =  qrisService.generateQris(username);
            return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseConstant.GET_SUCCESS));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.failure(400, e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.failure(500, e.getMessage()));
        }
    }

    @PostMapping({"/merchant", "merchant"})
    @Operation(summary = "Qris (done) ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> merchantQris(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody QrisMerchantRequestDto qrisMerchantRequestDto) {
        String jwt = token.substring("Bearer ".length());
        long id = jwtUtil.getId(jwt);

        return ResponseEntity.ok(BaseResponse.success(qrisService.merchnatQris(id, qrisMerchantRequestDto), ResponseConstant.GET_SUCCESS));
    }
}
