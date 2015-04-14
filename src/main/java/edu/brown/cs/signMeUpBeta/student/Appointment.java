package edu.brown.cs.signMeUpBeta.student;

public class Appointment {
  
  private Student student;
  private double start;
  private double duration;
  private double end;

  public Appointment(Student student, double start, double duration) {
    this.student = student;
    this.start = start;
    this.duration = duration;
    end = start + duration;
  }
  
  public double startTime() {
    return start;
  }
  
  public void setStartTime(double newStartTime) {
    start = newStartTime;
  }
  
  public double duration() {
    return duration;
  }
  
  public void setDuration(double newDuration) {
    duration = newDuration;
  }
  
  public Student student() {
    return student;
  }
  
  public void setStudent(Student newStudent) {
    student = newStudent;
  }
  
  public double endTime() {
    return end;
  }
}
