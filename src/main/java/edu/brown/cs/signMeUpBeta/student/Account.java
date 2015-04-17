package edu.brown.cs.signMeUpBeta.student;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.signMeUpBeta.project.Question;

public class Account {

  private String login;
  private String password;
  private String name;
  private String email;
  //private int phone;
  private List<String> enrolledCourses;
  private List<String> TACourses;
  private int numQuestions;
  private int timeOnHours;
  private int timeCurrProject;
  
  public Account(String login, String name, String email, String password, int timeOnHours, int timeCurrProject, int numQuestions) {
//public Account(String login, String name, String email, String password) {
    this.login = login;
    this.name = name;
    this.password = password;
    this.timeOnHours = timeOnHours;
    this.timeCurrProject = timeCurrProject;
    this.numQuestions = numQuestions;
    enrolledCourses = new ArrayList<String>();
    TACourses = new ArrayList<String>();
  }

  public String login() {
    return login;
  }
  
  public void setID(String newLogin) {
    login = newLogin;
  }
  
  public String password() {
    return password;
  }
  
  public void setPassword(String newPassword) {
    password = newPassword;
  }
  
  public String name() {
    return name;
  }
  
  public void setName(String newName) {
    name = newName;
  }
  
  public String email() {
    return email;
  }
  
  public void setEmail(String newEmail) {
    email = newEmail;
  }
  
//  public int phone() {
//    return phone;
//  }
//  
//  public void setPhone(int newPhone) {
//    phone = newPhone;
//  }
  
  public List<String> enrolledCourses() {
    return enrolledCourses;
  }
  
  public void addEnrolledCourse(String courseID) {
    enrolledCourses.add(courseID);
  }
  
  public void setEnrolledCourses(List<String> newCourseList) {
    enrolledCourses = newCourseList;
  }
  
  public List<String> TACourses() {
    return TACourses;
  }
  
  public void addTACourse(String courseID) {
    TACourses.add(courseID);
  }
  
  public void setTACourses(List<String> newCourseList) {
    TACourses = newCourseList;
  }
  
  public int questionsAsked() {
    return numQuestions;
  }
  
  public void incrementQuestions() {
    numQuestions++;
  }
  
  public double timeOnHours() {
    return timeOnHours;
  }
  
  public void addTime(double timeToAdd) {
    timeOnHours += timeToAdd;
    timeCurrProject += timeToAdd;
  }
  
  public double timeOnCurrentProject() {
    return timeCurrProject;
  }
  
//  public String contactMethod() {
//    return null;
//  }
//  
//  public void setContactMethod() {}
}
