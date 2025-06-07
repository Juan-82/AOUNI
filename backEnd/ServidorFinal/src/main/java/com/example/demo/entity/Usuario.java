package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String usuario;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
	private String senha;
	
	@Column
	private String nomeArquivoFoto;
	
	@Column
	private boolean temFoto;
	
	@OneToMany(mappedBy="idUsuario") 
	Set<Postagem> postagens = new HashSet<>();
	
	@ManyToMany(mappedBy="usuarios")
	private Set<ChatGrupo> chatsgrupo = new HashSet<>();
	
	public Usuario(String usuario, String email, String senha) {
		this.usuario = usuario;
		this.email = email;
		this.senha = senha;
		this.temFoto = false;
	}
	
	public Usuario(String usuario) {
		this.usuario = usuario;
		this.temFoto = false;
	}
	
	public Usuario() {
		this.temFoto = false;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	
	public String getNomeArquivoFoto() {
		return nomeArquivoFoto;
	}

	public void setNomeArquivoFoto(String nomeArquivoFoto) {
		this.nomeArquivoFoto = nomeArquivoFoto;
	}

	public boolean isTemFoto() {
		return temFoto;
	}

	public void setTemFoto(boolean temFoto) {
		this.temFoto = temFoto;
	}
	
	public String getCaminhoFoto() {
		if (nomeArquivoFoto == null) return null;
		return "fotos-perfil/" + Long.toString(id) + "/" + nomeArquivoFoto;
	}
}