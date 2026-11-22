package app.users.model;

public class Admin extends User{


    protected String rol = "ADMIN";


    public Admin(String[] campuri){
        super(campuri);

        //
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
