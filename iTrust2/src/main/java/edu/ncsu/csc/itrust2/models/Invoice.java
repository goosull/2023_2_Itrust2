package edu.ncsu.csc.itrust2.models;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.models.enums.InvoiceStatus;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Invoice extends DomainObject {
	
	/**
     * Empty constructor for Hibernate.
     */
	public Invoice() {
	}

	@Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long id;
	
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User hcp;
	
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User patient;
    
    @Basic
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    @CreatedDate
    private LocalDate createdDate;

    @NotNull
    @Basic
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate dueDate;
    
    @Min ( 0 )
    private int cost;
    
    @NotNull
    private InvoiceStatus status;
    
    /**
     * Returns the id of the Invoice
     *
     * @return ID of the Invoice
     */
    @Override
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
    public User getHcp () {
        return hcp;
    }

    /**
     * Sets the User object for the HCP on the invoice
     *
     * @param hcp
     *            User object for the HCP on the invoice
     */
    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

    /**
     * Retrieves the User object for the Patient for the invoice
     *
     * @return User object for the Patient on the invoice
     */
    public User getPatient () {
        return patient;
    }

    /**
     * Sets the Patient for the invoice
     *
     * @param patient
     *            User object for the Patient on the invoice
     */
    public void setPatient ( final User patient ) {
        this.patient = patient;
    }
    
    /**
     * Returns the service's created date.
     *
     * @return service's created date
     */
    public LocalDate getCreatedDate () {
        return createdDate;
    }

    /**
     * Returns the service's due date.
     *
     * @return service's due date
     */
    public LocalDate getDueDate () {
        return dueDate;
    }

    /**
     * Sets the service's due date.
     *
     * @param dueDate
     *            service's due date
     */
    public void setDueDate ( final LocalDate dueDate ) {
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
