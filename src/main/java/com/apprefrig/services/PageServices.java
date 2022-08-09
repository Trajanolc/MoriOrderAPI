package com.apprefrig.services;

import java.util.List;

import com.apprefrig.model.OrdemServicoEmpresa;
import com.apprefrig.model.OrdemServicoFuncionario;

import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public class PageServices {

	public static Integer getTotalSizeCompany(List<Page<OrdemServicoEmpresa>> resultsIterator, int itensInPage) {
		return (resultsIterator.size()-1)*itensInPage + resultsIterator.get(resultsIterator.size()-1).items().size();
	}

	public static Integer getTotalSizeEmployee(List<Page<OrdemServicoFuncionario>> resultsIterator, int itensInPage) {
		return (resultsIterator.size()-1)*itensInPage + resultsIterator.get(resultsIterator.size()-1).items().size();
	}
}
