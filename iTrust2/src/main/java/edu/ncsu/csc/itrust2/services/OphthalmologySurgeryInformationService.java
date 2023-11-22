package edu.ncsu.csc.itrust2.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryInformationRepository;

@Component
@Transactional
public class OphthalmologySurgeryInformationService extends Service {

    @Autowired
    private OphthalmologySurgeryInformationRepository   repository;

    @Autowired
    private UserService                                 userService;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public List<OphthalmologySurgeryInformation> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }

    public List<OphthalmologySurgeryInformation> findByHcp ( final User hcp ) {
        return repository.findByHcp( hcp );
    }
    
    public List<OphthalmologySurgeryInformation> findByHcpAndPatient ( final User hcp, final User patient ) {
        return repository.findByHcpAndPatient( hcp, patient );
    }

    public Optional<OphthalmologySurgeryInformation> findById ( final Long id ) {
        return repository.findById( id );
    }
    
    public OphthalmologySurgeryInformation build ( final OfficeVisitForm ovf ) {
        final OphthalmologySurgeryInformation osi = new OphthalmologySurgeryInformation();
        
        osi.setPatient( userService.findByName( ovf.getPatient() ) );
        osi.setHcp( userService.findByName( ovf.getHcp() ) );

        osi.setAxisOD(ovf.getAxisOD());
        osi.setAxisOS(ovf.getAxisOS());
        osi.setCylinderOD(ovf.getCylinderOD());
        osi.setCylinderOS(ovf.getCylinderOS());

        final ZonedDateTime visitDate = ZonedDateTime.parse( ovf.getSurgeryEntry() );
        osi.setDate(visitDate);
        
        osi.setNotes(ovf.getSurgeryNotes());
        osi.setSphereOD(ovf.getSphereOD());
        osi.setSphereOS(ovf.getSphereOS());

        OphthalmologySurgeryType type = null;
        try {
            type = OphthalmologySurgeryType.valueOf( ovf.getSurgeryType() );
        }
        catch ( final NullPointerException npe ) {
            type = OphthalmologySurgeryType.CATARACT;
        }
        osi.setType( type );

        osi.setVisualAcuityOD(ovf.getVisualAcuityOD());
        osi.setVisualAcuityOS(ovf.getVisualAcuityOS());
        osi.validate();

        return osi;
    }
}

