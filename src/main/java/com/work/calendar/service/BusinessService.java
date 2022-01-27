package com.work.calendar.service;

import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.dto.ClientJobFilterDTO;
import com.work.calendar.dto.BusinessSummaryDTO;
import com.work.calendar.dto.BusinessDetailsDTO;
import com.work.calendar.entity.Business;
import com.work.calendar.repository.BusinessRepository;
import com.work.calendar.repository.ClientRepository;
import com.work.calendar.repository.JobRepository;
import com.work.calendar.utility.GenericSpecification;
import com.work.calendar.utility.OperatorEnum;

@Service
public class BusinessService extends CrudService<Business> {

	private Logger log = LoggerFactory.getLogger(BusinessService.class);

	@Autowired
	private BusinessRepository clientJobRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private JobService jobService;

	public Business addEntity(BusinessDTO clientJobDTO) throws AccountNotFoundException {

		Business cj = new Business();
		cj.setClient(clientService.getClientById(clientJobDTO.getClientId()));
		cj.setJobtype(jobService.getJobById(clientJobDTO.getJobId()));
		cj.setHoursNumber(clientJobDTO.getTimeSpentInHrs());
		cj.setNote(clientJobDTO.getNote());
		cj.setPosition(clientJobDTO.getPosition());
		cj.setDate(clientJobDTO.getDate());
		return clientJobRepository.save(cj);

	}

	private boolean validateDto(BusinessDTO clientJobDTO) {

		if (getEntityById(clientJobDTO.getClientId()) != null && getEntityById(clientJobDTO.getJobId()) != null
				&& clientJobDTO.getTimeSpentInHrs() != 0) {
			return true;
		}

		return false;
	}

	@Override
	public JpaRepository<Business, Long> getRepository() {
		return clientJobRepository;
	}

	@Override
	public boolean validateEntity(Business entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public BusinessSummaryDTO getClientJobSummaryForClientId(Long id) {
		List<Business> summary = clientJobRepository.getClientJobSummaryForClientId(id);
		if (!CollectionUtils.isEmpty(summary)) {
			BusinessSummaryDTO cjs = new BusinessSummaryDTO();
			cjs.setClientName(summary.get(0).getClient().getFullName());
			for (Business clientJob : summary) {
				cjs.setTotalHrs(cjs.getTotalHrs() + clientJob.getHoursNumber());
				cjs.getBusinessDetails().add(new BusinessDetailsDTO(clientJob.getJobtype().getDescription(),
						clientJob.getHoursNumber(), clientJob.getDate()));
			}
			return cjs;
		}
		return null;

	}

	public BusinessSummaryDTO getClientSummaryOnSpecificPeriod(ClientJobFilterDTO filter) {
		List<Business> summary = clientJobRepository.findAll(buildSpecificationByClientJob(filter));
		BusinessSummaryDTO cjs = new BusinessSummaryDTO();
		log.info("summary size " + summary.size());
		if (!CollectionUtils.isEmpty(summary)) {
			cjs.setClientName(summary.get(0).getClient().getFullName());
			for (Business clientJob : summary) {
				cjs.setTotalHrs(cjs.getTotalHrs() + clientJob.getHoursNumber());
				cjs.getBusinessDetails().add(new BusinessDetailsDTO(clientJob.getJobtype().getDescription(),
						clientJob.getHoursNumber(), clientJob.getDate()));
			}
			return cjs;
		}
		return null;
	}

	public Specification<Business> buildSpecificationByClientJob(ClientJobFilterDTO filter) {
		log.info("filter data " + "start date " + filter.getStartDate() + " end date " + filter.getEndDate()
				+ "client id " + filter.getClientId());
		return Specification
				.where(new GenericSpecification<Business>(filter.getClientId(), "client.id", OperatorEnum.EQUAL)
						.and(new GenericSpecification<Business>(filter.getStartDate(), "date",
								OperatorEnum.DATE_AFTER))
						.and(new GenericSpecification<Business>(filter.getEndDate(), "date",
								OperatorEnum.DATE_BEFORE)));

	}

}
