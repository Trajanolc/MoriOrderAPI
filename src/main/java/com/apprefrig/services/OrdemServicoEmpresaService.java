package com.apprefrig.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.apprefrig.model.OrdemServicoEmpresa;
import com.apprefrig.repository.DynamoDBRepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.utils.Pair;

@Service
public class OrdemServicoEmpresaService {
	
	private String repositoryIndex = "Instalacao-ordemID-index";

	@Autowired
	DynamoDbTable<OrdemServicoEmpresa> repository = DynamoDBRepositories.ordemServicoEmpresaRepository();

	private int itensInPage = 20;

	
	//list of orders from company on this month
	public Pair<Integer, List<OrdemServicoEmpresa>> getOrdensCompanyCurrentMonth(String company) {

		AttributeValue attValDate = AttributeValue.builder()
				// Get fist order of current month
				.n(Long.toString((CalendarService.getMonthStart().getTimeInMillis() / 1000 - 1645473084L))).build();

		AttributeValue attValKey = AttributeValue.builder().s(company).build();

		QueryConditional queryDate = QueryConditional
				.sortGreaterThan(Key.builder().partitionValue(attValKey).sortValue(attValDate).build());

		List<Page<OrdemServicoEmpresa>> resultsIterator = repository.index(repositoryIndex)
				.query(QueryEnhancedRequest.builder().queryConditional(queryDate).scanIndexForward(false)
						.limit(itensInPage) // number of items in page
						.build())
				.stream().collect(Collectors.toList());

		
		List<OrdemServicoEmpresa> listOrder = new ArrayList<OrdemServicoEmpresa>(); // Final list with formated dates

		// Add all orders off all pages to result
		resultsIterator.forEach(page -> page.items().forEach(order -> listOrder.add(order)));
		
		Integer totalSizeOfItens = PageServices.getTotalSizeCompany(resultsIterator, itensInPage);
		
		

		return Pair.of(totalSizeOfItens, listOrder);

	}

	//list of orders from company on last 30 days
	public Pair<Integer, List<OrdemServicoEmpresa>> getOrdensCompanyLatest(String company) {

		AttributeValue attValKey = AttributeValue.builder().s(company).build();

		AttributeValue attValDate = AttributeValue.builder()
				// get exactly one month ago for filter
				.n(Long.toString((CalendarService.getOneMonthAgo().getTimeInMillis() / 1000 - 1645473084L))).build();

		QueryConditional queryDate = QueryConditional
				.sortGreaterThan(Key.builder().partitionValue(attValKey).sortValue(attValDate).build());
		
		List<Page<OrdemServicoEmpresa>> resultsIterator = repository.index(repositoryIndex)
				.query(QueryEnhancedRequest.builder().queryConditional(queryDate).scanIndexForward(false) // descending
																											// order
						.limit(itensInPage) // number of items in page
						.build())
				.stream().collect(Collectors.toList());

		Integer totalSizeOfItens = PageServices.getTotalSizeCompany(resultsIterator, itensInPage);
		

		List<OrdemServicoEmpresa> listOrder = new ArrayList<OrdemServicoEmpresa>(); // Final list with formated dates

		// get only the fist page
		resultsIterator.get(0).items().forEach(order ->
		// and add to the final result
		listOrder.add(order));

		return Pair.of(totalSizeOfItens, listOrder);
	}

	public Pair<Integer, List<OrdemServicoEmpresa>> getOrdensEquatorialCurrentMonth(String company) {
		List<String> listOfCompanys = Arrays.asList("SEDE","SUB ESTAÇAO","AGENCIA");
		
		List<OrdemServicoEmpresa> resultsPerCompany = new ArrayList<OrdemServicoEmpresa>(); // Temporary list
		
		listOfCompanys.forEach(place -> resultsPerCompany.addAll(getOrdensCompanyCurrentMonth(place).right())); 
		
		Collections.sort(resultsPerCompany, (o1, o2) -> o2.getOrdemID().compareTo(o1.getOrdemID()));
		
		return Pair.of(resultsPerCompany.size(), resultsPerCompany);
	}
}
