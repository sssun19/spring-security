package com.sp.fc.controller;

import com.sp.fc.student.Student;
import com.sp.fc.student.StudentManager;
import com.sp.fc.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/teacher")
public class MobTeacherController {


    /**
     * @ Autowired
     * private StudentManager studentManager;

     * @ Autowired 로 바로 주입 받는 것 보다는 private final 로 선언해 생성자로 객체 주입 받는 방식을 더 지양한다.
    */

    private final StudentManager studentManager;

    public MobTeacherController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> students(@AuthenticationPrincipal Teacher teacher) {
        return studentManager.myStudents(teacher.getId());
    }


}
