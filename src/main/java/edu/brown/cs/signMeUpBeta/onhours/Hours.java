package edu.brown.cs.signMeUpBeta.onhours;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.project.Question;
import edu.brown.cs.signMeUpBeta.project.QuestionInterface;
import edu.brown.cs.signMeUpBeta.student.Appointment;

public class Hours {
  private ConcurrentHashMap<Question, Integer> questions;
  
  public Hours() {}
  
  public void run() {}
  
  public List<Question> getQuestions() {
    ArrayList<Question> questionList = new ArrayList<Question>();
    questionList.addAll(questions.keySet());
    return questionList;
  }
  
  public void incrementQuestion(Question qToIncrement) {
    if (questions.containsKey(qToIncrement)) {
      int newVal = questions.get(qToIncrement) + 1;
      questions.put(qToIncrement, newVal);
    }
  }
  
  public void addQuestion(Question newQuestion) {
    if (!questions.containsKey(newQuestion)) {
      questions.put(newQuestion, 0);
    }
  }
  
//  public List<QuestionInterface> mostPopularQuestions() {
//    return null;
//  }
//  
//  public List<QuestionInterface> currentQuestions() {
//    return null;
//  }
//  
//  public List<Appointment> currentAppointments() {
//    return null;
//  }
}
