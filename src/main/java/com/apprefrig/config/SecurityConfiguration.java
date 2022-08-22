package com.apprefrig.config;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.apprefrig.filter.TokenAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeHttpRequests().anyRequest().permitAll().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION).build();
	}

	@Bean
	public UserDetailsManager users(DataSource dataSource) {

		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		
		for(UserDetails user : usersList()) {
			users.createUser(user);
		}

		return users;
	}
	
	private static ArrayList<UserDetails> usersList(){
		ArrayList<UserDetails> usersList = new ArrayList<UserDetails>();
		
		usersList.add(User.builder().username("rayssa@moriengenharia.com")
				.password("{bcrypt}$2a$12$qQsgCU42o8uEjRVQgl64HOZNMYt8MBKxeeDWnYB19TPzvdX/u2kbS").roles("ADMIN", "USER")
				.build());

		usersList.add(User.builder().username("elias@moriengenharia.com")
				.password("{bcrypt}$2a$12$qQsgCU42o8uEjRVQgl64HOZNMYt8MBKxeeDWnYB19TPzvdX/u2kbS").roles("USER").build());
		
		return usersList;
	}
	
	public static UserDetails getUserFromUsername(String username) {
		
		UserDetails unautenticatedUser = User.builder().username("unautenticated")
		.password("unautenticated")
		.build();
		
		for(UserDetails user : usersList()) {
			if(username.equals(user.getUsername())) return user;
		}
		return unautenticatedUser;
		
	}

}
