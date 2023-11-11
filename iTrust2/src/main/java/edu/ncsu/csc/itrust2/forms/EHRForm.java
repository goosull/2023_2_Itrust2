package edu.ncsu.csc.itrust2.forms;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

public class EHRForm {
	/**
     * ID of this patient
     */
	@Id
	@GeneratedValue ( strategy = GenerationType.AUTO )
	@Length ( min = 6, max = 20 )
    private Long id;
	
	/**
     * The Age of this patient
     */
	private int age;
	
	/**
     * The first name of this patient
     */
    @Length ( max = 20 )
    private String firstName;
    
    /**
     * The last name of this patient
     */
    @Length ( max = 30 )
    private String lastName;
	
	/**
     * The date of death of this patient
     */
    private String dateOfBirth;
	
	/**
     * The gender of this patient
     */
    private String gender;
	
	/**
     * The blood type of this patient
     */
    private String bloodType;
	
	public EHRForm() {
	}
	
	public Long getId () {
        return id;
    }
	
	public int getAge() {
		return age;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
}
