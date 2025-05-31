package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping
	public List<Usuario> listUsuario(){
		return usuarioRepository.findAll();
	}
	
	@PostMapping
	public Usuario postUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.save(usuario);
		return usuario;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuario(@PathVariable long id) {
		Optional<Usuario> usuarioOP = usuarioRepository.findById(id);
		if (usuarioOP.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		return ResponseEntity.ok().body(usuarioOP.get());
	}
}
