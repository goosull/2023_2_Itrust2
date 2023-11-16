package edu.ncsu.csc.itrust2.controllers.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.PatientForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.PatientService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Controller responsible for providing various REST API endpoints for the
 * Patient model.
 *
 * @author Kai Presler-Marshall
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIPatientController extends APIController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService    userService;

    @Autowired
    private LoggerUtil     loggerUtil;

    private String PatternOfId = "[A-Za-z0-9]+-_"; 

    private String PatternOfName = "[A-Za-z0-9]+-_\\s+";

    /**
     * Retrieves and returns a list of all Patients stored in the system
     *
     * @return list of patients
     */
    @GetMapping ( BASE_PATH + "/patients" )
    public List<Patient> getPatients () {
        final List<Patient> patients = (List<Patient>) patientService.findAll();
        return patients;
    }

    /**
     * If you are logged in as a patient, then you can use this convenience
     * lookup to find your own information without remembering your id. This
     * allows you the shorthand of not having to look up the id in between.
     *
     * @return The patient object for the currently authenticated user.
     */
    @GetMapping ( BASE_PATH + "/patient" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity getPatient () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        final Patient patient = (Patient) patientService.findByName( self.getUsername() );
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "Could not find a patient entry for you, " + self.getUsername() ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            loggerUtil.log( TransactionType.VIEW_DEMOGRAPHICS, self );
            return new ResponseEntity( patient, HttpStatus.OK );
        }
    }

    /**
     * Retrieves and returns the Patient with the username provided
     *
     * @param username
     *            The username of the Patient to be retrieved, as stored in the
     *            Users table
     * @return response
     */
    @GetMapping ( BASE_PATH + "/patients/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getPatient ( @PathVariable ( "username" ) final String username ) {
        final Patient patient = (Patient) patientService.findByName( username );
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            loggerUtil.log( TransactionType.PATIENT_DEMOGRAPHICS_VIEW, LoggerUtil.currentUser(), username,
                    "HCP retrieved demographics for patient with username " + username );
            return new ResponseEntity( patient, HttpStatus.OK );
        }
    }

    /**
     * Updates the Patient with the id provided by overwriting it with the new
     * Patient record that is provided. If the ID provided does not match the ID
     * set in the Patient provided, the update will not take place
     *
     * @param id
     *            The username of the Patient to be updated
     * @param patientF
     *            The updated Patient to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/patients/{id}" )
    public ResponseEntity updatePatient ( @PathVariable final String id, @RequestBody final PatientForm patientF ) {
        // check that the user is an HCP or a patient with username equal to id
        boolean userEdit = false; // true if user edits his or her own
                                  // demographics, false if hcp edits them
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final SimpleGrantedAuthority hcp = new SimpleGrantedAuthority( "ROLE_HCP" );
        try {
            userEdit = !auth.getAuthorities().contains( hcp );

            if ( !auth.getName().equals( id ) && userEdit ) {
                return new ResponseEntity( errorResponse( "You do not have permission to edit this record" ),
                        HttpStatus.UNAUTHORIZED );
            }

        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }

        try {
            final Patient patient = (Patient) patientService.findByName( id );

            // Shouldn't be possible but let's check anyways
            if ( null == patient ) {
                return new ResponseEntity( errorResponse( "Patient not found" ), HttpStatus.NOT_FOUND );
            }

            patient.update( patientF );

            final User dbPatient = patientService.findByName( id );
            if ( null == dbPatient ) {
                return new ResponseEntity( errorResponse( "No Patient found for id " + id ), HttpStatus.NOT_FOUND );
            }
            patientService.save( patient );

            // Log based on whether user or hcp edited demographics
            if ( userEdit ) {
                loggerUtil.log( TransactionType.EDIT_DEMOGRAPHICS, LoggerUtil.currentUser(),
                        "User with username " + patient.getUsername() + "updated their demographics" );
            }
            else {
                loggerUtil.log( TransactionType.PATIENT_DEMOGRAPHICS_EDIT, LoggerUtil.currentUser(),
                        patient.getUsername(),
                        "HCP edited demographics for patient with username " + patient.getUsername() );
            }
            return new ResponseEntity( patient, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + patientF.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Get the patient's zip code to autofill Find Expert Form
     *
     * @return Response entity with status and response data (zip or error
     *         message)
     *
     */
    @GetMapping ( BASE_PATH + "/patient/findexperts/getzip" )
    @PreAuthorize ( "hasRole( 'ROLE_PATIENT')" )
    public ResponseEntity getPatientZip () {
        final String user = LoggerUtil.currentUser();
        if ( user == null ) {
            return new ResponseEntity( errorResponse( "Patient not found" ), HttpStatus.NOT_FOUND );
        }
        final String zip = ( (Patient) patientService.findByName( user ) ).getZip();
        if ( zip == null ) {
            return new ResponseEntity( errorResponse( "Patient does not have zip stored" ), HttpStatus.NO_CONTENT );
        }
        else {
            final String[] zipParts = zip.split( "-" );
            return new ResponseEntity( zipParts, HttpStatus.OK );

        }

    }

    /**
     * The approved user enters the desired patients First and/or Last Name (or a correct substring of either/both) into the search box.  
     * The patients with first/last names containing the substring(s) provided are shown.
     * 
     * @param Nameid User's input 
     * 
     * @return list of searched patients
     */
    @GetMapping ( BASE_PATH + "/patient/{Name}" )
    @PreAuthorize ( "hasAnyRole( 'ROLE_HCP', 'ROLE_ER' )")
    public ResponseEntity getPatientsbyName ( @PathVariable("Name") String Name){
        if ( (Name == null) || (Pattern.matches(PatternOfName, Name) == false)) {
            return new ResponseEntity(errorResponse("You should enter the valid Patient's name"), HttpStatus.BAD_REQUEST);
        }
        else {
            List<Patient> patients = (List<Patient>) patientService.findAll();
            List<Patient> searchedPatients = new ArrayList<>();

            for (int i = 0; i < patients.size(); i++){
                    if((patients.get(i).getFirstName()+patients.get(i).getLastName()).contains(Name)){
                        searchedPatients.add(patients.get(i));
                    }
                }

            if ( searchedPatients.size() == 0 ) {
                return new ResponseEntity( errorResponse( "No Patient found for Name " + Name ),
                    HttpStatus.NOT_FOUND );
            }
            else {
                return new ResponseEntity( searchedPatients, HttpStatus.OK );
            }
        }
    } 

    /**
     * The approved user enters the desired patients MID (username), or a correct substring of it, in the search box.  
     * The patient/s matching the MID/MID substring are displayed.
     * 
     * @param Nameid User's input 
     * 
     * @return list of searched patients
     */
    @GetMapping ( BASE_PATH + "/patient/{Id}" )
    @PreAuthorize ( "hasAnyRole( 'ROLE_HCP', 'ROLE_ER' )")
    public ResponseEntity getPatientsbyId ( @PathVariable("Id") String Id){
        if ( (Id == null) || (Id.length() < 6) || (Id.length() > 20) || (Pattern.matches(PatternOfId, Id) == false)) {
            return new ResponseEntity(errorResponse("You should enter the valid Patient's Username"), HttpStatus.BAD_REQUEST);
        }
        else {
            List<Patient> patients = (List<Patient>) patientService.findAll();
            List<Patient> searchedPatients = new ArrayList<>();

            for (int i = 0; i < patients.size(); i++){
                    if(patients.get(i).getUsername().contains(Id)){
                        searchedPatients.add(patients.get(i));
                    }
                }

            if ( searchedPatients.size() == 0 ) {
                return new ResponseEntity( errorResponse( "No Patient found for Username " + Id ),
                    HttpStatus.NOT_FOUND );
            }
            else {
                return new ResponseEntity( searchedPatients, HttpStatus.OK );
            }
        }
    } 
}
