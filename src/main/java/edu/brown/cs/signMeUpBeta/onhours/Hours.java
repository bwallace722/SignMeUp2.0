package edu.brown.cs.signMeUpBeta.onhours;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.project.Question;

public class Hours {
  private Map<Question, Integer> questions;
  private int timeLim;
  private Map<Date, String> appointments;
  private String currProject; // The project that spans the current date
  public Hours(String currProject, List<Question> questionList) {
    questions = new ConcurrentHashMap<Question, Integer>();
    for (Question q : questionList) {
      questions.put(q, 0);
    }
    timeLim = 10;
    this.currProject = currProject;
    this.appointments = new HashMap<Date, String>();
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
  public void setUpAppointments(Date currDate, int durationOfHours) {
    long numberOfAppointments = durationOfHours * 2;
    long millisecondsInAMinute = 60000;
    for (long i = 1; i <= numberOfAppointments; i++) {
      Date slot = new Date();
      slot.setTime(currDate.getTime()
          + (i * 15 * millisecondsInAMinute));
      this.appointments.put(slot, null);
    }
  }
  public Map<Date, String> getAppointments() {
    return this.appointments;
  }
  // public List<QuestionInterface> mostPopularQuestions() {
  // return null;
  // }
  //
  // public List<QuestionInterface> currentQuestions() {
  // return null;
  // }
  //
  // public List<Appointment> currentAppointments() {
  // return null;
  // }
}
