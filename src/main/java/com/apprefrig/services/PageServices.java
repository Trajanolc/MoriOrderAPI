package com.apprefrig.services;

import java.util.List;

import com.apprefrig.model.OrdemServico;

import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public class PageServices {
	
	public static Integer getTotalSize(List<Page<OrdemServico>> listOfPages, int itensInPage) {
		return (listOfPages.size()-1)*itensInPage + listOfPages.get(listOfPages.size()-1).items().size();
	}

}
