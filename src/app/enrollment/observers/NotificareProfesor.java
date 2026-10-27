package app.enrollment.observers;

import app.course.model.Course;
import app.course.repository.CourseRepository;
import app.enrollment.model.Enrollment;
import app.users.model.Teacher;
import app.users.model.User;
import app.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificareProfesor implements ObservatorInscriere{

    //properties
    private CourseRepository courseRepo;
    private UserRepository userRepo;
    ;

    public NotificareProfesor(CourseRepository courseRepo, UserRepository userRepo){
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;


    }




    @Override
    public void laInscriere(Enrollment enrollment){

        scrie(enrollment, "inscris");
    }

    @Override
    public void laRenuntare(Enrollment enrollment){
        scrie(enrollment, "retras");
    }

    public void scrie(Enrollment enrollment, String actiune){

        String courseId = enrollment.getCourseId();
        Optional<Course> optionalCourse = courseRepo.findById(courseId);
        if (optionalCourse.isPresent()){
            Course curs = optionalCourse.get();
            String teacherId = curs.getTeacherId();

            Optional<User> profesor = userRepo.findById(teacherId);
            profesor.ifPresent(
                    prof -> System.out.println("Prof. " + ((Teacher) prof).getLastName() + " " +
                            ((Teacher) prof).getFirstName() + ": un student nou la cursul: " + curs.getCourseName() + " " + actiune));
        }


    }
}
