package app.student.model;

public class Student extends User {

    protected String rol = "STUDENT";


    public Student(String text){
        super(text);
    }

    @Override
    public String getRol(){
        return rol;
    }

    public void setRol(String rol){
        this.rol = rol;
    }

    @Override
    public String toText(){
        return getRol() + super.toText();
    }
}
