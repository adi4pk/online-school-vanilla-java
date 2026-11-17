package app.users.model;

public class FabricaProfesor implements FabricaUser{

    @Override
    public String getTip() {
        return "TEACHER";
    }

    @Override
    public User creeaza(String[] campuri) {

        User user = null;       //declaram initial null ca sa putem returna in cazul in care !campuri[0].equals(getRol());
        if (campuri[0].equals(getTip())){
            user = new Teacher(campuri);
        }

        return user;
    }

}
