package com.invoice.system.repository;
import org.springframework.data.repository.CrudRepository;
import com.invoice.system.model.Line;

public interface LineRepo extends CrudRepository<Line, Integer>{

	
}
