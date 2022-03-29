package com.work.calendar.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.dto.Base64DTO;
import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.dto.BusinessFilterDTO;
import com.work.calendar.dto.BusinessListDTO;
import com.work.calendar.dto.BusinessSummaryDTO;
import com.work.calendar.dto.ClientBusinessSummaryDTO;
import com.work.calendar.dto.JobsDetail;
import com.work.calendar.entity.Business;
import com.work.calendar.entity.Client;
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
	@Value("#{${monthsList}}")
	private List<String> monthsList;
	@Value("${dateformat}")
	private String dateFormat;

	public BusinessDTO findBusinessDtoById(Long businessId) {
		Optional<Business> business = businessRepository.findById(businessId);
		if (business.isPresent()) {
			return businessDTOMapper.toBusinessDTO(business.get());
		}
		throw new Error("business not found");
	}

	public Business findBusinessById(Long businessId) {
		Optional<Business> business = businessRepository.findById(businessId);
		if (business.isPresent()) {
			return business.get();
		}
		throw new Error("business not found");
	}

	public BusinessDTO addEntity(BusinessDTO businessDTO) throws Exception {

		Business business = new Business();
		Client theClient = clientService.getClientById(businessDTO.getClientId());
		business.setClient(theClient);
		business.setJobtype(jobService.getJobById(businessDTO.getJobId()));
		business.setTotalHours(getTimedifference(businessDTO.getEndTime(), businessDTO.getStartTime()));
		business.setNote(businessDTO.getNote());
		business.setStartTime(businessDTO.getStartTime());
		business.setEndTime(businessDTO.getEndTime());
		business.setPosition(businessDTO.getPosition());
		business.setDate(businessDTO.getDate());
		Business createdBusiness = businessRepository.save(business);
		businessDTO.setClientFullName(theClient.getFullName());
		businessDTO.setClientFullName(businessDTO.getClientFullName());
		businessDTO.setJobDescription(businessDTO.getJobDescription());
		businessDTO.setBusinessId(createdBusiness.getId());
		return businessDTO;

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

	private boolean validateDto(BusinessDTO businessDTO) {
		if (businessRepository.findById(businessDTO.getBusinessId()).isPresent()
				&& clientRepository.findById(businessDTO.getClientId()).isPresent()
				&& jobRepository.findById(businessDTO.getJobId()).isPresent() && businessDTO.getStartTime() != null
				&& businessDTO.getEndTime() != null) {
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

	public Base64DTO getBusinessSummary(Long clientId, Long jobId, String startDate, String endDate, String date,
			String month) throws IOException, ParseException {
		BusinessFilterDTO clientJobFilterDTO = buildclientJobFilterDTO(clientId, jobId, startDate, endDate, date,
				month);
		List<Business> businesRecords = businessRepository.findAll(buildSpecificationByClientJob(clientJobFilterDTO));
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
			if (!CollectionUtils.isEmpty(businessSummaries)) {
				ExcelCreator excelCreator = new ExcelCreator(businessSummaryDTO.getClientBusinessSummaryDTO(),
						clientJobFilterDTO.getCalendar());
				return excelCreator.exportToBase64("summaryFile");
			}
			return null;// return businessSummaryDTO;

		}
		log.info("it's empty");
		return null;

	}

	public Specification<Business> buildSpecificationByClientJob(BusinessFilterDTO filter) {
		return Specification
				.where(new GenericSpecification<Business>(filter.getClientId(), "client.id", OperatorEnum.EQUAL)
						.and(new GenericSpecification<Business>(filter.getJobId(), "job.id", OperatorEnum.EQUAL))
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

	public BusinessDTO editBusiness(Long businesId, BusinessDTO businessDTO) throws AccountNotFoundException {
		if (validateDto(businessDTO)) {
			Business requestedBusiness = findBusinessById(businesId);
			requestedBusiness.setClient(clientService.getClientById(businessDTO.getClientId()));
			requestedBusiness.setJobtype(jobService.getJobById(businessDTO.getJobId()));
			requestedBusiness.setStartTime(businessDTO.getStartTime());
			requestedBusiness.setEndTime(businessDTO.getEndTime());
			requestedBusiness.setNote(businessDTO.getNote());
			requestedBusiness.setPosition(businessDTO.getPosition());
			Business editedBusiness = businessRepository.save(requestedBusiness);
			return businessDTOMapper.toBusinessDTO(editedBusiness);
		}
		throw new Error("invalid business");
	}

	private BusinessFilterDTO buildclientJobFilterDTO(Long clientId, Long jobId, String startingDate, String endingDate,
			String date, String month) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(2022, monthsList.indexOf(month), 1);
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			Date startDate = startingDate != null ? formatter.parse(startingDate.replace("T", " "))
					: createStartDate(calendar, month);
			Date endDate = endingDate != null ? formatter.parse(endingDate.replace("T", " "))
					: createEndDate(calendar, month);
			if (startDate != null && endDate != null && endDate.getTime() < startDate.getTime()) {
				throw new Error("dates are not valid");
			}
			BusinessFilterDTO clientJobFilterDTO = new BusinessFilterDTO(clientId, jobId, startDate, endDate, calendar);
			return clientJobFilterDTO;
		} catch (Exception e) {
			log.info("error " + e.getMessage());
			return null;
		}
	}
	
	private Date createStartDate(Calendar calendar, String month) {
		int monthindex = monthsList.indexOf(month);
		calendar.set(calendar.get(Calendar.YEAR), monthindex, 1);
		java.util.Date utilDate = calendar.getTime();
		return utilDate;
	}

	private Date createEndDate(Calendar calendar, String month) {
		int monthindex = monthsList.indexOf(month);
		calendar.set(calendar.get(Calendar.YEAR), monthindex, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		java.util.Date utilDate = calendar.getTime();
		return utilDate;
	}

}
