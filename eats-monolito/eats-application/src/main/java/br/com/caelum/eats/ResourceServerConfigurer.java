package br.com.caelum.eats;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
				
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/parceiros/restaurantes/**").hasRole("RESTAURANTE")
			.antMatchers(HttpMethod.PUT, "/parceiros/restaurantes/**").hasRole("RESTAURANTE")
			.antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()
			.antMatchers("/pedidos/**").hasRole("ADMIN").anyRequest().authenticated()
			.antMatchers(HttpMethod.POST, "/parceiros/restaurantes/**").hasRole("ADMIN").anyRequest().authenticated()
			.antMatchers(HttpMethod.PUT, "/parceiros/restaurantes/**").hasRole("ADMIN").anyRequest().authenticated()
			.antMatchers("/pedidos/**").hasRole("CLIENTE").anyRequest().authenticated();
			

	}

}
