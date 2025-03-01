package com.workintech.s19d2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return  new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırakıyoruz.
                .authorizeHttpRequests(auth -> {
                    // "/auth/**" ve "/welcome/**" gibi belirli endpointlere herkese açık erişim izni veriyoruz.
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers("/welcome/**").permitAll();

                    // "/actuator/**" endpointine de herkese açık erişim izni veriyoruz.
                    auth.requestMatchers("/actuator/**").permitAll();

                    // "/account/**" endpointine GET isteği için "ADMIN" ve "USER" yetkisine sahip kullanıcılar erişebilir.
                    auth.requestMatchers(HttpMethod.GET, "/account/**").hasAnyAuthority("ADMIN", "USER");

                    // "/account/**" endpointine POST isteği için sadece "ADMIN" yetkisine sahip kullanıcılar erişebilir.
                    auth.requestMatchers(HttpMethod.POST, "/account/**").hasAnyAuthority("ADMIN");

                    // "/account/**" endpointine PUT isteği için sadece "ADMIN" yetkisine sahip kullanıcılar erişebilir.
                    auth.requestMatchers(HttpMethod.PUT, "/account/**").hasAnyAuthority("ADMIN");

                    // "/account/**" endpointine DELETE isteği için sadece "ADMIN" yetkisine sahip kullanıcılar erişebilir.
                    auth.requestMatchers(HttpMethod.DELETE, "/account/**").hasAnyAuthority("ADMIN");

                    // Diğer tüm istekler için kimlik doğrulama gereklidir.
                    auth.anyRequest().authenticated();
                })
                // Form tabanlı giriş ekranını varsayılan ayarlarla etkinleştiriyoruz.
                .formLogin(Customizer.withDefaults())
                // HTTP Basic kimlik doğrulamasını varsayılan ayarlarla etkinleştiriyoruz.
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
