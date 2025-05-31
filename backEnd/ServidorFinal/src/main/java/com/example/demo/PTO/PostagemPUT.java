package com.example.demo.PTO;

public class PostagemPUT {
	private String conteudo;
	private Boolean visivel;
	
	
	public PostagemPUT() {}


	public PostagemPUT(String conteudo, Boolean visivel) {
		this.conteudo = conteudo;
		this.visivel = visivel;
	}
	
	public PostagemPUT(String conteudo) {
		this.conteudo = conteudo;
	}

	public PostagemPUT(Boolean visivel) {
		this.visivel = visivel;
	}

	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public Boolean isVisivel() {
		return visivel;
	}
	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}
}
