package edu.ncsu.csc.itrust2.unit;

import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.itrust2.TestConfig;
import edu.ncsu.csc.itrust2.forms.InvoiceForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Invoice;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.InvoiceStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.InvoiceService;
import edu.ncsu.csc.itrust2.services.UserService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InvoiceTest {
	
	@Autowired
    private InvoiceService invoiceService;
	
	@Autowired
    private UserService userService;
	
	
	@Before
    public void setup () {
        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );
        final User alice = new Patient( new UserForm( "AliceThirteen", "123456", Role.ROLE_PATIENT, 1 ) );
        userService.saveAll( List.of( hcp, alice ) );
    }
	
	@Test
    @Transactional
    public void testInvoice () {
        final Invoice i1 = invoiceService.build( new InvoiceForm( "hcp", "AliceThirteen", LocalDate.now().toString(), 10000, InvoiceStatus.PENDING ) );
        invoiceService.save( i1 );

        final List<Invoice> savedRecords = (List<Invoice>) invoiceService.findAll();
        
        Assert.assertEquals( "hcp", savedRecords.get( savedRecords.size() - 1 ).getHcp().getUsername() );
        
        Assert.assertEquals( LocalDate.now(), savedRecords.get( savedRecords.size() - 1 ).getCreatedDate() );

    }

}
