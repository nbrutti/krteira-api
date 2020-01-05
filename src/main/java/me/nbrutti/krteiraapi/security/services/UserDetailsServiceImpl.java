package me.nbrutti.krteiraapi.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.nbrutti.krteiraapi.models.Usuario;
import me.nbrutti.krteiraapi.repository.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email)
							.orElseThrow(() -> new UsernameNotFoundException("Não foi encontrado o usuário com e-mail: " + email));

		return UserDetailsImpl.build(usuario);
	}

}