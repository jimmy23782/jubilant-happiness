package com.students.demo.Controller;

import com.students.demo.Service.StudentControllerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Replace the real bean with a mock so we test only the controller layer
@MockitoBean
private StudentControllerService service;

    @Test
    void getStudents_returnsListOfStudents() throws Exception {
        // Arrange
        List<String> students = List.of("Alice", "Bob", "Charlie");
        given(service.studentList()).willReturn(students);

        // Act & Assert
        mockMvc.perform(get("/v1/students").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0]").value("Alice"))
               .andExpect(jsonPath("$[1]").value("Bob"))
               .andExpect(jsonPath("$[2]").value("Charlie"));

        // Optional: verify the controller called the service once
        Mockito.verify(service).studentList();
    }

    @Test
    void getStudents_handlesEmptyList() throws Exception {
        given(service.studentList()).willReturn(List.of());

        mockMvc.perform(get("/v1/students").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }
}