package edu.brown.cs.signMeUpBeta.student;

import java.util.List;

public class Account {
  private String login;
  private String password;
  private String name;
  private String email;
  private List<String> enrolledCourses;
  private List<String> TACourses;
  private int numQuestions;
  private int timeAtHours;
  private int timeCurrProject;
  public Account(
      String login,
      String name,
      String email,
      String password,
      int timeAtHours,
      int timeCurrProject,
      int numQuestions,
      List<String> enrolledCourses,
      List<String> TACourses) {
    this.login = login;
    this.name = name;
    this.password = password;
    this.timeAtHours = timeAtHours;
    this.timeCurrProject = timeCurrProject;
    this.numQuestions = numQuestions;
    this.enrolledCourses = enrolledCourses;
    this.TACourses = TACourses;
  }
  public String getLogin() {
    return login;
  }
  public void setLogin(String newLogin) {
    login = newLogin;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String newPassword) {
    password = newPassword;
  }
  public String getName() {
    return name;
  }
  public void setName(String newName) {
    name = newName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String newEmail) {
    email = newEmail;
  }
  public List<String> getEnrolledCourses() {
    return enrolledCourses;
  }
  public void addEnrolledCourse(String courseID) {
    enrolledCourses.add(courseID);
  }
  public void setEnrolledCourses(List<String> newCourseList) {
    enrolledCourses = newCourseList;
  }
  public List<String> getTACourses() {
    return TACourses;
  }
  public void addTACourse(String courseID) {
    TACourses.add(courseID);
  }
  public void setTACourses(List<String> newCourseList) {
    TACourses = newCourseList;
  }
  public int getQuestionsAsked() {
    return numQuestions;
  }
  public void setQuestionsAsked(int numQuestions) {
    this.numQuestions = numQuestions;
  }
  public void incrementQuestionsAsked() {
    numQuestions++;
  }
  public double getTimeOnHours() {
    return timeAtHours;
  }
  public void setTimeAtHours(int timeAtHours) {
    this.timeAtHours = timeAtHours;
  }
  public void addTime(double timeToAdd) {
    timeAtHours += timeToAdd;
    timeCurrProject += timeToAdd;
  }
  public double getTimeOnCurrentProject() {
    return timeCurrProject;
  }
  public void setTimeOnCurrentProject(int timeCurrProject) {
    this.timeCurrProject = timeCurrProject;
  }
}
