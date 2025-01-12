package com.example.shop.security;

import org.springframework.http.ResponseCookie;

public class CookieUtil {
	public static ResponseCookie createCookie(String name, String value, long maxAge, boolean secure, String sameSite) {
        return ResponseCookie.from(name, value)
                .httpOnly(true) // 클라이언트에서 스크립트 접근 불가
                .secure(secure) // HTTPS 전송 여부 
                .path("/")      // 모든 경로에서 사용 가능
                .maxAge(maxAge) // 쿠키 유효 기간
                .sameSite(sameSite) // SameSite 설정
                .build();
    }
	
	public static ResponseCookie deleteCookie(String name) {
	    return ResponseCookie.from(name, "")
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(0) // 즉시 만료
	            .sameSite("Strict")
	            .build();
	}
	
	public static ResponseCookie updateCookie(String name, String value, long maxAge, boolean secure, String sameSite) {
	    return ResponseCookie.from(name, value)
	            .httpOnly(true) // 클라이언트에서 스크립트 접근 불가
	            .secure(secure) // HTTPS 환경에서만 사용
	            .path("/")      // 모든 경로에 적용
	            .maxAge(maxAge) // 쿠키 유효 기간 (초 단위)
	            .sameSite(sameSite) // SameSite 설정
	            .build();
	}
}
