package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Postagem;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, String> {

}
