package edu.brown.cs.signMeUpBeta.classSetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * This class handles the setup of the databases and tables used in the project.
 * @author omadarik
 */
public class Database {
  private Connection conn;
  /**
   * This is the constructor for this class.
   * @param db - the path to the database file used in this project.
   * @throws ClassNotFoundException - when ClassNotFoundException error is
   *         encountered
   * @throws SQLException - when SQLException error is encountered
   */
  public Database(String db) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:"
        + db;
    conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");
    stat.execute("DROP TABLE IF EXISTS ta");
    stat.execute("DROP TABLE IF EXISTS project");
    stat.execute("DROP TABLE IF EXISTS student");
    stat.execute("DROP TABLE IF EXISTS attendance");
    stat.close();
    String schema =
        "CREATE TABLE ta(ta_login TEXT, ta_name TEXT, ta_email TEXT, ta_password TEXT);";
    buildTable(schema);
    schema =
        "CREATE TABLE student(student_login TEXT, student_name TEXT, student_email TEXT, student_password TEXT, time_spent_at_hours INT, time_spent_curr_project INT, questions_asked INT, contact_method TEXT);";
    buildTable(schema);
    schema = "CREATE TABLE attendance(ta_on_block TEXT);";
    buildTable(schema);
    schema =
        "CREATE TABLE project(id INT NOT NULL AUTO_INCREMENT, category TEXT, subcategory TEXT, startdate DATE, enddate DATE);";
    buildTable(schema);
    schema =
        "CREATE TABLE lab(id INT NOT NULL AUTO_INCREMENT, category TEXT, startdate DATE, enddate DATE);";
    buildTable(schema);
  }
  /**
   * This method inserts an entry into the project table.
   * @param category - the main grouping for this insertion
   * @param subcategory - the subgroup for this insertion
   * @param startDate - the start date for this project
   * @param endDate - the date this project is due
   * @throws SQLException - when there is an SQL error
   */
  public void addProject(String category, String subcategory, Date startDate,
      Date endDate) throws SQLException {
    String query = "INSERT INTO project VALUES (?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, category);
    ps.setString(2, subcategory);
    ps.setDate(3, (java.sql.Date) startDate);
    ps.setDate(4, (java.sql.Date) endDate);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method inserts an entry into the lab table.
   * @param category - the main grouping for this insertion
   * @param startDate - the start date for this project
   * @param endDate - the date this project is due
   * @throws SQLException - when there is an SQL error
   */
  public void addLab(String category, String subcategory, Date startDate,
      Date endDate) throws SQLException {
    String query = "INSERT INTO project VALUES (?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, category);
    ps.setDate(2, (java.sql.Date) startDate);
    ps.setDate(3, (java.sql.Date) endDate);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method inserts an entry into the lab table. student_login TEXT,
   * student_name TEXT, student_email TEXT, student_password TEXT,
   * time_spent_at_hours TEXT, time_spent_curr_project TEXT, questions_asked
   * INT, contact_method TEXT
   * @param category - the main grouping for this insertion
   * @param startDate - the start date for this project
   * @param endDate - the date this project is due
   * @throws SQLException - when there is an SQL error
   */
  public void addStudent(String studentLogin, String studentName,
      String studentEmail, String studentPassword, String contactMethod)
      throws SQLException {
    String query = "INSERT INTO student VALUES (?,?,?,?,?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, studentLogin);
    ps.setString(2, studentName);
    ps.setString(3, studentEmail);
    ps.setString(4, studentPassword);
    ps.setInt(5, 0);
    ps.setInt(6, 0);
    ps.setInt(7, 0);
    ps.setString(8, contactMethod);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method updates the time spent at hours and number of questions asked
   * for any given student.
   * @param timeAtHours - the total spent at hours by this student
   * @param timeOnProject - the time spent on a particular project by a student.
   *        Will we need a foreign key to link to the project in the project
   *        table?
   * @param questionsAsked - the total number of questions asked by the given
   *        student
   * @throws SQLException On SQL error
   */
  public void updateStudent(String studentLogin, int timeAtHours,
      int timeOnProject, int questionsAsked) throws SQLException {
    String query =
        "UPDATE student SET time_spent_at_hours=?, time_spent_curr_project=?, questions_asked=? WHERE student_login=?";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setInt(1, timeAtHours);
    ps.setInt(2, timeOnProject);
    ps.setInt(3, questionsAsked);
    ps.setString(4, studentLogin);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * Creates a new table according to the schema.
   * @param schema - used in processing SQL commands
   * @throws SQLException - when there is an SQL error
   */
  private void buildTable(String schema) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement(schema);
    prep.executeUpdate();
    prep.close();
  }
}