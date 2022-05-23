package com.work.calendar.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.entity.Business;

@Mapper(componentModel = "spring")
public interface EntityMapper {

	@Mappings({ @Mapping(target = "businessId", source = "id"), @Mapping(target = "clientId", source = "client.id"),
			@Mapping(target = "jobId", source = "job.id"),
			@Mapping(target = "jobDescription", source = "job.description"),
			@Mapping(target = "clientFullName", source = "client.fullName") })
	BusinessDTO toBusinessDTO(Business business);
	
	
	
	
	
	
}
