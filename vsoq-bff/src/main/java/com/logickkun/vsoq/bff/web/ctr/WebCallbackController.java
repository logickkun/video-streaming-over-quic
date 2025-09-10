package com.logickkun.vsoq.bff.web.ctr;

import com.logickkun.vsoq.bff.shared.dto.TokenResponse;
import com.logickkun.vsoq.bff.shared.dto.UserInfo;
import com.logickkun.vsoq.bff.shared.session.UserProfile;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Controller
public class WebCallbackController {

    private final WebClient http;

    public WebCallbackController(WebClient http) {
        this.http = http;
    }

    @Value("${bff.oauth.token-uri}")
    private String tokenUri;
    @Value("${bff.oauth.userinfo-uri}")
    private String userInfoUri;
    @Value("${bff.oauth.client-id}")
    private String clientId;
    @Value("${bff.oauth.redirect-uri}")
    private String redirectUri;
    @Value("${bff.ui.after-login-redirect:/}")
    private String afterLoginRedirect;

    @GetMapping("/bff/web/callback")
    public ResponseEntity<Void> callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false, name = "error") String error,
            HttpSession session
    ) {
        // 0) 에러 콜백 처리
        if (error != null) {
            // 필요 시 로깅 후 에러 페이지/SPA 에러 라우트로 보냄
            return ResponseEntity.status(302).location(URI.create(afterLoginRedirect + "?login_error=" + error)).build();
        }
        if (code == null || state == null) {
            return ResponseEntity.badRequest().build();
        }

        // 1) state 검증
        String savedState = (String) session.getAttribute("OIDC_STATE");
        String codeVerifier = (String) session.getAttribute("OIDC_CODE_VERIFIER");
        // (nonce는 ID Token 검증 시 사용 가능. 지금은 UserInfo 경로라 생략)
        session.removeAttribute("OIDC_STATE"); // 재사용 방지
        if (savedState == null || !savedState.equals(state) || codeVerifier == null) {
            return ResponseEntity.status(302).location(URI.create(afterLoginRedirect + "?login_error=state_mismatch")).build();
        }

        // 2) 코드 교환 (백채널)
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);
        form.add("client_id", clientId);
        form.add("code_verifier", codeVerifier);

        TokenResponse tokens = http.post()
                .uri(tokenUri) // http://localhost:9000/oauth2/token
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        if (tokens == null || tokens.accessToken() == null) {
            return ResponseEntity.status(302).location(URI.create(afterLoginRedirect + "?login_error=token_exchange_failed")).build();
        }

        // 3) 사용자 정보 확보 (UserInfo 권장 / ID Token 파싱 대안)
        UserInfo info = http.get()
                .uri(userInfoUri) // http://localhost:9000/userinfo
                .headers(h -> h.setBearerAuth(tokens.accessToken()))
                .retrieve()
                .bodyToMono(UserInfo.class)
                .defaultIfEmpty(new UserInfo(null,null,null,null,null,null))
                .block();

        // 4) 세션에 최소 정보 저장 (표시용 + 서버 측 토큰 핸들)
        String userId = info != null ? info.sub() : null;
        String displayName = (info != null) ? info.displayName() : "user";
        String avatar = (info != null) ? info.picture() : null;

        session.setAttribute("USER", new UserProfile(userId, displayName, avatar));
        // 토큰은 브라우저에 주지 않음 — 서버 세션에만 저장
        session.setAttribute("TOKENS_ACCESS", tokens.accessToken());
        session.setAttribute("TOKENS_REFRESH", tokens.refreshToken());
        session.setAttribute("TOKENS_SCOPE", tokens.scope());
        session.setAttribute("TOKENS_EXPIRES_IN", tokens.expiresIn());

        // 5) SPA 홈으로 302
        return ResponseEntity.status(302)
                .location(URI.create(afterLoginRedirect))
                .build();
    }
}
