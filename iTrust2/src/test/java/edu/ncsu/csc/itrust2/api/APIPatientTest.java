package edu.ncsu.csc.itrust2.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.PatientForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.services.PatientService;

/**
 * Test for API functionality for interacting with Patients
 *
 * @author Kai Presler-Marshall
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIPatientTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PatientService        service;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests that getting a patient that doesn't exist returns the proper
     * status.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testGetNonExistentPatient () throws Exception {
        mvc.perform( get( "/api/v1/patients/-1" ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests PatientAPI
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    @Transactional
    public void testPatientAPI () throws Exception {

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        // Editing the patient before they exist should fail
        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isNotFound() );

        final User antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( antti );

        // Creating a User should create the Patient record automatically
        mvc.perform( get( "/api/v1/patients/antti" ) ).andExpect( status().isOk() );

        // Should also now be able to edit existing record with new information
        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        Patient anttiRetrieved = (Patient) service.findByName( "antti" );

        // Test a few fields to be reasonably confident things are working
        Assert.assertEquals( "Walhelm", anttiRetrieved.getLastName() );
        Assert.assertEquals( Gender.Male, anttiRetrieved.getGender() );
        Assert.assertEquals( "Viipuri", anttiRetrieved.getCity() );

        // Also test a field we haven't set yet
        Assert.assertNull( anttiRetrieved.getPreferredName() );

        patient.setPreferredName( "Antti Walhelm" );

        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        anttiRetrieved = (Patient) service.findByName( "antti" );

        Assert.assertNotNull( anttiRetrieved.getPreferredName() );

        // Editing with the wrong username should fail.
        mvc.perform( put( "/api/v1/patients/badusername" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Test accessing the patient PUT request unauthenticated
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testPatientUnauthenticated () throws Exception {
        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        final User antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( antti );

        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isUnauthorized() );
    }

    /**
     * Test accessing the patient PUT request as a patient
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "antti", roles = { "PATIENT" } )
    public void testPatientAsPatient () throws Exception {
        final User antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( antti );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        // a patient can edit their own info
        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        final Patient anttiRetrieved = (Patient) service.findByName( "antti" );

        // Test a few fields to be reasonably confident things are working
        Assert.assertEquals( "Walhelm", anttiRetrieved.getLastName() );
        Assert.assertEquals( Gender.Male, anttiRetrieved.getGender() );
        Assert.assertEquals( "Viipuri", anttiRetrieved.getCity() );

        // Also test a field we haven't set yet
        Assert.assertNull( anttiRetrieved.getPreferredName() );

        // but they can't edit someone else's
        patient.setUsername( "patient" );
        mvc.perform( put( "/api/v1/patients/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isUnauthorized() );

        // we should be able to update our record too
        mvc.perform( get( "/api/v1/patient/" ) ).andExpect( status().isOk() );
    }

     /**
     * Test case for retrieving patients by their fisrtName, lastName
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser(username = "hcp, er", roles = {"HCP", "ER"})
    public void testPatientListByName () throws Exception {
    	
        final PatientForm patient1 = new PatientForm();
        patient1.setFirstName( "Minsu" );
        patient1.setLastName( "Kim" );
        patient1.setUsername( "MinsuKim" );
        patient1.setAddress1( "1 Test Street" );
        patient1.setAddress2( "Some Location" );
        patient1.setBloodType( BloodType.APos.toString() );
        patient1.setCity( "Viipuri" );
        patient1.setDateOfBirth( "1977-06-15" );
        patient1.setEmail( "Minsu@itrust.fi" );
        patient1.setEthnicity( Ethnicity.Caucasian.toString() );
        patient1.setGender( Gender.Male.toString() );
        patient1.setPhone( "123-456-7890" );
        patient1.setState( State.NC.toString() );
        patient1.setZip( "27514" );
        
        final PatientForm patient2 = new PatientForm();
        patient2.setFirstName("Yeona");
        patient2.setLastName("Kim");
        patient2.setUsername("YeonaKim");
        patient2.setAddress1( "1 Test Street" );
        patient2.setAddress2( "Some Location" );
        patient2.setBloodType( BloodType.BPos.toString() );
        patient2.setCity( "Viipuri" );
        patient2.setDateOfBirth( "1977-06-15" );
        patient2.setEmail( "Yeona@itrust.fi" );
        patient2.setEthnicity( Ethnicity.Caucasian.toString() );
        patient2.setGender( Gender.Female.toString() );
        patient2.setPhone( "123-456-7890" );
        patient2.setState( State.NC.toString() );
        patient2.setZip( "27514" );
        
        final PatientForm patient3 = new PatientForm();
        patient3.setFirstName("Yewon");
        patient3.setLastName("Son");
        patient3.setUsername("YewonSon");
        patient3.setAddress1( "1 Test Street" );
        patient3.setAddress2( "Some Location" );
        patient3.setBloodType( BloodType.OPos.toString() );
        patient3.setCity( "Viipuri" );
        patient3.setDateOfBirth( "1977-06-15" );
        patient3.setEmail( "Yewon@itrust.fi" );
        patient3.setEthnicity( Ethnicity.Caucasian.toString() );
        patient3.setGender( Gender.Female.toString() );
        patient3.setPhone( "123-456-7890" );
        patient3.setState( State.NC.toString() );
        patient3.setZip( "27514" );
        
        final User MinsuKim = new Patient( new UserForm( "MinsuKim", "123456", Role.ROLE_PATIENT, 1 ) );
        final User YeonaKim = new Patient( new UserForm( "YeonaKim", "123456", Role.ROLE_PATIENT, 1 ) );
        final User YewonSon = new Patient( new UserForm( "YewonSon", "123456", Role.ROLE_PATIENT, 1 ) );
        
        service.save( MinsuKim );
        service.save( YeonaKim );
        service.save( YewonSon );
        
        mvc.perform( get( "/api/v1/patients/MinsuKim" ) ).andExpect( status().isOk() );
        
        mvc.perform( put( "/api/v1/patients/MinsuKim" ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( patient1 ) ) ).andExpect( status().isOk() );
        
        mvc.perform( get( "/api/v1/patients/YeonaKim" ) ).andExpect( status().isOk() );
        
        mvc.perform( put( "/api/v1/patients/YeonaKim" ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( patient2 ) ) ).andExpect( status().isOk() );
        
        mvc.perform( get( "/api/v1/patients/YewonSon" ) ).andExpect( status().isOk() );
        
        mvc.perform( put( "/api/v1/patients/YewonSon" ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( patient3 ) ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/patients/MinsuKim" ) ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/patients/yeonaKim" ) ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/patients/yewonSon" ) ).andExpect( status().isOk() );
        
        MvcResult result1 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/name/" + "a"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    	
    	MvcResult result2 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/name/" + "Ye"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    	
    	MvcResult result3 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/name/" + "Kim"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    	
    	MvcResult result4 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/name/" + "noa"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    	
    	final Gson gson = new GsonBuilder().create();
    	String content1 = result1.getResponse().getContentAsString();
        String content2 = result2.getResponse().getContentAsString();
        String content3 = result3.getResponse().getContentAsString();
        List<Patient> plist1 = gson.fromJson( content1, new TypeToken<ArrayList<Patient>>() {}.getType() );
        List<Patient> plist2 = gson.fromJson( content2, new TypeToken<ArrayList<Patient>>() {}.getType() );
        List<Patient> plist3 = gson.fromJson( content3, new TypeToken<ArrayList<Patient>>() {}.getType() );
        
        Assert.assertEquals( "1 matching patients", 1, plist1.size());
        Assert.assertEquals( "2 matching patients", 2, plist2.size());
        Assert.assertEquals( "2 matching patients", 2, plist3.size());
    }
    
    /**
     * Test case for retrieving patients by their MID/userName
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser(username = "hcp, er", roles = {"HCP", "ER"})
    public void testPatientListById () throws Exception {
        final PatientForm patient1 = new PatientForm();
        patient1.setUsername("KimMinsu");
        
        final PatientForm patient2 = new PatientForm();
        patient2.setUsername("KimMinsu2");
        
        final PatientForm patient3 = new PatientForm();
        patient3.setUsername("ChoiSujin");
        
        final User KimMinsu = new Patient( new UserForm( "KimMinsu", "123456", Role.ROLE_PATIENT, 1 ) );
        final User KimMinsu2 = new Patient( new UserForm( "KimMinsu2", "123456", Role.ROLE_PATIENT, 1 ) );
        final User ChoiSujin = new Patient( new UserForm( "ChoiSujin", "123456", Role.ROLE_PATIENT, 1 ) );

        service.save( KimMinsu );
        service.save( KimMinsu2 );
        service.save( ChoiSujin );
        
        mvc.perform( get( "/api/v1/patients/KimMinsu" ) ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/patients/KimMinsu2" ) ).andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/patients/ChoiSujin" ) ).andExpect( status().isOk() );
    	
    	MvcResult result1 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/id/" + "KimMinsu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    	
    	MvcResult result2 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/id/" + "mMinsu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    	
    	MvcResult result3 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/id/" + "uidname"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    	
    	MvcResult result4 = mvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/id/" + "oSujin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    	
    	final Gson gson = new GsonBuilder().create();
        String content1 = result1.getResponse().getContentAsString();
        String content2 = result2.getResponse().getContentAsString();
        String content4 = result4.getResponse().getContentAsString();
        List<Patient> plist1 = gson.fromJson( content1, new TypeToken<ArrayList<Patient>>() {}.getType() );
        List<Patient> plist2 = gson.fromJson( content2, new TypeToken<ArrayList<Patient>>() {}.getType() );
        List<Patient> plist4 = gson.fromJson( content4, new TypeToken<ArrayList<Patient>>() {}.getType() );
        
        Assert.assertEquals( "2 matching patients", 2, plist1.size());
        Assert.assertEquals( "2 matching patients", 2, plist2.size());
        Assert.assertEquals( "1 matching patients", 1, plist4.size());
    }
}
