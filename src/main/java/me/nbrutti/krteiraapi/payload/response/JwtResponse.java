package me.nbrutti.krteiraapi.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {
	
	private String token;
	private String type = "Bearer";
	private Long id;
	private String email;
	private List<String> roles;
	
	public JwtResponse(final String accessToken, final Long id, final String email, final List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.email = email;
		this.roles = roles;
	}
	
}