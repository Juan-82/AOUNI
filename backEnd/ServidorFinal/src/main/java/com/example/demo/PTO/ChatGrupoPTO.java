package com.example.demo.PTO;

public class ChatGrupoPTO {
	private String mensagem;
	private Long id;
	
	public ChatGrupoPTO() {}
	public ChatGrupoPTO(String mensagem, Long id) {
		this.mensagem = mensagem;
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
