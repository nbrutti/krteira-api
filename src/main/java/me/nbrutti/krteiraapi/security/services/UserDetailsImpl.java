package me.nbrutti.krteiraapi.security.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import me.nbrutti.krteiraapi.models.Usuario;

@Data
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	private Collection<SimpleGrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String email, String password,
			Collection<SimpleGrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(final Usuario usuario) {
		final List<SimpleGrantedAuthority> authorities = usuario.getPapeis().stream()
				.map(papel -> new SimpleGrantedAuthority(papel.getTipoPapel().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				usuario.getId(),
				usuario.getEmail(), 
				usuario.getSenha(), 
				authorities);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}