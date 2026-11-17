package app.users.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class IncarcatorUsers {

    private final List<FabricaUser> fabrici;


    public IncarcatorUsers(List<FabricaUser> fabrici){
        if (fabrici == null || fabrici.isEmpty()){
            throw new IllegalArgumentException("NU avem nicio fabrica creata - lista de fabrici este goala");
        }

        this.fabrici = fabrici;
    }

    public List<User> incarca(List<String> liniiText){

        List<User> arrUsers = new ArrayList<>();

        for (String linie:liniiText){
            boolean gasit = false;
            String userType = "";
            String[] campuri = linie.split(",");

            for (FabricaUser fab:fabrici){
                userType = campuri[0];

                if (fab.getTip().equals(campuri[0])){
                    User user = fab.creeaza(campuri);
                    arrUsers.add(user);
                    gasit = true;
                }
            }

            if (!gasit){
                throw new IllegalArgumentException("Unknown user type: " + userType);
            }
        }

        return arrUsers;

    }
}
