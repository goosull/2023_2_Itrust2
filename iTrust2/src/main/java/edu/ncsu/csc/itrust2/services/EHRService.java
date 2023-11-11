package edu.ncsu.csc.itrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.forms.EHRForm;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.EHR;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.EHRRepository;
import edu.ncsu.csc.itrust2.repositories.PrescriptionRepository;

import java.util.List;

@Component
@Transactional
public class EHRService extends Service {
	@Autowired
	private EHRRepository repository;
	
	@Override
	protected JpaRepository getRepository () {
        return repository;
    }
	
	@Autowired
    private UserService userService;
	
	@Autowired
	public EHRService(EHRRepository EHRRepository) {
		this.repository = EHRRepository;
	}
	
	public List<EHR> findByFirstName ( final EHRForm EHRForm ) {
        return repository.findByFirstName( EHRForm );
    }
	
	public List<EHR> findByLastName ( final EHRForm EHRForm ) {
        return repository.findByLastName( EHRForm );
    }
	
	public List<EHR> findByid ( final EHRForm EHRForm ) {
        return repository.findByid( EHRForm );
    }
	
	//
	public EHR getPatient(Long id) {
		return repository.findByid(id);
	}
	
    private DiagnosisRepository diagnosisRepository;
    private PrescriptionRepository prescriptionRepository;
	
    public EHRService(
            DiagnosisRepository diagnosisRepository,
            PrescriptionRepository prescriptionRepository
    ) {
        this.diagnosisRepository = diagnosisRepository;
        this.prescriptionRepository = prescriptionRepository;
    }


    public List<Diagnosis> getPatientDiagnosis( final Long id ) {
        // Implement logic to get diagnoses for the patient
        return diagnosisRepository.findByidOrderByVisitDesc(id);
    }

    public List<Prescription> getPatientPrescriptions(Long id) {
        // Implement logic to get prescriptions for the patient
        return prescriptionRepository.findByidOrderByStartDateDesc(id);
    }
}
