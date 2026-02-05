package com.authflow.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.authflow.filter.JwtRequestFilter;
import com.authflow.service.AppUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SeurityConfig {

	private final AppUserDetailsService appUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/login", "/register", "/send-reset-otp","/verify-otp", "/reset-password", "/logout", "/greet")
						.permitAll()
						.anyRequest()
						.authenticated())
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.logout(AbstractHttpConfigurer::disable)  //default logout disabled 
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex-> ex.authenticationEntryPoint(customAuthenticationEntryPoint));
		return http.build();
	}

	@Bean
	PasswordEncoder pswdEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CorsFilter corsFilter() {
		return new CorsFilter(CorsConfigurationSource());
	}

	//front-end related
	private CorsConfigurationSource CorsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);

		return urlBasedCorsConfigurationSource;
	}

	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(appUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(pswdEncoder());
		return new ProviderManager(daoAuthenticationProvider);
	}
}
