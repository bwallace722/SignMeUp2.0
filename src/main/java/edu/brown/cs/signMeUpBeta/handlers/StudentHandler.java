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

public class StudentHandler {
  private static final Gson GSON = new Gson();
  private static Database db;
  
  public StudentHandler(Database db) {
    StudentHandler.db = db;
    runSpark();
  }
  
  public void runSpark() {
//    Spark.setPort(4567);
//    Spark.externalStaticFileLocation("src/main/resources/static");
//    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.get("/studentLanding/:courseAndUserId", new StudentCoursePageHandler(),
        new FreeMarkerEngine());
    Spark.post("/checkcallStatus", new StudentCheckCallStatus());
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
     QueryParamsMap qm = req.queryMap();
     String course = qm.value("course");
     String loginToCall = qm.value("login");
//     JSONParser parser = new JSONParser();
//     try {
//       JSONObject queueEntry = (JSONObject) parser.parse(req.body());
//       String course = (String) queueEntry.get("course");
//
//       String login = (String) queueEntry.get("login");
 
//     } catch (ParseException e) {
//       System.out.println("ERROR: "
//           + e.getMessage());
//     }
     //TODO: look at queue
     //if the student's login has a new flag on it's object in the queue
     //send the message.
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
     String[] reqParams = courseAndUserId.split("~");
     
//
     System.out.println(courseAndUserId);
     String courseId = reqParams[0];
     String login = "";
     
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
