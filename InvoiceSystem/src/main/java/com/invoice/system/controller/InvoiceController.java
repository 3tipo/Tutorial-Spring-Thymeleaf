package com.invoice.system.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.invoice.system.util.paginator.PageRender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.invoice.system.model.Customer;
import com.invoice.system.model.Invoice;
import com.invoice.system.model.Line;
import com.invoice.system.model.Product;
import com.invoice.system.repository.CustomerRepo;
//import com.invoice.system.repository.CustomerRepo;
import com.invoice.system.repository.InvoiceRepo;
import com.invoice.system.repository.ProductRepo;
import com.invoice.system.util.criptografia.Assinatura;
@Controller
@RequestMapping("/invoices")
@SessionAttributes("invoice")
public class InvoiceController {

	@Autowired
	private ProductRepo prorepo;
	@Autowired
	private InvoiceRepo inrepo;
	@Autowired
	private CustomerRepo customerRepo;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private Calendar calendar;
	@GetMapping(value = "/home")
	public String getHome() {
		return "listar";
	}
	
	
	@SuppressWarnings("null")
	@PostMapping("/save")
	public String invoice(Invoice invoice, SessionStatus sesStatus, Map<String, Object> model, RedirectAttributes flash,
			@RequestParam(name = "item_id[]", required = false) Integer[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidade) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, IOException {
			
		if (itemId==null && cantidade==null) {
			flash.addFlashAttribute("error", " Factura inválida!");
			return "redirect:/invoices/form";
		}else {
		for (int i = 0; i < itemId.length; i++) {
			Product product = prorepo.findById(itemId[i]).orElseThrow();
			Line line = new Line();

			product.setQuantity(product.getQuantity() - cantidade[i]);
			line.setDescount(product.getDiscount());
			line.setQuantity(cantidade[i]);
			line.setUnitprice(product.getUnitprice());
			line.setProduct(product);
			invoice.addLine(line);
		}
		
		String lastHash, invoicedate, sistementrydate, invoiceno, mensagemAassinar,mensagenAssinadaEencriptada;
		DecimalFormat df = new DecimalFormat("#.00");
		
		//CRIAR UMA CONDIÇÃO CASO NÃO ENCONTRA DADOS NA BASE DE DADOS

		List<Invoice> invoices = inrepo.findByInvoicetypeAndSerieOrderByIdAsc("FT", "001");
		if(invoices.size()>0){
		Invoice lastInvoice = invoices.get(invoices.size() - 1);
		lastHash = lastInvoice.getHash();
		int QuantidadeDoTipodeDocumento = invoices.size() + 1;
		String grostotal = df.format(invoice.getGrossTotal());

		invoiceno = "FT" + " " + "001" + "/" + QuantidadeDoTipodeDocumento; // a série virá da entity
		invoicedate = invoice.getInvoicedate();
		sistementrydate = invoice.getSystementrydate();
		mensagemAassinar = invoicedate + ";" + sistementrydate + ";" + invoiceno + ";" + grostotal + ";" + lastHash;
		mensagenAssinadaEencriptada = Assinatura.Asignature(mensagemAassinar);
		
		}else {
			String grostotal = df.format(invoice.getGrossTotal());
			invoiceno = "FT" + " " + "001" + "/"+1;	
			invoicedate = invoice.getInvoicedate();
			sistementrydate = invoice.getSystementrydate();
			mensagemAassinar = invoicedate + ";" + sistementrydate + ";" + invoiceno + ";" + grostotal + ";";
			mensagenAssinadaEencriptada = Assinatura.Asignature(mensagemAassinar);
			
		}
		
		invoice.setInvoiceno(invoiceno);
		invoice.setInvoicestatus("N");
		invoice.setSourceid("Admin");
		invoice.setSourceid2("Admin");
		invoice.setSourcebilling("P");
		invoice.setHash(mensagenAssinadaEencriptada);
		//invoice.setHashcontrol("1");
		//invoice.setPeriod();
		invoice.setInvoicetype("FT");
		invoice.setSerie("001");
		invoice.setSelfbillingindicator("0");
		invoice.setCashvatschemeindicator("0");
		invoice.setThirdpartiesbillingindicator("0");
		invoice.getCustomer();

		log.info("" + invoice.getInvoicestatusdate());
		
		if(invoice.getCustomer().getCompanyname()=="" && 
		 invoice.getCustomer().getAddressdetail()=="" &&
		 invoice.getCustomer().getContactos()=="") {
			Customer c= new Customer();
			boolean b = customerRepo.findByCustomercode("Consumidor final").isEmpty();
			if(b) {
				c.setCustomercode("Consumidor final");
				c.setAccountid("Desconhecido");
				c.setCustomertaxid("XXXXXXXXXXX");
				c.setCompanyname("Consumidor final");
				c.setAddressdetail("Desconhecido");
				c.setCity("Desconhecido");
				c.setCountry("Desconhecido");
				invoice.setCustomer(c);
				inrepo.save(invoice);
				sesStatus.setComplete();
			}else {	
			c=customerRepo.findByCompanyname("Consumidor final").get();
			invoice.setCustomer(c);
			inrepo.save(invoice);
			sesStatus.setComplete();
			}
		}else {
			long tam =customerRepo.count()+1;
			invoice.setHashcontrol(""+tam);
			invoice.getCustomer().setSelfbillingindicatorc("0");
			invoice.getCustomer().setCustomercode("ABC"+tam+"TRH");
			invoice.getCustomer().setCountry("AO");
			invoice.getCustomer().setCity("Luanda");
			invoice.getCustomer().setAccountid(tam+"ATW"+calendar.DAY_OF_WEEK_IN_MONTH);
			if(invoice.getCustomer().getCustomertaxid()=="") invoice.getCustomer().setCustomertaxid("--");
			inrepo.save(invoice);
			sesStatus.setComplete();
		}
		//inrepo.save(invoice);
		//sesStatus.setComplete();
		flash.addFlashAttribute("success", "Factura processada com sucesso!");
		return "redirect:/invoices/form";
		}
	}

	@GetMapping("/form")
	public String form(Map<String, Object> model, RedirectAttributes flash) {
		
		Invoice invoice = new Invoice();
		model.put("invoice", invoice);
		model.put("titulo", "Facturar");

		return "invoice/form";
	}
	
	@GetMapping(value = "/facturas")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,RedirectAttributes flash) {
		long size= inrepo.count();
		if(size>0) {
	Pageable pageRequest =   PageRequest.of(page, 4);
	Page<Invoice> invoices = inrepo.findAll(pageRequest);
	PageRender<Invoice> pageRender = new PageRender<Invoice>("facturas", invoices);
	model.addAttribute("titulo", "Lista de facturas");
	model.addAttribute("invoices", invoices);
	model.addAttribute("page",pageRender);
	//model.addAttribute("invoices", inrepo.findAll());
	//model.addAttribute("titulo", "Facturas");
		return "ver";
		}else {
			flash.addFlashAttribute("error", " Não existe facturas!");
			return "redirect:/invoices/home";
		}
	}
	
	
	

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> findProduct(@PathVariable("term") String term) {
		return prorepo.findByProductdescriptionLikeIgnoreCase("%" + term + "%");
	}

	public static String getCaracterEnumero(String str, String numero) {
		return "" + str.charAt(0) + str.charAt(10) + str.charAt(20) + str.charAt(30)
				+ "-Processado por programa validado n.º" + numero + "/AGT";
	}

	public String notadeValidacaoDoSistema(String numero) {
		return "Emitido por programa validado n.º" + numero + "/AGT";
	}
}

//log.info("ID: " + itemId[i] + " Quantidade: " + cantidade[i] + " Desconto: " + product.getDiscount());
//log.info("GROS: " + invoice.getGrossTotal() + " PREÇO UNITARIO: " + product.getUnitprice());
