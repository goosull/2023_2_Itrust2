package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.InvoiceForm;
import edu.ncsu.csc.itrust2.models.Invoice;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.InvoiceStatus;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.InvoiceService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints that deal with invoices. 
 * Exposes functionality to add, edit, and fetch invoices.
 *
 * @author Woojun Ro(2018122053)
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIInvoiceController extends APIController {

	@Autowired
    private LoggerUtil     loggerUtil;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService    userService;

    /**
     * Adds a new invoice to the system. Requires HCP permissions.
     *
     * @param form
     *            details of the new invoice
     * @return the created invoice
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    @PostMapping ( BASE_PATH + "/invoices" )
    public ResponseEntity addPrescription ( @RequestBody final InvoiceForm form ) {
        try {
            final Invoice i = invoiceService.build( form );
            invoiceService.save( i );
            loggerUtil.log( TransactionType.INVOICE_CREATE, LoggerUtil.currentUser(), i.getPatient().getUsername(),
                    "Created invoice with id " + i.getId() );
            return new ResponseEntity( i, HttpStatus.CREATED );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.INVOICE_CREATE, LoggerUtil.currentUser(),
                    "Failed to create invoice" );
            return new ResponseEntity( errorResponse( "Could not save the invoice: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Pays an existing invoice in the system. Matches invoices by ids.
     * Requires patient permissions.
     *
     * @param id
     *            the id of the invoice to pay
     * @return the paid invoice
     */
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    @PostMapping ( BASE_PATH + "/prescriptions/{id}/pay" )
    public ResponseEntity payInvoice ( @PathVariable final Long id ) {
        try {
            final Invoice i = (Invoice) invoiceService.findById( id );
            if ( i == null ) {
            	loggerUtil.log( TransactionType.INVOICE_PAY, LoggerUtil.currentUser(),
            			"Could not find invoice with id " + id );
            	return new ResponseEntity( errorResponse( "No invoice found with id " + id ), HttpStatus.NOT_FOUND );
            }
            
            if ( i.getPatient().getUsername() != LoggerUtil.currentUser() ) {
            	loggerUtil.log( TransactionType.INVOICE_PAY, LoggerUtil.currentUser(),
            			"Could not pay invoice with id " + id );
            	return new ResponseEntity( errorResponse( "Paying invoices of other patients is forbidden" ), HttpStatus.FORBIDDEN );
            }
            
            if ( i.getStatus() != InvoiceStatus.PENDING ) {
            	loggerUtil.log( TransactionType.INVOICE_PAY, LoggerUtil.currentUser(),
            			"Could not pay invoice with id " + id );
            	return new ResponseEntity( errorResponse( "The invoice has been already paid" ), HttpStatus.BAD_REQUEST );
            }
            
            i.setStatus( InvoiceStatus.PAID );
            invoiceService.save( i );
            loggerUtil.log( TransactionType.INVOICE_PAY, LoggerUtil.currentUser(),
        			"Paid invoice with id " + id );
            return new ResponseEntity( i, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.PRESCRIPTION_EDIT, LoggerUtil.currentUser(),
                    "Failed to pay invoice" );
            return new ResponseEntity( errorResponse( "Failed to pay invoice: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns a collection of all the invoices
     * that are assigned to the user in the system.
     *
     * @return all saved invoices
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/invoices" )
    public List<Invoice> getPrescriptions () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        if ( self.isDoctor() ) {
        	// Return invoices assigned to the HCP
            loggerUtil.log( TransactionType.INVOICE_VIEW, LoggerUtil.currentUser(),
                    "HCP viewed a list of their invoices" );
            return invoiceService.findByHcp( self );
        }
        else {
            // Return invoices assigned to the patient
            loggerUtil.log( TransactionType.INVOICE_VIEW, LoggerUtil.currentUser(),
                    "Patient viewed a list of their invoices" );
            return invoiceService.findByPatient( self );
        }
    }

}
