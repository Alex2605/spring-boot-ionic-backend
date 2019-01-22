package com.alex.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alex.cursomc.domain.Categoria;
import com.alex.cursomc.repositories.CategoriaRpository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRpository repo;
	
	public Categoria buscar(Integer id) {
		Categoria obj = repo.findOne(id);
		return obj;
		
	}

}
