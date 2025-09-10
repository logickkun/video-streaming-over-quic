package com.logickkun.vsoq.bff.web.ctr;

import com.logickkun.vsoq.bff.shared.PkceUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class WebLoginController {

    @Value("${bff.oauth.authorize-uri}")
    private String authorizeUri;
    @Value("${bff.oauth.client-id}")
    private String clientId;
    @Value("${bff.oauth.redirect-uri}")
    private String redirectUri;
    @Value("${bff.oauth.scope}")
    private String scope;

    @GetMapping("/bff/web/login")
    public ResponseEntity<Void> startLogin(HttpSession session) {
        // 1) 랜덤 생성
        String state = PkceUtil.randomUrlSafe(16);
        String nonce = PkceUtil.randomUrlSafe(16);
        String codeVerifier = PkceUtil.randomUrlSafe(32);
        String codeChallenge = PkceUtil.s256(codeVerifier);

        // 2) 세션 저장 (TTL은 server.servlet.session.timeout에 따름)
        session.setAttribute("OIDC_STATE", state);
        session.setAttribute("OIDC_NONCE", nonce);
        session.setAttribute("OIDC_CODE_VERIFIER", codeVerifier);

        // 3) authorize URL 조립
        String url = authorizeUri
                + "?response_type=code"
                + "&client_id=" + enc(clientId)
                + "&redirect_uri=" + enc(redirectUri)
                + "&scope=" + enc(scope)
                + "&state=" + enc(state)
                + "&nonce=" + enc(nonce)
                + "&code_challenge=" + enc(codeChallenge)
                + "&code_challenge_method=S256";

        // 4) 302 리다이렉트
        return ResponseEntity.status(302)
                .header("Location", url)
                .build();
    }

    private static String enc(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
