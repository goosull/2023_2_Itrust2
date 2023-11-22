package edu.ncsu.csc.itrust2.api;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.InvoiceForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.Invoice;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.InvoiceStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.HospitalService;
import edu.ncsu.csc.itrust2.services.InvoiceService;
import edu.ncsu.csc.itrust2.services.UserService;

/**
 * Class for testing invoice API
 * 
 * @author Woojun Ro(2018122053)
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIInvoiceTest {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	/**
	 * Set up test
	 */
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		final User hcp = new Personnel(new UserForm("testhcp", "123456", Role.ROLE_HCP, 1));
		final User patient = new Patient(new UserForm("testpatient", "123456", Role.ROLE_PATIENT, 1));
		final User patient2 = new Patient(new UserForm("testpatient2", "123456", Role.ROLE_PATIENT, 1));
		
		userService.saveAll(List.of(hcp, patient, patient2));
		
		final Hospital hospital = new Hospital("testhospital", "address", "12345", "AL");
		
		hospitalService.save(hospital);
	}
	
	/**
	 * Test invoice APIs for HCP
	 */
	@Test
	@Transactional
	@WithMockUser(username = "testhcp", roles = {"HCP"})
	public void testInvoiceAPIForHcp() throws Exception {
		
		final Gson gson = new GsonBuilder().create();
		String content;
		
        final InvoiceForm form = new InvoiceForm();
        form.setHcp("testhcp");
        form.setPatient("testpatient");
        form.setDueDate("2040-01-08");
        form.setStatus(InvoiceStatus.PENDING);
        
        // Make sure cost cannot be negative
        form.setCost(-100);
        mvc.perform(post("/api/v1/invoices")
        		.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(form)))
        		.andExpect(status().isBadRequest());
        
        // Make sure an invoice is created
        form.setCost(100);
        mvc.perform(post("/api/v1/invoices")
        		.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(form)))
        		.andExpect(status().isCreated());
        
        // Retrieve the created invoice
        content = mvc
        		.perform(get("/api/v1/invoices").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Invoice> invoices = gson.fromJson(
        		content,
        		new TypeToken<ArrayList<Invoice>>() {}.getType()
        );
        
        assertTrue(invoices.get(0).getHcp().getId().equals(form.getHcp()));
        assertTrue(invoices.get(0).getPatient().getId().equals(form.getPatient()));
		assertTrue(invoices.get(0).getDueDate().toString().equals(form.getDueDate()));
		assertTrue(invoices.get(0).getCost() == form.getCost());
		assertTrue(invoices.get(0).getStatus() == InvoiceStatus.PENDING);
		
	}
	
	/**
	 * Test invoice APIs for patient
	 */
	@Test
	@Transactional
	@WithMockUser(username = "testpatient", roles = {"PATIENT"})
	public void testInvoiceAPIForPatient() throws Exception {
		
		final Gson gson = new GsonBuilder().create();
		String content;
        
		// Create an invoice
        final InvoiceForm form = new InvoiceForm();
        form.setHcp("testhcp");
        form.setPatient("testpatient");
        form.setDueDate("2040-01-08");
        form.setCost(100);
        form.setStatus(InvoiceStatus.PENDING);
        
        Invoice i = invoiceService.build(form);
        invoiceService.save(i);
        
        // Retrieve the created invoice
        content = mvc
        		.perform(get("/api/v1/invoices").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Invoice> invoices = gson.fromJson(
        		content,
        		new TypeToken<ArrayList<Invoice>>() {}.getType()
        );
        
        assertTrue(invoices.get(0).getHcp().getId().equals(form.getHcp()));
        assertTrue(invoices.get(0).getPatient().getId().equals(form.getPatient()));
		assertTrue(invoices.get(0).getDueDate().toString().equals(form.getDueDate()));
		assertTrue(invoices.get(0).getCost() == form.getCost());
		assertTrue(invoices.get(0).getStatus() == InvoiceStatus.PENDING);
		
		// Make sure invalid id request receives 404 exception
		mvc.perform(post("/api/v1/invoices/0/pay")
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isNotFound());
		
		// Pay the invoice
		Long invoiceId = invoices.get(0).getId();
		String url = "/api/v1/invoices/" + Long.toString(invoiceId) + "/pay";
		mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk());
		
		// Retrieve the paid invoice
        content = mvc
        		.perform(get("/api/v1/invoices").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        invoices = gson.fromJson(
        		content,
        		new TypeToken<ArrayList<Invoice>>() {}.getType()
        );
        
		assertTrue(invoices.get(0).getStatus() == InvoiceStatus.PAID);
		
		// Make sure the paid invoice cannot be paid again
		mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest());
		
	}
	
	/**
	 * Test another patient's forbidden access
	 */
	@Test
	@Transactional
	@WithMockUser(username = "testpatient2", roles = {"PATIENT"})
	public void testInvoiceAPIForAnotherPatient() throws Exception {
        
		// Create an invoice
        final InvoiceForm form = new InvoiceForm();
        form.setHcp("testhcp");
        form.setPatient("testpatient");
        form.setDueDate("2040-01-08");
        form.setCost(100);
        form.setStatus(InvoiceStatus.PENDING);
        
        Invoice i = invoiceService.build(form);
        invoiceService.save(i);
        
        // Retrieve the created invoice
        User patient = userService.findByName("testpatient");
        List<Invoice> invoices = invoiceService.findByPatient(patient);
        
        // Make sure the payment is forbidden
        Long invoiceId = invoices.get(0).getId();
        String url = "/api/v1/invoices/" + Long.toString(invoiceId) + "/pay";
		mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isForbidden());
		
	}
	
}