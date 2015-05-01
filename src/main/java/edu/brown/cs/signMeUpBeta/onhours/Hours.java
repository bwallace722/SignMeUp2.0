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
  private Map<Question, Integer> questions;
  private int timeLim;
  private Map<String, String> appointments;
  private String currAss; // The project that spans the current date
  public Hours(String currAss, List<Question> questionList) {
    questions = new ConcurrentHashMap<Question, Integer>();
    for (Question q : questionList) {
      questions.put(q, 0);
    }
    timeLim = 10;
    this.currAss = currAss;
    this.appointments = new HashMap<String, String>();
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
      DateFormat timeFormat = new SimpleDateFormat("h:mm a");
      String time = timeFormat.format(slot.clone());
      this.appointments.put(time, null);
    }
  }
  public int scheduleAppointment(String time, String login) {
    if (appointments.get(time) != null) {
      return 0;
    }
    appointments.put(time, login);
    return 1;
  }
  public int removeAppointment(String time) {
    if (appointments.get(time) == null) {
      return 0;
    }
    appointments.put(time, null);
    return 1;
  }
  public int checkOffAppointment(String time) {
    if (appointments.get(time) == null) {
      return 0;
    }
    appointments.remove(time);
    return 1;
  }
  public Map<String, String> getAppointments() {
    return this.appointments;
  }
  public String getCurrAssessment() {
    return currAss;
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
