package com.apprefrig.repository;

import com.apprefrig.model.Equipamento;
import com.apprefrig.model.OrdemServico;
import com.apprefrig.model.OrdemServicoEmpresa;
import com.apprefrig.model.OrdemServicoFuncionario;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBRepositories {
	
	
	static String tableOrder = "ordemServico";

	static String tableEquips = "historicoEquipamento";

	@Bean(value = "equipTable")
	public static DynamoDbTable<Equipamento> equipTable() {
		return enhancedClient().table(tableEquips, TableSchema.fromBean(Equipamento.class));
	}

	@Bean
	public
	static DynamoDbTable<OrdemServicoFuncionario> ordemServicoFuncionarioRepository() {
		return enhancedClient().table(tableOrder, TableSchema.fromBean(OrdemServicoFuncionario.class));
	}
	
	@Bean
	public
	static DynamoDbTable<OrdemServicoEmpresa> ordemServicoEmpresaRepository() {
		return enhancedClient().table(tableOrder, TableSchema.fromBean(OrdemServicoEmpresa.class));
	}

    @Bean
	public
    static DynamoDbTable<OrdemServico> ordemServicoRepository() {
        return enhancedClient().table(tableOrder, TableSchema.fromBean(OrdemServico.class));
    }
	
	
	@Bean
	private static DynamoDbEnhancedClient enhancedClient() {

		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoClient()).build();

	}

	@Bean
	private static DynamoDbClient dynamoClient() {
		return DynamoDbClient.builder().credentialsProvider(credentialsProvider()).region(regionAWS())
				.build();
	}

	@Bean
	private static StaticCredentialsProvider credentialsProvider() {
		return StaticCredentialsProvider.create(amazonAWSCredentials());
	}

	@Bean
	private static AwsBasicCredentials amazonAWSCredentials() {
		return AwsBasicCredentials.create(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"));
	}

	@Bean
	private static Region regionAWS() {
		return Region.US_EAST_2;
	}

}
