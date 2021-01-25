package com.invoice.system.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "lines")
@Data
@AllArgsConstructor
public class Line implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer linenumber;

	@ManyToOne//(fetch = FetchType.LAZY)
	@JoinColumn(name = "product")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "invoice")
	private Invoice invoice;
	
	private Integer 	 quantity;//
	@DecimalMin(value = "0.00" , inclusive = false)//@Digits(integer = 10,fraction = 2)
	@DecimalMax(value = "999999999999.99", inclusive = false)
	private double 	 	 unitprice;//
	@DecimalMax(value = "999999999999.99", inclusive = false)//@Digits(integer = 10,fraction = 2)
	@DecimalMin(value = "0.00", inclusive = false)
	private double   	 descount;
	
	private String taxpointdate;//
	@Digits(integer = 10,fraction = 2)
	private double 	 settlementamount;//
	
	
	public Line() {
		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
		this.taxpointdate = df.format(new Date());
	}
	
	public Double creditAmount() {
		return this.quantity.doubleValue()*product.getUnitprice()-descount();
	}

	public Double tax() {
	return	(this.quantity.doubleValue()*product.getUnitprice())*(product.getTax().getTaxpercentage()/100);
	}
	
	public Double descount() {	
	return	(this.quantity.doubleValue()*product.getUnitprice())*(product.getDiscount()/100);
	}
	
	
	public Double totalInLine() {
		return creditAmount() + tax();
	}
	
}
