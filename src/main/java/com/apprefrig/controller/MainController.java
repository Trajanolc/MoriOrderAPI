package com.apprefrig.controller;

import java.util.List;

import com.apprefrig.model.OrdemServico;
import com.apprefrig.services.OrdemServicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.enhanced.dynamodb.model.Page;

@RestController //TODO gateway erro handler
public class MainController {

	@Autowired
	OrdemServicoService ordemServicoService;

	@GetMapping("/orders")
	public ResponseEntity<Object> getOrders() {
		
		return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.getOrdens(20));

	}
	
	@GetMapping("/orders/{employee}") //TODO add employee not found response
	public ResponseEntity<Object> getOrdersFuncionario(@PathVariable String employee) {
		
		int itensInPage = 10;
		
		List<Page<OrdemServico>> resultsIterator = ordemServicoService.getOrdensEmployeeCurrentMonth(employee,itensInPage);
		
		Integer totalSizeOfItens = (resultsIterator.size()-1)*itensInPage + resultsIterator.get(resultsIterator.size()-1).items().size();
		
		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count",String.valueOf(totalSizeOfItens)).body(resultsIterator.get(0).items());

	}
	
	@GetMapping("/")
	public String Hello() {
		return "App working!!!";
	}

}
