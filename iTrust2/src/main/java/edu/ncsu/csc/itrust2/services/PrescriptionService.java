package edu.ncsu.csc.itrust2.services;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.PrescriptionRepository;

@Component
@Transactional
public class PrescriptionService extends Service {

    @Autowired
    private PrescriptionRepository repository;

    @Autowired
    private DrugService            drugService;

    @Autowired
    private UserService            userService;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public Prescription build ( final PrescriptionForm form ) {
        final Prescription pr = new Prescription();

        pr.setDrug( drugService.findByCode( form.getDrug() ) );
        pr.setDosage( form.getDosage() );
        pr.setRenewals( form.getRenewals() );
        pr.setPatient( userService.findByName( form.getPatient() ) );

        if ( form.getId() != null ) {
            pr.setId( form.getId() );
        }

        pr.setStartDate( LocalDate.parse( form.getStartDate() ) );
        pr.setEndDate( LocalDate.parse( form.getEndDate() ) );

        return pr;
    }

    public List<Prescription> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }

    //add
    public List<Prescription> findByPatientForEHR ( final User patient ) {
    	LocalDate startDate = LocalDate.now().minusDays(90);
    	
    	List<Prescription> prescriptions = repository.findByPatient(patient);
    	
        List<Prescription> prescriptionsLast90Days = prescriptions.stream()
                .filter(prescription -> prescription.getStartDate().isAfter(startDate))
                .collect(Collectors.toList());

        prescriptionsLast90Days.sort((p1, p2) -> p2.getStartDate().compareTo(p1.getStartDate()));

        return prescriptionsLast90Days;
    }

}
