package com.students.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StudentControllerService {
    
    public List<String> studentList(){

        return List.of("Alice", "Bob", "Charlie");
    }
}
