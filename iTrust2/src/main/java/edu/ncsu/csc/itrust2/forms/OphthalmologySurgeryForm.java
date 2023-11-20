package edu.ncsu.csc.itrust2.forms;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;


public class OphthalmologySurgeryForm implements Serializable {
    /**
     * Serial Version of the Form. For the Serializable
     */
    // private static final long serialVersionUID = 1L;

    /**
     * Empty constructor so that we can create an Office Visit form for the user
     * to fill out
     */
    public OphthalmologySurgeryForm () {
    }

    private Long    id;

    private String patient;

    /** The hcp of the appt request */
    @NotNull ( message = "Invalid HCP" )
    private String hcp;

    private String  date;

    private Long    visualAcuityOS;

    private Long    visualAcuityOD;

    private Double  sphereOS;

    private Double  sphereOD;

    private Double  cylinderOS;

    private Double  cylinderOD;
    
    private Long    axisOS;

    private Long    axisOD;
        
    @NotEmpty
    private String    type;

    private String      notes;

    /**
     * Creates an OphthalmologySurgeryForm from the OphthalmologySurgeryFInformation provided
     *
     * @param surgery
     *            OphthalmologySurgeryFInformation to turn into an OphthalmologySurgeryForm
     */
    public OphthalmologySurgeryForm ( final OphthalmologySurgeryInformation surgery ) {
        setPatient( surgery.getPatient().getUsername() );
        setHcp( surgery.getHcp().getUsername() );
        setDate( surgery.getDate().toString() );
        setVisualAcuityOS( surgery.getVisualAcuityOS() );
        setVisualAcuityOD( surgery.getVisualAcuityOD() );
        setSphereOS( surgery.getSphereOS() );
        setSphereOD( surgery.getSphereOD() );
        setCylinderOS( surgery.getCylinderOS() );
        setCylinderOD( surgery.getCylinderOD() );
        setAxisOS( surgery.getAxisOS() );
        setAxisOD( surgery.getAxisOD() );
        setType( surgery.getType().toString() );
        setNotes( surgery.getNotes() );
    }

    public void setId( final Long id ) {
        this.id = id;
    }

    public Long getId () {
        return id;
    }

    public String getPatient () {
        return patient;
    }

    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    public String getHcp () {
        return hcp;
    }

    public void setHcp ( final String hcp ) {
        this.hcp = hcp;
    }

    public void setDate ( final String date ) {
        this.date = date;
    }

    public String getDate () {
        return date;
    }

    public void setVisualAcuityOS ( final Long visualAcuityOS ) {
        this.visualAcuityOS = visualAcuityOS;
    }

    public Long getVisualAcuityOS () {
        return visualAcuityOS;
    }

    public void setVisualAcuityOD ( final Long visualAcuityOD) {
        this.visualAcuityOD = visualAcuityOD;
    }

    public Long getVisualAcuityOD () {
        return visualAcuityOD;
    }

    public void setSphereOS ( final Double sphereOS ) {
        this.sphereOS = sphereOS;
    }

    public Double getSphereOS () {
        return sphereOS;
    }

    public void setSphereOD ( final Double sphereOD ) {
        this.sphereOD = sphereOD;
    }

    public Double getSphereOD () {
        return sphereOD;
    }

    public void setCylinderOS ( final Double cylinderOS ) {
        this.cylinderOS = cylinderOS;
    }

    public Double getCylinderOS () {
        return cylinderOS;
    }

    public void setCylinderOD ( final Double cylinderOD ) {
        this.cylinderOD = cylinderOD;
    }

    public Double getCylinderOD () {
        return cylinderOD;
    }

    public void setAxisOS ( final Long axisOS ) {
        this.axisOS = axisOS;
    }

    public Long getAxisOS () {
        return axisOS;
    }

    public void setAxisOD ( final Long axisOD ) {
        this.axisOD = axisOD;
    }

    public Long getAxisOD () {
        return axisOD;
    }

    public void setType ( final String type) {
        this.type = type;
    }

    public String getType () {
        return type;
    }

    public void setNotes ( final String notes ) {
        this.notes = notes;
    }

    public String getNotes () {
        return notes;
    }
}
