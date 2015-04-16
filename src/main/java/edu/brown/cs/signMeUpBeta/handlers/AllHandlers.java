package edu.brown.cs.signMeUpBeta.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.student.Account;
import freemarker.template.Configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class AllHandlers {
  private static final Gson GSON = new Gson();
  private static Database db;
  public AllHandlers(Database db) {
    AllHandlers.db = db;
    runSparkServer();
  }
//  /**
//   * Creates a free marker engine.
//   * @return The created free marker engine.
//   */
//  private static FreeMarkerEngine createEngine() {
//    Configuration config = new Configuration();
//    File templates = new File("src/main/resources/spark/template/freemarker");
//    try {
//      config.setDirectoryForTemplateLoading(templates);
//    } catch (IOException ioe) {
//      System.out.printf("ERROR: Unable use %s for template loading.%n",
//          templates);
//      System.exit(1);
//    }
//    return new FreeMarkerEngine(config);
//  }
  private void runSparkServer() {
    Spark.setPort(4567);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
//    FreeMarkerEngine freeMarker = createEngine();
    Spark.get("/home", new FrontHandler(), new FreeMarkerEngine());
    Spark.get("/classes/:login", new CourseHandler(), new FreeMarkerEngine());
    Spark.get("/addCourses/:login", new AddCourseHandler(), new FreeMarkerEngine());
//    Spark.post("/signUp", new AccountSetupHandler());
    Spark.post("/updateCourse/:login", new UpdateCourseHandler());
    Spark.post("/signUp", new SignUpHandler());
    Spark.get("/welcomeStudent/:courseId", new StudentCoursePageHandler(), new FreeMarkerEngine());
    Spark.get("/taHoursSetUp/:courseId", new TAHoursSetUpHandler(), new FreeMarkerEngine());
    Spark.get("/confirmAppointment", new AppointmentHandler());
    Spark.get("/signUpForHours", new StudentSignUpForHours(), new FreeMarkerEngine());
    Spark.post("/labCheckOff/:login", new LabCheckOffHandler());
    // Spark.get("/addAssignment", new AssessmentHandler("assignment"));

    // Spark.get("/addLab", new AssessmentHandler("exam"));
    // Spark.get("/addExam", new AssessmentHandler("lab"));
    // Spark.get("/addNewCourse", new CourseSetupHandler());
    // Spark.get("/addNewStudent", new StudentSetupHandler());
    // Spark.get("/addNewTA", new TASetupHandler());
    // Spark.get("/studentLogin", new StudentLoginHandler());
  }
  /**
   * This is the front handler, which sends the user to the landing page. From
   * here they may either sign in or sign up.
   * @author kj13
   */
  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").build();
      return new ModelAndView(variables, "landingPage.html");
    }
  }
  
  /**
   * This is the sign up handler that deals with creating a new user. From here,
   * the user will be directed to a list of their courses.
   * @author kj13
   */
  private class CourseHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {

      String login = req.params(":login");
      //GET USERS CLASSES
      
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0").put("user", login).build();

      return new ModelAndView(variables, "myClasses.html");
    }
  }
  
  private class TAHoursSetUpHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {

      String courseId = req.params(":courseId");
      System.out.println(courseId);
      
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0")
          .put("course", courseId).build();

      return new ModelAndView(variables, "taHoursSetUp.html");
    }
  }
  
  
  
  private class StudentCoursePageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {

      String courseId = req.params(":courseId");
      System.out.println(courseId);
      
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0")
          .put("course", courseId).build();

      return new ModelAndView(variables, "welcomeStudent.html");
    }
  }
  
  private class LabCheckOffHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String login = req.params(":login");

      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").build();
      return null;
    }
  }
  
  private class StudentSignUpForHours implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0").build();

      return new ModelAndView(variables, "signUpForHours.html");
    }
  }
  
  
  /**
   * This is the sign up handler that deals with creating a new user. From here,
   * the user will be directed to a list of their courses.
   * @author kj13
   */
  private class SignUpHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      String login = qm.value("login");
      String email = qm.value("email");
      String password = qm.value("password");
      Account user = new Account(login, name, email, password);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("user",
              login).build();
      return variables;
    }
  }
  
  
  /**
   * This is the front handler, which initially builds the site.
   * @author kj13
   */
  private class UpdateCourseHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {

      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String role = qm.value("role");
      
      System.out.println(course + " , " + role);
      int toReturn = 1;
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0").build();

      return toReturn;
    }
  }
  
  /**
   * This is the front handler, which initially builds the site.
   * @author kj13
   */
  private class AddCourseHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String login = req.params(":login");
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0")
          .put("user", login).build();

      return new ModelAndView(variables, "addCourse.html");
    }
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
      JSONParser parser = new JSONParser();
      try {
        JSONArray assArray = (JSONArray) parser.parse(req.body());
        JSONObject success = new JSONObject();
        success.put("success", 1);
        for (int i = 0; i < assArray.size(); i++) {
          JSONObject toInsert = (JSONObject) assArray.get(i);
          String assName = (String) toInsert.get("name");
          Date start = (Date) toInsert.get("start");
          Date end = (Date) toInsert.get("end");
          String courseId = (String) toInsert.get("course");
          db.addAssessmentItem(tableName, assName, start, end, courseId);
          Map<String, Object> variables =
              new ImmutableMap.Builder<String, Object>()
                  .put("success", success).build();
          // In the event of some error, null will be returned
          return GSON.toJson(variables);
        }
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
      JSONParser parser = new JSONParser();
      try {
        JSONArray courseArray = (JSONArray) parser.parse(req.body());
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
  private static class AccountSetupHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      JSONParser parser = new JSONParser();
      Account user = null;
      try {
        /*
         * Creating a student object.
         */
        JSONObject toInsert = (JSONObject) parser.parse(req.body());
        String login = (String) toInsert.get("login");
        String name = (String) toInsert.get("name");
        String email = (String) toInsert.get("email");
        String password = (String) toInsert.get("password");
        db.addAccount(login, name, email, password);
        user = new Account(login, name, email, password);
        /*
         * Adding the courses taken by a student to the database.
         */
        JSONArray jsonEnrolledCourses =
            (JSONArray) toInsert.get("student_courses");
        for (int i = 0; i < jsonEnrolledCourses.size(); i++) {
          String courseId = (String) jsonEnrolledCourses.get(i);
          db.addStudentCoursePair(login, courseId);
        }
        JSONArray jsonTACourses = (JSONArray) toInsert.get("ta_courses");
        for (int i = 0; i < jsonTACourses.size(); i++) {
          String courseId = (String) jsonTACourses.get(i);
          db.addTACoursePair(login, courseId);
        }
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }

      
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("title", "SignMeUp 2.0").put("user", user.login())
          .put("courses", null).build();

      return user.login();
    }
  }
  // /**
  // * This class handles the inserting of new TA fields into the database.
  // * @author omadarik
  // */
  // private static class TASetupHandler implements Route {
  // @Override
  // public Object handle(Request req, Response res) {
  // QueryParamsMap qm = req.queryMap();
  // String ta = qm.value("new_ta");
  // JSONParser parser = new JSONParser();
  // try {
  // /*
  // * Creating a ta object.
  // */
  // JSONObject toInsert = (JSONObject) parser.parse(ta);
  // String taLogin = (String) toInsert.get("ta_login");
  // String taName = (String) toInsert.get("ta_name");
  // String taEmail = (String) toInsert.get("ta_email");
  // String taPassword = (String) toInsert.get("ta_password");
  // db.addTA(taLogin, taName, taEmail, taPassword);
  // /*
  // * Adding the courses taken by a student to the database.
  // */
  // JSONArray jsonCourses = (JSONArray) toInsert.get("all_courses");
  // for (int i = 0; i < jsonCourses.size(); i++) {
  // String courseId = (String) jsonCourses.get(i);
  // db.addTACoursePair(taLogin, courseId);
  // }
  // } catch (SQLException | ParseException e) {
  // System.out.println("ERROR: "
  // + e.getMessage());
  // }
  // return null;
  // }
  // }
  private static class AppointmentHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      JSONParser parser = new JSONParser();
      try {
        JSONObject apt = (JSONObject) parser.parse(req.body());
        Time appointmentTime = (Time) apt.get("time");
      } catch (ParseException e) {
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
  private static class AccountLoginHandler implements Route {
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
        Account loggedIn = db.getAccountByLogin(inputLogin, inputPassword);
      } catch (SQLException | ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  // /**
  // * This method handles the logging in of a ta checking the input login and
  // * password.
  // * @author omadarik
  // */
  // private static class TALoginHandler implements Route {
  // @Override
  // public Object handle(Request req, Response res) {
  // QueryParamsMap qm = req.queryMap();
  // String login = qm.value("ta_credentials");
  // JSONParser parser = new JSONParser();
  // try {
  // JSONObject credentials = (JSONObject) parser.parse(login);
  // String inputLogin = (String) credentials.get("ta_login");
  // String inputPassword = (String) credentials.get("ta_password");
  // TA loggedIn = db.getTAByLogin(inputLogin, inputPassword);
  // } catch (SQLException | ParseException e) {
  // System.out.println("ERROR: "
  // + e.getMessage());
  // }
  // return null;
  // }
  // }
  /**
   * This class prints out errors if the spark server fails.
   * @author kb25
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