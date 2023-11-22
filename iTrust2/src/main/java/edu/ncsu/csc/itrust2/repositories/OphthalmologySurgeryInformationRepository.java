package edu.ncsu.csc.itrust2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.User;

public interface OphthalmologySurgeryInformationRepository extends JpaRepository<OphthalmologySurgeryInformation, Long>{
    
    public List<OphthalmologySurgeryInformation> findByPatient ( User patient );

    public List<OphthalmologySurgeryInformation> findByHcp ( User hcp );

    public List<OphthalmologySurgeryInformation> findByHcpAndPatient ( User hcp, User patient );

    public Optional<OphthalmologySurgeryInformation> findById( Long id );
}
