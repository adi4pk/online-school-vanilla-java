package app.book.repository;


import app.book.Book;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public class BookRepository {

    private static final String FILE_PATH = "src/app/book/data/books.txt";

    private final List<Book> books = new ArrayList<>();

    public BookRepository(){
        loadData();
    }

    public List<Book> findAllBooks(){   //returns a shallow copy of books
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(String id){   //returneaza Obiect
        for(Book book : books){
            if (book.getId().equals(id)){
                return Optional.of(book);
            }
        }

        return Optional.empty();
    }

    public Optional<Book> findByBookName(String bookName){
        for (Book book : books){
            if (book.getBookName().equalsIgnoreCase(bookName)){
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    public boolean existsByBookName(String bookName){
        return findByBookName(bookName).isPresent();
    }

    public int countBooks(){
        return books.size();
    }


    public List<Book> findBooksByStudentId(String studentId){

        List<Book> studentBooks = new ArrayList<>();

        for (Book book : books){
            if (book.getStundentId().equals(studentId)){
                studentBooks.add(book);
            }
        }
        return studentBooks;
    }


    public int indexOfBook(String id){
        for (int i=0; i<books.size(); i++){
            if (books.get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }

    public Book addBook(String  name,String studentId){
        Book book = new Book();
        book.setStundentId(studentId);
        book.setBookName(name);
        //repository.add() book to repository
        if(existsByBookName(book.getBookName())){
            throw new IllegalArgumentException("Book is already in the store.");
        }
        book.setCreated_at(LocalDate.now());
        book.setId(UUID.randomUUID().toString());



        books.add(book);


        ////////SAVE
        save();
        return book;
    }



    public Book updateBook(Book book){      //update repository();
        int index = indexOfBook(book.getId());
        if (index == -1){
            throw new IllegalArgumentException("Book not found: " +book.getId());
        }

        Optional<Book> byBookName = findByBookName(book.getBookName());     //--> returneaza un obiect in baza numelui cartii
        //daca cartea este gasita si id-ul ei nu este acelasi cu book.getBookId() --> eroare;
        if(byBookName.isPresent() && !byBookName.get().getId().equals(book.getId())){
            throw new IllegalArgumentException("Book name is already used: " + book.getBookName());
            //daca id-urile nu se potrivesc --> update fails --> numele cartii e deja la alt obiect -> NOT unique.
        }

        books.set(index, book);
        save();
        return book;
    }


    // Book b1 = new obj("abcd");        || Book b2 = new obj("xyz");
// b1.setBookName("xyz");
// -- daca "abcd" exista deja la alta carte - gasit cu findByBookName(Book book)
//.update -> eroare
//

    public void deleteByBookId(String id){
        int index = indexOfBook(id);
        if(index == -1){
            throw new IllegalArgumentException("Book not found");
        }
        books.remove(index);
        save();
    }

    public boolean deleteByBookName(String bookName){

        Optional<Book> book =findByBookName(bookName);
        if(book.isPresent()){
            this.books.remove(book.get());
            this.save();
            return  true;
        }
        return  false;
    }


    private void loadData(){
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return;
        }

        try(Scanner scanner = new Scanner(file)){
            int lineNumber = 0;
            while (scanner.hasNextLine()){
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()){
                    continue;
                }
                try{
                    books.add(new Book(line));
                } catch (RuntimeException ex){
                    throw new IllegalArgumentException("Invalid book at line: " + lineNumber + ": " +line, ex);
                }
            }
        } catch (FileNotFoundException ex){
            throw new IllegalArgumentException("Cannot read file: " + file.getAbsolutePath(), ex);
        }
    }

    private void save(){
        StringBuilder content = new StringBuilder();
        for (Book book: books){
            content.append(book.toText()).append(System.lineSeparator());
        }

        try(PrintWriter writer = new PrintWriter(FILE_PATH)){
            writer.print(content);
        } catch (FileNotFoundException ex){
            throw new IllegalArgumentException("Cannot write file: " + FILE_PATH, ex);
        }
    }
}

