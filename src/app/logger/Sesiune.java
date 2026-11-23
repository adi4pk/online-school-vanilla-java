package app.logger;

import app.users.model.User;

public class Sesiune {

    private static Sesiune instance;
    private  User utilizator;

    //constructorul este privat
    private Sesiune(){

    }

    public static Sesiune getInstance(){

        if (instance == null){
            instance = new Sesiune();
        }

        return instance;
    }


    public void login(User user){           //nu returneaza User, doar il salveaza in prop
        this.utilizator = user;
    }

    public void logout(){
        this.utilizator = null;
        System.out.println("Te-ai delogat.");
    }

    public User getUtilizator(){
        return utilizator;
    }


    public void log(String message){
        System.out.println("User logged in.");
    }
}
