package edu.ncsu.csc.itrust2.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryInformationRepository;

@Component
@Transactional

public class OphthalmologySurgeryInformationService extends Service {

    @Autowired
    private OphthalmologySurgeryInformationRepository repository;

    @Autowired
    private OfficeVisitService  service;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public List<OphthalmologySurgeryInformation> findByPatient ( final User patient ) {
        return service.findByPatient( patient ).stream().map( e -> findByVisit( e ) ).flatMap( e -> e.stream() )
                .collect( Collectors.toList() );
    }

    public List<OphthalmologySurgeryInformation> findByHcp ( final User HCP ) {
        return service.findByHcp( HCP ).stream().map( e -> findByVisit( e ) ).flatMap( e -> e.stream() )
                .collect( Collectors.toList() );
    }

    public List<OphthalmologySurgeryInformation> findByVisit ( final OfficeVisit visit ) {
        return repository.findByVisit( visit );
    }

}

