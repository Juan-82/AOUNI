package com.example.demo.PTO;

public class UsuarioCadastroDTO {
	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	private String nomeUsuario;
	
	public UsuarioCadastroDTO() {}
	
	public UsuarioCadastroDTO(String nome, String sobrenome, String email, String senha, String nomeUsuario) {
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.senha = senha;
		this.nomeUsuario = nomeUsuario;
	}
	
	public UsuarioCadastroDTO(String nome, String sobrenome, String email, String senha) {
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
}