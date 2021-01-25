package com.invoice.system.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "invoices")
@Data
@AllArgsConstructor
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String invoiceno;
	/* DocumentStatus */
	private String invoicestatus;

	private String invoicestatusdate;
	private String sourceid;
	private String sourcebilling;

	private String hash;
	private String hashcontrol;
	private String period;

	private String invoicedate;
	private String invoicetype;

	/* Special Regime */
	private String selfbillingindicator;
	private String cashvatschemeindicator;
	private String thirdpartiesbillingindicator;
	
	private String sourceid2;
	private String systementrydate;
	//@Embedded
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer")
	private Customer customer;
	
	@OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
	private List<Line> lines;
	
	@SuppressWarnings("unused")
	public void addc(Customer customer) {
		this.customer=customer;
		customer.getInvoices().add(this);
	}
	
	
	public Invoice() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat Month = new SimpleDateFormat("MM");
		this.period = Month.format(new Date());
		
		this.invoicestatusdate = df.format(new Date()) + "T" + df1.format(new Date());
		this.systementrydate = df.format(new Date()) + "T" + df1.format(new Date());
		this.invoicedate = df.format(new Date());
		this.lines = new ArrayList<Line>();
		
	}
 
	public void addLine(Line line) {
		this.lines.add(line);
	}

	public void removeLines() {
		Iterator<Line> iterator = this.lines.iterator();
		while (iterator.hasNext()) {
			iterator.remove();
		}
	}
	
	public Double getTaxPayable() {
		Double taxpayble = 0.00;
		int size = this.lines.size();
		for (int i = 0; i < size; i++) {
			taxpayble += this.lines.get(i).tax();
		}
		return taxpayble;
	}

	public Double getDiscount() {
		Double discount = 0.00;
		int size = this.lines.size();
		for (int i = 0; i < size; i++) {
			discount += this.lines.get(i).descount();
		}
		return discount;
	}

	public Double getNetTotal() {
		Double nettotal = 0.00;
		int size = this.lines.size();
		for (int i = 0; i < size; i++) {
			nettotal += this.lines.get(i).creditAmount();
		}
		return nettotal;
	}

	public Double getGrossTotal() {
		Double total = 0.0;
		int size = this.lines.size();
		for (int i = 0; i < size; i++) {
			total += this.lines.get(i).totalInLine();
		}
		return total;
	}

	private String expressao;//expressão onde inclui o codigo da factura vindo do hash e o numeroda  AGT
	private String fabricante;//informações do fabricante de sistema
	private String regimeiva;//que regime se encontra
	private String copiaouoriginal;//informa se foi cópia ou original
	private String motivoisencao;//informação sobre produtos isentos
	private String serie;// serve de auxilio para trazer uma fatura de uma determinada serie
}
