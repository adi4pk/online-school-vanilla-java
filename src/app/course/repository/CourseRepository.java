package app.course.repository;

import app.course.model.Course;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class CourseRepository {

    private static final String FILE_PATH = "src/app/course/data/courses.txt";

    private final List<Course> courses = new ArrayList<>();

    public CourseRepository(){


    }

    public List<Course> findAllCourses(){
        return new ArrayList<>(courses);
    }

    public Optional<Course> findById(String id){
        for (Course course : courses){
            if(course.getCourseId().equals(id)){
                return Optional.of(course);
            }
        }
        return Optional.empty();
    }

    public Optional<Course> findByCourseName(String courseName){
        for (Course course : courses){
            if (course.getCourseName().equalsIgnoreCase(courseName)){
                return Optional.of(course);
            }
        }
        return Optional.empty();
    }

    public boolean existsByCourseName(String courseName){
        return findByCourseName(courseName).isPresent();
    }

    public int countCourses(){
        return courses.size();
    }

    public int indexOfCourse(String id){
        for (int i=0; i< courses.size(); i++){
            if (courses.get(i).getCourseId().equals(id)){
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
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if(line.isEmpty()){
                    continue;
                }
                try{
                    courses.add(new Course(line));
                }catch (RuntimeException ex){
                    throw new IllegalStateException("Invalid course at line " + lineNumber + ": " +line, ex);
                }
            }
        } catch (FileNotFoundException ex){
            throw new IllegalStateException("Cannot read file " +file.getAbsolutePath(), ex);
        }
    }

    private void save(){
        StringBuilder content = new StringBuilder();
        for(Course course : courses){
            content.append(course.toText()).append(System.lineSeparator());

        }
        try(PrintWriter writer = new PrintWriter(FILE_PATH)){
            writer.print(content);
        } catch (FileNotFoundException ex){
            throw new IllegalStateException("Cannot write file: " + FILE_PATH, ex);
        }
    }

}
