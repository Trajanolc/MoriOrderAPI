package com.apprefrig.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

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

	public Iterable<OrdemServico> getOrdensFuncionario(String funcionario) {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//set the calendar to fist day of month at 00:00:00
		
		
		AttributeValue attValDate = AttributeValue.builder()
                .n(Long.toString( (c.getTimeInMillis()/1000 - 1645473084L) )) //Getting the fist order ID of month
                .build();
		
		QueryConditional queryDate = QueryConditional.sortGreaterThan(Key.builder().partitionValue(attValDate).build());

		AttributeValue attValKey = AttributeValue.builder()
                .s(funcionario)
                .build();
		
		QueryConditional queryKey = QueryConditional.keyEqualTo(Key.builder().partitionValue(attValKey).build());
		
		Optional<Page<OrdemServico>> resultsIterator = repository.index("FuncionarioID-ordemID-index").query(QueryEnhancedRequest.builder()
				.queryConditional(queryDate)
				.queryConditional(queryKey)
				.limit(10)
				.build()).stream().findFirst();
		
		ArrayList<OrdemServico> results = new ArrayList<OrdemServico>();

		resultsIterator.ifPresent(it -> results.addAll(it.items()));

		return results;
	}
}
