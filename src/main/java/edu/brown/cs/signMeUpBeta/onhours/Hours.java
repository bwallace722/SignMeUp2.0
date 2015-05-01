package edu.brown.cs.signMeUpBeta.onhours;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.project.Question;

public class Hours {
  private Map<String, Integer> questionCount;
  private List<Question> questions;
  private int timeLim;
  private Map<String, String> appointments;
  private String currAss;
  public Hours(String currAss, List<Question> questionList) {
    questions = questionList;
    questionCount = new ConcurrentHashMap<String, Integer>();
    for (Question q : questionList) {
      questionCount.put(q.content(), 0);
    }
    timeLim = 10;
    this.currAss = currAss;
    appointments = new HashMap<String, String>();
  }
  public List<Question> getQuestions() {
    return questions;
  }
  public void addQuestion(Question newQuestion) {
    questions.add(newQuestion);
    questionCount.putIfAbsent(newQuestion.content(), 0);
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
      DateFormat timeFormat = new SimpleDateFormat("h:mm a");
      String time = timeFormat.format(slot.clone());
      appointments.put(time, null);
    }
  }
  public int scheduleAppointment(String time, String login) {
    if (appointments.get(time) != null) {
      return 0;
    }
    appointments.put(time, login);
    return 1;
  }
  
  
  public Map<String, String> getAppointments() {
    return appointments;
  }
  public String getCurrAssessment() {
    return currAss;
  }
  public void incrementQuestion(String q) {
    if (questionCount.containsKey(q)) {
      questionCount.put(q, questionCount.get(q)+1);
    } else {
      questionCount.put(q, 1);
    }
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
