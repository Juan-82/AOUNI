package com.example.demo.PTO;

public class CriarChat {
	private String curso;
	private Long idUsuario;
	private String universidade;
	
	public CriarChat() {}
	public CriarChat(String curso, Long idUsuario, String universidade) {
		this.curso = curso;
		this.idUsuario = idUsuario;
		this.universidade = universidade;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public String getUniversidade() {
		return universidade;
	}
	public void setUniversidade(String universidade) {
		this.universidade = universidade;
	}
	
}
