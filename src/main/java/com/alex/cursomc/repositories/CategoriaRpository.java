package com.alex.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.cursomc.domain.Categoria;

@Repository
public interface CategoriaRpository extends JpaRepository<Categoria, Integer>{

}
