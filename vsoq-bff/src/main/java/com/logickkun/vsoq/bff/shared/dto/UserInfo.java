package com.logickkun.vsoq.bff.shared.dto;


public record UserInfo(
        String sub,
        String name,
        String nickname,
        String preferred_username,
        String picture,
        String email
) {
    public String displayName() {
        if (nickname != null && !nickname.isBlank()) return nickname;
        if (preferred_username != null && !preferred_username.isBlank()) return preferred_username;
        if (name != null && !name.isBlank()) return name;
        return sub;
    }
}
