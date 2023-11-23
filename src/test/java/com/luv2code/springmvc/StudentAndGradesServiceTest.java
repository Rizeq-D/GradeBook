package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradesServiceTest {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private StudentAndGradesService studentService;
    @Autowired
    private StudentDao studentDao;

    @Autowired
    MathGradesDao mathGradeDao;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
                "values (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
    }

    @Test
    public void createStudentService() {

        studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

        CollegeStudent student = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");

        assertEquals("chad.darby@luv2code_school.com", student.getEmailAddress(), "find by email");
        }

    @Test
    public void isStudentNullChecked() {

        assertTrue(studentService.checkIfStudentISNull(1));
        assertFalse(studentService.checkIfStudentISNull(0));
    }

    @Test
    public void deleteStudentService() {

        Optional<CollegeStudent> deleteCollegeStudent = studentDao.findById(1);

        assertTrue(deleteCollegeStudent.isPresent(), "Return true");

        studentService.deleteStudent(1);

        deleteCollegeStudent = studentDao.findById(1);

        assertFalse(deleteCollegeStudent.isPresent(), "Return false");

    }

    @Sql("/insertData.sql")
    @Test
    public void getGradebookService() {

        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for (CollegeStudent collegeStudent : iterableCollegeStudents){
            collegeStudents.add(collegeStudent);
        }

        assertEquals(5, collegeStudents.size());

    }
    @Test
    public void createGradeService() {

        // create the grade
        assertTrue(studentService.createGrade(85, 1, "math"));

        // get all grades with student ID
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);

        // verify there is grades
        assertTrue(mathGrades.iterator().hasNext(),"Student has math grades");

    }
    @AfterEach
    public void setUpAfterTransaction() {
        jdbc.execute("DELETE FROM student");
    }

}


























