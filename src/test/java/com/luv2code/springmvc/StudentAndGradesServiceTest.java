package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
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
import java.util.Collection;
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
    @Autowired
    private ScienceGradeDao scienceGradeDao;
    @Autowired
    private HistoryGradesDao historyGradesDao;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
                "values (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
        jdbc.execute("insert into math_grade(id, student_id, grade) values (1, 1, 100.00)");
        jdbc.execute("insert into history_grade(id, student_id, grade) values (1, 1, 100.00)");
        jdbc.execute("insert into science_grade(id, student_id, grade) values (1, 1, 100.00)");
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
        assertTrue(studentService.createGrade(60, 1, "science"));
        assertTrue(studentService.createGrade(100, 1, "history"));

        // get all grades with student ID
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(1);

        // verify there is grades
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2,
                "Student has math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2,
                "Student has science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2,
                "Student has history grades");

    }
    @Test
    public void createGradeServiceReturnFalse() {
        assertFalse(studentService.createGrade(-5, 1, "math"));
        assertFalse(studentService.createGrade(200, 1, "science"));
        assertFalse(studentService.createGrade(50, 2, "math"));
        assertFalse(studentService.createGrade(90, 1, "literature"));
    }
    @Test
    public void deleteGradeService() {
        assertEquals(1, studentService.deleteGrade(1, "math"),
                "Return student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "science"),
                "Return student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "history"),
                "Return student id after delete");
    }
    @AfterEach
    public void setUpAfterTransaction() {
        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");
    }

}


























