package com.apprefrig.services;

import java.util.ArrayList;
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

@Service
public class OrdemServicoService {

	@Autowired
	DynamoDbTable<OrdemServico> repository = DynamoDBRepositories.ordemServicoRepository();

	public Iterable<OrdemServico> getOrdens(int n) {
		
		ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().limit(n).build();

		Optional<Page<OrdemServico>> resultsIterator = repository.scan(scanRequest).stream().findFirst();

		ArrayList<OrdemServico> results = new ArrayList<OrdemServico>();
		
		
		resultsIterator.ifPresent(it -> results.addAll(it.items()));

		return results;
	}

	public List<Page<OrdemServico>> getOrdensEmployeeCurrentMonth(String employee, int itensInPage) {
		
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
		
		return resultsIterator;
	}
}
