package app.users.model;

public class FabricaAdmin implements FabricaUser{

    @Override
    public String getTip(){
        return "ADMIN";
    }


    @Override
    public User creeaza(String[] campuri) {

        User user = null;       //declaram initial null ca sa putem returna in cazul in care !campuri[0].equals(getRol());
        if (campuri[0].equals(getTip())){
            user = new Admin(campuri);
        }

        return user;
    }

}
