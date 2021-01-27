package com.invoice.system.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.invoice.system.model.Product;
import com.invoice.system.model.Tax;
import com.invoice.system.repository.ProductRepo;
import com.invoice.system.repository.TaxRepo;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private TaxRepo taxrepo;
	
	@Autowired
	private ProductRepo pr;
	
	@GetMapping("/form")
	public String getProdutos( Map<String, Object> model, RedirectAttributes flash) {
		
		Product product = new Product();
		model.put("product", product);
		model.put("titulo", "Registar Produto");
		return "product/product-form";
	}
	
	@PostMapping("/save")
	public String SaveProduct(Product product ,Model model, RedirectAttributes flash) {
		if(product.getProductdescription()=="") {
			flash.addFlashAttribute("error", " Producto não salvo!");
			return "redirect:/products/form";
		}else {
			Tax tax = taxrepo.findByTaxcode(product.getTax().getTaxcode()).get();
			product.setTax(tax);
			pr.save(product);
			flash.addFlashAttribute("success", "Produto salvo com sucesso!");
			return "redirect:/products/form";
		}
	}
}
