package com.invoice.system.repository;

import org.springframework.data.repository.CrudRepository;

import com.invoice.system.model.Tax;
public interface TaxRepo extends CrudRepository<Tax, Integer>{

}