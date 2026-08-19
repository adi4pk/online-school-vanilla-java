package app.book;

import app.student.model.Student;

import java.time.LocalDate;

public class Book {

    private String bookId;
    private String bookName;
    private LocalDate createdAt;

    private Student student;    //o carte poate avea doar un student

    public Book(String text){  //dam String in argument, functia il parseaza ca sa umple proprietatile cu tipul potrivit (e.g. String, LocalDate etc.)

        String[] arr = text.split(",");
        this.setBookId(arr[0]);
        this.setBookName(arr[1]);
        this.setCreated_at(LocalDate.parse(arr[2]));
    }

    public void assignToStudent(Student student){
        this.student = student;
    }

    public void setBookId(String bookId){
        this.bookId = bookId;
    }

    public String getBookId(){
        return bookId;
    }

    public void setBookName(String bookName){
        this.bookName = bookName;
    }

    public String getBookName(){
        return bookName;
    }

    public void setCreated_at(LocalDate createdAt){
        this.createdAt = createdAt;
    }

    public LocalDate getCreated_at(){
        return createdAt;
    }

    public String toText(){
        return this.bookId + "," +this.bookName + "," + this.createdAt;
    }



}
