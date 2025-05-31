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
	private String nome;
	
	
	@OneToMany(mappedBy="idUsuario") 
	Set<Postagem> postagens = new HashSet<>();
	
	@ManyToMany(mappedBy="usuarios")
	private Set<ChatGrupo> chatsgrupo = new HashSet<>();
	
	public Usuario(String nome) {
		this.nome = nome;
	}
	public Usuario() {
		
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
	
	
}
