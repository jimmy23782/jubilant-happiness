package com.students.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class StudentController {

    @GetMapping("/students")
    public List<String> getStudents() {
        // Example payload; replace with a service call later
        return List.of("Alice", "Bob", "Charlie");
    }
}
