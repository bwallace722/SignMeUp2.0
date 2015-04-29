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
    Spark.post("/confirmAppointment", new ConfirmAppointmentHandler());
    Spark.get("/makeAppointment/:courseIdAndUserId", new MakeAppointmentHandler(), new FreeMarkerEngine());
    Spark.get("/signUpForHours/:courseAndUserId", new StudentSignUpForHours(),
        new FreeMarkerEngine());
    Spark.post("/startHours/:courseId", new StartCourseHours());
    Spark.post("/addStudentToQueue", new AddStudentToQueue());
    Spark.post("/labCheckOff/:login", new AddLabCheckoffToQueue());
    Spark.post("/updateQueue/:courseId", new UpdateQueueHandler());
    Spark.post("/callStudent/:courseId", new CallStudentToHours());
    Spark.post("/checkQueue", new QueueChecker());
  }
  /**
   * This handler checks to see if the hours for a particular class have started
   * running yet.
   * @author omadarik
   */
  private class QueueChecker implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      Queue queue = runningHours.getQueueForCourse(course);
      if (queue == null) {
        return 0;
      }
      return 1;
    }
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
      account.setPriorityMultiplier(true);
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
      int toReturn = 0;
      String courseId = req.params(":courseId");
      QueryParamsMap qm = req.queryMap();
      String studentLogin = qm.value("studentLogin");
      String message = qm.value("message");
      Queue queue = runningHours.getQueueForCourse(courseId);
      queue.callOffQueue(studentLogin);
      toReturn = 1;
      return toReturn;
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
      account.setPriorityMultiplier(false);
      queue.add(account);
      toReturn = 1;
      return toReturn;
    }
  }
  private class UpdateQueueHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      Queue currentQueue = runningHours.getQueueForCourse(courseId);
      List<String> toReturn = currentQueue.getStudentsInOrder();
      for (String s : toReturn) {
        System.out.println(s);
      }
      return toReturn;
    }
  }
  private class StartCourseHours implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String courseId = req.params(":courseId");
      return runningHours.startHours(courseId);
    }
  }
  private static class ConfirmAppointmentHandler implements Route {
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
  private class MakeAppointmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseAndUserId = req.params(":courseAndUserId");
      String[] reqParams = courseAndUserId.split("~");
      String courseId = reqParams[0];
      String login = reqParams[1];
      String timesHTMLTags = "<button class=\"aptTime btn btn-success btn-lg\">";
      String closeTag = "</button>";
      //NEEDED: AVAILABLE APPOINTMENT TIMES 
      List<String> availTimes = new ArrayList<String>();
      StringBuilder timesHTML = new StringBuilder();
      for(String a : availTimes) {
        String t = timesHTMLTags + a + closeTag;
        timesHTML.append(t);
      }
      //NEEDED: SUBQUESTIONS PER QUESTION
      Hours hours = runningHours.getHoursForCourse(courseId);
      List<Question> questions = new ArrayList<Question>();
      boolean running = false;
      if (hours != null) {
        running = true;
        questions = hours.getQuestions();
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).put("aptTimes", timesHTML)
              .put("questions", questions).put("running", running).build();
      return new ModelAndView(variables, "signUpForHours.html");
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
      String courseId = reqParams[0];
      String login = reqParams[1];
      Hours hours = runningHours.getHoursForCourse(courseId);
      List<Question> questions = new ArrayList<Question>();
      boolean running = false;
      if (hours != null) {
        running = true;
        questions = hours.getQuestions();
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).put("questions", questions).put(
              "running", running).build();
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
