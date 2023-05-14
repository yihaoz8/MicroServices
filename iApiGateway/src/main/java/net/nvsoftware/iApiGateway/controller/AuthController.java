package net.nvsoftware.iApiGateway.controller;

import net.nvsoftware.iApiGateway.model.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

//我们需要这个因为Login好了是在okta验证好了，但是我们需要把token什么的发给客户端，然后客户端才能下次用这个token
@RestController
@RequestMapping("/auth")
public class AuthController {
   @GetMapping("/login")
   public ResponseEntity<AuthResponse> login(
         //config的时候选过oidc方式,前面的annotation告诉他是啥，这个意思是authorized这个user
         @AuthenticationPrincipal OidcUser oidcUser,
         Model model,
         //注册的是这个client
         @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client
   ) {
      AuthResponse authResponse = AuthResponse.builder()
            .userId(oidcUser.getEmail())
            .accessToken(client.getAccessToken().getTokenValue())
            .refreshToken(client.getRefreshToken().getTokenValue())
            .expireAt(client.getAccessToken().getExpiresAt().getEpochSecond())
            .authList(oidcUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .build();

      return new ResponseEntity<>(authResponse, HttpStatus.OK);
   }
}
