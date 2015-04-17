package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.onhours.Queue;
import edu.brown.cs.signMeUpBeta.student.Account;

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
  private Map<String, Queue> onHoursQueue;
  /**
   * This is te constructor for this class.
   * @param db
   */
  public AllHandlers(Database db) {
    onHoursQueue = new HashMap<String, Queue>();
    AllHandlers.db = db;
    runSparkServer();
  }
  // /**
  // * Creates a free marker engine.
  // * @return The created free marker engine.
  // */
  // private static FreeMarkerEngine createEngine() {
  // Configuration config = new Configuration();
  // File templates = new File("src/main/resources/spark/template/freemarker");
  // try {
  // config.setDirectoryForTemplateLoading(templates);
  // } catch (IOException ioe) {
  // System.out.printf("ERROR: Unable use %s for template loading.%n",
  // templates);
  // System.exit(1);
  // }
  // return new FreeMarkerEngine(config);
  // }
  private void runSparkServer() {
    Spark.setPort(4567);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    // FreeMarkerEngine freeMarker = createEngine();
    Spark.get("/home", new FrontHandler(), new FreeMarkerEngine());
    Spark.get("/classes/:login", new CourseHandler(), new FreeMarkerEngine());
    Spark.get("/addCourses/:login", new AddCourseHandler(),
        new FreeMarkerEngine());
     Spark.post("/signUp", new AccountSetupHandler());

    Spark.post("/updateCourse/:login", new UpdateCourseHandler());
    Spark.get("/welcomeStudent/:courseId", new StudentCoursePageHandler(),
        new FreeMarkerEngine());
    Spark.get("/taHoursSetUp/:courseId", new TAHoursSetUpHandler(),
        new FreeMarkerEngine());
    Spark.get("/confirmAppointment", new AppointmentHandler());
    Spark.get("/signUpForHours", new StudentSignUpForHours(),
        new FreeMarkerEngine());
    Spark.post("/addStudentToQueue", new AddStudentToQueue());
    Spark.post("/labCheckOff/:login", new AddLabCheckoffToQueue());
    Spark.post("/callStudent/:login", new CallStudentToHours());
    Spark.post("/updateQueue", new TAUpdateQueueHandler());
    Spark.post("/checkcallStatus", new StudentCheckCallStatus());
    Spark.get("/onHours/:courseId", new TAOnHoursHandler(),
        new FreeMarkerEngine());
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
      List<Object> classList = new ArrayList<Object>();
      try {
        List<String> studentClasses = db.getStudentClasses(login);
        List<String> taClasses = db.getTAClasses(login);
        for (String tClass : taClasses) {
          JSONObject taIn = new JSONObject();
          taIn.put("class", tClass);
          taIn.put("role", "TA");
          classList.add(taIn);
        }
        for (String sClass : studentClasses) {
          JSONObject studentIn = new JSONObject();
          studentIn.put("class", sClass);
          studentIn.put("role", "Student");
          classList.add(studentIn);
        }
        Map<String, Object> variables =
            new ImmutableMap.Builder().put("user_class_list", classList).put(
                "title", "SignMeUp 2.0").put("user", login).build();
        return new ModelAndView(variables, "myClasses.html");
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
    }
  }
  private class TAHoursSetUpHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return new ModelAndView(variables, "taHoursSetUp.html");
    }
  }
  /**
   * This is the TA Course Set Up handler.
   * @author kj13
   */
  private class TACourseSetUpHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      //TODO: KIERAN
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return new ModelAndView(variables, "taCourseSetUp.html");
    }
  }

  /**
   * This handler initially displays the signupforhours page.
   * It will display the assignment, questions, and subquestions
   * relevant to that student's course.
   * @author kj13
   *
   */
  private class StudentSignUpForHours implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseAndUserId = req.params(":courseAndUserId");
      String[] reqParams = courseAndUserId.split("?");
      System.out.println(courseAndUserId);
      String courseId = reqParams[0];
      String login = reqParams[1];
      
      //TODO send over all of the assignments, questions and subquestions
      String questions = ""; // to be sent as JSON array?
      //is it possible to send this with html tags?
      String assignment = ""; // string, or JSON object?
      String subQuestions = ""; //JSON array?
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0")
          .put("courseId", courseId).put("login", login)
          .put("assignment", assignment).put("questions", questions)
          .put("subQuestions", subQuestions).build();
      return new ModelAndView(variables, "signUpForHours.html");
    }
  }
  

  
  /**
   * This is the TA On Hours handler.
   * From this page, the ta may call a student to hours.
   * @author kj13
   */
  private class TAOnHoursHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      //initially sends the queue.
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return new ModelAndView(variables, "taOnHours.html");
    }
  }
  private class TAUpdateQueueHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      //send list of students on queue
      //send list of added students on queue? whichever is faster/better
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return null;
    }
  }

  
  /*
  * This handler will be used to call the student to hours.
  * Here, the student's call status will be updated.
  * @author kj13
  */
 private class CallStudentToHours implements Route {
   @Override
   public Object handle(final Request req, final Response res) {
     String courseId = req.params(":courseId");
     System.out.println(courseId);
     //CALL STUDENT TO HOURS
     //NEED WAY TO ALERT STUDENT
     Map<String, Object> variables =
         new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
             courseId).build();
     return null;
   }
 }
  /**
   * This is the handler that takes the 
   * student to a course's hours page.
   * @author kj13
   *
   */
  private class StudentCoursePageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseAndUserId = req.params(":courseAndUserId");
      String[] reqParams = courseAndUserId.split("?");
      System.out.println(courseAndUserId);
      String courseId = reqParams[0];
      String login = reqParams[1];
      
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).build();
      return new ModelAndView(variables, "welcomeStudent.html");
    }
  }
  /**
   * This is the handler that updates
   * a user's course list.
   * @author kj13
   */
  private class UpdateCourseHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String login = req.params(":login");
      JSONParser parser = new JSONParser();
      try {
        JSONObject queueEntry = (JSONObject) parser.parse(req.body());
        String course = (String) queueEntry.get("course");
        String role = (String) queueEntry.get("role");
        System.out.println(login + ": " + course + " , " + role);
        
     } catch (ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      int toReturn = 1;
      return toReturn;
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
   * This class handles the adding of lab check offs to the queue.
   * @author omadarik
   */
  private class AddLabCheckoffToQueue implements Route {
    @Override
    public Object handle(Request req, Response res) {
      JSONParser parser = new JSONParser();
      try {
        JSONObject queueEntry = (JSONObject) parser.parse(req.body());
        String course = (String) queueEntry.get("course");

        String login = (String) queueEntry.get("login");
        Queue q;
        if (onHoursQueue.containsKey(course)) {
          q = onHoursQueue.get(course);
          
        } else {
          onHoursQueue.put(course, new Queue());
          q = onHoursQueue.get(course);
        }
        //TODO: KIERAN, THINK...
        // Also doesn't it make more sense to include some sort of priority when
        // adding things to the queue? That way, it will be easier to control
        // the priority of things? This priority could be a flag, with
        // appointments getting the 'best' flag.
        q.add(db.getAccountByLogin(login, null));
      } catch (ParseException | SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      //TODO what is the success and fail markers?
      return null;
    }
  }
  
  /**
   * This class handles the adding of lab check offs to the queue.
   * @author omadarik
   */
  private class AddStudentToQueue implements Route {
    @Override
    public Object handle(Request req, Response res) {
      JSONParser parser = new JSONParser();
      try {
        JSONObject queueEntry = (JSONObject) parser.parse(req.body());
        String course = (String) queueEntry.get("course");

        String login = (String) queueEntry.get("login");
        Queue q;
        if (onHoursQueue.containsKey(course)) {
          q = onHoursQueue.get(course);
        } else {
          onHoursQueue.put(course, new Queue());
          q = onHoursQueue.get(course);
        }
        //TODO: KIERAN, THINK...
        // do we always need to get the password when getting an account from
        // the database? Why are we doing this?
        // Also doesn't it make more sense to include some sort of priority when
        // adding things to the queue? That way, it will be easier to control
        // the priority of things? This priority could be a flag, with
        // appointments getting the 'best' flag.
        q.add(db.getAccountByLogin(login, null));
      } catch (ParseException | SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      //TODO what is the success and fail markers?
      return null;
    }
  }
  /**
   * This class checks the student's call status
   * on the queue. If their call status has been change,
   * they will be alerted.
   * @author kj13
   */
  private class StudentCheckCallStatus implements Route {
    @Override
    public Object handle(Request req, Response res) {
      JSONParser parser = new JSONParser();
      try {
        JSONObject queueEntry = (JSONObject) parser.parse(req.body());
        String course = (String) queueEntry.get("course");

        String login = (String) queueEntry.get("login");
  
      } catch (ParseException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      return null;
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
        System.out.println("adding "+login+" to database");
        Account account = db.addAccount(login, name, email, password);
//        user = db.;
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
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("user",
              user.login()).put("courses", null).build();
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
