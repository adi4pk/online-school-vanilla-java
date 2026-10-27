package app.enrollment.observers;

import app.course.model.Course;
import app.course.repository.CourseRepository;
import app.enrollment.model.Enrollment;
import app.users.repository.UserRepository;

import java.time.LocalDate;

public class JurnalInscrieri implements ObservatorInscriere {


    private CourseRepository courseRepo;
    private UserRepository userRepository;

    public JurnalInscrieri(CourseRepository courseRepo, UserRepository userRepo){
        this.courseRepo = courseRepo;
        this.userRepository = userRepo;
    }


    @Override
    public void laInscriere(Enrollment enrollment){

         scrie(enrollment,"inscriere");

    }

    @Override
    public void laRenuntare(Enrollment enrollment){
        scrie(enrollment,"renuntare");

    }


    private void scrie(Enrollment enrollment,String actiune){
        System.out.println(LocalDate.now() + " " + enrollment.getStudentId() + " -> " + enrollment.getCourseId() + " "+actiune);
    }



}
