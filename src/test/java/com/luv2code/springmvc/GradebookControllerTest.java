package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradesService;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    StudentAndGradesService studentAndGradesServiceMock;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
                "values (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
    }
    @Test
    public void getStudentAndHttpRequest() throws Exception {

        CollegeStudent student1 = new GradebookCollegeStudent(
                "Eric", "Roby", "eric.roby@luv2code_school.com");

        CollegeStudent student2 = new GradebookCollegeStudent(
                "Chad", "Darby", "chad.darby@luv2code_school.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(student1, student2));

        when(studentAndGradesServiceMock.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentAndGradesServiceMock.getGradebook());
    }

    @AfterEach
    public void setUpAfterTransaction() {
        jdbc.execute("DELETE FROM student");
    }

}


























