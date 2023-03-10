package com.codevumc.codev_backend.security;

import com.codevumc.codev_backend.errorhandler.AutheniticationEntryPointHandler;
import com.codevumc.codev_backend.errorhandler.WebAccessDeniedHandler;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AutheniticationEntryPointHandler autheniticationEntryPointHandler;
    private final WebAccessDeniedHandler webAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //http.httpBasic().disable(); // ???????????? ????????? ?????? ?????? ???????????? ????????? ??????, header??? id, pw??? ?????? token(jwt)??? ?????? ??????. ????????? basic??? ?????? bearer??? ????????????.
        http.httpBasic().disable()
                .authorizeRequests()// ????????? ?????? ???????????? ??????
                .antMatchers("/v1/**").authenticated()
                .antMatchers("/codev/my-page/**","/codev/project/**","/codev/study/**","/codev/user/update/password", "/codev/chat/**", "/codev/notification/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/codev/user/duplication/**").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(autheniticationEntryPointHandler)
                .accessDeniedHandler(webAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter??? UsernamePasswordAuthenticationFilter ?????? ?????????

        // + ????????? ????????? ??????????????? ??????????????? ?????? ????????? CustomUserDetailService ???????????? ???????????????.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
