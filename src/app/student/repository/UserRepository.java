package app.student.repository;

import app.student.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import app.student.model.Teacher;

public class UserRepository {

    private static final String FILE_PATH = "src/app/student/data/users.txt";

    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        loadData();
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findById(String id) {      //returneaza Student sau optional empty
        for (User user : users) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }       //daca bucla nu gaseste nimic -> merge mai departe la return .empty();
        return Optional.empty();
    }

    public Optional<User> findUserTypeById(String id){
        for (User user: users){
            if (user instanceof Teacher && user.getId().equals(id)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }



    public Optional<User> findByEmail(String email) {        //returneaza Student sau optional empty
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public int count() {
        return users.size();
    }

    public User add(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already used: " + user.getEmail());
        }
        user.setId(UUID.randomUUID().toString());        //????
        users.add(user);
        save();
        return user;
    }

    public User update(User user) {
        int index = indexOf(user.getId());   //studentul pe care îl modifici -- return index from .getId()
        if (index == -1) {
            throw new IllegalArgumentException("Student not found: " + user.getId());
        }
        Optional<User> byEmail = findByEmail(user.getEmail());    //arg String email, returns Student student
        if (byEmail.isPresent() && !byEmail.get().getId().equals(user.getId())) {
        //studentul găsit în repository după email -- return id from email

            throw new IllegalArgumentException("Email already used: " + user.getEmail());
        }
        users.set(index, user);  //int index, E element -- inlocuieste studentul de la indexul x cu studentul modificat.
        save();
        return user;
    }

    public void deleteById(String id) {
        int index = indexOf(id);
        if (index == -1) {
            throw new IllegalArgumentException("Student not found: " + id);
        }
        users.remove(index);
        save();
    }

    private int indexOf(String id) {        //returneaza un int care e egal cu pozitia lui Student in students arr
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
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
                    users.add(new User(line));
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
        for (User user : users) {
            content.append(user.toText()).append(System.lineSeparator());        //"\n" append student as String + separate line by line "\n"
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