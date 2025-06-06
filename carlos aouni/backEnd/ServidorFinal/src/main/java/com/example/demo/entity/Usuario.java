package com.example.demo.entity;

import java.time.LocalDateTime;
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
	private String nome;
	
	@Column(nullable=false)
	private String sobrenome;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
	private String senha;
	
	@Column(nullable=false, unique=true)
	private String nomeUsuario;
	
	@Column
	private String nomeArquivoFoto;
	
	@Column
	private boolean temFoto;
	
	@Column
	private String codigoRecuperacao;
	
	@Column
	private LocalDateTime dataExpiracaoCodigo;
	
	@OneToMany(mappedBy="idUsuario") 
	Set<Postagem> postagens = new HashSet<>();
	
	@ManyToMany(mappedBy="usuarios")
	private Set<ChatGrupo> chatsgrupo = new HashSet<>();
	
	public Usuario(String nome, String sobrenome, String email, String senha, String nomeUsuario) {
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.senha = senha;
		this.nomeUsuario = nomeUsuario;
		this.temFoto = false;
	}
	
	public Usuario(String nome, String sobrenome, String email, String senha) {
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.senha = senha;
		this.temFoto = false;
	}
	
	public Usuario(String nome) {
		this.nome = nome;
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
	
	public String getCodigoRecuperacao() {
		return codigoRecuperacao;
	}

	public void setCodigoRecuperacao(String codigoRecuperacao) {
		this.codigoRecuperacao = codigoRecuperacao;
	}

	public LocalDateTime getDataExpiracaoCodigo() {
		return dataExpiracaoCodigo;
	}

	public void setDataExpiracaoCodigo(LocalDateTime dataExpiracaoCodigo) {
		this.dataExpiracaoCodigo = dataExpiracaoCodigo;
	}
	
	public String getCaminhoFoto() {
		if (nomeArquivoFoto == null) return null;
		return "fotos-perfil/" + Long.toString(id) + "/" + nomeArquivoFoto;
	}
	
	public String getNomeCompleto() {
		return nome + " " + sobrenome;
	}
}