package edu.brown.cs.signMeUpBeta.handlers;

import java.sql.Date;
import java.sql.SQLException;

import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class AllHandlers {
  private static final Gson GSON = new Gson();
  private static Database db;
  public AllHandlers(Database db) {
    AllHandlers.db = db;
    runSparkServer();
  }
  private void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.get("/addAssignment", new AssessmentHandler("assignment"));
    Spark.get("/addLab", new AssessmentHandler("exam"));
    Spark.get("/addExam", new AssessmentHandler("lab"));
    Spark.get("/addClass", new CourseHandler());
    // Spark.get("/addStudent", new StudentHandler());
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
  private static class CourseHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("courseItem");
      JSONParser parser = new JSONParser();
      try {
        JSONArray courseArray = (JSONArray) parser.parse(course);
        JSONObject toInsert = (JSONObject) courseArray.get(0);
        String courseId = (String) toInsert.get("course_id");
        String courseName = (String) toInsert.get("course_name");
        db.addCourse(courseId, courseName);
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  // private static class StudentHandler implements Route {
  // @Override
  // public Object handle(Request req, Response res) {
  // QueryParamsMap qm = req.queryMap();
  // String student = qm.value("student");
  // JSONParser parser = new JSONParser();
  // try {
  // JSONArray studentArray = (JSONArray) parser.parse(student);
  // JSONObject toInsert = (JSONObject) studentArray.get(0);
  // String studentLogin = (String) toInsert.get("student_login");
  // String studentLogin = (String) toInsert.get("student_login")
  // db.addCourse(courseId, courseName);
  // } catch (SQLException | ParseException e) {
  // System.out.println("ERROR: "
  // + e.getMessage());
  // }
  // return null;
  // }
  // }
}