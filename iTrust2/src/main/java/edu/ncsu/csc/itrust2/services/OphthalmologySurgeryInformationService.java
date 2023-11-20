package edu.ncsu.csc.itrust2.services;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
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
    
    public OphthalmologySurgeryInformation build ( final OfficeVisitForm ovf ) {
        final OphthalmologySurgeryInformation osi = new OphthalmologySurgeryInformation();

        osi.setAxisOD(ovf.getAxisOD());
        osi.setAxisOS(ovf.getAxisOS());
        osi.setCylinderOD(ovf.getCylinderOD());
        osi.setCylinderOS(ovf.getCylinderOS());

        final ZonedDateTime visitDate = ZonedDateTime.parse( ovf.getDate() );
        osi.setDate(visitDate);

        if ( ovf.getId() != null ) {
            osi.setId( Long.parseLong( ovf.getId() ) );
        }

        osi.setNotes(ovf.getNotes());
        osi.setSphereOD(ovf.getSphereOD());
        osi.setSphereOS(ovf.getSphereOS());
        osi.setType(ovf.getsurgeryType());

        osi.setVisit(service.build(ovf));
        osi.setVisualAcuityOD(ovf.getVisualAcuityOD());
        osi.setVisualAcuityOS(ovf.getVisualAcuityOS());
        osi.vaildate();

        return osi;
    }
}

