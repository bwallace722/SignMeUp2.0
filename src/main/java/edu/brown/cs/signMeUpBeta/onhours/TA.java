package edu.brown.cs.signMeUpBeta.onhours;

import java.util.List;

public class TA {

  private String login;
  private String name;
  private String email;
  private String password;
  
  public TA(String login, String name, String email, String password) {
    this.login = login;
    this.name = name;
    this.email = email;
    this.password = password;
  }
  
  public String login() {
    return login;
  }
  
  public void setLogin(String newLogin) {
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
  
//  public List<String> courses() {
//    return null;
//  }
//  
//  public void addCourse(String newCourse) {}
//  
//  public void setCourses(List<String> newCourses) {}
}
