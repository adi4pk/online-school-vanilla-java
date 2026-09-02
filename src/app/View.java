package app;

import app.book.Book;
import app.book.repository.BookRepository;
import app.student.model.Student;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class View {

    private Student student;
    private Scanner scanner;
    private BookRepository bookRepository;

    public View(){
        this.student= new Student("a5e-12-32,Bogdan,Popescu,bogdan.popescu@example.com,Pass123,22");
        this.scanner= new Scanner(System.in);       //este un obiect care știe să citească caractere dintr-o sursă.
        this.bookRepository = new BookRepository();
        this.play();


    }


    public void play(){     //ruleaza din Constructor()

        int alegere=-1;
        while (alegere != 0) {

            meniu();
            alegere=Integer.parseInt(scanner.nextLine());  //threadul principal este blocat și așteaptă -->  //e.g. 0 / 1 / 2 etc...
            switch (alegere){
                case 1:
                    showStudentBooks();
                    break;
                case 2:
                    deleteBook();
                    break;
                case 3:
                    addBook();
                    break;
                case 4:
                    editBook();
                    break;  //MAKE SURE YOU use break, otherwise the code block keeps going down to case 0;
                case 0:
                    System.out.println("Bye");
                    break;
                    default:
                        System.out.println("invalid choice");
            }
        }

    }




    public  void meniu(){

        System.out.println("1-show books");
        System.out.println("2-delete book");
        System.out.println("3-add book");
        System.out.println("4-edit book");
    }

    public  void showStudentBooks(){


        List<Book> bookList= bookRepository.findBooksByStudentId(student.getId());

        for(Book b :bookList){

            System.out.println(b.toText());
        }


    }

    public void addBook(){
        System.out.println("Enter the book name");
        String bookName = scanner.nextLine();// --> .nextLine() returns a String

        try{

            bookRepository.addBook(bookName,student.getId());
            System.out.println("book added");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void editBook(){
        System.out.println("Enter the book name you would like to edit: ");
        String oldName=scanner.nextLine();
        System.out.println("Enter the new name of the book: ");
        String newName = scanner.nextLine();

        try{
            bookRepository.updateBookName(oldName,newName,student.getId());
        }catch (Exception ex){
            ex.printStackTrace();
        }





        //System.out.println("Book name changed to" + bookName);
    }


    public void deleteBook(){
        System.out.println("Enter the book name");
        String bookName=scanner.nextLine();
        boolean erased=bookRepository.deleteByBookName(bookName);
        if(erased){
            System.out.println("success");
        }else{

            System.out.println("The book doesnt exist");
        }
    }



}
