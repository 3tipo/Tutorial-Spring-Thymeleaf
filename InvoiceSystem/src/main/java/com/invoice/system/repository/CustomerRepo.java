package com.invoice.system.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.invoice.system.model.Customer;
public interface CustomerRepo extends CrudRepository<Customer, Integer>{

	List<Customer> findByCustomercode(String customercode);
	Optional<Customer>  findByCustomertaxid(String companyname);
}
