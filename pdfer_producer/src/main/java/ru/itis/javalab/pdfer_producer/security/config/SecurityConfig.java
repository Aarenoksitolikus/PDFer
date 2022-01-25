package ru.itis.javalab.pdfer_producer.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.itis.javalab.pdfer_producer.security.jwt.JwtLogoutFilter;
import ru.itis.javalab.pdfer_producer.security.token.TokenAuthenticationFilter;
import ru.itis.javalab.pdfer_producer.security.token.TokenAuthenticationProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${pdfer.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private JwtLogoutFilter jwtLogoutFilter;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().disable();
        http
                .addFilterAt(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLogoutFilter, LogoutFilter.class)
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/teachers").hasAnyAuthority()
                .antMatchers("/users").hasAuthority("ADMIN")
                .and()
                .sessionManagement().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
