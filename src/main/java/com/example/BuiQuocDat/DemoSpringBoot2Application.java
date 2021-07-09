package com.example.BuiQuocDat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class DemoSpringBoot2Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBoot2Application.class, args);
	}
	/*@Configuration
	static class ApplicationSecuritt extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(WebSecurity web) throws Exception{
			web.ignoring().antMatchers("/**");
		}
	}*/
}
