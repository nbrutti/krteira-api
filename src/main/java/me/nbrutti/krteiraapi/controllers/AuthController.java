package me.nbrutti.krteiraapi.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.nbrutti.krteiraapi.models.Papel;
import me.nbrutti.krteiraapi.models.TipoPapel;
import me.nbrutti.krteiraapi.models.Usuario;
import me.nbrutti.krteiraapi.payload.request.LoginRequest;
import me.nbrutti.krteiraapi.payload.request.SignupRequest;
import me.nbrutti.krteiraapi.payload.response.JwtResponse;
import me.nbrutti.krteiraapi.payload.response.MessageResponse;
import me.nbrutti.krteiraapi.repository.PapelRepository;
import me.nbrutti.krteiraapi.repository.UsuarioRepository;
import me.nbrutti.krteiraapi.security.jwt.JwtUtils;
import me.nbrutti.krteiraapi.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	PapelRepository papelRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		List<String> papeis = userDetails.getAuthorities().stream()
										.map(item -> item.getAuthority())
										.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(),
												 userDetails.getEmail(),
												 papeis));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		Usuario usuario = new Usuario(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

		Set<String> strPapeis = signUpRequest.getRole();
		Set<Papel> papeis = new HashSet<>();

		if (strPapeis == null) {
			Papel userRole = papelRepository.findByTipoPapel(TipoPapel.USUARIO)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			papeis.add(userRole);
		} else {
			strPapeis.forEach(role -> {
				switch (role) {
				case "admin":
					Papel adminRole = papelRepository.findByTipoPapel(TipoPapel.ADMINISTRADOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					papeis.add(adminRole);

					break;
				default:
					Papel userRole = papelRepository.findByTipoPapel(TipoPapel.USUARIO)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					papeis.add(userRole);
				}
			});
		}

		usuario.setPapeis(papeis);
		usuarioRepository.save(usuario);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}
