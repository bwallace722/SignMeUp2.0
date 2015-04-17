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
  private Map<String, Queue> onHoursQueue;
  
  public QueueHandler(Database db) {
    
    QueueHandler.db = db;
    onHoursQueue = new HashMap<String, Queue>();
    runSpark();
  }
  
  
  public void runSpark() {
//    Spark.setPort(4567);
//    Spark.externalStaticFileLocation("src/main/resources/static");
//    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.get("/confirmAppointment", new AppointmentHandler());
    Spark.get("/signUpForHours/:courseAndUserId", new StudentSignUpForHours(),
        new FreeMarkerEngine());
    Spark.post("/addStudentToQueue", new AddStudentToQueue());
    Spark.post("/labCheckOff/:login", new AddLabCheckoffToQueue());
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
      int toReturn = 0;
//      JSONParser parser = new JSONParser();
      try {
//        JSONObject queueEntry = (JSONObject) parser.parse(req.body());
//        String course = (String) queueEntry.get("course");
//
//        String login = (String) queueEntry.get("login");
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
        toReturn = 1;
      } catch ( SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
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
//      JSONParser parser = new JSONParser();
      try {
//        JSONObject queueEntry = (JSONObject) parser.parse(req.body());
//        String course = (String) queueEntry.get("course");
//
//        String login = (String) queueEntry.get("login");
        Queue q;
        if (onHoursQueue.containsKey(course)) {
          q = onHoursQueue.get(course);
        } else {
          onHoursQueue.put(course, new Queue());
          q = onHoursQueue.get(course);
        }
        q.add(db.getAccountByLogin(login, null));
        toReturn = 1;
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      //TODO what is the success and fail markers?
      return toReturn;
    }
  }
  
  private static class AppointmentHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
//      JSONParser parser = new JSONParser();
//      try {
//        JSONObject apt = (JSONObject) parser.parse(req.body());
//        Time appointmentTime = (Time) apt.get("time");
//      } catch (ParseException e) {
//        System.out.println("ERROR: "
//            + e.getMessage());
//      }
      return null;
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
      String[] reqParams = courseAndUserId.split("~");
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
          .put("course", courseId).put("login", login)
          .put("assignment", assignment).put("questions", questions)
          .put("subQuestions", subQuestions).build();
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
