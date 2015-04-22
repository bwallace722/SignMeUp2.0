package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.main.RunningHours;
import edu.brown.cs.signMeUpBeta.onhours.Hours;
import edu.brown.cs.signMeUpBeta.onhours.Queue;
import edu.brown.cs.signMeUpBeta.project.Question;
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

public class QueueHandler {
  private static final Gson GSON = new Gson();
  private static Database db;
  private RunningHours runningHours;
  public QueueHandler(Database db, RunningHours hours) {
    QueueHandler.db = db;
    this.runningHours = hours;
    runSpark();
  }
  public void runSpark() {
    // Spark.setPort(4567);
    // Spark.externalStaticFileLocation("src/main/resources/static");
    // Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.get("/confirmAppointment", new AppointmentHandler());
    Spark.get("/signUpForHours/:courseAndUserId", new StudentSignUpForHours(),
        new FreeMarkerEngine());
    Spark.post("/startHours/:courseId", new StartCourseHours());
    Spark.post("/addStudentToQueue", new AddStudentToQueue());
    Spark.post("/labCheckOff/:login", new AddLabCheckoffToQueue());
    Spark.post("/updateQueue/:courseId", new UpdateQueueHandler());
    Spark.post("/callStudent/:courseId", new CallStudentToHours());
  }
  /**
   * This class handles the adding of lab check offs to the queue.
   * @author omadarik
   */
  private class AddLabCheckoffToQueue implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String login = qm.value("login");
      // are we being passed the student's password too? I'll need it to get
      // their account
      int toReturn = 0;
      Queue queue = runningHours.getQueueForCourse(course);
      Account account;
      try {
        account = db.getAccount(login);
      } catch (Exception e) {
        System.err.println("ERROR: sql error on add lab checkoff");
        return 0;
      }
      // TODO: Calculate and set student priority field;
      account.setPriority(0);
      if (queue == null) {
        // FUCK
      }
      queue.add(account);
      toReturn = 1;
      return toReturn;
    }
  }
  /*
   * This handler will be used to call the student to hours. Here, the student's
   * call status will be updated.
   * @author kj13
   */
  private class CallStudentToHours implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      QueryParamsMap qm = req.queryMap();
      String studentLogin = qm.value("studentLogin");
      String message = qm.value("message");
      // CALL STUDENT TO HOURS
      // NEED WAY TO ALERT STUDENT
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
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
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String login = qm.value("login");
      System.out.println(course + " , " + login);
      int toReturn = 0;
      Queue queue = runningHours.getQueueForCourse(course);
      Account account;
      System.out.println(course + " ,2 " + login);
      try {
        account = db.getAccount(login);
      } catch (Exception e) {
        System.err.println("ERROR: sql error on add lab checkoff");
        return 0;
      }
      // TODO: Calculate and set student priority field;
      account.setPriority(Math.random());
      System.out.println(course + " ,3 " + login);
      if (queue == null) {
        // Alert student that hours are not running
      }
      queue.add(account);
      toReturn = 1;
      // TODO what is the success and fail markers?
      System.out.println("here");
      return toReturn;
    }
  }
  private class UpdateQueueHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId
          + " updating queue");
      Queue currentQueue = runningHours.getQueueForCourse(courseId);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("queue", currentQueue).build();
      return 1;
    }
  }
  private class StartCourseHours implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId
          + " starting hrs");
      // TODO return 1 if queue object was created
      // return 0 if there was a problem.
      return runningHours.startHours(courseId);
    }
  }
  private static class AppointmentHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      // JSONParser parser = new JSONParser();
      // try {
      // JSONObject apt = (JSONObject) parser.parse(req.body());
      // Time appointmentTime = (Time) apt.get("time");
      // } catch (ParseException e) {
      // System.out.println("ERROR: "
      // + e.getMessage());
      // }
      return null;
    }
  }
  /**
   * This handler initially displays the signupforhours page. It will display
   * the assignment, questions, and subquestions relevant to that student's
   * course.
   * @author kj13
   */
  private class StudentSignUpForHours implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseAndUserId = req.params(":courseAndUserId");
      String[] reqParams = courseAndUserId.split("~");
      System.out.println(courseAndUserId);
      String courseId = reqParams[0];
      String login = reqParams[1];
      Hours hours =
          runningHours.getHoursForCourse(courseId);
      List<Question> questions = new ArrayList<Question>();
      System.out.println(courseId + " - now");
      boolean running = false;
      if(hours != null) {
        running = true;
        questions = hours.getQuestions();
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).put("questions", questions).put("running", running)
              .build();
      return new ModelAndView(variables, "signUpForHours.html");
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
