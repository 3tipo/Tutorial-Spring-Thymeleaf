package com.invoice.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Table(name = "taxs")
@Data
@AllArgsConstructor
public class Tax implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;
	private String  taxtype;
	private String  taxcountryregion;
	private String  taxcode;
	private String  description;
	private double  taxpercentage;
	@OneToMany(fetch = FetchType.LAZY)
	private List<Product> produtcs;
	public Tax() {
		this.produtcs= new ArrayList<Product>();
	}
	
	
}
