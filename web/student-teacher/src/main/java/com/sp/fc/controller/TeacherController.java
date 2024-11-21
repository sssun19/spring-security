package com.sp.fc.controller;

import com.sp.fc.student.StudentManager;
import com.sp.fc.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/teacher")
public class TeacherController {


    /**
     * @Autowired
     * private StudentManager studentManager;
     *
     * @Autowired 로 바로 주입 받는 것 보다는 private final 로 선언해 생성자로 객체 주입 받는 방식을 더 지양한다.
    */

    private final StudentManager studentManager;

    public TeacherController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model) {
        model.addAttribute("studentList", studentManager.myStudents(teacher.getId()));
        return "TeacherMain";
    }


}
