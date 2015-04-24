package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
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
   * This renders a page where the user can see a list of their courses, and be
   * bale to add new courses.
   * @author kj13
   */
  private class CourseListHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String login = req.params(":login");
      StringBuilder classList = new StringBuilder();
      try {
        List<String> studentClasses = db.getStudentClasses(login);
        List<String> taClasses = db.getTAClasses(login);
        if (studentClasses.size() == 0
            && taClasses.size() == 0) {
          String noClasses =
              "<h3>You've got no courses! Try adding a course!</h3>"
                  + "<a class=\"btn btn-primary btn-lg\" onclick=\"addCourses()\""
                  + "id=\"addCourseBtn\">Add a Course</a>";
          classList.append(noClasses);
        } else {
          String tableTags =
              "<table class=\"table table-hover\" id=\"courseTable\">"
                  + "<thead><tr><th>Course</th><th>Position</th></tr></thead><tbody id=\"courseTableBody\">";
          String closeTableTags = "</tbody></table>"
              + "<a class=\"btn btn-primary btn-sm\""
              + "onclick=\"addCourses()\" id=\"addCourseBtn\">Add a Course</a>";
          classList.append(tableTags);
          String startTags = "<tr class=\"clickable-row\">"
              + "<td class=\"courseId\">";
          String middleTags = "</td><td>";
          String endTags = "</td></tr>";
          for (String tClass : taClasses) {
            String line = startTags
                + tClass
                + middleTags
                + "TA"
                + endTags;
            classList.append(line);
          }
          for (String sClass : studentClasses) {
            String line = startTags
                + sClass
                + middleTags
                + "Student"
                + endTags;
            classList.append(line);
          }
          classList.append(closeTableTags);
        }
        Map<String, Object> variables =
            new ImmutableMap.Builder().put("userCourseList",
                classList.toString()).put("title", "SignMeUp 2.0").put("user",
                login).build();
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
      // <option value="cs015">CS015</option>
      // <option value="cs016">CS016</option>
      // <option value="cs017">CS017</option>
      // <option value="cs018">CS018</option>
      // <option value="cs019">CS019</option>
      // <option value="cs022">CS022</option>
      // <option value="cs032">CS032</option>
      List<String> courses;
      try {
        courses = db.getAllCourses();
      } catch (Exception e) {
        // GO TO SQLEXCEPTION PAGE
        return null;
      }
      String html = "";
      for (String c : courses) {
        html = html.concat("<option value=\""
            + c
            + "\">"
            + c.toUpperCase()
            + "</option>");
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("user",
              login).put("allCoursesHTML", html).build();
      return new ModelAndView(variables, "addCourse.html");
    }
  }
  /**
   * This is the handler that updates a user's course list.
   * @author omadarik
   */
  private class UpdateCourseHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("courseSelected");
      String role = qm.value("role");
      String login = req.params(":login");
      String response = null;
      try {
        if (role.equals("TA")) {
          response = db.addTACoursePair(login, course);
        } else {
          response = db.addStudentCoursePair(login, course);
        }
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return response;
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
      Account loggedIn = null;
      String toReturn = "account does not exist";
      System.out.println(login + " - " + password);
      try {
        loggedIn = db.approveCredentials(login, password);
        if(loggedIn == null) {
          return toReturn;
        }
        System.out.println(loggedIn.getLogin());
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
        System.out.println("not exist");
        return toReturn;
      }
      toReturn = loggedIn.getLogin();
      return toReturn;
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
        user = db.addAccount(login, name, email, password);
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return "account exists";
      }
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
