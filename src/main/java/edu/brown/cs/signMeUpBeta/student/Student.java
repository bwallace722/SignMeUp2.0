package edu.brown.cs.signMeUpBeta.student;

import java.util.List;

import edu.brown.cs.signMeUpBeta.project.Question;

public class Student {

  private String login;
  private String password;
  private String name;
  private String email;
  private int phone;
  private List<String> courses;
  private int numQuestions;
  private int timeOnHours;
  private int timeCurrProject;
  
  
  public Student(String login, String name, String email, String password, int timeOnHours, int timeCurrProject, int numQuestions) {
    this.login = login;
    this.name = name;
    this.password = password;
    this.timeOnHours = timeOnHours;
    this.timeCurrProject = timeCurrProject;
    this.numQuestions = numQuestions;
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
  
  public int phone() {
    return phone;
  }
  
  public void setPhone(int newPhone) {
    phone = newPhone;
  }
  
  public List<String> courses() {
    return courses;
  }
  
  public void addCourse(String courseID) {
    courses.add(courseID);
  }
  
  public void setCourses(List<String> newCourseList) {
    courses = newCourseList;
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
