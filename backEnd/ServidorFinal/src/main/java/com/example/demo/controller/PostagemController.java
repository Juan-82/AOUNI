package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


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
		Usuario usuario = usuarioRepository.findById(Long.parseLong(idUsuario))
				.orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

		Postagem postagem = new Postagem(conteudo, arquivo.getOriginalFilename(), usuario);
		
		try {
			postagemRepository.save(postagem);
			Path caminho = Paths.get("postagens/" + idUsuario + "/" + Long.toString(postagem.getId()) + "/" + arquivo.getOriginalFilename());
			
			
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
		Postagem postagem = postagemRepository.findById(post).orElseThrow(() -> new RuntimeException("Postagem nao encontrada."));
		return ResponseEntity.ok().body(postagem);
	}
	
	
	@GetMapping("/{post}/arquivo")
	public ResponseEntity<Resource> getArquivo(@PathVariable long post) {
		Postagem postagem = postagemRepository.findById(post).orElseThrow(() -> new RuntimeException("Postagem nao encontrada."));
		
		Path caminho = Paths.get("postagens/" + Long.toString(postagem.getIdUsuario().getId()) + "/" +
				Long.toString(postagem.getId()) + "/" + postagem.getNomeArquivo());
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
	public String deletePostagem(@PathVariable Long id) {
		Postagem postagem = postagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Postagem nao encontrada."));
		Path caminho = Paths.get("postagens/" + Long.toString(postagem.getIdUsuario().getId()) + "/" +
				Long.toString(postagem.getId()) + "/" + postagem.getNomeArquivo());
		try {
			Files.delete(caminho.getParent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		String msg = ("Deletado com sucesso!\nID: " + postagem.getId() + "\nUsuarioID: " + postagem.getIdUsuario().getId());
		postagemRepository.deleteById(id);
		return msg;
	}
	
	
	@PutMapping("/{id}")
	public Postagem putPostagem(@RequestBody PostagemPUT postagemPUT, @PathVariable Long id) {
		Postagem postagem = postagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Postagem nao encontrada."));
		if (!(postagemPUT.getConteudo() == null))
			postagem.setConteudo(postagemPUT.getConteudo());
		 
		if(!(postagemPUT.isVisivel() == null))
			postagem.setVisivel(postagemPUT.isVisivel());
		
		postagemRepository.save(postagem);
		return postagem;
	}
	
	
	@PutMapping("/{id}/like")
	public int like(@PathVariable Long id) {
		Postagem postagem = postagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Postagem nao encontrada."));
		postagem.setCurtidas(postagem.getCurtidas()+1);
		postagemRepository.save(postagem);
		return postagem.getCurtidas();
	}
	
	
	@PutMapping("/{id}/deslike")
	public int deslike(@PathVariable Long id) {
		Postagem postagem = postagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Postagem nao encontrada."));
		if (postagem.getCurtidas() != 0) {
			postagem.setCurtidas(postagem.getCurtidas()-1);
			postagemRepository.save(postagem);
		}
		return postagem.getCurtidas();
	}
}
