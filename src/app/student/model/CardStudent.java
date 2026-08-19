package app.student.model;

public class CardStudent {

    private String cardNumber;
    private Student student;


    public CardStudent(){

    }

    public CardStudent(String cardNumber){
        setCardNumber(cardNumber);
    }

    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public void setStudent(Student student){
        this.student = student;
    }
}
