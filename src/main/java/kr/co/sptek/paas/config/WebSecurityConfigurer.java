package kr.co.sptek.paas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


	@Override
    public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers("/resources/**");
    }

	@Override
	public void configure(HttpSecurity https) throws Exception {
		https.authorizeRequests().
			antMatchers("/**").permitAll();

		https.csrf().disable();
	}	
}
