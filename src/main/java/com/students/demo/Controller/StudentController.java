package com.students.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.students.demo.Service.StudentControllerService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class StudentController {

    private final StudentControllerService service;

    public StudentController(StudentControllerService service) {
        this.service = service;
    }
    
    @GetMapping("/students")
    public List<String> getStudents() {
        // Example payload; replace with a service call later
        return service.studentList();
    }
}
