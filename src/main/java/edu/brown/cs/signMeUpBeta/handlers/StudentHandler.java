package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.main.RunningHours;
import edu.brown.cs.signMeUpBeta.onhours.Queue;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class StudentHandler {
  private static final Gson GSON = new Gson();
  private static Database db;
  private RunningHours runningHours;
  public StudentHandler(Database db, RunningHours hours) {
    StudentHandler.db = db;
    this.runningHours = hours;
    runSpark();
  }
  public void runSpark() {
    Spark.get("/studentLanding/:courseId",
        new StudentCoursePageHandler(), new FreeMarkerEngine());
    Spark.post("/checkCallStatus", new StudentCheckCallStatus());
  }
  /**
   * This class checks the student's call status on the queue. If their call
   * status has been change, they will be alerted.
   * @author kj13
   */
  private class StudentCheckCallStatus implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String loginToCall = qm.value("login");
      Queue queue = runningHours.getQueueForCourse(course);
      int calledToHours = queue.calledToHours(loginToCall);
      return calledToHours;
    }
  }
  /**
   * This is the handler that takes the student to a course's hours page.
   * @author kj13
   */
  private class StudentCoursePageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      String login = req.cookie("login"); 
      System.out.println(login + " - login");
      res.cookie("login", login);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).build();
      return new ModelAndView(variables, "studentLanding.html");
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
