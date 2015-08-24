package tr.org.lkd.lyk2015.camp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan({ "tr.org.lkd.lyk2015.camp" })
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

	//	@formatter:off

		http.authorizeRequests()

		// authentication
		.antMatchers("/basvuru", "/resources/**", "/applications/validate/**").permitAll()

		// authorization
		.antMatchers("/admin/**").hasAuthority("ADMIN")
		.antMatchers("/course/create/**").hasAuthority("ADMIN")// courses (list) will be seen by any authenticated user
																// but only admin will be able to create new course
		.antMatchers("/instructor/**").hasAnyAuthority("ADMIN", "INSTRUCTOR")
		.antMatchers("/instructor/create/**").hasAuthority("ADMIN")
		.anyRequest().authenticated()
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.and().formLogin();
		// hangi kullanicinin url e gore nereye giris yapabilecegi

	//	@formatter:on

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService);
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(this.userDetailsService);
		authProvider.setPasswordEncoder(this.passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authProvider());
	}
}