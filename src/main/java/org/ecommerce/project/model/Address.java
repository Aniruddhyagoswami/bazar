package org.ecommerce.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @NotBlank
    @Size(min = 5, message = "Address must contain atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must contain atleast 5 characters")
    private String buildingName;


    @NotBlank
    @Size(min = 2, message = "Country name must be atleast 2 characters")
    private String country;

    @NotBlank
    @Size(min = 5, message = "City name must contain atleast 5 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must contain atleast 2 characters")
    private String state;

    @NotBlank
    @Size(min = 5, message = "pincode contain atleast 6 characters")
    private String pincode;




    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
