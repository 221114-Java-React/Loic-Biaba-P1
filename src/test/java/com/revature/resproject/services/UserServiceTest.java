package com.revature.resproject.services;

import com.revature.resproject.daos.UserDAO;
import org.junit.Test;

import static org.junit.Assert.*;

/*
   Mockito is a mocking framework. JAVA-based library that is used for effective unit testing of JAVA applications.
   Mocking is the act of removing external dependencies from a unit test in order to create a controlled environment around it. Typically, we mock all other classes that interact with the class that we want to test.
 */

public class UserServiceTest {
    private final UserService sut = new UserService((new UserDAO())); // sut = system under test

    @Test
    public void test_isValidUsername_givenCorrectUsername() {
        //AAA = Arrange Act Assert

        // Arrange (essentially the setup)
        String validUsername = "mjordan18";

        // Act (essentially calling the method you are testing)
        boolean condition = sut.isValidUsername(validUsername);

        // Assert (Compare the actual to the expected)
        assertTrue(condition);
    }

    @Test
    public void test_isValidUsername_givenUniqueUsername() {
        // Arrange (essentially the setup)
        String validUsername = "mjordan10";

        // Act (essentially calling the method you are testing)
        boolean condition = sut.isDuplicateUsername(validUsername);

        // Assert (Compare the actual to the expected)
        assertFalse(condition);
    }
}