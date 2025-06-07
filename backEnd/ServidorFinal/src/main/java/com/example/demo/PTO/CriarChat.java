package com.example.demo.PTO;

public class CriarChat {
	private String nome;
	private Long idUsuario;
	
	public CriarChat() {}
	public CriarChat(String nome, Long idUsuario) {
		this.nome = nome;
		this.idUsuario = idUsuario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
}
