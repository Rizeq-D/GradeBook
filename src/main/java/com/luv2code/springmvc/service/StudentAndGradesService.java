package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradesService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;
    @Autowired
    private MathGradesDao mathGradeDao;
    @Autowired
    private ScienceGradeDao scienceGradeDao;
    @Autowired
    private ScienceGrade scienceGrade;
    @Autowired
    private HistoryGradesDao historyGradesDao;
    @Autowired
    private HistoryGrade historyGrade;
    public void createStudent(String firstname, String lastname, String emailAddress) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
        student.setId(0);
        studentDao.save(student);
    }
    public boolean checkIfStudentISNull (int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        if (student.isPresent()) {
            return true;
        }
        return false;
    }
    public void deleteStudent(int id) {
        if(checkIfStudentISNull(id)) {
            studentDao.deleteById(id);

            mathGradeDao.deleteByStudentId(id);
            scienceGradeDao.deleteByStudentId(id);
            historyGradesDao.deleteByStudentId(id);
        }
    }
    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        return collegeStudents;
    }
    public boolean createGrade(double grade, int studentId, String gradeType) {

        if(!checkIfStudentISNull(studentId)){
            return false;
        }

        if(grade >= 0 && grade <= 100){
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
                return true;
            }
            if(gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDao.save(scienceGrade);
                return true;
            }
            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradesDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }
    public int deleteGrade(int id, String gradeType) {
        int studentId = 0;

        if(gradeType.equals("math")) {
            Optional<MathGrade> grade = mathGradeDao.findById(id);
            if(!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradeDao.deleteById(id);
        }
        if(gradeType.equals("science")) {
            Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
            if(!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradeDao.deleteById(id);
        }
        if(gradeType.equals("history")) {
            Optional<HistoryGrade> grade = historyGradesDao.findById(id);
            if(!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradesDao.deleteById(id);
        }
        return studentId;
    }

}
























