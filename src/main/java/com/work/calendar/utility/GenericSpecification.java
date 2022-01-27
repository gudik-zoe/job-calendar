package com.work.calendar.utility;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification<T> implements Specification<T> {

	private static final long serialVersionUID = 1L;

	protected Object value;
	protected String pathField;
	protected OperatorEnum operator;

	public GenericSpecification(Object value, String pathField, OperatorEnum operator) {
		this.value = value;
		this.pathField = pathField;
		this.operator = operator;
	}

	@SuppressWarnings("unchecked")
	protected <F> Path<F> getPath(Root<T> root) {
		Path<?> expr = null;
		if (pathField.contains(".")) {
			for (String field : pathField.split("\\.")) {
				if (expr == null) {
					expr = root.get(field);
				} else {
					expr = expr.get(field);
				}
			}
		} else {
			expr = root.get(pathField);
		}
		return (Path<F>) expr;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (value == null) {
			return cb.isTrue(cb.literal(true)); // always true = no filtering
		}

		if (operator == OperatorEnum.LIKE) {
			return cb.like(getPath(root), "%" + this.value.toString() + "%");
		} else if (operator == OperatorEnum.DATE_AFTER) {
			Date dateTockeck = (Date) value;
			return cb.greaterThanOrEqualTo(getPath(root).as(Date.class), dateTockeck);
		} else if (operator == OperatorEnum.DATE_BEFORE) {
			Date dateTockeck = (Date) value;
			return cb.lessThanOrEqualTo(getPath(root).as(Date.class), dateTockeck);
		} else if (operator == OperatorEnum.IS_NULL) {
			return cb.isNull(getPath(root));
		} else if (operator == OperatorEnum.IS_NOT_NULL) {
			return cb.isNotNull(getPath(root));
		} else if (operator == OperatorEnum.EQUAL_OBJECT) {
			return cb.equal(getPath(root), this.value);
		} else {
			return cb.equal(getPath(root), this.value.toString());

		}

	}

}
