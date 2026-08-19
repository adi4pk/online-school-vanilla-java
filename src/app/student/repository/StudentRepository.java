package app.student.repository;

import app.student.model.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class StudentRepository {

    private static final String FILE_PATH = "src/app/student/data/students.txt";

    private final List<Student> students = new ArrayList<>();

    public StudentRepository() {
        loadData();
    }

    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    public Optional<Student> findById(String id) {      //returneaza Student sau optional empty
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return Optional.of(student);
            }
        }       //daca bucla nu gaseste nimic -> merge mai departe la return .empty();
        return Optional.empty();
    }

    public Optional<Student> findByEmail(String email) {        //returneaza Student sau optional empty
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(email)) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public int count() {
        return students.size();
    }

    public Student add(Student student) {
        if (existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already used: " + student.getEmail());
        }
        student.setId(UUID.randomUUID().toString());        //????
        students.add(student);
        save();
        return student;
    }

    public Student update(Student student) {
        int index = indexOf(student.getId());   //studentul pe care îl modifici -- return index from .getId()
        if (index == -1) {
            throw new IllegalArgumentException("Student not found: " + student.getId());
        }
        Optional<Student> byEmail = findByEmail(student.getEmail());    //arg String email, returns Student student
        if (byEmail.isPresent() && !byEmail.get().getId().equals(student.getId())) {
        //studentul găsit în repository după email -- return id from email

            throw new IllegalArgumentException("Email already used: " + student.getEmail());
        }
        students.set(index, student);  //int index, E element -- inlocuieste studentul de la indexul x cu studentul modificat.
        save();
        return student;
    }

    public void deleteById(String id) {
        int index = indexOf(id);
        if (index == -1) {
            throw new IllegalArgumentException("Student not found: " + id);
        }
        students.remove(index);
        save();
    }

    private int indexOf(String id) {        //returneaza un int care e egal cu pozitia lui Student in students arr
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                //getId() al studentului si verifica daca == cu id din argument -> if true return int pozitia
                return i;   //-> index-ul lui Student in students arr
            }
        }
        return -1;
    }

    private void loadData() {
        File file = new File(FILE_PATH);        // de ce cream obiectul aici si nu in proprietati? il recream de fiecare data cand facem load()
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {     //Mai există o linie? if true -> continua | if false -> stop loop
                lineNumber++;   //incrementam direct lineNumber la 1 -> pentru ca in documente, numerotarea liniilor incepe de la 1.
                String line = scanner.nextLine().trim();   //citește următoarea linie. -- returneaza un String by default
                if (line.isEmpty()) {
                    continue;      //--> sari peste iterația curentă -- dar continuă bucla
                }
                try {
                    students.add(new Student(line));
                } catch (RuntimeException ex) {     //NumberFormatException -- in cazul in care dam um String | e.g. age = "x" - eroare la parsare().
                    throw new IllegalStateException("Invalid student at line " + lineNumber + ": " + line, ex);

                    //Dacă apare o excepție de tip RuntimeException (sau o subclasă a ei),
                    //pune obiectul excepției în variabila ex și execută blocul catch.
                }
            }
        } catch (FileNotFoundException ex) {        //type -> FileNotFoundException, variable -> ex
            throw new IllegalStateException("Cannot read file: " + file.getAbsolutePath(), ex);  //String mesaj, Throwable ex --> variable ex becomes the cause
        }
    }

    private void save() {     //OVERWRITE function
        StringBuilder content = new StringBuilder();
        for (Student student : students) {
            content.append(student.toText()).append(System.lineSeparator());        //"\n" append student as String + separate line by line "\n"
        }
        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {     //ia ca argument un obiect de tip File sau String Filename
            writer.print(content);
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException("Cannot write file: " + FILE_PATH, ex);
        }
    }
}




//notes
//În loadData(), continue este folosit doar ca să ignore liniile goale din fișier
//și să nu încerce să construiască un obiect Student dintr-un șir gol.

//scanner-ul începe înainte de prima linie
// și trebuie să-i dai un nextLine() ca să consume și să returneze prima linie.