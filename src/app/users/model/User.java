package app.users.model;
//todo:validatori

import app.course.model.Course;

import java.util.List;

//constructor care ia ca parametru "a5e-12-32,bogdan,test,test@showStudentsFromCourse,pass,12"
public abstract class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;
    protected String rol = "USER";

//    public User(String text){
//        String[] arr = text.split(",");         //split the String argument in the constructor
//        this.setId(arr[0]);
//        this.setFirstName(arr[1]);
//        this.setLastName(arr[2]);
//        this.setEmail(arr[3]);
//        this.setPassword(arr[4]);
//        this.setAge(Integer.parseInt(arr[5]));          //parse the String to an Integer
//
//
//    }

    protected User (String[] campuri){

//        String[] campuri = row.split(","); X

        if (campuri[1] == null || campuri[1].isBlank()){        //id
            throw  new IllegalArgumentException("ID cannot be null");
        }
        if (campuri[2] == null || campuri[2].isBlank()){        //first name
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (campuri[3] == null || campuri[3].isBlank()){        //last name
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.setId(campuri[1]);
        this.setFirstName(campuri[2]);
        this.setLastName(campuri[3]);
        this.setEmail(campuri[4]);
        this.setPassword(campuri[5]);
        this.setAge(Integer.parseInt(campuri[6]));


    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRol() {
        return rol;
    }


    ////??????
    public List<Course> getCoursesList(){
        return null;
    }

    public String toText(){     //printam Obiectul intreg sub forma unui string -> il folosim in save()
        return this.id+","+this.firstName+","+this.lastName+","+this.email+","+this.password+","+this.age;
    }


}
