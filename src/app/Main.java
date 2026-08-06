package app;


import app.student.model.Student;
import app.student.repository.StudentRepository;

public class Main {
    public static void main(String[] args) {

        StudentRepository studentRepository = new StudentRepository();

        Student student = new Student("t8v-16-92,Gabriela,Rusu2,gabriela2.rusu@example.com,Gamma987,27");

        studentRepository.add(student);




    }

}