package app.student.repository;

import app.student.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentRepository {

    private List<Student> students;

    public  StudentRepository(){
        this.students= new ArrayList<>();
        this.loadData();
        this.afisare();
    }

    public  void loadData(){
        Scanner scanner = new Scanner("src/app/student/data/students.txt");
         try{

             while (scanner.hasNextLine()){
                 String text=scanner.nextLine();
                 this.students.add(new Student(text));
             }

         }catch (Exception ex){
            ex.printStackTrace();
         }
    }

    public  void afisare(){
        for (int i=0; i<students.size(); i++){
            students.get(i).toText();
        }
    }


}
