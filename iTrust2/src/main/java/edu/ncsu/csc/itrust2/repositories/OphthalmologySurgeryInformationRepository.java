package edu.ncsu.csc.itrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;

public interface OphthalmologySurgeryInformationRepository extends JpaRepository<OphthalmologySurgeryInformation, Long>{
    
    public List<OphthalmologySurgeryInformation> findByVisit ( OfficeVisit visit );

}
