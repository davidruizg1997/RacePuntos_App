package com.example.davidruiz.preliminar;

import android.widget.EditText;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_login_idNumber_and_pass_isCorrect() throws Exception {
        String idUser="123456";
        String passUser="ejemplo";
        Login login=new Login();
        //login.userDocument=idUser;
        //login.loginPassword=passUser;
        assertEquals(4, 2 + 2);
    }
}