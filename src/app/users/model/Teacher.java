package app.users.model;

import app.course.model.Course;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {

    private List<Course> coursesList = new ArrayList<>();
    protected String rol = "TEACHER";

    public Teacher(String[] campuri){
        super(campuri);

        //coursesList este assigned by default, la fel si rol
    }



    @Override
    public String getRol(){
        return rol;
    }

    public void setRol(String rol){
        this.rol = rol;
    }

    @Override
    public List<Course> getCoursesList(){
        return coursesList;
    }

    @Override
    public String toText(){
        return getRol()+ "," + super.toText();
    }

}
