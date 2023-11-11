package edu.ncsu.csc.itrust2.models;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;

@Entity
public class EHR extends DomainObject{

	/**
     * ID of this patient
     */
	@Id
	@GeneratedValue ( strategy = GenerationType.AUTO )
    private Long id;
	
	/**
     * The Age of this patient
     */
	private int age;
	
	/**
     * The first name of this patient
     */
    private String    firstName;
    
    /**
     * The last name of this patient
     */
    private String    lastName;
	
	/**
     * The date of death of this patient
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate dateOfBirth;
	
	/**
     * The gender of this patient
     */
    @Enumerated ( EnumType.STRING )
    private Gender    gender;
	
	/**
     * The blood type of this patient
     */
    @Enumerated ( EnumType.STRING )
    private BloodType bloodType;
	
	public EHR() {
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public BloodType getBloodType() {
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

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}
	
}
