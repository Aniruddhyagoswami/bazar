package org.ecommerce.project.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ecommerce.project.model.User;
import org.ecommerce.project.payload.AddressDTO;
import org.ecommerce.project.service.AddressService;
import org.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {


    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthUtil authUtil;


    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(
            summary = "Create a new address",
            description = "Creates a new address for the currently logged-in user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user=authUtil.loggedInUser();
        AddressDTO savedAddressDTO=addressService.createAddress(addressDTO,user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }


    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(
            summary = "Get all addresses",
            description = "Fetches all addresses available in the system. Admin-level access may be required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addressList=addressService.getAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(
            summary = "Get address by ID",
            description = "Fetches a single address using its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO=addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }
    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(
            summary = "Get logged-in user's addresses",
            description = "Retrieves all addresses associated with the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses fetched successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user=authUtil.loggedInUser();
        List<AddressDTO> addressList=addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(
            summary = "Update address by ID",
            description = "Updates an existing address using its ID. The logged-in user must own this address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddressDTO=addressService.updateAddressById(addressId,addressDTO);
        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }


    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(
            summary = "Delete address by ID",
            description = "Deletes an existing address by its ID. Only the owner can delete their own address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> updateAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
