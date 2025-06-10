package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.PTO.AlterarSenhaDTO;
import com.example.demo.PTO.UsuarioCadastroDTO;
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
	
	@PostMapping("/cadastro")
	public ResponseEntity<String> cadastrarUsuario(@RequestBody UsuarioCadastroDTO cadastroDTO) {
		
		if (cadastroDTO.getUsuario() == null || cadastroDTO.getUsuario().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Usuario é obrigatório");
		}
		
		if (cadastroDTO.getEmail() == null || cadastroDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		if (cadastroDTO.getSenha() == null || cadastroDTO.getSenha().length() < 6) {
			return ResponseEntity.badRequest().body("Senha deve ter pelo menos 6 caracteres");
		}
		
		String nomeUsuario = cadastroDTO.getUsuario().trim();
		if (nomeUsuario.length() < 3 || nomeUsuario.length() > 20) {
			return ResponseEntity.badRequest().body("Nome de usuário deve ter entre 3 e 20 caracteres");
		}
		
		if (!nomeUsuario.matches("^[a-zA-Z0-9_]+$")) {
			return ResponseEntity.badRequest().body("Nome de usuário pode conter apenas letras, números e _");
		}
		
		Optional<Usuario> usuarioExistentePorEmail = usuarioRepository.findByEmail(cadastroDTO.getEmail());
		if (usuarioExistentePorEmail.isPresent()) {
			return ResponseEntity.badRequest().body("Email já está em uso");
		}
		
		Usuario novoUsuario = new Usuario(
			nomeUsuario,
			cadastroDTO.getEmail().trim().toLowerCase(),
			cadastroDTO.getSenha()
		);
		
		usuarioRepository.save(novoUsuario);
		
		return ResponseEntity.ok("Usuário cadastrado com sucesso! ID: " + novoUsuario.getId() + 
		                         ", Usuário: " + novoUsuario.getUsuario());
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUsuario(@RequestBody UsuarioCadastroDTO loginDTO) {
		
		if (loginDTO.getEmail() == null || loginDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		if (loginDTO.getSenha() == null) {
			return ResponseEntity.badRequest().body("Senha é obrigatória");
		}
		
		Optional<Usuario> usuarioOP = usuarioRepository.findByEmail(loginDTO.getEmail().trim().toLowerCase());
		
		if (usuarioOP.isEmpty()) {
			return ResponseEntity.badRequest().body("Email não encontrado");
		}
		
		Usuario usuario = usuarioOP.get();
		
		if (!usuario.getSenha().equals(loginDTO.getSenha())) {
			return ResponseEntity.badRequest().body("Senha incorreta");
		}
		
		return ResponseEntity.ok("Login realizado com sucesso! Bem-vindo, " + usuario.getUsuario() + 
		                         "! ID: " + usuario.getId());
	}
	
	@PostMapping("/verificar-email")
	public ResponseEntity<String> verificarEmail(@RequestBody AlterarSenhaDTO emailDTO) {
		
		if (emailDTO.getEmail() == null || emailDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		Optional<Usuario> usuarioOP = usuarioRepository.findByEmail(emailDTO.getEmail().trim().toLowerCase());
		
		if (usuarioOP.isEmpty()) {
			return ResponseEntity.badRequest().body("Email não encontrado");
		}
		
		Usuario usuario = usuarioOP.get();
		
		return ResponseEntity.ok("Email encontrado! Usuário: " + usuario.getUsuario() + 
		                         " (ID: " + usuario.getId() + ")");
	}
	
	@PutMapping("/alterar-senha")
	public ResponseEntity<String> alterarSenha(@RequestBody AlterarSenhaDTO alterarDTO) {
		
		if (alterarDTO.getEmail() == null || alterarDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		if (alterarDTO.getNovaSenha() == null || alterarDTO.getNovaSenha().length() < 6) {
			return ResponseEntity.badRequest().body("Nova senha deve ter pelo menos 6 caracteres");
		}
		
		Optional<Usuario> usuarioOP = usuarioRepository.findByEmail(alterarDTO.getEmail().trim().toLowerCase());
		
		if (usuarioOP.isEmpty()) {
			return ResponseEntity.badRequest().body("Email não encontrado");
		}
		
		Usuario usuario = usuarioOP.get();
		
		if (usuario.getSenha().equals(alterarDTO.getNovaSenha())) {
			return ResponseEntity.badRequest().body("A nova senha deve ser diferente da senha atual");
		}
		
		usuario.setSenha(alterarDTO.getNovaSenha());
		usuarioRepository.save(usuario);
		
		return ResponseEntity.ok("Senha alterada com sucesso para o usuário: " + usuario.getUsuario() + "!");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuario(@PathVariable long id) {
		Optional<Usuario> usuarioOP = usuarioRepository.findById(id);
		if (usuarioOP.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		return ResponseEntity.ok().body(usuarioOP.get());
	}
	
	@PostMapping("/{id}/foto")
	public ResponseEntity<String> adicionarFoto(@PathVariable long id, @RequestParam MultipartFile foto) {
		Optional<Usuario> usuarioOP = usuarioRepository.findById(id);
		if (usuarioOP.isEmpty())
			return ResponseEntity.notFound().build();
		
		Usuario usuario = usuarioOP.get();
		
		if (usuario.isTemFoto()) {
			try {
				Path caminhoAntigo = Paths.get(usuario.getCaminhoFoto());
				Files.deleteIfExists(caminhoAntigo);
				Files.deleteIfExists(caminhoAntigo.getParent());
			} catch (IOException e) {
			}
		}
		
		usuario.setNomeArquivoFoto(foto.getOriginalFilename());
		usuario.setTemFoto(true);
		
		try {
			usuarioRepository.save(usuario);
			Path caminho = Paths.get(usuario.getCaminhoFoto());
			
			Files.createDirectories(caminho.getParent());
			Files.write(caminho, foto.getBytes());
			
			return ResponseEntity.ok("Foto de perfil atualizada com sucesso! Usuário: " + usuario.getUsuario());
			
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao salvar foto: " + e.getMessage());
		}
	}
	
	@GetMapping("/{id}/foto")
	public ResponseEntity<Resource> getFoto(@PathVariable long id) {
		Optional<Usuario> usuarioOP = usuarioRepository.findById(id);
		if (usuarioOP.isEmpty())
			return ResponseEntity.notFound().build();
		
		Usuario usuario = usuarioOP.get();
		
		if (!usuario.isTemFoto()) {
			return ResponseEntity.notFound().build();
		}
		
		Path caminho = Paths.get(usuario.getCaminhoFoto());
		try {
			Resource recurso = new UrlResource(caminho.toUri());
			
			if (!recurso.exists()) 
				return ResponseEntity.notFound().build();
			
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, 
					       "attachment; filename=\"" + recurso.getFilename() + "\"")
					.body(recurso);
					
		} catch (MalformedURLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@DeleteMapping("/{id}/foto")
	public ResponseEntity<String> removerFoto(@PathVariable long id) {
		Optional<Usuario> usuarioOP = usuarioRepository.findById(id);
		if (usuarioOP.isEmpty())
			return ResponseEntity.notFound().build();
		
		Usuario usuario = usuarioOP.get();
		
		if (!usuario.isTemFoto()) {
			return ResponseEntity.badRequest().body("Usuário não possui foto de perfil");
		}
		
		Path caminho = Paths.get(usuario.getCaminhoFoto());
		try {
			Files.deleteIfExists(caminho);
			Files.deleteIfExists(caminho.getParent());
			
			usuario.setNomeArquivoFoto(null);
			usuario.setTemFoto(false);
			usuarioRepository.save(usuario);
			
			return ResponseEntity.ok("Foto de perfil removida com sucesso! Usuário: " + usuario.getUsuario());
			
		} catch (IOException e) {
			return ResponseEntity.internalServerError()
					.body("Erro ao remover foto: " + e.getMessage());
		}
	}
}