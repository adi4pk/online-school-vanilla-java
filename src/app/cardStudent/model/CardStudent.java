package app.cardStudent.model;

import app.student.model.Student;

public class CardStudent {

    private String cardId;
    private String cardNumber;
    private String studentId;


    public CardStudent(String text) {
        String[] arr = text.split(",");
        this.setCardId(arr[0]);
        this.setCardNumber(arr[1]);
        this.setStudentId(arr[2]);
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String toText(){
        return this.cardId+"," + this.cardNumber + "," + this.studentId;
    }
}
