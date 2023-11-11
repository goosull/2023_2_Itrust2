package edu.ncsu.csc.itrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.forms.EHRForm;
import edu.ncsu.csc.itrust2.models.EHR;

public interface EHRRepository extends JpaRepository<EHR, Long>{
	
	public List<EHR> findByFirstName ( final EHRForm EHRForm );
	
	public List<EHR> findByLastName ( final EHRForm EHRForm );
	
	public List<EHR> findByid (final EHRForm EHRForm);
	
	public EHR findByid( final Long id );
	
}