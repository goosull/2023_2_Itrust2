package edu.ncsu.csc.itrust2.forms;

import java.io.Serializable;

import edu.ncsu.csc.itrust2.models.Invoice;
import edu.ncsu.csc.itrust2.models.enums.InvoiceStatus;

public class InvoiceForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private String hcp;

    private String patient;

    private String dueDate;

    private int cost;

    private InvoiceStatus status;

    /**
     * Empty constructor for filling in fields without a Invoice object.
     */
    public InvoiceForm() {
    }
    
    /**
     * Constructs a new form with information from the given Invoice.
     *
     * @param invoice
     *            the invoice object
     */
    public InvoiceForm( final Invoice invoice ) {
    	setId( invoice.getId() );
    	setHcp( invoice.getHcp().getUsername() );
    	setPatient( invoice.getPatient().getUsername() );
    	setDueDate ( invoice.getDueDate().toString() );
    	setCost( invoice.getCost() );
    	setStatus( invoice.getStatus() );
    }
    
    /**
     * Create a InvoiceForm from all of its fields.
     *
     * @param hcp
     *            Name of HCP
     * @param patient
     *            Name of Patient
     * @param dueDate
     *            invoice's due date
     * @param cost
     *            invoice's cost
     * @param status
     *            invoice status  
     *
     */
    public InvoiceForm(String hcp, String patient, String dueDate, int cost, InvoiceStatus status) {
    	setHcp( hcp );
    	setPatient( patient );
    	setDueDate ( dueDate );
    	setCost( cost );
    	setStatus( status );
    }
    
    /**
     * Returns the id of the Invoice
     *
     * @return ID of the Invoice
     */
    public Long getId () {
        return this.id;
    }
    
    /**
     * Sets the ID of the Invoice
     *
     * @param id
     *            ID of the Invoice
     */
    public void setId ( final Long id ) {
        this.id = id;
    }
    
    /**
     * Gets the User object for the HCP on the invoice
     *
     * @return User object for the HCP on the invoice
     */
    public String getHcp () {
        return hcp;
    }

    /**
     * Sets the User object for the HCP on the invoice
     *
     * @param hcp
     *            User object for the HCP on the invoice
     */
    public void setHcp ( final String hcp ) {
        this.hcp = hcp;
    }

    /**
     * Retrieves the User object for the Patient for the invoice
     *
     * @return User object for the Patient on the invoice
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Sets the Patient for the invoice
     *
     * @param patient
     *            User object for the Patient on the invoice
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Returns the service's due date.
     *
     * @return service's due date
     */
    public String getDueDate () {
        return dueDate;
    }

    /**
     * Sets the service's due date.
     *
     * @param dueDate
     *            service's due date
     */
    public void setDueDate ( final String dueDate ) {
        this.dueDate = dueDate;
    }
    
    /**
     * Gets the invoice's total cost.
     *
     * @return cost
     */
    public int getCost () {
        return cost;
    }

    /**
     * Sets the invoices's total cost.
     *
     * @param cost
     *            total cost
     */
    public void setCost ( final int cost ) {
        this.cost = cost;
    }

    /**
     * Gets the invoice status.
     *
     * @return the invoice status
     */
    public InvoiceStatus getStatus () {
        return status;
    }

    /**
     * Sets the invoice status.
     *
     * @param status
     *            the invoice status
     */
    public void setStatus ( final InvoiceStatus status ) {
        this.status = status;
    }
}
