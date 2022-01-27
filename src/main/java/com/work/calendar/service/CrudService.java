package com.work.calendar.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class CrudService<T> {
	private Logger log = LoggerFactory.getLogger(CrudService.class);

	public abstract JpaRepository<T, Long> getRepository();

	public abstract boolean validateEntity(T entity);

	public T getEntityById(Long id) {
		log.info(" this is the id " + id);
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
