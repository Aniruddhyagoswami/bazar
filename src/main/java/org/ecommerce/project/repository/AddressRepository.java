package org.ecommerce.project.repository;


import org.ecommerce.project.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;


@Repository

public interface AddressRepository extends JpaRepository<Address,Long> {

}
