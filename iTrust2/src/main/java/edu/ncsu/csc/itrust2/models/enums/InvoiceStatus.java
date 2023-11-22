package edu.ncsu.csc.itrust2.models.enums;

public enum InvoiceStatus {

	/**
     * Pending
     */
    PENDING ( 1 ),

    /**
     * Paid
     */
    PAID ( 2 ),

    ;

    /**
     * Code of the status
     */
    private int code;

    /**
     * Create a Status from the numerical code.
     *
     * @param code
     *            Code of the Status
     */
    private InvoiceStatus ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numerical Code of the Status
     *
     * @return Code of the Status
     */
    public int getCode () {
        return code;
    }
}
