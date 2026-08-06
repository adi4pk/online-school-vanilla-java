package app;


import app.student.model.Student;

public class Main {
    public static void main(String[] args) {


        Student s1 = new Student("a5e-12-32,bogdan,test,test@ceva,pass,12");
        Student s2 = new Student("a5e-99-99,Andrei,Popescu,test@test.com,passTest,20");














        System.out.println(s1.toText());

    }

}