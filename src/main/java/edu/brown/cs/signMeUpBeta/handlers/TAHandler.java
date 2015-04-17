package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.main.RunningHours;
import edu.brown.cs.signMeUpBeta.onhours.Queue;
import edu.brown.cs.signMeUpBeta.project.Question;
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
  private static RunningHours runningHours;
  public TAHandler(Database db, RunningHours hours) {
    this.db = db;
    this.runningHours = hours;
    runSpark();
  }
  public void runSpark() {
    // Spark.setPort(4567);
    // Spark.externalStaticFileLocation("src/main/resources/static");
    // Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.get("/taHoursSetUp/:courseId", new TAHoursSetUpHandler(),
        new FreeMarkerEngine());
    Spark.post("/addQuestionForHours/:courseId", new AddQuestionForHours());
    Spark.post("/setHoursTimeLimit/:courseId", new SetHoursTimeLimit());
    Spark.get("/onHours/:courseId", new TAOnHoursHandler(),
        new FreeMarkerEngine());
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
      QueryParamsMap qm = req.queryMap();
      String courseName = qm.value("name");
      try {
        db.addCourse(courseId, courseName);
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return new ModelAndView(variables, "taCourseSetUp.html");
    }
  }
  /**
   * This is the TA On Hours handler. From this page, the ta may call a student
   * to hours.
   * @author kj13
   */
  private class TAOnHoursHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      System.out.println(courseId);
      // initially sends the queue.
      Queue courseQueue = runningHours.getQueueForCourse(courseId);
      List<Question> questions =
          runningHours.getHoursForCourse(courseId).getQuestions();
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("queue", courseQueue).put("questions", questions)
              .build();
      return new ModelAndView(variables, "taOnHours.html");
    }
  }
  
  private class SetHoursTimeLimit implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      QueryParamsMap qm = req.queryMap();
      String newTimeLimit = qm.value("newTimeLimit");
      System.out.println(courseId);
      // send list of students on queue
      // send list of added students on queue? whichever is faster/better
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return null;
    }
  }
  private class AddQuestionForHours implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      QueryParamsMap qm = req.queryMap();
      String question = qm.value("newQuestion");
      String assessmentName = qm.value("name");
      Question questionObject;
      try {
        questionObject = db.addQuestion(assessmentName, question, courseId);
//        runningHours.getHoursForCourse(courseId);
        
        
      } catch (SQLException e) {
        System.out.println("ERROR: sql exception in adding question");
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).build();
      return 1;
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
