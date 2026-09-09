package app;

import app.book.Book;
import app.book.repository.BookRepository;
import app.course.repository.CourseRepository;
import app.course.model.Course;
import app.student.model.Student;
import app.enrollment.repository.EnrollmentRepository;
import app.enrollment.model.Enrollment;
import app.student.repository.StudentRepository;

import java.time.LocalDate;
import java.util.*;

public class View {

    private Student student;
    private Scanner scanner;
    private BookRepository bookRepository;
    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;
    private CourseRepository courseRepository;

    public View(){
        this.student= new Student("m6n-52-67,Alexandra,Tudor,alexandra.tudor@example.com,Alpha321,25");
        this.scanner= new Scanner(System.in);       //este un obiect care știe să citească caractere dintr-o sursă.
        this.bookRepository = new BookRepository();
        this.studentRepository = new StudentRepository();
        this.enrollmentRepository= new EnrollmentRepository();
        this.courseRepository = new CourseRepository();
        this.play();


    }


    public void play(){     //ruleaza din Constructor()

        int alegere=-1;
        while (alegere != 0) {      //menu() o sa ruleze si o sa fie afisat de fiecare data.

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
                case 5:
                    showCoursesByEnrollments();
                    break;
                case 6:
                    showAllCourses();
                    break;
                case 7:
                    removeCourse();
                    break;
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
        //show my enrolments -- findAllEnrollmentsByStudentId(String studentId)
        System.out.println("5-show my enrollments");
        System.out.println("6-show available courses");
        System.out.println("7-inscrie-te la un curs:");
    }

    public  void showStudentBooks(){


        List<Book> bookList= bookRepository.findBooksByStudentId(student.getId());

        for(Book b :bookList){

            System.out.println(b.getBookName().toString());
//            System.out.println(b.toText());
        }


    }


    public void showAllCourses(){

        List<Course> allCourses = courseRepository.findAllCourses();

        for (Course course : allCourses){
            System.out.println(course.toText() + "\n");
        }
    }

    public void registerForCourse(){
        System.out.println("Introdu numele cursului la care vrei sa te inregistrezi:");
        String courseName = scanner.nextLine();
        Optional<Course> foundCourse = courseRepository.findByCourseName(courseName);
        if (foundCourse.isPresent()){
            try{
                Enrollment newEnrollment = new Enrollment(student.getId(), foundCourse.get().getCourseId(), LocalDate.now());
                enrollmentRepository.addEnrollment(newEnrollment);
                System.out.println("enrolled");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void removeCourse(){
        System.out.println("Introdu numele cursului: ");
        System.out.println("Type 0 to go back");

        String courseName = scanner.nextLine();

        if (courseName.equals("0")){
            return;
        }

        Optional<Course> foundCourseByName = courseRepository.findByCourseName(courseName);

        if (foundCourseByName.isPresent()){
            try{ Optional<Enrollment> enrollmentToRemove = enrollmentRepository.findByStudentIdAndCourseId(this.student.getId(), foundCourseByName.get().getCourseId());
                enrollmentRepository.deleteById(enrollmentToRemove.get().getStudentId(), enrollmentToRemove.get().getCourseId());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    public void addBook(){
        System.out.println("Enter the book name");
        System.out.println("Type 0 to go back");
        String bookName = scanner.nextLine();// --> .nextLine() returns a String

        if(bookName.equals("0")){
            return;
        }

        try{
            bookRepository.addBook(bookName,student.getId());
            System.out.println("book added");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editBook(){
        System.out.println("Enter the book name you would like to edit: ");
        System.out.println("Type 0 to go back");

        String oldName=scanner.nextLine();

        if (oldName.equals("0")){
            return;
        }

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
        System.out.println("Type 0 to go back to the main menu.");
        String bookName=scanner.nextLine();

        if(bookName.equals("0")){
            return;
        }

        try{
            boolean erased=bookRepository.deleteByBookName(bookName);
            if(erased){
                System.out.println("success");
            }else{

                System.out.println("The book doesn't exist");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

        //use Student ID to find Enrollments --> get course ID from Enrollment property (course ID)
        // --> create a <Course> courseList based on courseIds List
    public void showCoursesByEnrollments(){      //gaseste toate enrollments pentru un Student specific
        List<Enrollment> enrollmentsList = enrollmentRepository.findAllEnrollmentsByStudentId(student.getId());
        List<String> courseIds=new ArrayList<>();


        for (Enrollment enrollment : enrollmentsList){
            courseIds.add(enrollment.getCourseId());
        }

        List<Course> coursesList = courseRepository.findAllCoursesByIds(courseIds);

        for (Course course : coursesList){
            System.out.println(course.getCourseName());
        }
    }


    public void showCoursesByEnrollmentIds(String enrollmentsIds) {



    }

}
