package com.students.demo.Service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class StudentControllerServiceTest {

    private final StudentControllerService service = new StudentControllerService();

    @Test
    void studentList_shouldReturnListOfStudents() {
        List<String> students = service.studentList();

        // Basic assertions
        assertNotNull(students, "The student list should not be null");
        assertEquals(4, students.size(), "The student list should contain 4 entries");

        // Validate contents
        assertTrue(students.contains("Alice"), "Student list should contain Alice");
        assertTrue(students.contains("Bob"), "Student list should contain Bob");
        assertTrue(students.contains("Charlie"), "Student list should contain Charlie");
    }
}