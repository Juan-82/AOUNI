package com.example.demo.PTO;

public class AlterarNomeUsuarioDTO {
    private String novoNome;
    
    public AlterarNomeUsuarioDTO() {}
    
    public AlterarNomeUsuarioDTO(String novoNome) {
        this.novoNome = novoNome;
    }

    public String getNovoNome() {
        return novoNome;
    }

    public void setNovoNome(String novoNome) {
        this.novoNome = novoNome;
    }
}