package net.nvsoftware.iApiGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


//在java中想用okta我们需要暴露创建这个object,这里就是new 出security object然后声明为一个global 的bean
@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurityConfig {
   @Bean
   public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
      http.authorizeExchange().anyExchange().authenticated()
            .and().oauth2Login()
            .and().oauth2ResourceServer()
            .jwt();
      return http.build();
   }
}
