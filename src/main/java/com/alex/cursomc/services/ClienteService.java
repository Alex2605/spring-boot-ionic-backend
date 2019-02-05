package com.alex.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.cursomc.domain.Cidade;
import com.alex.cursomc.domain.Cliente;
import com.alex.cursomc.domain.Endereco;
import com.alex.cursomc.domain.enums.TipoCliente;
import com.alex.cursomc.dto.ClienteDTO;
import com.alex.cursomc.dto.ClienteNewDTO;
import com.alex.cursomc.repositories.CidadeRepository;
import com.alex.cursomc.repositories.ClienteRepository;
import com.alex.cursomc.repositories.EnderecoRepository;
import com.alex.cursomc.services.exceptions.DataIntegrityException;
import com.alex.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		Cliente obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: "+ id +
											  ", Tipo: "+Cliente.class.getName());
		}
		return obj;
		
	}
	@Transactional
	public Cliente Insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}

	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData (newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas.");
		}
		
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linePerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linePerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDtO) {
		return new Cliente(objDtO.getId(), objDtO.getNome(), objDtO.getEmail(), null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDtO) {
		Cliente cli = new Cliente(null, objDtO.getNome(), objDtO.getEmail(), objDtO.getCpfOuCnpj(),TipoCliente.toEnum(objDtO.getTipo()));
		Cidade cid = cidadeRepository.findOne(objDtO.getCidadeId());
		Endereco end = new Endereco(null, objDtO.getLogradouro(), objDtO.getNumero(), objDtO.getComplemento(), objDtO.getBairro(), objDtO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDtO.getTelefone1());
		if (objDtO.getTelefone2() != null){
			cli.getTelefones().add(objDtO.getTelefone2());
		}
		if (objDtO.getTelefone3() != null){
			cli.getTelefones().add(objDtO.getTelefone3());
		}
		return cli;

	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
