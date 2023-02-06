package com.mindex.challenge.exceptions;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(String id) {
        super("Circular dependency found in the reporting structure for id: " + id);
    }
}
