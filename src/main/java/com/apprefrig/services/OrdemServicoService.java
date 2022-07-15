package com.apprefrig.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.apprefrig.model.OrdemServico;
import com.apprefrig.repository.DynamoDBRepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.utils.Pair;

@Service
public class OrdemServicoService {

	@Autowired
	DynamoDbTable<OrdemServico> repository = DynamoDBRepositories.ordemServicoRepository();
	
	private int itensInPage = 20;

	public Iterable<OrdemServico> getAllOrdens(int n) {

		ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().limit(n).build();

		Optional<Page<OrdemServico>> resultsIterator = repository.scan(scanRequest).stream().findFirst();

		ArrayList<OrdemServico> results = new ArrayList<OrdemServico>();

		resultsIterator.ifPresent(it -> results.addAll(it.items()));

		return results;
	}
	
	public Pair<Integer,List<OrdemServico>> getOrdensAllEmployeesCurrentMonth(){
		
		List<String> listOfEmployees = Arrays.asList("matheus","elias","geronildo","thalisson"); //TODO retrieve as checkbox in final app
		
		List<OrdemServico> resultsIterator = new ArrayList<OrdemServico>(); //Final list
		List<Page<OrdemServico>> resultsPerEmployee = new ArrayList<Page<OrdemServico>>(); //Temporary list
		
		Integer totalSizeOfItens = 0; //Count of items
		
		for(int i = 0; i < listOfEmployees.size(); i++) {

			resultsPerEmployee.addAll(getOrdensEmployeeCurrentMonth(listOfEmployees.get(i)).right());  // add to temporary list; 300 to set 300 orders per employee
			
			totalSizeOfItens += PageServices.getTotalSize(resultsPerEmployee, itensInPage); // get count and add
			
			resultsPerEmployee.forEach(page -> resultsIterator.addAll(page.items())); //add temporary list to final list
			
			resultsPerEmployee.clear(); //clear temporary list
		}
		
		return Pair.of(totalSizeOfItens, resultsIterator);
	}


	public Pair<Integer,List<Page<OrdemServico>>> getOrdensEmployeeCurrentMonth(String employee) {

		AttributeValue attValDate = AttributeValue.builder()
                .n(Long.toString( (CalendarService.getMonthStart().getTimeInMillis()/1000 - 1645473084L) )) //Getting the fist order ID of month
                .build();
	
		AttributeValue attValKey = AttributeValue.builder()
                .s(employee)
                .build();
		
		QueryConditional queryDate = QueryConditional.sortGreaterThan(Key.builder().partitionValue(attValKey).sortValue(attValDate).build());
		
		List<Page<OrdemServico>> resultsIterator = repository.index("FuncionarioID-ordemID-index").query(QueryEnhancedRequest.builder()
				.queryConditional(queryDate)
				.scanIndexForward(false) // descending order
				.limit(itensInPage) // number of items in page
				.build()).stream().collect(Collectors.toList());
		
		Integer totalSizeOfItens = PageServices.getTotalSize(resultsIterator, itensInPage);
		
		
		return Pair.of(totalSizeOfItens, resultsIterator);

	}
}
