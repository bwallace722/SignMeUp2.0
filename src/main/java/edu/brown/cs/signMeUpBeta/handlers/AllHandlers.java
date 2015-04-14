package edu.brown.cs.signMeUpBeta.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;

import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.onhours.TA;
import edu.brown.cs.signMeUpBeta.student.Student;
import freemarker.template.Configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spark.ExceptionHandler;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class AllHandlers {
  private static final Gson GSON = new Gson();
  private static Database db;
  public AllHandlers(Database db) {
    AllHandlers.db = db;
    runSparkServer();
  }
  
  /**
   * Creates a free marker engine.
   * @return The created free marker engine.
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  
  private void runSparkServer() {
    Spark.setPort(4567);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/addAssignment", new AssessmentHandler("assignment"));
    Spark.get("/addLab", new AssessmentHandler("exam"));
    Spark.get("/addExam", new AssessmentHandler("lab"));
    Spark.get("/addNewCourse", new CourseSetupHandler());
    Spark.get("/addNewStudent", new StudentSetupHandler());
    Spark.get("/studentLogin", new StudentLoginHandler());
  }
  /**
   * This class handles the insertion of assessment items into the database
   * during class setup.
   * @author omadarik
   */
  private static class AssessmentHandler implements Route {
    private String tableName;
    private AssessmentHandler(String tableName) {
      this.tableName = tableName;
    }
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String assessmentItem = qm.value("assessmentItem");
      JSONParser parser = new JSONParser();
      try {
        JSONArray assArray = (JSONArray) parser.parse(assessmentItem);
        JSONObject toInsert = (JSONObject) assArray.get(0);
        String assName = (String) toInsert.get("assessment_name");
        Date start = (Date) toInsert.get("start_date");
        Date end = (Date) toInsert.get("end_date");
        String courseId = (String) toInsert.get("course_id");
        db.addAssessmentItem(tableName, assName, start, end, courseId);
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  /**
   * This class is where I add courses to the database.
   * @author omadarik
   */
  private static class CourseSetupHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("courseItem");
      JSONParser parser = new JSONParser();
      try {
        JSONArray courseArray = (JSONArray) parser.parse(course);
        for (int i = 0; i < courseArray.size(); i++) {
          JSONObject toInsert = (JSONObject) courseArray.get(i);
          String courseId = (String) toInsert.get("course_id");
          String courseName = (String) toInsert.get("course_name");
          db.addCourse(courseId, courseName);
        }
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  /**
   * This class handles the inserting of new student fields into the database.
   * @author omadarik
   */
  private static class StudentSetupHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String student = qm.value("new_student");
      JSONParser parser = new JSONParser();
      try {
        /*
         * Creating a student object.
         */
        JSONObject toInsert = (JSONObject) parser.parse(student);
        String studentLogin = (String) toInsert.get("student_login");
        String studentName = (String) toInsert.get("student_name");
        String studentEmail = (String) toInsert.get("student_email");
        String studentPassword = (String) toInsert.get("student_password");
        db.addStudent(studentLogin, studentName, studentEmail, studentPassword);
        /*
         * Adding the courses taken by a student to the database.
         */
        JSONArray jsonCourses = (JSONArray) toInsert.get("all_courses");
        for (int i = 0; i < jsonCourses.size(); i++) {
          String courseId = (String) jsonCourses.get(i);
          db.addStudentCoursePair(studentLogin, courseId);
        }
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  /**
   * This method handles the logging in of a student checking the input login
   * and password.
   * @author omadarik
   */
  private static class StudentLoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String login = qm.value("student_credentials");
      JSONParser parser = new JSONParser();
      try {
        /*
         * Retrieving a ta object.
         */
        JSONObject credentials = (JSONObject) parser.parse(login);
        String inputLogin = (String) credentials.get("student_login");
        String inputPassword = (String) credentials.get("student_password");
        Student loggedIn = db.getStudentByLogin(inputLogin, inputPassword);
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  /**
   * This method handles the logging in of a ta checking the input login and
   * password.
   * @author omadarik
   */
  private static class TALoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String login = qm.value("ta_credentials");
      JSONParser parser = new JSONParser();
      try {
        JSONObject credentials = (JSONObject) parser.parse(login);
        String inputLogin = (String) credentials.get("ta_login");
        String inputPassword = (String) credentials.get("ta_password");
        TA loggedIn = db.getTAByLogin(inputLogin, inputPassword);
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  
  /**
   * This class prints out errors if the spark server fails.
   * @author kb25
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    /**
     * This method prints the proper errors upon failure.
     */
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
