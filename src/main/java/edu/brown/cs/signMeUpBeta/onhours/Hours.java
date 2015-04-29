package edu.brown.cs.signMeUpBeta.onhours;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.project.Question;
import edu.brown.cs.signMeUpBeta.project.QuestionInterface;
import edu.brown.cs.signMeUpBeta.student.Appointment;

public class Hours {
  private ConcurrentHashMap<Question, Integer> questions;
  private int timeLim;
  private String currProject; //The project that spans the current date
  
  public Hours(String currProject, List<Question> questionList) {
    questions = new ConcurrentHashMap<Question, Integer>();
    for (Question q: questionList) {
      questions.put(q, 0);
    }
    timeLim = 10;
    this.currProject = currProject;
  }

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
  
  public int getTimeLim() {
    return timeLim;
  }
  
  public void setTimeLim(int newLim) {
    timeLim = newLim;
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
