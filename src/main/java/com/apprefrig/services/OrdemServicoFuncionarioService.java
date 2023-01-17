package com.apprefrig.services;

import java.util.*;
import java.util.stream.Collectors;

import com.apprefrig.model.OrdemServicoFuncionario;
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
public class OrdemServicoFuncionarioService {
	
	private String repositoryIndex = "FuncionarioID-ordemID-index";

	@Autowired
	DynamoDbTable<OrdemServicoFuncionario> repository = DynamoDBRepositories.ordemServicoFuncionarioRepository();

	private int itensInPage = 20;

	public Iterable<OrdemServicoFuncionario> getAllOrdens(int n) {

		ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().limit(n).build();

		Optional<Page<OrdemServicoFuncionario>> resultsIterator = repository.scan(scanRequest).stream().findFirst();

		ArrayList<OrdemServicoFuncionario> results = new ArrayList<OrdemServicoFuncionario>();

		resultsIterator.ifPresent(it -> results.addAll(it.items()));

		return results;
	}

	public Pair<Integer, List<OrdemServicoFuncionario>> getOrdensAllEmployeesCurrentMonth() {

		List<String> listOfEmployees = Arrays.asList("matheus", "elias", "geronildo", "thalisson","rayssa","ruan","marcos" , "elias padilha", "geronildo reis"); // TODO retrieve as
																									// checkbox in final
																									// app

		List<OrdemServicoFuncionario> resultsPerEmployee = new ArrayList<OrdemServicoFuncionario>(); // Temporary list

		listOfEmployees.forEach(employee -> resultsPerEmployee.addAll(getOrdensEmployeeCurrentMonth(employee).right())); // call
																															// employee
																															// function

		Collections.sort(resultsPerEmployee, (o1, o2) -> o2.getOrdemID().compareTo(o1.getOrdemID())); // Sort by OrderID

		return Pair.of(resultsPerEmployee.size(), resultsPerEmployee);
	}

	public Pair<Integer, List<OrdemServicoFuncionario>> getOrdensEmployeeCurrentMonth(String employee) {

		AttributeValue attValDate = AttributeValue.builder()
				.n(Long.toString((CalendarService.getMonthStart().getTimeInMillis() / 1000 - 1645473084L))) // Getting
																											// the fist
																											// order ID
																											// of month
				.build();

		AttributeValue attValKey = AttributeValue.builder().s(employee).build();

		QueryConditional queryDate = QueryConditional
				.sortGreaterThan(Key.builder().partitionValue(attValKey).sortValue(attValDate).build());
		
		List<Page<OrdemServicoFuncionario>> resultsIterator = repository.index(repositoryIndex)
				.query(QueryEnhancedRequest.builder().queryConditional(queryDate).scanIndexForward(false) // descending
																											// order
						.limit(itensInPage) // number of items in page
						.build())
				.stream().collect(Collectors.toList());

		Integer totalSizeOfItens = PageServices.getTotalSizeEmployee(resultsIterator, itensInPage);

		List<OrdemServicoFuncionario> listOrder = new ArrayList<OrdemServicoFuncionario>(); // Final list with formated dates

		// Add all orders off all pages to result
		resultsIterator.forEach(page -> page.items().forEach(order -> listOrder.add(order)));

		return Pair.of(totalSizeOfItens, listOrder);

	}

	public Pair<Integer, List<OrdemServicoFuncionario>> getOrdensAllEmployeesMonthBefore() {

		List<String> listOfEmployees = Arrays.asList("matheus", "elias", "geronildo", "thalisson","rayssa","ruan","marcos"); // TODO retrieve as
		// checkbox in final
		// app

		List<OrdemServicoFuncionario> resultsPerEmployee = new ArrayList<OrdemServicoFuncionario>(); // Temporary list

		listOfEmployees.forEach(employee -> resultsPerEmployee.addAll(getOrdensEmployeeMonthBefore(employee).right())); // call
		// employee
		// function

		Collections.sort(resultsPerEmployee, (o1, o2) -> o2.getOrdemID().compareTo(o1.getOrdemID())); // Sort by OrderID

		return Pair.of(resultsPerEmployee.size(), resultsPerEmployee);
	}

	public Pair<Integer, List<OrdemServicoFuncionario>> getOrdensEmployeeMonthBefore(String employee) {

		Calendar test = CalendarService.getMonthBeforeStart();

		long test2 = test.getTimeInMillis();

		AttributeValue attValDateStart = AttributeValue.builder()
				.n(Long.toString((CalendarService.getMonthBeforeStart().getTimeInMillis() / 1000 - 1645473084L))) // Getting
				// the fist
				// order ID
				// of month
				.build();

		AttributeValue attValDateEnd = AttributeValue.builder()
				.n(Long.toString((CalendarService.getMonthStart().getTimeInMillis() / 1000 - 1645473084L))) // Getting
				// the fist
				// order ID
				// of month
				.build();


		AttributeValue attValKey = AttributeValue.builder().s(employee).build();

		Key keyStart = Key.builder().partitionValue(attValKey).sortValue(attValDateStart).build();

		Key keyEnd = Key.builder().partitionValue(attValKey).sortValue(attValDateEnd).build();

		QueryConditional queryDate = QueryConditional.sortBetween(keyStart,keyEnd);

		List<Page<OrdemServicoFuncionario>> resultsIterator = repository.index(repositoryIndex)
				.query(QueryEnhancedRequest.builder().queryConditional(queryDate).scanIndexForward(false) // descending
						// order
						.limit(itensInPage) // number of items in page
						.build())
				.stream().collect(Collectors.toList());

		Integer totalSizeOfItens = PageServices.getTotalSizeEmployee(resultsIterator, itensInPage);

		List<OrdemServicoFuncionario> listOrder = new ArrayList<OrdemServicoFuncionario>(); // Final list with formated dates

		// Add all orders off all pages to result
		resultsIterator.forEach(page -> listOrder.addAll(page.items()));

		return Pair.of(totalSizeOfItens, listOrder);

	}

	public Pair<Integer, List<OrdemServicoFuncionario>> getOrdensEmployeeLatest(String employee) {

		AttributeValue attValKey = AttributeValue.builder().s(employee).build();

		AttributeValue attValDate = AttributeValue.builder()
				// get exactly one month ago for filter
				.n(Long.toString((CalendarService.getOneMonthAgo().getTimeInMillis() / 1000 - 1645473084L))).build();

		QueryConditional queryDate = QueryConditional
				.sortGreaterThan(Key.builder().partitionValue(attValKey).sortValue(attValDate).build());

		List<Page<OrdemServicoFuncionario>> resultsIterator = repository.index(repositoryIndex)
				.query(QueryEnhancedRequest.builder().queryConditional(queryDate).scanIndexForward(false) // descending
																											// order
						.limit(itensInPage) // number of items in page
						.build())
				.stream().collect(Collectors.toList());

		Integer totalSizeOfItens = PageServices.getTotalSizeEmployee(resultsIterator, itensInPage);

		List<OrdemServicoFuncionario> listOrder = new ArrayList<OrdemServicoFuncionario>(); // Final list with formated dates

		// get only the fist page
		resultsIterator.get(0).items().forEach(order ->
		// and add to the final result
		listOrder.add(order));

		return Pair.of(totalSizeOfItens, listOrder);
	}
}
