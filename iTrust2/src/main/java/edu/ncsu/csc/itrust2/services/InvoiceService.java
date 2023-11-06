package edu.ncsu.csc.itrust2.services;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.forms.InvoiceForm;
import edu.ncsu.csc.itrust2.models.Invoice;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.InvoiceRepository;

@Component
@Transactional
public class InvoiceService extends Service {

	@Autowired
    private InvoiceRepository repository;
	
	@Autowired
    private UserService userService;
	
	@Override
    protected JpaRepository getRepository () {
        return repository;
    }
	
    public List<Invoice> findByHcp ( final User hcp ) {
        return repository.findByHcp( hcp );
    }
	
	public List<Invoice> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }
	
	public Invoice build ( final InvoiceForm form ) {
        final Invoice iv = new Invoice();

        iv.setId( form.getId() );
        iv.setHcp( userService.findByName( form.getHcp() ) );
        iv.setPatient( userService.findByName( form.getPatient() ) );
        iv.setStartDate( LocalDate.parse( form.getStartDate() ) );
        iv.setEndDate( LocalDate.parse( form.getEndDate() ) );
        iv.setCost( form.getCost() );
        iv.setStatus( form.getStatus() );

        return iv;
    }
}
