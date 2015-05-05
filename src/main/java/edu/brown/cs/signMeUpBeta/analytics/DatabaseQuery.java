package edu.brown.cs.signMeUpBeta.analytics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseQuery {


  String[] tables = {"course", "category", "assignment", "topic"};
  Connection conn;

  public DatabaseQuery(
      String databaseURL) throws ClassNotFoundException, SQLException {
    // TODO Auto-generated constructor stub
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.sqlite.JDBC");
    databaseURL = "data/randomDatabase.sqlite3";
    String urlToDB = "jdbc:sqlite:" + databaseURL;
    conn = DriverManager.getConnection(urlToDB);
    // these two lines tell the database to enforce foreign
    // keys during operations, and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");
  }

  //0: course
  //1: category
  //2: assignment
  //3: topic
  public List<String[]> getSelectOptions(String[] parameters)
      throws SQLException {

    List<String[]> toReturn = new ArrayList<String[]>();

    int paramIndex = parameters.length - 1;
    //blank search:
    if (paramIndex < 0 || paramIndex >= 3) {
      toReturn = new ArrayList<String[]>();
    } else {
      String param = parameters[paramIndex];
      String table = tables[paramIndex + 1];
      String foreignTable = tables[paramIndex] + "_id";
      //System.out.println(table + ", " + foreignTable);
      //can't set tables:
      PreparedStatement prep = conn.prepareStatement(
          "SELECT name, id FROM "+ table + " WHERE " + foreignTable + " = ?;");
      prep.setInt(1, Integer.parseInt(param));
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        toReturn.add(new String[]{rs.getString(1), rs.getString(2)});
      }
      rs.close();
    }
    return toReturn;
  }

  private String getFilter(String[] parameters) {
    int paramIndex = parameters.length - 1;
    switch (paramIndex) {
    case 0:
      return
          "SELECT <query> FROM hours, question, topic, assignment, category"
          + " WHERE hours.question_id = question.id"
          + " AND question.topic_id = topic.id"
          + " AND topic.assignment_id = assignment.id "
          + " AND assignment.category_id = category.id"
          + " AND category.course_id = ?;";
    case 1:
      return
          "SELECT <query> FROM hours, question, topic, assignment"
          + " WHERE hours.question_id = question.id"
          + " AND question.topic_id = topic.id"
          + " AND topic.assignment_id = assignment.id "
          + " AND assignment.category_id = ?";
    case 2:
      return
          "SELECT <query> FROM hours, question, topic"
          + " WHERE hours.question_id = question.id"
          + " AND question.topic_id = topic.id"
          + " AND topic.assignment_id = ?";
    case 3:
      return 
          "SELECT <query> FROM hours, question"
          + " WHERE hours.question_id = question.id"
          + " AND question.topic_id = ?";
    default:
      return "SELECT <query> FROM hours"
      + " WHERE hours.id = hours.id;";
    }
  }

  private List<Integer> getSignupTimes(
      String[] parameters) throws SQLException {
    //setup the query with a call to getFilter
    String query =
        getFilter(parameters).replaceFirst("<query>", "hours.check_off_time");
    PreparedStatement prep = conn.prepareStatement(query);
    int paramIndex = parameters.length - 1;
    if (paramIndex >= 0 && paramIndex < 4) {
      prep.setInt(1, Integer.parseInt(parameters[paramIndex]));
    }
    ResultSet rs = prep.executeQuery();
    List<Integer> times = new ArrayList<Integer>();
    while(rs.next()) {
      times.add(rs.getInt(1));
    }
    rs.close();
    return times;
  }



  public List<Integer[]> getStudentsVisits(
      String[] parameters) throws SQLException {

    PreparedStatement prep;
    ResultSet rs;

    if (parameters.length == 0) {
      prep = conn.prepareStatement(
          "SELECT id FROM student;");
    } else {
      prep = conn.prepareStatement(
          "SELECT student_id FROM student_course WHERE course_id = ?;");
      prep.setInt(1, Integer.parseInt(parameters[0]));
    }
    rs = prep.executeQuery();
    List<Integer> students = new ArrayList<Integer>();
    while(rs.next()) {
      students.add(rs.getInt(1));
    }
    rs.close();


    List<Integer[]> studentTimes = new ArrayList<Integer[]>();
    for (int key : students) {


      //setup the query with a call to getFilter
      String query =
          getFilter(parameters)
          .replaceFirst("<query>", "COUNT(hours.id)")
          .replaceFirst("WHERE ", "WHERE hours.student_id = ? AND ");

      prep = conn.prepareStatement(query);
      prep.setInt(1, key);

      int paramIndex = parameters.length - 1;
      if (paramIndex >= 0 && paramIndex < 4) {
        prep.setInt(2, Integer.parseInt(parameters[paramIndex]));
      }

      rs = prep.executeQuery();

      rs.next();

      studentTimes.add(new Integer[]{key, rs.getInt(1)});
    }

    return studentTimes;
  }

  @SuppressWarnings("deprecation")
  public List<String[]> getWeekData(String[] parameters) throws SQLException {
    List<Integer> times = getSignupTimes(parameters);

    int[] daysOfWeek = new int[7];

    for (Integer t : times) {
      //TODO: find non-deprecated way that works
      Date date = new Date(t * 1000L);
      daysOfWeek[date.getDay()]++;
    }

    String[] days = new String[]{
        "Sunday", "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday"};
    List<String[]> toReturn = new ArrayList<String[]>();

    for (int i = 0; i < 7; i++) {
      toReturn.add(new String[]{days[i], String.valueOf(daysOfWeek[i])});
    }

    return toReturn;
  }
}
