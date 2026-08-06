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

    public Optional<Student> findById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    public Optional<Student> findByEmail(String email) {
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
        student.setId(UUID.randomUUID().toString());
        students.add(student);
        save();
        return student;
    }

    public Student update(Student student) {
        int index = indexOf(student.getId());
        if (index == -1) {
            throw new IllegalArgumentException("Student not found: " + student.getId());
        }
        Optional<Student> byEmail = findByEmail(student.getEmail());
        if (byEmail.isPresent() && !byEmail.get().getId().equals(student.getId())) {
            throw new IllegalArgumentException("Email already used: " + student.getEmail());
        }
        students.set(index, student);
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

    private int indexOf(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    students.add(new Student(line));
                } catch (RuntimeException ex) {
                    throw new IllegalStateException("Invalid student at line " + lineNumber + ": " + line, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException("Cannot read file: " + file.getAbsolutePath(), ex);
        }
    }

    private void save() {
        StringBuilder content = new StringBuilder();
        for (Student student : students) {
            content.append(student.toText()).append(System.lineSeparator());
        }
        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {
            writer.print(content);
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException("Cannot write file: " + FILE_PATH, ex);
        }
    }
}
