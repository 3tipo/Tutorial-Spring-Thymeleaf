package com.invoice.system.repository;
import java.util.List;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.invoice.system.model.Product;
public interface ProductRepo extends CrudRepository<Product, Integer>{
	//@Query("select p from Product p where p.productdescription like %?1%")
    List<Product> findByProductdescriptionLikeIgnoreCase(String productdescription);
}
