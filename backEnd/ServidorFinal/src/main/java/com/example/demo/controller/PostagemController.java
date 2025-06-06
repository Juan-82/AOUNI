package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.PTO.PostagemPUT;
import com.example.demo.entity.Postagem;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.PostagemRepository;
import com.example.demo.repository.UsuarioRepository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@RestController
@RequestMapping("/postagem")
public class PostagemController {
	@Autowired
	PostagemRepository postagemRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	
	@GetMapping
	public List<Postagem> listPostagem(){
		return postagemRepository.findAll();
	}
	
	
	@PostMapping
	public ResponseEntity<Postagem> postPostagem(@RequestParam MultipartFile arquivo, @RequestParam String idUsuario, @RequestParam String conteudo){
		Optional<Usuario> usuarioOP = usuarioRepository.findById(Long.parseLong(idUsuario));
		if (usuarioOP.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		Usuario usuario = usuarioOP.get();
		Postagem postagem = new Postagem(conteudo, arquivo.getOriginalFilename(), usuario);
		
		if (arquivo.isEmpty())
			return ResponseEntity.badRequest().build();
		try {
			postagemRepository.save(postagem);
			Path caminho = Paths.get(postagem.getCaminhoString());
			
			Files.createDirectories(caminho.getParent());
			Files.write(caminho, arquivo.getBytes());
			
			return ResponseEntity.ok().body(postagem);
			
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	
	@GetMapping("/{post}")
	public ResponseEntity<Postagem> getPostagem(@PathVariable long post) {
		Optional<Postagem> postagemOP = postagemRepository.findById(post);
		if (postagemOP.isEmpty())
			return ResponseEntity.notFound().build();
		Postagem postagem = postagemOP.get();
		return ResponseEntity.ok().body(postagem);
	}
	
	
	@GetMapping("/{post}/arquivo")
	public ResponseEntity<Resource> getArquivo(@PathVariable long post) {
		Optional<Postagem> postagemOP = postagemRepository.findById(post);
		if (postagemOP.isEmpty())
			return ResponseEntity.notFound().build();
		Postagem postagem = postagemOP.get();
		
		Path caminho = Paths.get(postagem.getCaminhoString());
		try {
			Resource recurso = new UrlResource(caminho.toUri());
			
			if (!recurso.exists()) return ResponseEntity.notFound().build();
			
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
		} catch (MalformedURLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
		
	}
	

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePostagem(@PathVariable Long id) {
		Optional<Postagem> postagemOP = postagemRepository.findById(id);
		if (postagemOP.isEmpty())
			return ResponseEntity.notFound().build();
		Postagem postagem = postagemOP.get();

		Path caminho = Paths.get(postagem.getCaminhoString());
		try {
			Files.delete(caminho);
			Files.delete(caminho.getParent());
			String msg = ("Deletado com sucesso!\nID: " + postagem.getId() + "\nUsuarioID: " + postagem.getIdUsuario().getId());
			postagemRepository.deleteById(id);
			return ResponseEntity.ok().body(msg);
			
		} catch (IOException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Postagem> putPostagem(@RequestBody PostagemPUT postagemPUT, @PathVariable Long id) {
		Optional<Postagem> postagemOP = postagemRepository.findById(id);
		if (postagemOP.isEmpty())
			return ResponseEntity.notFound().build();
		Postagem postagem = postagemOP.get();

		if (!(postagemPUT.getConteudo() == null))
			postagem.setConteudo(postagemPUT.getConteudo());
		 
		if(!(postagemPUT.isVisivel() == null))
			postagem.setVisivel(postagemPUT.isVisivel());
		
		postagemRepository.save(postagem);
		return ResponseEntity.ok().body(postagem);
	}
	
	
	@PutMapping("/{id}/like")
	public ResponseEntity<Integer> like(@PathVariable Long id) {
		Optional<Postagem> postagemOP = postagemRepository.findById(id);
		if (postagemOP.isEmpty())
			return ResponseEntity.notFound().build();
		Postagem postagem = postagemOP.get();

		postagem.setCurtidas(postagem.getCurtidas()+1);
		postagemRepository.save(postagem);
		return ResponseEntity.ok().body(postagem.getCurtidas());
	}
	
	
	@PutMapping("/{id}/deslike")
	public ResponseEntity<Integer> deslike(@PathVariable Long id) {
		Optional<Postagem> postagemOP = postagemRepository.findById(id);
		if (postagemOP.isEmpty())
			return ResponseEntity.notFound().build();
		Postagem postagem = postagemOP.get();

		postagem.setCurtidas(postagem.getCurtidas()-1);
		postagemRepository.save(postagem);
		return ResponseEntity.ok().body(postagem.getCurtidas());
	}
}
