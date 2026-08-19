package app.student.model;
//todo:validatori

import app.book.Book;

import java.util.ArrayList;
import java.util.List;

//constructor care ia ca parametru "a5e-12-32,bogdan,test,test@ceva,pass,12"
public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;

    private List<Book> arrBooks = new ArrayList<>();
    private CardStudent card;

    public Student(String text){
        String[] arr = text.split(",");         //split the String argument in the constructor
        this.setId(arr[0]);
        this.setFirstName(arr[1]);
        this.setLastName(arr[2]);
        this.setEmail(arr[3]);
        this.setPassword(arr[4]);
        this.setAge(Integer.parseInt(arr[5]));          //parse the String to an Integer

//        CardStudent ownCard = new CardStudent();
//        setCard(ownCard);

    }

    public void setCard(CardStudent card){
        this.card = card;
        card.setStudent(this);
    }

    public void cumparaCarte(Book book){
        arrBooks.add(book);   //legatura Student -> to Book
        book.assignToStudent(this); //creeaza legatura Book -> to Student
    }

    public void aruncaCarte(Book book){
        arrBooks.remove(book);
        book.assignToStudent(null);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String toText(){     //printam Obiectul intreg sub forma unui string -> il folosim in save()
        return this.id+","+this.firstName+","+this.lastName+","+this.email+","+this.password+","+this.age;
    }

    int x = 0;
    String text = "";
    Integer y = 0;

    ArrayList<Integer> arr = new ArrayList<>();
}
