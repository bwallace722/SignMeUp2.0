package edu.brown.cs.signMeUpBeta.onhours;

import edu.brown.cs.signMeUpBeta.student.Account;

public class Appointment {
  private Account student;
  private double start;
  public Appointment(Account student) {
    this.student = student;
    this.start = start;
  }
  public Account student() {
    return student;
  }
  public void setStudent(Account newStudent) {
    student = newStudent;
  }
}
