package com.work.calendar.dto;

import java.util.ArrayList;
import java.util.List;

public class BusinessListDTO {

	private int total;

	private List<BusinessDTO> resultList = new ArrayList<>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<BusinessDTO> getResultList() {
		return resultList;
	}

	public void setResultList(List<BusinessDTO> resultList) {
		this.resultList = resultList;
	}

}
