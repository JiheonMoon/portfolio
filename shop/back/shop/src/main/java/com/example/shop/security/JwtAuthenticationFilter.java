package com.example.shop.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String accessToken = parseCookieToken(request, "access_token");
			String refreshToken = parseCookieToken(request, "refresh_token");

			if (accessToken != null && !accessToken.equalsIgnoreCase("null")) {
				if (tokenProvider.validateToken(accessToken)) {
					authenticateUser(accessToken, request);
				} else if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
					log.info("Access token expired. Generating new access token.");
					String userId = tokenProvider.validateAndGetUserId(refreshToken);

					// 새 Access Token 생성
					String newAccessToken = tokenProvider.createAccessToken(userId);
					
					// 기존 Access Token 쿠키 갱신
	                ResponseCookie updatedCookie = CookieUtil.updateCookie("access_token", newAccessToken, 60 * 60 * 24, false, "Strict"); // 1일
	                response.addHeader("Set-Cookie", updatedCookie.toString());

					authenticateUser(newAccessToken, request);
				} else {
					log.info("Invalid access and refresh tokens.");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("Authentication failed: Invalid tokens.");
					return;
				}
			} else {
				log.info("No tokens found in cookies.");
			}
		} catch (Exception e) {
			log.error("Could not set user authentication in security context", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Authentication failed: " + e.getMessage());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void authenticateUser(String token, HttpServletRequest request) {
		String userId = tokenProvider.validateAndGetUserId(token);
		log.info("Authenticated user ID: {}", userId);

		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	private String parseCookieToken(HttpServletRequest request, String tokenName) {
		jakarta.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (jakarta.servlet.http.Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
