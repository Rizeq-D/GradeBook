package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${sql.script.create.student}")
    private String sqlAddStudent;
    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathStudent;
    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceStudent;
    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryStudent;
    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;
    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;
    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;
    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;


    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathStudent);
        jdbc.execute(sqlAddScienceStudent);
        jdbc.execute(sqlAddHistoryStudent);
    }

    @Test
    public void createStudentService() {

        studentService.createStudent("Maher", "co", "maher@gmail.com");

        CollegeStudent student = studentDao.findByEmailAddress("maher@gmail.com");

        assertEquals("maher@gmail.com", student.getEmailAddress(), "find by email");
        }

    @Test
    public void isStudentNullChecked() {

        assertTrue(studentService.checkIfStudentISNull(1));
        assertFalse(studentService.checkIfStudentISNull(0));
    }

    @Test
    public void deleteStudentService() {

        Optional<CollegeStudent> deleteCollegeStudent = studentDao.findById(1);

        Optional<MathGrade> deleteMathGrade = mathGradeDao.findById(1);
        Optional<ScienceGrade> deleteScienceGrade = scienceGradeDao.findById(1);
        Optional<HistoryGrade> deleteHistoryGrade = historyGradesDao.findById(1);

        assertTrue(deleteCollegeStudent.isPresent(), "Return true");

        assertTrue(deleteMathGrade.isPresent());
        assertTrue(deleteScienceGrade.isPresent());
        assertTrue(deleteHistoryGrade.isPresent());

        studentService.deleteStudent(1);

        deleteCollegeStudent = studentDao.findById(1);

        deleteMathGrade = mathGradeDao.findById(1);
        deleteScienceGrade = scienceGradeDao.findById(1);
        deleteHistoryGrade = historyGradesDao.findById(1);

        assertFalse(deleteCollegeStudent.isPresent(), "Return false");

        assertFalse(deleteMathGrade.isPresent());
        assertFalse(deleteScienceGrade.isPresent());
        assertFalse(deleteHistoryGrade.isPresent());
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
    @Test
    public void deleteGradeServiceReturnStudentIfOfZero() {
        assertEquals(0, studentService.deleteGrade(0, "science"),
                "student should have 0 id");
        assertEquals(0, studentService.deleteGrade(1, "literature"),
                "student should have a literature class");
    }
    @Test
    public void studentInformation() {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);

        assertNotNull(gradebookCollegeStudent);

        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Jalta", gradebookCollegeStudent.getFirstname());
        assertEquals("Co", gradebookCollegeStudent.getLastname());
        assertEquals("jalta@gmail.com", gradebookCollegeStudent.getEmailAddress());

        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
    }
    @Test
    public void studentInformationServiceReturnNull() {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);
        assertNull(gradebookCollegeStudent);
    }
    @AfterEach
    public void setUpAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }

}


























