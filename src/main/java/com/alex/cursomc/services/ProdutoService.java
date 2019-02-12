package com.alex.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.alex.cursomc.domain.Categoria;
import com.alex.cursomc.domain.Produto;
import com.alex.cursomc.repositories.CategoriaRepository;
import com.alex.cursomc.repositories.ProdutoRepository;
import com.alex.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		Produto obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: "+ id +
											  ", Tipo: "+Produto.class.getName());
		}
		return obj;
		
	}
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linePerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linePerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAll(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

}
