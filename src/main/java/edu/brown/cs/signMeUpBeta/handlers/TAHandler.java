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

public class TAHandler {

  private static final Gson GSON = new Gson();
  private static Database db;
  
  public TAHandler(Database db) {
    
    TAHandler.db = db;
    runSpark();
  }
  
  public void runSpark() {
//    Spark.setPort(4567);
//    Spark.externalStaticFileLocation("src/main/resources/static");
//    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.get("/taHoursSetUp/:courseId", new TAHoursSetUpHandler(),
        new FreeMarkerEngine());
    Spark.get("/onHours/:courseId", new TAOnHoursHandler(),
        new FreeMarkerEngine());
    Spark.post("/updateQueue", new TAUpdateQueueHandler());
    Spark.get("/courseSetUp/:courseId", new TACourseSetUpHandler(), 
        new FreeMarkerEngine());
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

      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return new ModelAndView(variables, "taCourseSetUp.html");
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