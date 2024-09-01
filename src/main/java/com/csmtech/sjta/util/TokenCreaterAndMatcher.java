package com.csmtech.sjta.util;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

@Component
public class TokenCreaterAndMatcher {
    public static String getHmacMessage(String message) {

        String hash = Hashing.hmacSha256("22CSMTOOL2022".getBytes(StandardCharsets.UTF_8))
                .hashString(message, StandardCharsets.UTF_8).toString();

        return hash;
    }
}


