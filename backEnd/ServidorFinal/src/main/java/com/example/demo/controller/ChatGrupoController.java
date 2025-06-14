package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.PTO.ChatGrupoPTO;
import com.example.demo.PTO.CriarChat;
import com.example.demo.PTO.UsuarioPegarID;
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
	@GetMapping("/{id}/usuario")
	public ResponseEntity<Set<Usuario>> listUsuario(@PathVariable long id){
		Optional<ChatGrupo> chatGrupoOP = chatGrupoRepository.findById(id);
		if (chatGrupoOP.isEmpty())
			return ResponseEntity.notFound().build();
		
		ChatGrupo chatGrupo = chatGrupoOP.get();
		
		return ResponseEntity.ok(chatGrupo.getUsuarios());
		
	}
	
	@PostMapping
	public ResponseEntity<ChatGrupo> postChat(@RequestBody CriarChat criarChat) {
		Optional<Usuario> usuarioOP = usuarioRepository.findById(criarChat.getIdUsuario());
		if (usuarioOP.isEmpty())
			return ResponseEntity.badRequest().build();
		Set<Usuario> usuarios = new HashSet<>();
		usuarios.add(usuarioOP.get());
		ChatGrupo chatGrupo = new ChatGrupo(criarChat.getCurso(), usuarios, criarChat.getUniversidade());
		return ResponseEntity.ok(chatGrupoRepository.save(chatGrupo));
	}
	
	@PutMapping("/{id}/mensagem")
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
	
	@GetMapping("/{id}/mensagem")
	public ResponseEntity<List<ChatGrupoPTO>> getMensagem(@PathVariable long id){
		Optional<ChatGrupo> chatGrupoOP = chatGrupoRepository.findById(id);
		if (chatGrupoOP.isEmpty())
			return ResponseEntity.notFound().build();
		
		ChatGrupo chatGrupo = chatGrupoOP.get();
		List<ChatGrupoPTO> chatGrupoPTOs = new ArrayList<>();
		
		if (chatGrupo.getMensagens() ==  null)
			return ResponseEntity.ok(null);
		for (String i : chatGrupo.getMensagens().split("\n")) {
			System.out.print(i.toCharArray());
			String msg = i.substring(1, i.lastIndexOf(")"));
			String idUsuario = i.substring(i.lastIndexOf(":")+1);
			
			Optional<Usuario> usuario = usuarioRepository.findById(Long.parseLong(idUsuario));
			chatGrupoPTOs.add(new ChatGrupoPTO(msg, Long.parseLong(idUsuario), usuario.get().getUsuario()));
		}
		return ResponseEntity.ok(chatGrupoPTOs);
	}
	
	@PutMapping("/{id}/usuario")
	public ResponseEntity<String> adicionarUsuario(@PathVariable long id, @RequestBody UsuarioPegarID idUsuario){
		Optional<ChatGrupo> chatGrupoOP = chatGrupoRepository.findById(id);
		Optional<Usuario> usuarioOP = usuarioRepository.findById(idUsuario.getIdUsuario());
		if (chatGrupoOP.isEmpty())
			return ResponseEntity.notFound().build();
		if (usuarioOP.isEmpty())
			return ResponseEntity.badRequest().body("usuario nao encontrado");
		
		Usuario usuario = usuarioOP.get();
		ChatGrupo chatGrupo = chatGrupoOP.get();
		
		if(chatGrupo.getUsuarios().contains(usuario))
			return ResponseEntity.badRequest().body("Usuario ja esta no chat");
		
		chatGrupo.adicionarUsuario(usuario);
		chatGrupoRepository.save(chatGrupo);
		return ResponseEntity.ok("usuario adiconado com sucesso.\nUsuario: " + usuario.getId());
		
	}
	
	@DeleteMapping("/{id}/usuario")
	public ResponseEntity<String> removerUsuario(@PathVariable long id, @RequestBody UsuarioPegarID idUsuario){
		Optional<ChatGrupo> chatGrupoOP = chatGrupoRepository.findById(id);
		Optional<Usuario> usuarioOP = usuarioRepository.findById(idUsuario.getIdUsuario());
		if (chatGrupoOP.isEmpty())
			return ResponseEntity.notFound().build();
		if (usuarioOP.isEmpty())
			return ResponseEntity.badRequest().body("usuario nao encontrado");
		
		Usuario usuario = usuarioOP.get();
		ChatGrupo chatGrupo = chatGrupoOP.get();
		
		if(!chatGrupo.getUsuarios().contains(usuario))
			return ResponseEntity.badRequest().body("Usuario nao esta no chat");
		
		chatGrupo.removerUsuario(usuario);
		chatGrupoRepository.save(chatGrupo);
		return ResponseEntity.ok("usuario removido com sucesso.\nUsuario: " + usuario.getId());
	}

}