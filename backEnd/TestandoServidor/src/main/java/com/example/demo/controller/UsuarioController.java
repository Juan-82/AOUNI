package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@RestController
@RequestMapping("/")
public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping("/usuario")
	public String saveUsuario(@RequestBody Usuario usuario) {
		System.out.print(usuario);
		usuarioRepository.save(usuario);
		return "Usuario salvo com sucesso";
	}
	
	@GetMapping("/usuario")
	public List<Usuario> getUsuario() {
		return usuarioRepository.findAll();
	}
}
