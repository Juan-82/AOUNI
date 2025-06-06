package com.example.demo.PTO;

public class EsqueciSenhaDTO {
    private String email;
    
    public EsqueciSenhaDTO() {}
    
    public EsqueciSenhaDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}