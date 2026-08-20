package app.book;

import app.student.model.Student;

import java.time.LocalDate;

public class Book {

    private String id;
    private String bookName;
    private LocalDate createdAt;
    private String stundentId ;//o carte poate avea doar un student

    public Book(String text){  //dam String in argument, functia il parseaza ca sa umple proprietatile cu tipul potrivit (e.g. String, LocalDate etc.)

        String[] arr = text.split(",");

        this.setId(arr[0]);
        this.setBookName(arr[1]);
        this.setCreated_at(LocalDate.parse(arr[2]));
        this.setStundentId(arr[3]);
    }


    public  Book(){

    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
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

    public void setStundentId(String studentId){
        this.stundentId = studentId;
    }

    public String getStundentId(){
        return stundentId;
    }

    public String toText(){
        return this.id + "," +this.bookName + "," + this.createdAt + "," +this.stundentId;
    }



}
