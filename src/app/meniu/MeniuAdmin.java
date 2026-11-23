package app.meniu;

import app.View;
import app.logger.Sesiune;

public class MeniuAdmin implements MeniuRol{

    private View view;

    public MeniuAdmin(View view){

        this.view = view;

    }


    @Override
    public void afiseazaOptiuni(){

        System.out.println("1-show all users");
        System.out.println("2 - delete User");
        System.out.println("3-Log out");

        System.out.println("0 - go back");
    }

    public boolean executa(int alegere){


        switch (alegere){
            case 1:
                view.showUsers();
                break;
            case 2:
                view.deleteUser();
                break;
            case 3:
                Sesiune.getInstance().logout();
                return false;
            case 0:
                return false;
            default:
                System.out.println("invalid choice");
        }

        return true;
    }
}
