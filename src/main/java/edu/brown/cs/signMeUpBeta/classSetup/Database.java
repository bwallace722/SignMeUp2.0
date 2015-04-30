package edu.brown.cs.signMeUpBeta.classSetup;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.signMeUpBeta.project.Question;
import edu.brown.cs.signMeUpBeta.student.Account;

/**
 * This class handles the setup of the databases and tables used in the project.
 * @author omadarik
 */
public class Database {
  private Connection conn;
  private Map<String, Account> allAccounts;
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
    allAccounts = new HashMap<String, Account>();
  }
  /**
   * This method returns all the classes taken by a student in the learner role.
   * @param studentId - the login of the student
   * @return
   * @throws SQLException
   */
  public List<String> getStudentClasses(String studentId) throws SQLException {
    String query =
        "SELECT course_id FROM student_course WHERE student_course.student_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, studentId);
    ResultSet rs = ps.executeQuery();
    List<String> toReturn = new ArrayList<String>();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    return toReturn;
  }
  /**
   * This method returns all the classes in which a student serves as a teaching
   * student.
   * @param taId - the login of the ta
   * @return
   * @throws SQLException
   */
  public List<String> getTAClasses(String taId) throws SQLException {
    String query = "SELECT course_id FROM ta_course WHERE ta_course.ta_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, taId);
    ResultSet rs = ps.executeQuery();
    List<String> toReturn = new ArrayList<String>();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    return toReturn;
  }
  /**
   * This methods inserts a class into the course table.
   * @param courseNumber - eg CS032
   * @param courseTitle - eg Intro To Software Engineering
   * @throws SQLException on SQL error
   */
  public void addCourse(String courseNumber, String courseTitle)
      throws SQLException {
    String query = "INSERT INTO course VALUES (?, ?);";
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
  public Question addQuestion(String assessmentName, String question,
      String course, String currProject) throws SQLException {
    String query = "INSERT INTO questions VALUES (?,?,?,?);";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, question);
    ps.setString(2, course);
    ps.setString(3, assessmentName);
    ps.setInt(4, 0);
    ps.executeUpdate();
    ps.close();
    Question q = new Question(course, question, assessmentName);
    return q;
  }
  /**
   * This method adds a student-course entry to the student_course table.
   * @param studentId - the student's login
   * @param courseId - the course number
   * @throws SQLException on SQL error
   */
  public String addStudentCoursePair(String studentId, String courseId)
      throws SQLException {
    String response;
    /*
     * Checking to see if the entry already exists in the student table.
     */
    String query =
        "SELECT * FROM student_course WHERE student_course.student_id = ? AND student_course.course_id = ?";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, studentId);
    ps.setString(2, courseId);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      response = "Student already in table.";
      return response;
    }
    /*
     * Checking to see if the entry already exists in the ta table.
     */
    query =
        "SELECT * FROM ta_course WHERE ta_course.ta_id = ? AND ta_course.course_id = ?";
    ps = conn.prepareStatement(query);
    ps.setString(1, studentId);
    ps.setString(2, courseId);
    rs = ps.executeQuery();
    if (rs.next()) {
      response = "Student is a TA.";
      return response;
    }
    /*
     * The student isn't a TA for this class and isn't already in the table.
     * Thus they are eligible to be enrolled.
     */
    String update = "INSERT INTO student_course VALUES (?,?,?,?,?);";
    ps = conn.prepareStatement(update);
    ps.setString(1, studentId);
    ps.setString(2, courseId);
    ps.setInt(3, 0);
    ps.setInt(4, 0);
    ps.setString(5, ""); //no current project because haven't signed up for hours yet
    ps.executeUpdate();
    ps.close();
    response = "Success.";
    return response;
  }
  /**
   * This method inserts a ta-course pair entry into the database.
   * @param taId
   * @param courseId
   * @throws SQLException
   */
  public String addTACoursePair(String taId, String courseId)
      throws SQLException {
    String response;
    /*
     * Checking to see if the entry already exists in the TA table.
     */
    String query =
        "SELECT * FROM ta_course WHERE ta_course.ta_id = ? AND ta_course.course_id = ?";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, taId);
    ps.setString(2, courseId);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      response = "TA already in table.";
      return response;
    }
    /*
     * Checking to see if the entry already exists in the student table.
     */
    query =
        "SELECT * FROM student_course WHERE student_course.student_id = ? AND student_course.course_id = ?";
    ps = conn.prepareStatement(query);
    ps.setString(1, taId);
    ps.setString(2, courseId);
    rs = ps.executeQuery();
    if (rs.next()) {
      response = "TA is a student.";
      return response;
    }
    /*
     * The TA isn't a student for this class and isn't already in the table.
     * Thus they are eligible to be enrolled.
     */
    String update = "INSERT INTO ta_course VALUES (?, ?);";
    ps = conn.prepareStatement(update);
    ps.setString(1, taId);
    ps.setString(2, courseId);
    ps.executeUpdate();
    ps.close();
    response = "Success.";
    return response;
  }
  public void removeAssessmentsByCourse(String table, String course)
      throws SQLException {
    String query = "DELETE FROM "
        + table
        + " WHERE course_id = ?;";
    System.out.println(query);
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, course);
    ps.executeUpdate();
    ps.close();
  }
  public void removeAccountCoursePair(String login, String courseId) throws SQLException {
    String query = "DELETE FROM student_course WHERE student_id = ? AND course_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, login);
    ps.setString(2, courseId);
    ps.executeUpdate();
    ps.close();
    
    query = "DELETE FROM ta_course WHERE ta_id = ? AND course_id = ?;";
    ps = conn.prepareStatement(query);
    ps.setString(1, login);
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
  public void addAssessmentItem(String table, String name, String startDate,
      String endDate, String courseId) throws SQLException {
    String query = "INSERT INTO "
        + table
        + " VALUES (?,?,?,?);";
    System.out.println(query);
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, name);
    ps.setDate(2, java.sql.Date.valueOf(startDate));
    ps.setDate(3, java.sql.Date.valueOf(endDate));
    ps.setString(4, courseId);
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
  public Account addAccount(String login, String name, String email,
      String password) throws SQLException {
    String query = "INSERT INTO account VALUES (?,?,?,?,?);";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, login);
    ps.setString(2, name);
    ps.setString(3, email);
    ps.setString(4, password);
    ps.setString(5, "");
    ps.executeUpdate();
    ps.close();
    Account account =
        new Account(login, name, email, password,
            new ArrayList<String>(), new ArrayList<String>());
    allAccounts.put(login, account);
    return account;
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
//  public void updateAccount(String studentLogin, int timeAtHours,
//      int timeOnProject) throws SQLException {
//    String query = "SELECT * from account WHERE login = ?;";
//    PreparedStatement ps = conn.prepareStatement(query);
//    ResultSet rs = ps.executeQuery();
//    int currTimeAtHours = 0;
//    int currTimeOnProject = 0;
//    int currQuestionsAsked = 0;
//    while (rs.next()) {
//      currTimeAtHours = rs.getInt(5);
//      currTimeOnProject = rs.getInt(6);
//      currQuestionsAsked = rs.getInt(7);
//    }
//    String update =
//        "UPDATE account SET time_spent_at_hours=?, time_spent_curr_project=?, questions_asked=? WHERE login=?;";
//    ps = conn.prepareStatement(update);
//    ps.setInt(1, currTimeAtHours
//        + timeAtHours);
//    ps.setInt(2, currTimeOnProject
//        + timeOnProject);
//    ps.setInt(3, currQuestionsAsked + 1);
//    ps.setString(4, studentLogin);
//    ps.executeUpdate();
//    ps.close();
//    Account student = allAccounts.get(studentLogin);
//    student.setQuestionsAsked(currQuestionsAsked + 1);
//    student.setTimeOnCurrentProject(timeOnProject);
//    student.setTimeAtHours(timeAtHours);
//  }
  /**
   * Returns an account by the login of a student.
   * @param login
   * @return
   * @throws SQLException
   */
  public Account getAccount(String login) throws SQLException {
    if (allAccounts.containsKey(login)) {
      return allAccounts.get(login);
    } else {
      String query = "SELECT name, email, password FROM account WHERE login = ?;";
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, login);
      ResultSet rs = ps.executeQuery();
      String name, email, password;
      if (rs.next()) {
        name = rs.getString(1);
        email = rs.getString(2);
        password = rs.getString(3);
      } else {
        /*
         * The student with the login could not be found in the database.
         */
        return null;
      }
      ps.close();
      rs.close();
      /*
       * Getting the courses the student is enrolled in.
       */
      query = "SELECT course_id FROM student_course WHERE student_id = ?;";
      ps = conn.prepareStatement(query);
      ps.setString(1, login);
      rs = ps.executeQuery();
      List<String> enrolledCourses = new ArrayList<String>();
      if (rs.next()) {
        enrolledCourses.add(rs.getString(1));
      }
      ps.close();
      rs.close();
      /*
       * Getting the courses the student is a TA in.
       */
      query = "SELECT course_id FROM ta_course WHERE ta_id = ?;";
      ps = conn.prepareStatement(query);
      ps.setString(1, login);
      rs = ps.executeQuery();
      List<String> TACourses = new ArrayList<String>();
      if (rs.next()) {
        TACourses.add(rs.getString(1));
      }
      ps.close();
      rs.close();
      Account account =
          new Account(login, name, email, password, enrolledCourses, TACourses);
      return account;
    }
  }
  /**
   * This method checks the input credentials and returns a student object if
   * the credentials are approved.
   * @param studentId
   * @param password
   * @return
   * @throws SQLException
   */
  public Account approveCredentials(String login, String password) //TODO: KIERAN LOOK AT THIS
      throws SQLException {
    String query = "SELECT * FROM account WHERE login = ? AND password = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, login);
    ps.setString(2, password);
    ResultSet rs = ps.executeQuery();
    String name, email;
    if (rs.next()) {
      return getAccount(login);
    } else {
      return null;
    }
  }
  
  public List<Question> getQuestions(String courseID, String assessment) throws SQLException {
    String query = "SELECT * FROM questions WHERE course_id=? AND assessment_name=?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, courseID);
    ps.setString(2, assessment);
    ResultSet rs = ps.executeQuery();
    List<Question> questionList = new ArrayList<Question>();
    while (rs.next()) {
      Question q = new Question(courseID, rs.getString(3), rs.getString(1));
      questionList.add(q);
    }
    ps.close();
    rs.close();
    return questionList;
  }
  public List<String> getAllCourses() throws SQLException {
    String query = "SELECT course_id FROM course ORDER BY course_id ASC;";
    PreparedStatement ps = conn.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    List<String> courses = new ArrayList<String>();
    while (rs.next()) {
      courses.add(rs.getString(1));
    }
    ps.close();
    rs.close();
    return courses;
  }
  
  public String getCurrAssessment(String courseId) throws SQLException {
    String query = "SELECT assignment_name, start_date, end_date FROM assignment WHERE course_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, courseId);
    ResultSet rs = ps.executeQuery();
    String curr = "none";
    java.sql.Date start, end;
    java.util.Date today = new java.util.Date();
    while (rs.next()) {
      start = rs.getDate(2);
      end = rs.getDate(3);
      if ((today.after(start)) && (today.before(end))) {
        curr = rs.getString(1);
      }
    }
    return curr;
  }
  
  public int getNumberQuestionsAsked(String login, String courseId) throws SQLException {
    String query = "SELECT questions_asked_curr_project FROM student_course WHERE student_id=? AND course_id=?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, login);
    ps.setString(2, courseId);
    ResultSet rs = ps.executeQuery();
    int num = 0;
    if (rs.next()) {
      num = rs.getInt(1);
    }
    ps.close();
    rs.close();
    return num;
  }
  public void updateStudentInfo(String login, String courseId, String[] questions, String currAss) throws SQLException {
    String query = "UPDATE student_course SET questions_asked = questions_asked+1, questions_asked_curr_project = questions_asked_curr_project+1, last_project = ? WHERE student_id = ? AND course_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, currAss);
    ps.setString(2, login);
    ps.setString(3, courseId);
    ps.executeUpdate();
    ps.close();
    
    for (String q: questions) {
      query = "UPDATE questions SET count = count+1 WHERE question = ? AND course_id = ? AND assessment_name = ?;";
      ps = conn.prepareStatement(query);
      ps.setString(1, q);
      ps.setString(2, courseId);
      ps.setString(3, currAss);
      ps.executeUpdate();
      ps.close();
    }
  }
  public String getLastProject(String login, String course) throws SQLException{
    String query = "SELECT last_project FROM student_course WHERE student_id = ? AND course_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, login);
    ps.setString(2, course);
    ResultSet rs = ps.executeQuery();
    String last = "none";
    if (rs.next()) {
      last = rs.getString(1);
    }
    ps.close();
    rs.close();
    return last;
  }
  
  public void resetNumQuestions(String login, String courseId) throws SQLException {
    String query = "UPDATE student_course SET questions_asked_curr_project = 0 WHERE student_id = ? AND course_id = ?;";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, login);
    ps.setString(2, courseId);
    ps.executeUpdate();
    ps.close();
  }
  
  // SCHEMAS
  // String schema =
  // "CREATE TABLE course(course_id TEXT PRIMARY KEY, course_title TEXT);";
  // schema =
  // "CREATE TABLE assignment(assignment_name TEXT, start_date DATE, end_date DATE, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
  // schema =
  // "CREATE TABLE exam(exam_name TEXT, start_date DATE, end_date DATE, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
  // schema =
  // "CREATE TABLE lab(lab_name TEXT, start_date DATE, end_date DATE, course_id TEXT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
  // schema =
  // "CREATE TABLE account(login TEXT PRIMARY KEY, name TEXT, email TEXT, password TEXT, contact_method TEXT);";
  // schema =
  // "CREATE TABLE questions(question TEXT, course_id TEXT, assessment_name TEXT, count INT, FOREIGN KEY(course_id) REFERENCES course(course_id));";
  // schema =
  // CREATE TABLE student_course(student_id TEXT, course_id TEXT, questions_asked INT, questions_asked_curr_project INT, last_project TEXT, FOREIGN KEY(student_id) REFERENCES account(login), FOREIGN KEY(course_id) REFERENCES course(course_id));
  // schema =
  // "CREATE TABLE ta_course(ta_id TEXT, course_id TEXT, FOREIGN KEY(ta_id) REFERENCES account(login), FOREIGN KEY(course_id) REFERENCES course(course_id));";
  // 
  // ATTENDANCE TABLE NOT CREATED
  // schema = "CREATE TABLE attendance(student_id TEXT, time TEXT);";
}