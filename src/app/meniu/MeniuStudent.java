package app.meniu;

import app.View;

public class MeniuStudent implements MeniuRol{

    private View view;

    public MeniuStudent(View view){

         this.view=view;
    }

    public void afiseazaOptiuni(){

        System.out.println("1-show books");
        System.out.println("2-delete book");
        System.out.println("3-add book");
        System.out.println("4-edit book");
        //show my enrolments -- findAllEnrollmentsByStudentId(String studentId)
        System.out.println("5-show my enrollments");
        System.out.println("6-show available courses");
        System.out.println("7-inscrie-te la un curs:");
        System.out.println("8-inscrie-te la un curs");
    }

    public boolean executa(int alegere){

         switch (alegere){
             case 1:
                 view.showStudentBooks();
                 break;
             case 2:
                 view.deleteBook();
                 break;
             case 3:
                 view.addBook();
                 break;
             case 4:
                 view.editBook();
                 break;
             case 5:
                 view.showCoursesByEnrollments();
                 break;
             case 6:
                 view.showAllCourses();
                 break;
             case 7:
                 view.removeCourse();
                 break;
             case 8:
                 view.registerForCourse();
             case 0:
                 return false;
             default:
                 System.out.println("invalid choice");
         }

         return true;
    }

}
