package com.invoice.system.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer productcode;
	private String 	producttype;
	private String 	productgroup;
	private String 	productdescription;
	private String 	productnumbercode;
	private String  unitofmeasure;//
	private String  taxexemptionreason;//
	private String  taxexemptioncode; //
	private int 	quantity;
	
	@DecimalMin(value = "0.00", inclusive = false)//@Digits(integer = 10,fraction = 2)
	@DecimalMax(value = "999999999999.99", inclusive = false)
	private double 	unitprice; 
	@DecimalMax(value = "999999999999.99", inclusive = false)//@Digits(integer = 10,fraction = 2)
	@DecimalMin(value = "0.00", inclusive = false)
	private double	discount;
	@ManyToOne
	@JoinColumn(name = "tax_id")
	private Tax tax;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Line> lines;
	public Product() {
	 this.lines = new ArrayList<Line>();
	}
	
	public double selesPrice() {
		return (this.getUnitprice()- (this.getUnitprice()*(this.getDiscount()/100))+(this.getUnitprice()*(this.getTax().getTaxpercentage()/100)));
	}
}
