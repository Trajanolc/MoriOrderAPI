package com.apprefrig.controller;

import java.util.List;

import com.apprefrig.model.OrdemServicoEmpresa;
import com.apprefrig.services.OrdemServicoEmpresaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.utils.Pair;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController // TODO gateway error handler
public class OrderCompanyController {

	@Autowired
	OrdemServicoEmpresaService ordemServicoServiceEmpresa;
	
	@GetMapping("/company/latest/{company}")
	public ResponseEntity<Object> getLatestOrderPerCompany(@PathVariable String company) {
		
		Pair<Integer, List<OrdemServicoEmpresa>> result =  ordemServicoServiceEmpresa.getOrdensCompanyLatest(company);
		
		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(result.left()))
				.body(result.right());

	}
	
	@GetMapping("/company/month/{company}") // TODO add employee not found response
	public ResponseEntity<Object> getOrdersCompany(@PathVariable String company) {
		if(company.equals("EQUATORIAL")){

			
			Pair<Integer, List<OrdemServicoEmpresa>> result =  ordemServicoServiceEmpresa.getOrdensEquatorialCurrentMonth(company);
			
			return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(result.left()))
					.body(result.right());
		}
		
		Pair<Integer, List<OrdemServicoEmpresa>> result =  ordemServicoServiceEmpresa.getOrdensCompanyCurrentMonth(company);

		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(result.left()))
				.body(result.right());

	}
}
