package app.cardStudent.repository;

import app.cardStudent.model.CardStudent;
import app.student.model.Student;

import javax.smartcardio.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class cardRepository {

    private static final String FILE_PATH = "src/app/cardStudent/data/cards.txt";

    private final List<CardStudent> cards = new ArrayList<>();


    public cardRepository(){
        loadData();
    }

    public Optional<CardStudent> findCardByStudentId(String studentId){
        for (CardStudent card : cards){
            if(card.getStudentId().equals(studentId)){
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    private void loadData(){
        File file = new File(FILE_PATH); //-- creeaza o reprezentare a fisierului
        if(!file.exists()){
            return;
        }

        try (Scanner scanner = new Scanner(file)){
            int lineNumber = 0;
            while (scanner.hasNextLine()){
                lineNumber++;
                String line = scanner.nextLine().trim(); // -- citeste prima linie, scannerul incepe de la 0.
            if (line.isEmpty()){
                continue;
            }
            try {
                cards.add(new CardStudent(line));
            } catch (RuntimeException ex){
                throw new IllegalArgumentException("Invalid card at line: " + lineNumber + ": " + line, ex);
            }

            }
        } catch (FileNotFoundException ex){
            throw new IllegalArgumentException("Cannot read file: " +file.getAbsolutePath(), ex);
        }
    }


    private void save(){
        StringBuilder content = new StringBuilder();
        for(CardStudent card : cards){
            content.append(card.toString()).append(System.lineSeparator()); //-- asemanator cu +"\n";
        }

        try(PrintWriter writer = new PrintWriter(FILE_PATH)){
            writer.print(content);
        } catch (FileNotFoundException ex){
            throw new IllegalArgumentException("Cannot write file: " + FILE_PATH, ex);
        }
    }
}
