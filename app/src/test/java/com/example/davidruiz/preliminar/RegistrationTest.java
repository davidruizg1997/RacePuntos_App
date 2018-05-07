package com.example.davidruiz.preliminar;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrationTest {

    @Test
    public void addUser() {
        String passwordEntered="CONTRASENA12345";
        String confirmPassword="CONTRASENA12345";
        String documentType = "C.C.";
        String documentEntered = "";
        String roleEntered = "N/A";
        String officeEntered = "3";
        String userCreation = "ANDROID";
        String namesEntered = "LUIS";
        String surnamesEntered = "GONZALEZ";
        String birthDate = "1/2/1998";
        String addressEntered = "Calle 31 No. 10-31";
        String cellNumber = "3104738219";
        String email = "lgonzalez@gmail.com";

        String expected="OK";
        String output;
        Registration registration=new Registration();
        //output=registration.addUser(passwordEntered, confirmPassword, documentType, documentEntered, roleEntered, officeEntered, userCreation, namesEntered, surnamesEntered, birthDate, addressEntered, cellNumber, email);
        //assertEquals(expected, output);
    }
}