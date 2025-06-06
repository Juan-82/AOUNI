package com.example.demo.PTO;

public class RedefinirSenhaComCodigoDTO {
    private String email;
    private String codigo;
    private String novaSenha;
    private String confirmarNovaSenha;
    
    public RedefinirSenhaComCodigoDTO() {}
    
    public RedefinirSenhaComCodigoDTO(String email, String codigo, String novaSenha, String confirmarNovaSenha) {
        this.email = email;
        this.codigo = codigo;
        this.novaSenha = novaSenha;
        this.confirmarNovaSenha = confirmarNovaSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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