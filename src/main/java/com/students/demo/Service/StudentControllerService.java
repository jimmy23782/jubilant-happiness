package com.students.demo.Service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StudentControllerService {
    
    public List<String> studentList(){

        log.info("Fetching student list");
        return List.of("Alice", "Bob", "Charlie","Lodada");
    }
}
