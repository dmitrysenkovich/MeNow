package com.sam_solutions.app.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * PhoneValidator test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class PhoneValidatorTest {
    @Autowired
    private PhoneValidator phoneValidator;

    @Test
    public void testEmptyNumber() {
        String phoneNumber = "";

        assertFalse(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
    public void testPhoneNumberWithLetter() {
        String phoneNumber = "test";

        assertFalse(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
    public void testSimpleNumber() {
        String phoneNumber = "375299420344";

        assertTrue(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
    public void testSimpleNumberWithPlus() {
        String phoneNumber = "+375299420344";

        assertTrue(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
     public void testSimpleNumberWithSpaces() {
        String phoneNumber = "375 29 9420344";

        assertTrue(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
    public void testSimpleNumberWithBrackets() {
        String phoneNumber = "375 (29) 9420344";

        assertTrue(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
    public void testSimpleNumberWithDashes() {
        String phoneNumber = "375 (29) 9420344";

        assertTrue(phoneValidator.isValid(phoneNumber, null));
    }
}
