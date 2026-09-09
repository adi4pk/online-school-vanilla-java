package app.enrollment.repository;

import app.book.Book;
import app.enrollment.model.Enrollment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Optional;
import java.util.List;
import java.util.UUID;


public class EnrollmentRepository {

    private static final String FILE_PATH = "src/app/enrollment/data/enrollments.txt";

    private final List<Enrollment> enrollments = new ArrayList<>();

    public EnrollmentRepository(){

        loadData();
    }

//    public Enrollment addEnrollment(String studentId, String courseId, LocalDate createdAt){
//
////        Enrollment enrollment = new Enrollment();
//    };

    public Enrollment addEnrollment(Enrollment enrollment){
        if(this.checkEnrollmentByStudentId(enrollment.getStudentId(), enrollment.getCourseId())){
            throw new IllegalArgumentException("You are already registered for this course.");
        }
        enrollments.add(enrollment);
        save();
        return enrollment;
    }


    public void deleteById(String studentId, String courseId) {
        int index = indexOf(studentId, courseId);
        if (index == -1) {
            throw new IllegalArgumentException("Enrollment not found: " + studentId +", " + courseId);
        }
        enrollments.remove(index);
        save();
    }

    private int indexOf(String studentId, String courseId) {
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getStudentId().equals(studentId) && enrollments.get(i).getCourseId().equals(courseId)) {
                return i;   //-> index-ul lui enrollments in enrollments arr
            }
        }
        return -1;
    }


    public List<Enrollment> findAllEnrollments(){
        return new ArrayList<>(enrollments);
    }

    public  List<Enrollment> findAllEnrollmentsByStudentId(String id){

        List<Enrollment> studentEnrollmentsArr = new ArrayList<>();

        for (Enrollment e : enrollments){
            if (e.getStudentId().equalsIgnoreCase(id)){
                studentEnrollmentsArr.add(e);
            }
        }

        return studentEnrollmentsArr;
    }

    public Optional<Enrollment> findByCourseId(String courseId){
        for (Enrollment e : enrollments){
            if (e.getCourseId().equals(courseId)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Optional<Enrollment> findByStudentIdAndCourseId(String studentId, String courseId){
        for(Enrollment e: enrollments){
            if (e.getStudentId().equals(studentId) && e.getCourseId().equals(courseId)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public boolean existsByCourseId(String courseId){
        return findByCourseId(courseId).isPresent();
    }

    public int countEnrollments(){
        return enrollments.size();
    }

    public List<Enrollment> findEnrollmentsByStudentId(String studentId){
        List<Enrollment> studentEnrollments = new ArrayList<>();

        for(Enrollment e : enrollments){
            if (e.getStudentId().equals(studentId)){
                studentEnrollments.add(e);
            }
        }

        return studentEnrollments;

    }

    public int indexOfEnrollmentById(String id){
        for (int i=0; i<enrollments.size();i++){
            if (enrollments.get(i).getStudentId().equals(id)){
                return i;
            }
        }
        return -1;
    }


    private void loadData(){
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return;
        }

        try(Scanner scanner = new Scanner(file)){
            int lineNumber = 0;
            while (scanner.hasNextLine()){
                lineNumber++;
                String line = scanner.nextLine().trim();
                if(line.isEmpty()){
                    continue;
                }
                try {
                    enrollments.add(new Enrollment(line));
                } catch (RuntimeException ex){
                    throw new IllegalArgumentException("Invalid enrollment at line: " + lineNumber + ": " +line);
                }
            }
        } catch (FileNotFoundException ex){
            throw new IllegalArgumentException("Cannot read file: " + file.getAbsolutePath(), ex);
        }
    }

    public boolean checkEnrollmentByStudentId(String studentId, String courseId){
        for(Enrollment enrollment : enrollments){
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)){

                return true;
            }


        }
        return false;
    }


    private void save(){
        StringBuilder content = new StringBuilder();
        for (Enrollment enrollment: enrollments){
            content.append(enrollment.toText()).append(System.lineSeparator());
        }

        try(PrintWriter writer = new PrintWriter(FILE_PATH)){
            writer.print(content);
        } catch (FileNotFoundException ex){
            throw new IllegalArgumentException("Cannot write file: " + FILE_PATH, ex);
        }
    }




}
