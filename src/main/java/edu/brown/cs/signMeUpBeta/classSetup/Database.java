package edu.brown.cs.signMeUpBeta.classSetup;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.brown.cs.signMeUpBeta.onhours.TA;
import edu.brown.cs.signMeUpBeta.student.Student;

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
    stat.close();
    String schema =
        "CREATE TABLE course(course_id TEXT PRIMARY KEY, course_title TEXT);";
    buildTable(schema);
    schema =
        "CREATE TABLE assignment(id INT AUTO_INCREMENT, assignment_name TEXT, start_date DATE, end_date DATE, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id);";
    buildTable(schema);
    schema =
        "CREATE TABLE exam(id INT AUTO_INCREMENT, exam_name TEXT, start_date DATE, end_date DATE, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
    buildTable(schema);
    schema =
        "CREATE TABLE lab(id INT AUTO_INCREMENT, lab_name TEXT, start_date DATE, end_date DATE, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
    buildTable(schema);
    schema =
        "CREATE TABLE ta(ta_login TEXT, ta_name TEXT, ta_email TEXT, ta_password TEXT);";
    buildTable(schema);
    schema =
        "CREATE TABLE student(student_login TEXT, student_name TEXT, student_email TEXT, student_password TEXT, time_spent_at_hours INT, time_spent_curr_project INT, questions_asked INT, contact_method TEXT);";
    buildTable(schema);
    schema = "CREATE TABLE attendance(ta_on_block TEXT);";
    buildTable(schema);
    schema =
        "CREATE TABLE questions(assessment_item_name TEXT, question_section TEXT, question TEXT, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
    buildTable(schema);
    schema = "CREATE TABLE student_course(student_id TEXT, course_id TEXT);";
    buildTable(schema);
  }
  /**
   * This methods inserts a class into the course table.
   * @param courseNumber - eg CS032
   * @param courseTitle - eg Intro To Software Engineering
   * @throws SQLException on SQL error
   */
  public void addCourse(String courseNumber, String courseTitle)
      throws SQLException {
    String query = "INSERT INTO course VALUES (?, ?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, courseNumber);
    ps.setString(2, courseTitle);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method inserts a question prepared by TAs into the questions table.
   * @param assessmentName - the name of the project, exam, or lab
   * @param questionSection - the particular item beign asked about for the
   *        question
   * @param question - the actual question
   * @param course - the course number of the course under which this question
   *        is being asked
   * @throws SQLException on SQL error
   */
  public void addQuestion(String assessmentName, String questionSection,
      String question, String course) throws SQLException {
    String query = "INSERT INTO questions VALUES (?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, assessmentName);
    ps.setString(2, questionSection);
    ps.setString(3, question);
    ps.setString(4, course);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method adds a student-course entry to the student_course table.
   * @param studentId - the student's login
   * @param courseId - the course number
   * @throws SQLException on SQL error
   */
  public void addStudentCoursePair(String studentId, String courseId)
      throws SQLException {
    String query = "INSERT INTO student_course VALUES (?, ?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, studentId);
    ps.setString(2, courseId);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method inserts an entry into the lab table. This assessment item can
   * be either an assignment, an exam, or a lab.
   * @param table - the table name in which the assessment item will be inserted
   * @param name - the name for this assessment item
   * @param startDate - the start date for this project
   * @param endDate - the date this lab is due
   * @param courseId - the course number for the course udner which this
   *        assessment item is assigned
   * @throws SQLException - when there is an SQL error
   */
  public void addAssessmentItem(String table, String name, Date startDate,
      Date endDate, String courseId) throws SQLException {
    String query = "INSERT INTO ? VALUES (?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, table);
    ps.setString(2, name);
    ps.setDate(3, (java.sql.Date) startDate);
    ps.setDate(4, (java.sql.Date) endDate);
    ps.setString(5, courseId);
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
      String studentEmail, String studentPassword) throws SQLException {
    String query = "INSERT INTO student VALUES (?,?,?,?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, studentLogin);
    ps.setString(2, studentName);
    ps.setString(3, studentEmail);
    ps.setString(4, studentPassword);
    ps.setInt(5, 0);
    ps.setInt(6, 0);
    ps.setInt(7, 0);
    // ps.setString(8, contactMethod);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method adds a ta into the ta table.
   * @param taLogin - the login of the teaching assistant
   * @param taName - the name of the teaching assistant
   * @param email - the email of the teaching assistant
   * @param password - the password to the teaching assistant's account
   * @throws SQLException on SQL error
   */
  public
      void
      addTA(String taLogin, String taName, String email, String password)
          throws SQLException {
    String query = "INSERT INTO ta VALUES (?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, taLogin);
    ps.setString(2, taName);
    ps.setString(3, email);
    ps.setString(4, password);
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
    String update =
        "UPDATE student SET time_spent_at_hours=?, time_spent_curr_project=?, questions_asked=? WHERE student_login=?";
    PreparedStatement ps = conn.prepareStatement(update);
    ps.setInt(1, timeAtHours);
    ps.setInt(2, timeOnProject);
    ps.setInt(3, questionsAsked);
    ps.setString(4, studentLogin);
    ps.executeUpdate();
    ps.close();
  }
  /**
   * This method checks the input credentials and returns a student object if
   * the credentials are approved.
   * @param studentId
   * @param password
   * @return
   * @throws SQLException
   */
  public Student getStudentByLogin(String studentId, String password)
      throws SQLException {
    String query =
        "SELECT * FROM student WHERE student.student_login = ? AND student.student_password = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, studentId);
    ps.setString(2, password);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      Student loggedInStudent =
          new Student(rs.getString(1), rs.getString(2), rs.getString(3), rs
              .getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
      return loggedInStudent;
    }
    return null;
  }
  /**
   * This method checks the input credentials and returns a student object if
   * the credentials are approved.
   * @param taId
   * @param password
   * @return
   * @throws SQLException
   */
  public TA getTAByLogin(String taId, String password) throws SQLException {
    String query =
        "SELECT * FROM ta WHERE ta.ta_login = ? AND ta.ta_password = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, taId);
    ps.setString(2, password);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      TA loggedInTA =
          new TA(rs.getString(1), rs.getString(2), rs.getString(3), rs
              .getString(4));
      return loggedInTA;
    }
    return null;
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