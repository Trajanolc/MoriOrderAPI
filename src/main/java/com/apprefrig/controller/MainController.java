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
import software.amazon.awssdk.utils.Pair;

@RestController // TODO gateway error handler
public class MainController {

	@Autowired
	OrdemServicoService ordemServicoService;

	@GetMapping("/orders")
	public ResponseEntity<Object> getAllOrders() {

		return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.getAllOrdens(20));

	}

	@GetMapping("/orders/month")
	public ResponseEntity<Object> getMonthOrders() {
		
		Pair<Integer,List<OrdemServico>> result =  ordemServicoService.getOrdensAllEmployeesCurrentMonth();
		
		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count",String.valueOf(result.left())).body(result.right());

	}

	@GetMapping("/orders/month/{employee}") // TODO add employee not found response
	public ResponseEntity<Object> getOrdersFuncionario(@PathVariable String employee) {
		
		Pair<Integer, List<Page<OrdemServico>>> result =  ordemServicoService.getOrdensEmployeeCurrentMonth(employee);

		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(result.left()))
				.body(result.right().get(0).items());

	}

	@GetMapping("/")
	public String Hello() {
		return "App working!!!";
	}

}
