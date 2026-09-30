package app.course.model;

import app.student.model.User;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseId;
    private String courseName;
    private String department;

    private String teacherId;

    private List<User> registeredUsers = new ArrayList<>();


    public Course(String text){

        String[] arr = text.split(",");
        this.setCourseId(arr[0]);
        this.setCourseName(arr[1]);
        this.setDepartment(arr[2]);
        this.setTeacherId(arr[3]);

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

    public void setTeacherId(String id){
        this.teacherId = id;
    }

    public String getTeacherId(){
        return teacherId;
    }



    public String toText(){
        return this.courseId+"," +this.courseName + "," + this.department;
    }


    @Override
    public String toString(){
        return this.courseId+"," +this.courseName + "," + this.department + "\n";
    }



}
