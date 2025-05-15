package br.edu.uninassau.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("upload")
public class EnvioController {
	
	@PostMapping
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
		try {
			Path caminho = Paths.get("upload/" + file.getOriginalFilename());
			Files.createDirectories(caminho.getParent());
			Files.write(caminho, file.getBytes());
			
			return ResponseEntity.ok("Arquivo enviado com sucesso");
			
			
		}catch (IOException e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("erro ao enviar o arquivo" + e.getMessage());
		}
	}
	
}
