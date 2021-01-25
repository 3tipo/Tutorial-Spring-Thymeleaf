package com.invoice.system.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.invoice.system.model.Invoice;
import java.util.List;
public interface InvoiceRepo extends PagingAndSortingRepository<Invoice, Integer>{
	 List<Invoice> findByInvoicetypeAndSerieOrderByIdAsc(String invoicetype,String serie);
	 
}