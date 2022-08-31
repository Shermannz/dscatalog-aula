package com.devSuperior.dscatalog.services;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devSuperior.dscatalog.dto.CategoryDTO;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.repositories.CategoryRepository;
import com.devSuperior.dscatalog.services.exceptions.DatabaseException;
import com.devSuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list =  repository.findAll();
	
		return list.stream()
		.map(x -> new CategoryDTO(x))
		.collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
       Optional<Category> obj = repository.findById(id);
	   Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
	   return new CategoryDTO(entity);
	}

	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
		
	}

	@Transactional
	public CategoryDTO update(Long id,CategoryDTO dto) {
		try {
        Category entity = repository.getOne(id); // pegou o id imaginario
		entity.setName(dto.getName()); // atualizou o nome com o id imaginario
		entity = repository.save(entity); // mudou o nome com base no id identificado
		return new CategoryDTO(entity); // retornou o DTO pra n dar ruim
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		}

	public void delete(Long id) {
		try {
           repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
			
		}
	}
}
