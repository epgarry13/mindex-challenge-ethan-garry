package com.mindex.challenge.exceptions;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CircularDependencyExceptionTest {
    @Test
    public void testThrownCircularDependencyException() {
        String id = "123";
        String expectedOutput = "Circular dependency found in the reporting structure for id: 123";
        CircularDependencyException exception = new CircularDependencyException(id);
        assertEquals(expectedOutput, exception.getMessage());
    }
}
