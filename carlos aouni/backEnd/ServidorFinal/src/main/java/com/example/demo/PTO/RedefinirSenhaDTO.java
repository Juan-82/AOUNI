package com.example.demo.PTO;

public class RedefinirSenhaDTO {
    private String email;
    private String senhaAtual;
    private String novaSenha;
    private String confirmarNovaSenha;
    
    public RedefinirSenhaDTO() {}
    
    public RedefinirSenhaDTO(String email, String senhaAtual, String novaSenha, String confirmarNovaSenha) {
        this.email = email;
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
        this.confirmarNovaSenha = confirmarNovaSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmarNovaSenha() {
        return confirmarNovaSenha;
    }

    public void setConfirmarNovaSenha(String confirmarNovaSenha) {
        this.confirmarNovaSenha = confirmarNovaSenha;
    }
}