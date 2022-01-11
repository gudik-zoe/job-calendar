package com.work.calendar.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class CrudService<T> {

	public abstract JpaRepository<T, Long> getRepository();

	public abstract boolean validateEntity(T entity);

	public T getEntityById(Long id) {
		T theEntity = getRepository().findById(id).get();
		return theEntity;
	}

	public T addEntity(T entity) {
		if (validateEntity(entity)) {
			getRepository().save(entity);
			return entity;
		} else {
			return null;
		}

	}

	public List<T> getEntities() {
		return getRepository().findAll();

	}

}
