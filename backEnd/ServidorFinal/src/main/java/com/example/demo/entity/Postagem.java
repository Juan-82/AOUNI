package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name="postagem")
public class Postagem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id_postagem;
	
	@Column(nullable=false)
	private String conteudo;
	
	@Column(nullable=false)
	private LocalDateTime data_postagem;
	
	@Column(nullable=false)
	private String nomeArquivo;
	
	@Column
	private int curtidas;
	
	@Column
	private boolean visivel;
	
	@ManyToOne
	@JoinColumn(name="id_usuario") 
	private Usuario idUsuario;
	 

	public Postagem() {}
	public Postagem(String conteudo, String nomeArquivo, Usuario idUsuario) {
		this.conteudo = conteudo;
		this.nomeArquivo = nomeArquivo;
		this.curtidas = 0;
		this.data_postagem = LocalDateTime.now();
		this.visivel = true;
		this.idUsuario = idUsuario;
	}

	
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Long getId() {
		return id_postagem;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public LocalDateTime getData_postagem() {
		return data_postagem;
	}

	public void setData_postagem(LocalDateTime data_postagem) {
		this.data_postagem = data_postagem;
	}

	public int getCurtidas() {
		return curtidas;
	}

	public void setCurtidas(int curtidas) {
		this.curtidas = curtidas;
	}

	public boolean isVisivel() {
		return visivel;
	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}
}
