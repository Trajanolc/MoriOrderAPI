package com.apprefrig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.apprefrig.services.OrdemServicoService;

@RestController
public class MainController {

	@Autowired
	OrdemServicoService ordemServicoService;

	@GetMapping("/orders")
	public ResponseEntity<Object> getOrders() {

		return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.getOrdens(20));

	}
	
	@GetMapping("/orders/{funcionario}")
	public ResponseEntity<Object> getOrdersFuncionario(@PathVariable String funcionario) {

		return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.getOrdensFuncionario(funcionario));

	}

}
