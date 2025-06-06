package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

import com.example.demo.PTO.EsqueciSenhaDTO;
import com.example.demo.PTO.RedefinirSenhaComCodigoDTO;
import com.example.demo.PTO.RedefinirSenhaDTO;
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
		
		if (cadastroDTO.getNome() == null || cadastroDTO.getNome().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Nome é obrigatório");
		}
		
		if (cadastroDTO.getSobrenome() == null || cadastroDTO.getSobrenome().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Sobrenome é obrigatório");
		}
		
		if (cadastroDTO.getEmail() == null || cadastroDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		if (cadastroDTO.getSenha() == null || cadastroDTO.getSenha().length() < 6) {
			return ResponseEntity.badRequest().body("Senha deve ter pelo menos 6 caracteres");
		}
		
		if (cadastroDTO.getNomeUsuario() == null || cadastroDTO.getNomeUsuario().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Nome de usuário é obrigatório");
		}
		
		String nomeUsuario = cadastroDTO.getNomeUsuario().trim();
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
		
		Optional<Usuario> usuarioExistentePorNome = usuarioRepository.findByNomeUsuario(nomeUsuario);
		if (usuarioExistentePorNome.isPresent()) {
			return ResponseEntity.badRequest().body("Nome de usuário já está em uso");
		}
		
		Usuario novoUsuario = new Usuario(
			cadastroDTO.getNome().trim(),
			cadastroDTO.getSobrenome().trim(),
			cadastroDTO.getEmail().trim().toLowerCase(),
			cadastroDTO.getSenha(),
			nomeUsuario
		);
		
		usuarioRepository.save(novoUsuario);
		
		return ResponseEntity.ok("Usuário cadastrado com sucesso! ID: " + novoUsuario.getId() + 
		                         ", Nome de usuário: @" + novoUsuario.getNomeUsuario());
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
		
		return ResponseEntity.ok("Login realizado com sucesso! Bem-vindo, " + usuario.getNomeCompleto() + 
		                         " (@" + usuario.getNomeUsuario() + ")! ID: " + usuario.getId());
	}
	
	@PutMapping("/redefinir-senha")
	public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaDTO redefinirDTO) {
		
		if (redefinirDTO.getEmail() == null || redefinirDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		if (redefinirDTO.getSenhaAtual() == null || redefinirDTO.getSenhaAtual().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Senha atual é obrigatória");
		}
		
		if (redefinirDTO.getNovaSenha() == null || redefinirDTO.getNovaSenha().length() < 6) {
			return ResponseEntity.badRequest().body("Nova senha deve ter pelo menos 6 caracteres");
		}
		
		if (redefinirDTO.getConfirmarNovaSenha() == null || redefinirDTO.getConfirmarNovaSenha().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Confirmação da nova senha é obrigatória");
		}
		
		if (!redefinirDTO.getNovaSenha().equals(redefinirDTO.getConfirmarNovaSenha())) {
			return ResponseEntity.badRequest().body("Nova senha e confirmação não coincidem");
		}
		
		if (redefinirDTO.getSenhaAtual().equals(redefinirDTO.getNovaSenha())) {
			return ResponseEntity.badRequest().body("A nova senha deve ser diferente da senha atual");
		}
		
		Optional<Usuario> usuarioOP = usuarioRepository.findByEmail(redefinirDTO.getEmail().trim().toLowerCase());
		
		if (usuarioOP.isEmpty()) {
			return ResponseEntity.badRequest().body("Email não encontrado");
		}
		
		Usuario usuario = usuarioOP.get();
		
		if (!usuario.getSenha().equals(redefinirDTO.getSenhaAtual())) {
			return ResponseEntity.badRequest().body("Senha atual incorreta");
		}
		
		usuario.setSenha(redefinirDTO.getNovaSenha());
		usuarioRepository.save(usuario);
		
		return ResponseEntity.ok("Senha redefinida com sucesso para o usuário @" + usuario.getNomeUsuario() + "!");
	}
	
	@PostMapping("/esqueci-senha")
	public ResponseEntity<String> esqueciSenha(@RequestBody EsqueciSenhaDTO esqueciSenhaDTO) {
		
		if (esqueciSenhaDTO.getEmail() == null || esqueciSenhaDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		Optional<Usuario> usuarioOP = usuarioRepository.findByEmail(esqueciSenhaDTO.getEmail().trim().toLowerCase());
		
		if (usuarioOP.isEmpty()) {
			return ResponseEntity.badRequest().body("Email não encontrado");
		}
		
		Usuario usuario = usuarioOP.get();
		
		Random random = new Random();
		String codigo = String.format("%06d", random.nextInt(1000000));
		
		LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(15);
		
		usuario.setCodigoRecuperacao(codigo);
		usuario.setDataExpiracaoCodigo(dataExpiracao);
		usuarioRepository.save(usuario);
		
		System.out.println("=== SIMULAÇÃO DE EMAIL ===");
		System.out.println("Para: " + usuario.getEmail());
		System.out.println("Assunto: Código de Recuperação de Senha");
		System.out.println("Olá " + usuario.getNomeCompleto() + ",");
		System.out.println("Seu código de recuperação é: " + codigo);
		System.out.println("Este código expira em 15 minutos.");
		System.out.println("Se você não solicitou esta recuperação, ignore este email.");
		System.out.println("========================");
		
		return ResponseEntity.ok("Código de recuperação enviado para o email " + usuario.getEmail() + 
		                         ". Verifique também o console do servidor. O código expira em 15 minutos.");
	}
	
	@PostMapping("/redefinir-senha-codigo")
	public ResponseEntity<String> redefinirSenhaComCodigo(@RequestBody RedefinirSenhaComCodigoDTO redefinirDTO) {
		
		if (redefinirDTO.getEmail() == null || redefinirDTO.getEmail().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email é obrigatório");
		}
		
		if (redefinirDTO.getCodigo() == null || redefinirDTO.getCodigo().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Código de recuperação é obrigatório");
		}
		
		if (redefinirDTO.getNovaSenha() == null || redefinirDTO.getNovaSenha().length() < 6) {
			return ResponseEntity.badRequest().body("Nova senha deve ter pelo menos 6 caracteres");
		}
		
		if (redefinirDTO.getConfirmarNovaSenha() == null || redefinirDTO.getConfirmarNovaSenha().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Confirmação da nova senha é obrigatória");
		}
		
		if (!redefinirDTO.getNovaSenha().equals(redefinirDTO.getConfirmarNovaSenha())) {
			return ResponseEntity.badRequest().body("Nova senha e confirmação não coincidem");
		}
		
		Optional<Usuario> usuarioOP = usuarioRepository.findByEmail(redefinirDTO.getEmail().trim().toLowerCase());
		
		if (usuarioOP.isEmpty()) {
			return ResponseEntity.badRequest().body("Email não encontrado");
		}
		
		Usuario usuario = usuarioOP.get();
		
		if (usuario.getCodigoRecuperacao() == null || usuario.getDataExpiracaoCodigo() == null) {
			return ResponseEntity.badRequest().body("Nenhum código de recuperação foi solicitado para este email");
		}
		
		if (LocalDateTime.now().isAfter(usuario.getDataExpiracaoCodigo())) {
			usuario.setCodigoRecuperacao(null);
			usuario.setDataExpiracaoCodigo(null);
			usuarioRepository.save(usuario);
			return ResponseEntity.badRequest().body("Código de recuperação expirado. Solicite um novo código.");
		}
		
		if (!usuario.getCodigoRecuperacao().equals(redefinirDTO.getCodigo().trim())) {
			return ResponseEntity.badRequest().body("Código de recuperação incorreto");
		}
		
		if (usuario.getSenha().equals(redefinirDTO.getNovaSenha())) {
			return ResponseEntity.badRequest().body("A nova senha deve ser diferente da senha atual");
		}
		
		usuario.setSenha(redefinirDTO.getNovaSenha());
		usuario.setCodigoRecuperacao(null);
		usuario.setDataExpiracaoCodigo(null);
		usuarioRepository.save(usuario);
		
		return ResponseEntity.ok("Senha redefinida com sucesso para o usuário @" + usuario.getNomeUsuario() + "!");
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
			
			return ResponseEntity.ok("Foto de perfil atualizada com sucesso! Usuário: " + usuario.getNomeCompleto());
			
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
			
			return ResponseEntity.ok("Foto de perfil removida com sucesso! Usuário: " + usuario.getNomeCompleto());
			
		} catch (IOException e) {
			return ResponseEntity.internalServerError()
					.body("Erro ao remover foto: " + e.getMessage());
		}
	}
}