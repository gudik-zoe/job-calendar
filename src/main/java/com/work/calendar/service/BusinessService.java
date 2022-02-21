package com.work.calendar.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.dto.Base64DTO;
import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.dto.BusinessDetailsDTO;
import com.work.calendar.dto.BusinessListDTO;
import com.work.calendar.dto.BusinessSummaryDTO;
import com.work.calendar.dto.ClientBusinessSummaryDTO;
import com.work.calendar.dto.ClientJobFilterDTO;
import com.work.calendar.dto.JobsDetail;
import com.work.calendar.entity.Business;
import com.work.calendar.entity.Client;
import com.work.calendar.entity.Job;
import com.work.calendar.mappers.BusinessDTOMapper;
import com.work.calendar.repository.BusinessRepository;
import com.work.calendar.repository.ClientRepository;
import com.work.calendar.repository.JobRepository;
import com.work.calendar.utility.GenericSpecification;
import com.work.calendar.utility.OperatorEnum;

@Service
public class BusinessService extends CrudService<Business> {

	private Logger log = LoggerFactory.getLogger(BusinessService.class);

	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private JobService jobService;
	@Autowired
	BusinessDTOMapper businessDTOMapper;

	public BusinessDTO findBusinessById(Long businessId) {
		Optional<Business> business = businessRepository.findById(businessId);
		if (business.isPresent()) {
			log.info("here");
			return businessDTOMapper.toBusinessDTO(business.get());
		}
		log.info("not found");
		return null;
	}

	public BusinessDTO addEntity(BusinessDTO clientJobDTO) throws Exception {

		Business cj = new Business();
//		log.info("time difference " +((clientJobDTO.getEndTime().getTime() - clientJobDTO.getStartTime().getTime()) / (1000 * 60 * 60))
//				+ "hours");
		cj.setClient(clientService.getClientById(clientJobDTO.getClientId()));
		cj.setJobtype(jobService.getJobById(clientJobDTO.getJobId()));
		cj.setTotalHours(getTimedifference(clientJobDTO.getEndTime(), clientJobDTO.getStartTime()));
		cj.setNote(clientJobDTO.getNote());
		cj.setStartTime(clientJobDTO.getStartTime());
		cj.setEndTime(clientJobDTO.getEndTime());
		cj.setPosition(clientJobDTO.getPosition());
		cj.setDate(clientJobDTO.getDate());
		clientJobDTO.setClientFullName(clientJobDTO.getClientFullName());
		clientJobDTO.setJobDescription(clientJobDTO.getJobDescription());
		businessRepository.save(cj);
		return clientJobDTO;

	}

	private double getTimedifference(Date endTime, Date startTime) throws Exception {
		double difference;
		if (endTime.getTime() > startTime.getTime()) {
			difference = Math.floor(endTime.getTime() - startTime.getTime()) / (1000 * 3600);
			log.info("difference " + difference);
			return difference;
		}
		throw new Exception("dates are not valid");
	}

	private boolean validateDto(BusinessDTO clientJobDTO) {

		if (getEntityById(clientJobDTO.getClientId()) != null && getEntityById(clientJobDTO.getJobId()) != null
				&& clientJobDTO.getStartTime() != null && clientJobDTO.getEndTime() != null) {
			return true;
		}

		return false;
	}

	@Override
	public JpaRepository<Business, Long> getRepository() {
		return businessRepository;
	}

	@Override
	public boolean validateEntity(Business entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public Base64DTO getBusinessSummary(ClientJobFilterDTO filter) throws IOException {
		List<Business> businesRecords = businessRepository.findAll(buildSpecificationByClientJob(filter));
		List<Client> relatedClients = new ArrayList<>();
		BusinessSummaryDTO businessSummaryDTO = new BusinessSummaryDTO();
		List<ClientBusinessSummaryDTO> businessSummaries = new ArrayList<>();

		if (!CollectionUtils.isEmpty(businesRecords)) {
			for (Business businesRecord : businesRecords) {
				if (!relatedClients.contains(businesRecord.getClient())) {
					relatedClients.add(businesRecord.getClient());
				}
			}
			for (Client client : relatedClients) {
				businessSummaries
						.add(new ClientBusinessSummaryDTO(client.getId(), client.getFullName(), new HashMap<>(), 0));
			}

			for (Business businesRecord : businesRecords) {
				for (ClientBusinessSummaryDTO businessSummary : businessSummaries) {
					if (businesRecord.getClient().getId() == businessSummary.getClientId()) {
						businessSummary.setTotalHoursForClient(
								businessSummary.getTotalHoursForClient() + businesRecord.getTotalHours());
						if (!businessSummary.getJobs().containsKey(businesRecord.getJob().getDescription())) {
							List<JobsDetail> jobsDetails = new ArrayList<>();
							jobsDetails.add(new JobsDetail(businesRecord.getTotalHours(), businesRecord.getDate()));
							businessSummary.getJobs().put(businesRecord.getJob().getDescription(), jobsDetails);
						} else {
							businessSummary.getJobs().get(businesRecord.getJob().getDescription())
									.add(new JobsDetail(businesRecord.getTotalHours(), businesRecord.getDate()));
						}
					}
				}
			}

			for (ClientBusinessSummaryDTO businessSummary : businessSummaries) {
				businessSummaryDTO
						.setTotalHours(businessSummaryDTO.getTotalHours() + businessSummary.getTotalHoursForClient());
			}
			businessSummaryDTO.setClientBusinessSummaryDTO(businessSummaries);
			Calendar c = Calendar.getInstance();
			c.set(2022, Calendar.JANUARY, 1);
			int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			log.info("dayyyyys" + maxDay);
			if (!CollectionUtils.isEmpty(businessSummaries)) {
			ExcelCreator excelCreator = new ExcelCreator(businessSummaryDTO.getClientBusinessSummaryDTO() , maxDay);
			return excelCreator.exportToBase64("summaryFile");
//				return businessSummaryDTO;
			} else {
				log.info("its null");
			}
			return null;// return businessSummaryDTO;

		}
		return null;

	}

	public Specification<Business> buildSpecificationByClientJob(ClientJobFilterDTO filter) {
		return Specification
				.where(new GenericSpecification<Business>(filter.getClientId(), "client.id", OperatorEnum.EQUAL)
						.and(new GenericSpecification<Business>(filter.getStartDate(), "date", OperatorEnum.DATE_AFTER))
						.and(new GenericSpecification<Business>(filter.getEndDate(), "date",
								OperatorEnum.DATE_BEFORE)));

	}

	public BusinessListDTO getBusinessOnDate(Date date) {
		BusinessListDTO resultList = new BusinessListDTO();
		List<Business> businessResult = businessRepository.getBusinessOnDate(date);
		if (!CollectionUtils.isEmpty(businessResult)) {
			for (Business business : businessResult) {
				resultList.getResultList()
						.add(new BusinessDTO(business.getId(), business.getNote(), business.getClient().getId(),
								business.getJob().getId(), business.getClient().getFullName(),
								business.getJob().getDescription(), business.getPosition(), business.getStartTime(),
								business.getEndTime(), business.getDate()));
				resultList.setTotal(resultList.getTotal() + 1);
			}
			return resultList;

		}

		return resultList;
	}

}
