package com.assignment.eventledger.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtKey {
    public static final Key SECRET_KEY =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
