package com.invoice.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
//@Embeddable
@Data
@Entity
@Table(name = "customers")
public class Customer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerid;
	
	private String customercode;
	private String accountid;
	private String customertaxid;
	private String companyname;
	private String addressdetail;
	private String city;
	private String contactos;
	private String country;
	private String selfbillingindicatorc;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Invoice> invoices;
	public Customer() {
		this.invoices= new ArrayList<Invoice>();
	}


	public void addInvoice(Invoice i) {
		this.invoices.add(i);
		i.setCustomer(this);
	}
	
}
