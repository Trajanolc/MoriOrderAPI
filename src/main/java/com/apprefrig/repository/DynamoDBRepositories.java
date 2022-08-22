package com.apprefrig.repository;

import com.apprefrig.enums.HCredentials;
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

	static String table = "ordemServico-test";

	@Bean
	public static DynamoDbTable<OrdemServicoFuncionario> ordemServicoFuncionarioRepository() {
		return enhancedClient().table(table, TableSchema.fromBean(OrdemServicoFuncionario.class));
	}

	@Bean
	public static DynamoDbTable<OrdemServicoEmpresa> ordemServicoEmpresaRepository() {
		return enhancedClient().table(table, TableSchema.fromBean(OrdemServicoEmpresa.class));
	}

	@Bean
	public static DynamoDbTable<OrdemServico> ordemServicoRepository() {
		return enhancedClient().table(table, TableSchema.fromBean(OrdemServico.class));
	}

	@Bean
	private static DynamoDbEnhancedClient enhancedClient() {

		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoClient()).build();

	}

	@Bean
	private static DynamoDbClient dynamoClient() {
		DynamoDbClient ddb = DynamoDbClient.builder().credentialsProvider(credentialsProvider()).region(regionAWS())
				.build();
		return ddb;
	}

	@Bean
	private static StaticCredentialsProvider credentialsProvider() {
		return StaticCredentialsProvider.create(amazonAWSCredentials());
	}

	@Bean
	private static AwsBasicCredentials amazonAWSCredentials() {
		return AwsBasicCredentials.create(HCredentials.AWS_ACCESS_KEY.key, HCredentials.AWS_SECRET_KEY.key);
	}

	@Bean
	private static Region regionAWS() {
		return Region.US_EAST_2;
	}

}
