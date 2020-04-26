package edu.uga.cinemaapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration {
    @Configuration
    @Order(2)
    public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        SimpleAuthenticationSuccessHandler customSuccessHandler;

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        private DataSource dataSource;

        @Value("${spring.queries.users-query}")
        private String usersQuery;

        @Value("${spring.queries.roles-query}")
        private String rolesQuery;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery)
                    .dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                    .antMatchers("/**", "/login", "/resources/**", "/registration", "/success", "/activate/**")
                    .permitAll().antMatchers("/admin/**").hasAuthority("ROLE_ADMIN").anyRequest().fullyAuthenticated().and()
                    .formLogin().loginPage("/login").successHandler(customSuccessHandler)
                    .usernameParameter("email").passwordParameter("password").and().logout().deleteCookies("JSESSIONID")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and().rememberMe()
                    .key("uniqueAndSecret").tokenValiditySeconds(86400).and().exceptionHandling()
                    .accessDeniedPage("/access-denied");
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }

    }

    @Configuration
    @Order(1)
    public class UserSecurityCOnfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        SimpleAuthenticationSuccessHandler customSuccessHandler;

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        private DataSource dataSource;

        @Value("${spring.queries.users-query}")
        private String usersQuery;

        @Value("${spring.queries.roles-query}")
        private String rolesQuery;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery)
                    .dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                    .antMatchers("/**", "/login", "/resources/**", "/registration", "/success", "/activate/**")
                    .permitAll().antMatchers("/profile/**").hasAuthority("ROLE_USER").anyRequest().authenticated().and()
                    .formLogin().loginPage("/login").successHandler(customSuccessHandler)
                    .usernameParameter("email").passwordParameter("password").and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and().rememberMe()
                    .key("uniqueAndSecret").tokenValiditySeconds(86400).and().exceptionHandling()
                    .accessDeniedPage("/access-denied");
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }
    }
}