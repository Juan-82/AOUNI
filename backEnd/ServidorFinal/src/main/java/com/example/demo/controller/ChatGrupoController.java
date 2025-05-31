package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.PTO.ChatGrupoPTO;
import com.example.demo.entity.ChatGrupo;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ChatGrupoRepository;
import com.example.demo.repository.UsuarioRepository;

@RestController
@RequestMapping("chat")
public class ChatGrupoController {
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	ChatGrupoRepository chatGrupoRepository;

	
	@GetMapping
	public List<ChatGrupo> getChat(){
		/*
		 * Set<Usuario> usuarios = new HashSet<>(); Usuario usuario =
		 * usuarioRepository.findById((long) 1).orElseThrow(); usuarios.add(usuario);
		 * chatGrupoRepository.save(new ChatGrupo("uninassau", usuarios));  */
		 return chatGrupoRepository.findAll();
	}
	
	@PostMapping
	public ChatGrupo postChat(@RequestBody ChatGrupo chatGrupo) {
		return chatGrupoRepository.save(chatGrupo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> putMensagem(@PathVariable long id, @RequestBody ChatGrupoPTO chatGrupoPTO){
		Optional<ChatGrupo> chatGrupoOP = chatGrupoRepository.findById(id);
		Optional<Usuario> usuarioOP = usuarioRepository.findById(chatGrupoPTO.getId());
		
		if (chatGrupoOP.isEmpty())
			return ResponseEntity.notFound().build();
		if (usuarioOP.isEmpty())
			return ResponseEntity.badRequest().body("usuario nao encontrado");
		
		Usuario usuario = usuarioOP.get();
		ChatGrupo chatGrupo = chatGrupoOP.get();
		
		String msg = String.format("(%s):%d\n", chatGrupoPTO.getMensagem(), usuario.getId());
		chatGrupo.adicionarMensagem(msg);
		chatGrupoRepository.save(chatGrupo);
		return ResponseEntity.ok().body(msg);
		
	}

}
