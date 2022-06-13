package com.work.calendar.mappers;

import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.entity.Business;
import com.work.calendar.entity.Client;
import com.work.calendar.entity.Job;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-10T21:48:39+0200",
    comments = "version: 1.3.0.Final, compiler: Eclipse JDT (IDE) 1.3.1200.v20200916-0645, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public BusinessDTO toBusinessDTO(Business business) {
        if ( business == null ) {
            return null;
        }

        BusinessDTO businessDTO = new BusinessDTO();

        businessDTO.setBusinessId( business.getId() );
        businessDTO.setJobId( businessJobId( business ) );
        businessDTO.setJobDescription( businessJobDescription( business ) );
        businessDTO.setClientId( businessClientId( business ) );
        businessDTO.setClientFullName( businessClientFullName( business ) );
        businessDTO.setNote( business.getNote() );
        businessDTO.setPosition( business.getPosition() );
        businessDTO.setDate( business.getDate() );
        businessDTO.setStartTime( business.getStartTime() );
        businessDTO.setEndTime( business.getEndTime() );
        businessDTO.setUserId( business.getUserId() );

        return businessDTO;
    }

    private Long businessJobId(Business business) {
        if ( business == null ) {
            return null;
        }
        Job job = business.getJob();
        if ( job == null ) {
            return null;
        }
        Long id = job.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String businessJobDescription(Business business) {
        if ( business == null ) {
            return null;
        }
        Job job = business.getJob();
        if ( job == null ) {
            return null;
        }
        String description = job.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private Long businessClientId(Business business) {
        if ( business == null ) {
            return null;
        }
        Client client = business.getClient();
        if ( client == null ) {
            return null;
        }
        Long id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String businessClientFullName(Business business) {
        if ( business == null ) {
            return null;
        }
        Client client = business.getClient();
        if ( client == null ) {
            return null;
        }
        String fullName = client.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }
}
