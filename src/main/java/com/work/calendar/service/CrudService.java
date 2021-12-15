package com.work.calendar.service;

import java.util.Optional;

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

}
