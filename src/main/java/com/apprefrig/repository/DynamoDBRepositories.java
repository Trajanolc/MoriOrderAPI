package com.apprefrig.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apprefrig.enums.HCredentials;
import com.apprefrig.model.OrdemServico;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBRepositories {


	@Bean
	public static DynamoDbTable<OrdemServico> ordemServicoRepository() {
		return enhancedClient().table("ordemServico-test", TableSchema.fromBean(OrdemServico.class));
	}
	
	
	@Bean
	public static DynamoDbEnhancedClient enhancedClient() {

		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoClient()).build();

	}

	@Bean
	public static DynamoDbClient dynamoClient() {
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
