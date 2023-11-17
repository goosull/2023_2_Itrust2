package edu.ncsu.csc.itrust2.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.models.OphthalmologySurgeryInformation;
import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;


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

    @NotNull
    @Basic
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime    date;

    @NotEmpty
    private Long    visualAcuityOS;

    @NotEmpty
    private Long    visualAcuityOD;

    @NotEmpty
    private Double  sphereOS;

    @NotEmpty
    private Double  sphereOD;

    @NotEmpty
    private Double  cylinderOS;

    @NotEmpty
    private Double  cylinderOD;
    
    @NotEmpty
    private Long    axisOS;

    @NotEmpty
    private Long    axisOD;
        
    @NotEmpty
    @Enumerated ( EnumType.STRING )
    private OphthalmologySurgeryType    type;

    @NotEmpty
    @JoinColumn ( columnDefinition = "varchar(500)" )
    private String      notes;

    /**
     * Creates an OphthalmologySurgeryForm from the OphthalmologySurgeryFInformation provided
     *
     * @param surgery
     *            OphthalmologySurgeryFInformation to turn into an OphthalmologySurgeryForm
     */
    public OphthalmologySurgeryForm ( final OphthalmologySurgeryInformation surgery ) {
        
        setDate( surgery.getDate().toString() );
    =   setVisualAcuityOS( surgery.getVisualAcuityOS().toString() );
        setVisualAcuityOD( surgery.getVisualAcuityOD().toString() );
        setSphereOS( surgery.getSphereOS().toString() );
        setSphereOD( surgery.getSphereOD().toString() );
        setCylinderOS( surgery.getCylinderOS().toString() );
        setCylinderOD( surgery.getCylinderOD().toString() );
        setAxisOS( surgery.getAxisOS().toString() );
        setAxisOD( surgery.getAxisOD().toString() );
        setType( surgery.getType() );
        setNotes( surgery.getNote() );

    }

    public void setLong( final Long id ) {
        this.id = id;
    }

    @Override
    public Long getId () {
        return id;
    }
    
    public void setVisit ( final OfficeVisit visit ) {
        this.visit = visit;
    }

    public OfficeVisit getVisit () {
        return visit;
    }

    public void setDate ( final ZonedDateTime date ) {
        this.date = date;
    }

    public ZonedDateTime getDate () {
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

    public void setType ( final OphthalmologySurgeryType type) {
        this.type = type;
    }

    public OphthalmologySurgeryType getType () {
        return type;
    }

    public void setNotes ( final String notes ) {
        this.notes = notes;
    }

    public String getNotes () {
        return notes;
    }
}
