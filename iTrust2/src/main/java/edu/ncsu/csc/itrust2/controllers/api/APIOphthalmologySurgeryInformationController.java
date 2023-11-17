package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import edu.ncsu.csc.itrust2.services.OphthalmologySurgeryInformationService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provided the REST endpoints for dealing with diagnoses. Diagnoses
 * can either be retrieved individually by ID, or in lists by office visit or by
 * patient.
 *
 * @author Thomas
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIOphthalmologySurgeryInformationController extends APIController {

    @Autowired
    private LoggerUtil         loggerUtil;

    @Autowired
    private OphthalmologySurgeryInformationService   ophthalmologySurgeryInformationService;

    @Autowired
    private OfficeVisitService officeVisitService;

    @Autowired
    private UserService        userService;

    @GetMapping ( BASE_PATH + "/past" )
    public List<OphthalmologySurgeryInformation> getSurgeryInformations () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        if ( self == null ) {
            return null;
        }
        loggerUtil.log( TransactionType.DIAGNOSIS_PATIENT_VIEW_ALL, self.getUsername(),
                self.getUsername() + " viewed their past office visit" );

        return ophthalmologySurgeryInformationService.findByPatient( self );
    }

}
