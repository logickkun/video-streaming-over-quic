package com.logickkun.vsoq.bff.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type")   String tokenType,
        @JsonProperty("expires_in")   Long   expiresIn,
        @JsonProperty("refresh_token")String refreshToken,
        @JsonProperty("id_token")     String idToken,
        @JsonProperty("scope")        String scope
) {}
