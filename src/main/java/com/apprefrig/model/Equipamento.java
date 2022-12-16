package com.apprefrig.model;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@DynamoDbBean
public class Equipamento {
    private String TAG;
    private String Instalacao;
    private String predio;
    private String sala;
    private String tipo;
    private String btus;


    private String modelo;
    private Set<String> historico;

    public Equipamento() {

    }

    @DynamoDbSortKey
    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    @DynamoDbPartitionKey
    public String getInstalacao() {
        return Instalacao;
    }

    public void setInstalacao(String instalacao) {
        this.Instalacao = instalacao;
    }

    @DynamoDbAttribute(value = "predio")
    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    @DynamoDbAttribute(value = "sala")
    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    @DynamoDbAttribute(value = "tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @DynamoDbAttribute(value = "btus")
    public String getBtus() {
        return btus;
    }

    public void setBtus(String btus) {
        this.btus = btus;
    }

    @DynamoDbAttribute(value = "modelo")
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @DynamoDbAttribute(value = "historico")
    public Set<String> getHistorico() {
        return historico;
    }

    public void setHistorico(Set<String> historico) {
        this.historico = historico;
    }


}