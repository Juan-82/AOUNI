package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ChatGrupo;

@Repository
public interface ChatGrupoRepository extends JpaRepository<ChatGrupo, Long>{

}
