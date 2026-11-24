package app.course.model;

import app.users.model.Student;
import app.users.model.User;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseId;
    private static int idCounter = 0;

    private String courseName;
    private String department;
    private int capacitate;


    private String teacherId;

    private List<User> registeredUsers;


    private static void syncIdCounter(String courseId){
        String numberPart = courseId.substring(1);
        int idNumber = Integer.parseInt(numberPart);

        if (idNumber > idCounter){
            idNumber = idCounter;
        }
    }

    private static String generateId() {
        idCounter++;
        return String.format("C%03d", idCounter);
    }

    public Course(String text){

        String[] arr = text.split(",");
        this.setCourseId(arr[0]);
        this.setCourseName(arr[1]);
        this.setDepartment(arr[2]);
        if (arr.length > 3) {
            setTeacherId(arr[3]);
        }
        if (arr.length > 4) {
            setCapacitate(Integer.parseInt(arr[4]));
        }

    }

    public Course(CourseBuilder builder){
        // set props using builder props
    }



    public void addStudentsToCourse(Student student){
        registeredUsers.add(student);
    }

    public void removeStudentFromCourse(Student student){
        registeredUsers.remove(student);
    }

    public void setCourseId(String courseId){
        this.courseId = courseId;
    }

    public String getCourseId(){
        return courseId;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public String getCourseName(){
        return courseName;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public String getDepartment(){
        return department;
    }

    public int getCapacitate(){
        return capacitate;
    }

    public void setCapacitate(int capacitate){
        this.capacitate = capacitate;
    }

    public void setTeacherId(String id){
        this.teacherId = id;
    }

    public String getTeacherId(){
        return teacherId;
    }



    public String toText(){
        return this.courseId+"," +this.courseName + "," + this.department+"," + this.getTeacherId();
    }


    @Override
    public String toString(){
        return this.courseId+"," +this.courseName + "," + this.department + "\n";
    }


    public static class CourseBuilder{
        //private props
        private String courseId;
        private String courseName;
        private String department;
        private Integer capacitate;

        private String teacherId;

        private List<User> registeredUsers;


        // CONSTRUCTOR  -- load() din fisier .txt
        public CourseBuilder(String text){
            String[] arr = text.split(",");
            setCourseId(arr[0]);        /// păstrează ID-ul existent
            syncIdCounter(arr[0]);      /// sincronizează contorul

            setCourseName(arr[1]);
            setDepartment(arr[2]);
            if (arr.length > 3) {
                setTeacherId(arr[3]);
            }
            if (arr.length > 4) {
                setCapacitate(Integer.parseInt(arr[4]));
            }
            setRegisteredUsersArr();
        }

        // CONSTRUCTOR -- new Course
        public CourseBuilder(){

        }



        //methods-setters

        public CourseBuilder setCourseId(String id){    //// Curs încărcat din fișier cu load() → păstrează ID-ul
            this.courseId = id;
            return this;
        }

        public CourseBuilder setCourseId(){     //// Curs NOU - addCourse din TEACHER menu → generează ID
            this.courseId = generateId();
            return this;
        }

        public CourseBuilder setCourseName(String name){
            this.courseName = name;
            return this;
        }

        public CourseBuilder setDepartment(String department){
            this.department = department;
            return this;
        }

        public CourseBuilder setCapacitate(Integer capacitate){
            this.capacitate = capacitate;
            return this;
        }

        public CourseBuilder setTeacherId(String teacherId){
            this.teacherId = teacherId;
            return this;
        }

        public CourseBuilder setRegisteredUsersArr(){
            this.registeredUsers = new ArrayList<>();
            return this;
        }

        public Course build(){
            if (courseName == null){
                throw new IllegalStateException("Numele cursului este obligatoriu.");
            }
            if (capacitate !=null && capacitate <= 0){
                throw new IllegalStateException("Capacitatea nu poate fi mai mica de 1.");
            }
            return new Course(this);
        }
    }


}
