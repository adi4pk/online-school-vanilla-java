package app.enrollment.repository;

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

    }

    public List<Enrollment> findAllEnrollments(){
        return new ArrayList<>(enrollments);
    }

    public Optional<Enrollment> findByStudentId(String id){
        for (Enrollment e : enrollments){
            if (e.getStudentId().equalsIgnoreCase(id)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Optional<Enrollment> findByCourseId(String courseId){
        for (Enrollment e : enrollments){
            if (e.getCourseId().equals(courseId)){
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


}
