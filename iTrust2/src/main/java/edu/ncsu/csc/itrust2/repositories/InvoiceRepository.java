package edu.ncsu.csc.itrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.models.Invoice;
import edu.ncsu.csc.itrust2.models.User;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	
	public List<Invoice> findByHcp ( final User hcp );
	
	public List<Invoice> findByPatient ( final User patient );
	
}
