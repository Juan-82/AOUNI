package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@RestController
@RequestMapping("/arquivos")
public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping
	public ResponseEntity<String> postar(@RequestParam("file") MultipartFile file, @RequestParam String id) {
		Path caminho = Paths.get("postagens/" + id, file.getOriginalFilename());
		try {
			Files.createDirectories(caminho.getParent());
			Files.write(caminho,  file.getBytes());
			return ResponseEntity.ok("Deu certo");
		} catch (IOException e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Deu errado: " + e.getMessage());
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<Resource> download(@RequestParam String nome){
		Path caminho = Paths.get("postagens\\" + nome);
		try {
			Resource recurso = new UrlResource(caminho.toUri());
			if (!recurso.exists()) return ResponseEntity.notFound().build();
			
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename='" + recurso.getFilename() + "'")
					.body(recurso);

		} catch (MalformedURLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}
