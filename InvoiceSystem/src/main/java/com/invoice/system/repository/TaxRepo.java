package com.invoice.system.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.invoice.system.model.Tax;
public interface TaxRepo extends CrudRepository<Tax, Integer>{
	
  Optional<Tax>	findByTaxcode(String taxcode);
}