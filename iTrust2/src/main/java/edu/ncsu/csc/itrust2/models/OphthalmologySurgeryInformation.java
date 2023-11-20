package edu.ncsu.csc.itrust2.models;

import java.time.ZonedDateTime;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

@Entity
@Table ( name = "OphthalmologySurgeryInf" )
public class OphthalmologySurgeryInformation extends DomainObject {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long            id;
    
    @NotNull
    @OneToOne
    @JoinColumn ( name = "visit_id", nullable = false )
    @JsonBackReference
    private OfficeVisit     visit;

    @NotNull
    @Basic
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime    date;

    @NotNull
    private Long    visualAcuityOS;

    @NotNull
    private Long    visualAcuityOD;

    @NotNull
    private Double  sphereOS;

    @NotNull
    private Double  sphereOD;

    private Double  cylinderOS;

    private Double  cylinderOD;
    
    private Long    axisOS;

    private Long    axisOD;

    @NotNull
    @Enumerated ( EnumType.STRING )
    private OphthalmologySurgeryType    type;

    @NotNull
    @JoinColumn ( columnDefinition = "varchar(500)" )
    private String      notes;


    public void vaildate () {
        if ( cylinderOS == null && cylinderOD == null ) {
            if ( axisOS != null || axisOD != null ) {
                throw new IllegalArgumentException( "the axis fields must be empty");
            }
        }
        else {
            if ( axisOS == null || axisOD == null ) {
                throw new IllegalArgumentException( "the axis fields must not be empty");
            }
        }
    }

    public void setId( final Long id ) {
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
