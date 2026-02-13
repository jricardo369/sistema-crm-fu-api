package com.cargosyabonos.adapter.out.jwt;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import com.cargosyabonos.application.port.out.GeneraTokenDeAutenticacionPort;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class JwtAdapter implements GeneraTokenDeAutenticacionPort {

	private static final Logger logger = LoggerFactory.getLogger(JwtAdapter.class);

	private final String KEY = "ricma";

	@Value("${jwt-id}")
	private String JwtId;

	@Override
	public String generarTokenDeAutenticacion(String usuario) {
		logger.info("jwtSecretKey:" + KEY);
		logger.info("JwtId:" + JwtId);
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts
				.builder()
				.setId(JwtId)
				.setSubject(usuario)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 38000000))
				.signWith(SignatureAlgorithm.HS512,
						KEY.getBytes())
				.compact();

		return "Bearer " + token;
	}

}
