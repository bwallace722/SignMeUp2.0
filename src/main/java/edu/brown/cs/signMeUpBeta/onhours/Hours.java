package edu.brown.cs.signMeUpBeta.onhours;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.project.Question;

public class Hours {
  private Map<String, Integer> questionCount;
  private List<Question> questions;
  private Map<String, List<String>> studentQuestions;
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
    studentQuestions = new ConcurrentHashMap<String, List<String>>();
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
    return appointments;
  }
  public String getCurrAssessment() {
    return currAss;
  }
  public void incrementQuestion(String q) {
    if (questionCount.containsKey(q)) {
      questionCount.put(q, questionCount.get(q) + 1);
    } else {
      questionCount.put(q, 1);
    }
  }
  public void updateQuestions(String login, List<String> questions) {
    studentQuestions.put(login, questions);
    for (String q : questions) {
      incrementQuestion(q);
    }
  }
  private class CountComp implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
      return questionCount.get(b)
          - questionCount.get(a);
    }
  }
  public List<String> mostPopularQuestions() {
    PriorityQueue<String> pq = new PriorityQueue<String>(new CountComp());
    for (String q : questionCount.keySet()) {
      pq.add(q);
    }
    List<String> ret = new ArrayList<String>();
    for (int i = 0; i < 3; i++) {
      if (!pq.isEmpty()) {
        ret.add(pq.poll());
      }
    }
    return ret;
  }
  public List<String> studentsWhoAsked(String question) {
    List<String> ret = new ArrayList<String>();
    for (String student : studentQuestions.keySet()) {
      List<String> questions = studentQuestions.get(student);
      if (questions.contains(question)) {
        ret.add(student);
      }
    }
    return ret;
  }
  //
  // public List<QuestionInterface> currentQuestions() {
  // return null;
  // }
  //
  // public List<Appointment> currentAppointments() {
  // return null;
  // }
}
