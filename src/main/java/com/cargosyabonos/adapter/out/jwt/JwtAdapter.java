package com.cargosyabonos.adapter.out.jwt;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.cargosyabonos.UtilidadesAdapter;
import com.cargosyabonos.application.port.out.GeneraTokenDeAutenticacionPort;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@PropertySource(value = "classpath:configuraciones-global.properties")
public class JwtAdapter implements GeneraTokenDeAutenticacionPort{
	
	private final String KEY = "ricma";

	@Value("${jwt-id}")
	private String JwtId;

	@Override
	public String generarTokenDeAutenticacion(String usuario) {
		UtilidadesAdapter.pintarLog("jwtSecretKey:"+KEY);
		UtilidadesAdapter.pintarLog("JwtId:"+JwtId);
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
						KEY.getBytes()).compact();

		return "Bearer " + token;
	}
	
//	private void parseJWT(String jwt) {
//	    //This line will throw an exception if it is not a signed JWS (as expected)
//	    Claims claims = Jwts.parser()         
//	       .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
//	       .parseClaimsJws(jwt).getBody();
//	    UtilidadesAdapter.pintarLog("ID: " + claims.getId());
//	    UtilidadesAdapter.pintarLog("Subject: " + claims.getSubject());
//	    UtilidadesAdapter.pintarLog("Issuer: " + claims.getIssuer());
//	    UtilidadesAdapter.pintarLog("Expiration: " + claims.getExpiration());
//	}

}
