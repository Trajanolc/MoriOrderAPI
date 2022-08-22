package com.apprefrig.services;

import java.security.Key;
import java.util.Date;

import com.apprefrig.enums.HCredentials;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenServices {

	private static String secret = HCredentials.SECRET_CRYPT_KEY.key;

	private static Key secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

	public String createToken(String extendedInformation) {

		Date now = new Date();
		Date exp = CalendarService.getOneMonthFromNow();

		return Jwts.builder().setIssuer("MoriOrder").setSubject(extendedInformation).setIssuedAt(now)
				.setExpiration(exp).signWith(secretKey).compact();
	}
	
	public static String getTokenName(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build()
				.parseClaimsJws(token).getBody()
				.getSubject();
		
	}

	public static boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
