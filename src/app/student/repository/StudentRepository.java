package app.student.repository;

import app.student.model.Student;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class StudentRepository {

    private List<Student> students;

    public  StudentRepository(){
        this.students= new ArrayList<>();
        this.loadData();
        this.afisare();
    }

    public  void loadData(){

         try{
             Scanner scanner = new Scanner(new File("src/app/student/data/students.txt"));
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
            System.out.println(students.get(i).toText());
        }
    }


    public  void  add(Student student){
        student.setId(String.valueOf(UUID.randomUUID()));
        this.students.add(student);
        save();
    }
    private   void save(){

        try{
            PrintWriter printWriter= new PrintWriter("src/app/student/data/students.txt");
            String text="";
            int i=0;
            for(;i<students.size()-1;i++){
                text+=students.get(i).toText()+"\n";
            }
            text+=students.get(i).toText();
            printWriter.print(text);
            printWriter.close();

        }catch (Exception ex){

        }

    }
}
