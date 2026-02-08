package com.itmo.nxzage.web4.security;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtPrincipal {
    private String name;
    private Set<String> roles;
}
