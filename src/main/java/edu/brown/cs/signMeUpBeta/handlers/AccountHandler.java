package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.student.Account;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class AccountHandler {
  private static final Gson GSON = new Gson();
  private static Database db;
  public AccountHandler(Database db) {
    AccountHandler.db = db;
    runSpark();
  }
  public void runSpark() {
    Spark.post("/signUp", new AccountSetupHandler());
    Spark.post("/login", new AccountLoginHandler());
    Spark.post("/updateCourse/:login", new UpdateCourseHandler());
    Spark.get("/courses/:login", new CourseListHandler(),
        new FreeMarkerEngine());
    Spark.get("/addCourses/:login", new AddCourseHandler(),
        new FreeMarkerEngine());
  }
  /**
   * This is the sign up handler that deals with creating a new user. From here,
   * the user will be directed to a list of their courses.
   * @author kj13
   */
  private class CourseListHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String login = req.params(":login");
      List<List<String>> classList = new ArrayList<List<String>>();
      try {
        List<String> studentClasses = db.getStudentClasses(login);
        List<String> taClasses = db.getTAClasses(login);
        classList.add(taClasses);
        classList.add(studentClasses);
        // for (String tClass : taClasses) {
        // JSONObject taIn = new JSONObject();
        // taIn.put("class", tClass);
        // taIn.put("role", "TA");
        // classList.add(taIn);
        // }
        // for (String sClass : studentClasses) {
        // JSONObject studentIn = new JSONObject();
        // studentIn.put("class", sClass);
        // studentIn.put("role", "Student");
        // classList.add(studentIn);
        // }
        Map<String, Object> variables =
            new ImmutableMap.Builder().put("userclasslist", classList).put(
                "title", "SignMeUp 2.0").put("user", login).build();
        return new ModelAndView(variables, "myClasses.html");
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  /**
   * This handler that takes the student to the page to add classes.
   * @author kj13
   */
  private class AddCourseHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String login = req.params(":login");
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("user",
              login).build();
      return new ModelAndView(variables, "addCourse.html");
    }
  }
  /**
   * This is the handler that updates a user's course list.
   * @author kj13
   */
  private class UpdateCourseHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String role = qm.value("role");
      String login = req.params(":login");
      try {
        if (role.equals("TA")) {
          db.addTACoursePair(login, course);
        } else {
          db.addStudentCoursePair(login, course);
        }
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
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
      String login = qm.value("login");
      String password = qm.value("password");
      System.out.println(login
          + " , "
          + password);
      // JSONParser parser = new JSONParser();
      Account loggedIn = null;
      try {
        /*
         * Retrieving a ta object.
         */
        // JSONObject credentials = (JSONObject) parser.parse(login);
        // String inputLogin = (String) credentials.get("student_login");
        // String inputPassword = (String) credentials.get("student_password");
        loggedIn = db.approveCredentials(login, password);
        System.out.println(loggedIn.getLogin()
            + "loged in");
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return loggedIn.getLogin();
    }
  }
  /**
   * This class handles the inserting of new student fields into the database.
   * @author omadarik
   */
  private static class AccountSetupHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      String login = qm.value("login");
      String email = qm.value("email");
      String password = qm.value("password");
      Account user = null;
      try {
        /*
         * Creating a student object.
         */
        db.addAccount(login, name, email, password);
        System.out.println(login
            + " - was added");
        user = db.getAccount(login);
        System.out.println(login
            + " - was found in db");
        /*
         * Adding the courses taken by a student to the database.
         */
        // JSONArray jsonEnrolledCourses =
        // (JSONArray) toInsert.get("student_courses");
        // for (int i = 0; i < jsonEnrolledCourses.size(); i++) {
        // String courseId = (String) jsonEnrolledCourses.get(i);
        // db.addStudentCoursePair(login, courseId);
        // }
        // JSONArray jsonTACourses = (JSONArray) toInsert.get("ta_courses");
        // for (int i = 0; i < jsonTACourses.size(); i++) {
        // String courseId = (String) jsonTACourses.get(i);
        // db.addTACoursePair(login, courseId);
        // }
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("user",
              user.getLogin()).put("courses", null).build();
      System.out.println(user.getLogin()
          + " - is being returned");
      return user.getLogin();
    }
  }
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
