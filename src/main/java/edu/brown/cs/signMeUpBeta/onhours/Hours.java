package edu.brown.cs.signMeUpBeta.onhours;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.project.Question;
import edu.brown.cs.signMeUpBeta.project.QuestionInterface;
import edu.brown.cs.signMeUpBeta.student.Appointment;

public class Hours {
  private ConcurrentHashMap<Question, Integer> questions;
  
  public Hours() {}
  
  public void run() {}
  
  public List<QuestionInterface> mostPopularQuestions() {
    return null;
  }
  
  public List<QuestionInterface> currentQuestions() {
    return null;
  }
  
  public List<Appointment> currentAppointments() {
    return null;
  }
}
