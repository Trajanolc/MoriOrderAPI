package com.apprefrig.services;

import java.util.Arrays;
import java.util.Objects;

import com.apprefrig.exceptions.ModelException;
import com.apprefrig.model.OrdemServico;
import com.apprefrig.repository.DynamoDBRepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Service
public class OrdemServicoPrincipalService {

	@Autowired
	DynamoDbTable<OrdemServico> repository = DynamoDBRepositories.ordemServicoRepository();

	EquipServices equipServices = new EquipServices();

	public void addOrder(@NonNull OrdemServico order) throws Exception {

		if (Objects.isNull(order.getDataFim()))
			throw new ModelException("Necessaria data para inserir ordem.");
		
		if (Objects.isNull(order.getInstalacao()) || Objects.isNull(order.getLocal())
				|| Objects.isNull(order.getLocal()))
			throw new ModelException("Necessario equipamento para inserir ordem.");
		
		if (Objects.isNull(order.getFuncionarioID()))
			throw new ModelException("Necessario funcionário para inserir ordem.");

		repository.putItem(order);

		String[] equipArray = order.getEquipamento().split(" - ");

		equipServices.addHistoric(equipArray[equipArray.length-1], order.getInstalacao(), order.getOrdemID().toString());

	}

	public void updatedOrder(OrdemServico order) throws ModelException {
		if (Objects.isNull(order.getFuncionarioID()))
			throw new ModelException("Necessario funcionário para inserir ordem.");
		
		repository.putItem(order);
		
	}
}
