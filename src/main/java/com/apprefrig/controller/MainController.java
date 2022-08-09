package com.apprefrig.controller;

import java.util.List;

import com.apprefrig.model.OrdemServico;
import com.apprefrig.model.OrdemServicoFuncionario;
import com.apprefrig.services.OrdemServicoFuncionarioService;
import com.apprefrig.services.OrdemServicoPrincipalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.utils.Pair;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController // TODO gateway error handler
public class MainController {

	@Autowired
	OrdemServicoFuncionarioService ordemServicoServiceEmployee;
	
	@Autowired
	OrdemServicoPrincipalService ordemServicoService;


	
	// ----------------------------------- All ---------------------------------

	@GetMapping("/orders/month")
	public ResponseEntity<Object> getMonthOrders() {
		
		
		Pair<Integer,List<OrdemServicoFuncionario>> result =  ordemServicoServiceEmployee.getOrdensAllEmployeesCurrentMonth();
		
		return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count",String.valueOf(result.left())).body(result.right());

	}
	
	@PostMapping("/orders")
	public ResponseEntity<Object> addOrder(@RequestBody OrdemServico order){
		try {
			ordemServicoService.addOrder(order);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("Ordem adcionada com sucesso!");
		
			
		
	}


	//-----------------------------------just for testing ------------------------------------------
	
	@GetMapping("/")
	public String Hello() {
		return "App working!!!";
	}

}
