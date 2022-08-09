package com.apprefrig.controller;

import java.util.List;

import com.apprefrig.model.OrdemServicoFuncionario;
import com.apprefrig.services.OrdemServicoFuncionarioService;

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
public class OrderEmployeeController {
	
	@Autowired
	OrdemServicoFuncionarioService ordemServicoServiceEmployee;
	
	@GetMapping("/orders/latest/{employee}")
	public ResponseEntity<Object> getLatestOrderPerEmployee(@PathVariable String employee) {
		
		Pair<Integer, List<OrdemServicoFuncionario>> result =  ordemServicoServiceEmployee.getOrdensEmployeeLatest(employee);

		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(result.left()))
				.body(result.right());

	}
	
	@GetMapping("/orders/month/{employee}") // TODO add employee not found response
	public ResponseEntity<Object> getOrdersFuncionario(@PathVariable String employee) {
		
		Pair<Integer, List<OrdemServicoFuncionario>> result =  ordemServicoServiceEmployee.getOrdensEmployeeCurrentMonth(employee);
		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(result.left()))
				.body(result.right());

	}

}
