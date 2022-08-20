package com.apprefrig.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration {
	
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.httpBasic()
		.and()
		.authorizeHttpRequests()
		.anyRequest().permitAll()
		.and()
		.csrf().disable()
		.addFilterBefore(null, null);
	return http.build();
    }

	@Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build();
    }

	
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.builder()
            .username("rayssa@moriengenharia.com")
            .password("{bcrypt}$2a$12$qQsgCU42o8uEjRVQgl64HOZNMYt8MBKxeeDWnYB19TPzvdX/u2kbS")
            .roles("ADMIN","USER")
            .build();
        
        UserDetails user2 = User.builder()
                .username("elias@moriengenharia.com")
                .password("{bcrypt}$2a$12$qQsgCU42o8uEjRVQgl64HOZNMYt8MBKxeeDWnYB19TPzvdX/u2kbS")
                .roles("USER")
                .build();
        
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(user2);
        return users;
    }
}
