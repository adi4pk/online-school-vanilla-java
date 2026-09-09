package app.enrollment.model;

import app.enrollment.repository.EnrollmentRepository;

import java.time.LocalDate;

public class Enrollment {
    private String studentId;
    private String courseId;
    private LocalDate createdAt;


    public  Enrollment(String studentId, String courseId, LocalDate createdAt){
        this.setStudentId(studentId);
        this.setCourseId(courseId);
        this.setCreatedAt(createdAt);
    }


    public Enrollment(String text){
        String[] arr = text.split(",");
        this.setStudentId(arr[0]);
        this.setCourseId(arr[1]);
        this.setCreatedAt(LocalDate.parse(arr[2]));
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId){
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId){
        this.courseId = courseId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt){
        this.createdAt = createdAt;
    }

    public String toText(){
        return this.studentId + "," + this.courseId +"," + this.createdAt;
    }
}


