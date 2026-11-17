package app;


import app.course.model.Course;
import app.course.repository.CourseRepository;
import app.enrollment.model.Enrollment;
import app.enrollment.repository.EnrollmentRepository;
import app.users.model.Student;
import app.users.model.Teacher;
import app.users.model.User;
import app.users.repository.UserRepository;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {



//
//        EnrollmentRepository enrollRepo = new EnrollmentRepository();
//        System.out.println(enrollRepo.findAllEnrollments());


        View view = new View();
//        User user = new Student("c8f-45-71,Marculescu,Alexandru,Marculescu.Alexandru@example.com,Secret456,24");

        Course curs1 = new Course("C011,Macroeconomie,Marketing,9000fd14-6373-4854-b98f-d8298379b95c");

        Enrollment enrollment1 = new Enrollment(user.getId(), curs1.getCourseId(), LocalDate.now());

        UserRepository userRepo = new UserRepository();
//        System.out.println(userRepo.findAll().toString());

//        for (User u: userRepo.findAll()){
//            System.out.println(u.toText());
//        }

        CourseRepository courseRepo = view.getCourseRepo();
        courseRepo.add(curs1);
        courseRepo.assignCourseToTeacher(curs1, ((Teacher)view.getViewUser()));

    }

}