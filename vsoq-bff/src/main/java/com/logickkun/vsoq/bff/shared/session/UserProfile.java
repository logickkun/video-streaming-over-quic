package com.logickkun.vsoq.bff.shared.session;

public record UserProfile(
        String userId,
        String displayName,
        String avatarUrl
) {}