package com.apprefrig.model;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
public class OrdemServicoEmpresa extends OrdemServico{
	private Integer ordemID;

	private Long DataFim;
	
	
	private String PAT;
	
	private Float gas_KG;
	
	private String instalacao;
	
	private String local;
	
	private String equipamento;
	
	private String funcionarioID;
	
	private String OBS;
	
	private List<String> tipoServicos;
	
	private List<String> tipoManut;
	
	private List<String> tipoTroca;
	
	private List<String> fotosAntes;
	
	private List<String> fotosDepois;
	
	public OrdemServicoEmpresa() {
		super();
	}
	
	@DynamoDbSecondarySortKey(indexNames = { "Instalacao-ordemID-index" })
	@DynamoDbPartitionKey
	public Integer getOrdemID() {
		return ordemID;
	}

	public void setOrdemID(Integer ordemID) {
		this.ordemID = ordemID;
	}
	
	@DynamoDbSecondaryPartitionKey(indexNames = { "DataFim" })
	@DynamoDbAttribute(value = "DataFim")
	public Long getDataFim() {
		return DataFim;
	}

	public void setDataFim(Long dataFim) {
		this.DataFim = dataFim;
	}
	
	public String getPAT() {
		return PAT;
	}

	public void setPAT(String pAT) {
		PAT = pAT;
	}
	
	@DynamoDbSecondaryPartitionKey(indexNames = { "Instalacao-ordemID-index" })
	@DynamoDbAttribute(value = "Instalacao")
	public String getInstalacao() {
		return instalacao;
	}

	public void setInstalacao(String instalacao) {
		this.instalacao = instalacao;
	}
	
	@DynamoDbAttribute(value = "Local")
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
	
	@DynamoDbAttribute(value = "Equipamento")
	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}
	

	@DynamoDbAttribute(value = "FuncionarioID")
	public String getFuncionarioID() {
		return funcionarioID;
	}

	public void setFuncionarioID(String funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	
	public String getOBS() {
		return OBS;
	}

	public void setOBS(String oBS) {
		OBS = oBS;
	}
	
	@DynamoDbAttribute(value = "tipoServicos")
	public List<String> getTipoServicos() {
		return tipoServicos;
	}
	
	@DynamoDbAttribute(value = "tipoManut")
	public List<String> getTipoManut() {
		return tipoManut;
	}


	public void setTipoServicos(List<String> tipoServicos) {
		this.tipoServicos = tipoServicos;
	}

	public void setTipoManut(List<String> tipoManut) {
		this.tipoManut = tipoManut;
	}

	public void setTipoTroca(List<String> tipoTroca) {
		this.tipoTroca = tipoTroca;
	}

	public void setFotosAntes(List<String> fotosAntes) {
		this.fotosAntes = fotosAntes;
	}

	public void setFotosDepois(List<String> fotosDepois) {
		this.fotosDepois = fotosDepois;
	}

	@DynamoDbAttribute(value = "tipoTroca")
	public List<String> getTipoTroca() {
		return tipoTroca;
	}

	@DynamoDbAttribute(value = "fotosAntes")
	public List<String> getFotosAntes() {
		return fotosAntes;
	}


	@DynamoDbAttribute(value = "fotosDepois")
	public List<String> getFotosDepois() {
		return fotosDepois;
	}

	
	@DynamoDbAttribute(value = "gas_KG")
	public Float getGas_KG() {
		return gas_KG;
	}

	public void setGas_KG(Float gas_KG) {
		this.gas_KG = gas_KG;
	}
}
