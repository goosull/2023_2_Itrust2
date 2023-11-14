package edu.ncsu.csc.itrust2.models.enums;

/**
 * Enum of all of the types of Ophthalmology Surgery
 *
 * @author Lee Hyeob
 */
public enum OphthalmologySurgeryType {
    
    /**
     * This field is unrelated
     */
    NONAPPLICABLE ( 0 ),
    
    /**
     * Cataract Surgery
     */
    CATARACT ( 1 ),

    /**
     * Laser Surgery
     */
    LASER ( 2 ),

    /**
     * Refractive Surgery
     */
    REFRACTIVE ( 3 ),

    ;

    /**
     * Numerical code of the OphthalmologySurgeryType
     */
    private int code;

    /**
     * Creates the OphthalmologySurgeryType from its code.
     *
     * @param code
     *            Code of the OphthalmologySurgeryType
     */
    private OphthalmologySurgeryType ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numerical code of the OphthalmologySurgeryType 
     *
     * @return Code of the OphthalmologySurgeryType
     */
    public int getCode () {
        return code;
    }
}
