package edu.ncsu.csc.itrust2.controllers.api;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.OphthalmologySurgeryInformationService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;


@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIOphthalmologySurgeryController extends APIController {

    @Autowired
    private LoggerUtil         loggerUtil;

    @Autowired
    private OphthalmologySurgeryInformationService  ophthalmologySurgeryInformationService;

    @Autowired
    private UserService        userService;

    @PutMapping ( BASE_PATH + "/ophtsurgery/{id}")
    @PreAuthorize ( "hasRole('ROLE_OPH')" )
    public ResponseEntity updateOphtSurgery ( @PathVariable final String id, 
            @RequestBody final OphthalmologySurgeryForm surgeryForm ) {
        try {
            final Optional<OphthalmologySurgeryInformation> opsurgery = ophthalmologySurgeryInformationService.findById( Long.parseLong(id) );
            if( opsurgery.isEmpty() ) {
                return new ResponseEntity(
                    errorResponse( "Surgery with the id " + id + " doesn't exist" ),
                    HttpStatus.NOT_FOUND );
            }
            
            final OphthalmologySurgeryInformation surgery = opsurgery.get();

            surgery.setPatient( userService.findByName( surgeryForm.getPatient() ) );
            surgery.setHcp( userService.findByName( surgeryForm.getHcp() ) );

            surgery.setAxisOD(surgeryForm.getAxisOD());
            surgery.setAxisOS(surgeryForm.getAxisOS());
            surgery.setCylinderOD(surgeryForm.getCylinderOD());
            surgery.setCylinderOS(surgeryForm.getCylinderOS());

            final ZonedDateTime visitDate = ZonedDateTime.parse( surgeryForm.getDate() );
            surgery.setDate(visitDate);
            
            surgery.setNotes(surgeryForm.getNotes());
            surgery.setSphereOD(surgeryForm.getSphereOD());
            surgery.setSphereOS(surgeryForm.getSphereOS());

            OphthalmologySurgeryType type = OphthalmologySurgeryType.valueOf( surgeryForm.getType() );
            surgery.setType( type );

            surgery.setVisualAcuityOD(surgeryForm.getVisualAcuityOD());
            surgery.setVisualAcuityOS(surgeryForm.getVisualAcuityOS());
            surgery.validate();

            ophthalmologySurgeryInformationService.save(surgery);
            loggerUtil.log( TransactionType.SURGERY_OPHTHALMOLOGIST_EDIT, LoggerUtil.currentUser(),
                    surgery.getPatient().getUsername());
            return new ResponseEntity( surgery, HttpStatus.OK );
        } catch ( Exception e) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Surgery provided due to " + e.getMessage() + ophthalmologySurgeryInformationService),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
