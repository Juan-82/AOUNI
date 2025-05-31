package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name="chat")
public class ChatGrupo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Lob
	private String mensagens;
	
	@Column(nullable=false)
	private String curso;
	
	@ManyToMany
    @JoinTable(
        name = "chat_usuario",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuarios = new HashSet<>();
	
	public ChatGrupo() {}
	public ChatGrupo(String curso, Set<Usuario> usuarios) {
		this.curso = curso;
		this.usuarios = usuarios;
	}
	
	public String adicionarMensagem(String mensagem) {
		mensagens += mensagem;
		return mensagens;
	}
	public String getMensagens() {
		return mensagens;
	}

	public void setMensagens(String mensagens) {
		this.mensagens = mensagens;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Long getId() {
		return id;
	}
}
