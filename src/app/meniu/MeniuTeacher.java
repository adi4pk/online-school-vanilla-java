package app.meniu;

import app.View;

public class MeniuTeacher implements MeniuRol{

    private View view;


    public MeniuTeacher(View view){

        this.view = view;
    }

    public void afiseazaOptiuni(){

        System.out.println("OK, you are telling the truth! - TEACHER");
        System.out.println("1-show Courses");
        System.out.println("2 - MY STUDENTS");
        System.out.println("0 - go back");
    }

    public boolean executa(int alegere){

        switch (alegere){
            case 1:
                view.showCoursesByTeacher();
                break;
            case 2:
                view.showStudentsFromCourse();
                break;
            case 0:
                return false;
            default:
                System.out.println("invalid choice");
        }
        return true;

    }

}