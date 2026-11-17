package app.users.model;

public class Student extends User {

    protected String rol = "STUDENT";


    public Student(String[] campuri){
//        super(row); X
        super(campuri);

        //this.rol este assigned by default
        //pastram setter in caz de schimbare tip user
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
        return getRol() + "," + super.toText();
    }
}
