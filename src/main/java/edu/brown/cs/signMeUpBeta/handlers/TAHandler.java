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
import edu.brown.cs.signMeUpBeta.main.RunningHours;
import edu.brown.cs.signMeUpBeta.onhours.Hours;
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
    Spark.get("/taHoursSetUp/:courseId", new TAHoursSetUpHandler(),
        new FreeMarkerEngine());
    Spark.post("/addQuestionForHours/:courseId", new AddQuestionForHours());
    Spark.post("/setHoursTimeLimit/:courseId", new SetHoursTimeLimit());
//    Spark.post("/getQuestions/:courseId", new GetQuestionHandler(),
//        new FreeMarkerEngine());
    Spark.get("/onHours/:courseId", new TAOnHoursHandler(),
        new FreeMarkerEngine());
    Spark.get("/courseSetUp/:courseId", new TACourseSetUpHandler(),
        new FreeMarkerEngine());
    Spark.post("/saveAssignment", new SaveAssignment());
    Spark.post("/saveExam", new SaveExam());
    Spark.post("/saveLab", new SaveLab());
    Spark.post("/addCourse/:courseId", new AddCourseToDatabase());
    Spark.get("/createCourse", new CreateCourseHandler(),
        new FreeMarkerEngine());
  }
  private class SaveAssignment implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String name = qm.value("name");
      String start = qm.value("start");
      String end = qm.value("end");
      String[] splitStart = start.split("/");
      String[] splitEnd = end.split("/");
      String startDate = splitStart[2]
          + "-"
          + splitStart[0]
          + "-"
          + splitStart[1];
      String endDate = splitEnd[2]
          + "-"
          + splitEnd[0]
          + "-"
          + splitEnd[1];
      try {
        db.removeAssessmentsByCourse("assignment", course);
        db.addAssessmentItem("assignment", name, startDate, endDate, course);
      } catch (Exception e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
    }
  }
  private class SaveExam implements Route {
    @Override
    public Object handle(Request req, Response res) {
      System.out.println("in exam");
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String name = qm.value("name");
      String start = qm.value("start");
      String end = qm.value("end");
      String[] splitStart = start.split("/");
      String[] splitEnd = end.split("/");
      String startDate = splitStart[2]
          + "-"
          + splitStart[0]
          + "-"
          + splitStart[1];
      String endDate = splitEnd[2]
          + "-"
          + splitEnd[0]
          + "-"
          + splitEnd[1];
      try {
        db.removeAssessmentsByCourse("assignment", course);
        db.addAssessmentItem("exam", name, startDate, endDate, course);
      } catch (Exception e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
    }
  }
  private class SaveLab implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String name = qm.value("name");
      String start = qm.value("start");
      String end = qm.value("end");
      String[] splitStart = start.split("/");
      String[] splitEnd = end.split("/");
      String startDate = splitStart[2]
          + "-"
          + splitStart[0]
          + "-"
          + splitStart[1];
      String endDate = splitEnd[2]
          + "-"
          + splitEnd[0]
          + "-"
          + splitEnd[1];
      try {
        db.removeAssessmentsByCourse("assignment", course);
        db.addAssessmentItem("lab", name, startDate, endDate, course);
      } catch (Exception e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
    }
  }
  private class CreateCourseHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").build();
      return new ModelAndView(variables, "taCreateClass.html");
    }
  }
  private class TAHoursSetUpHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      String currAss = "none";
      List<Question> questions = new ArrayList<Question>();
      try {
        currAss= db.getCurrAssessment(courseId);
        questions = db.getQuestions(courseId, currAss);
      } catch (Exception e) {
        System.err.println(e);
      }
      String qStartTags = "<h6>";
      String qEndTags = "</h6>";
      StringBuilder qs = new StringBuilder();
      for (Question q : questions) {
        qs.append(qStartTags
            + q.content()
            + qEndTags);
      }
      
//      System.out.println(courseId);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0")
          .put("currAss", currAss).put("questions", qs.toString())
          .put("course",courseId).build();
      return new ModelAndView(variables, "taHoursSetUp.html");
    }
  }
  private class AddCourseToDatabase implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String courseId = req.params(":courseId");
      QueryParamsMap qm = req.queryMap();
      String courseName = qm.value("courseName");
      try {
        db.addCourse(courseId, courseName);
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
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
      // initially sends the queue.
      // to be sent: list of students in the queue, list of current questions,
      // list of popular questions, list of clinic suggestions.
      Hours hours = runningHours.getHoursForCourse(courseId);
      List<Question> questions = null;
      int timeLim = 10;
      if (hours != null) {
        questions = hours.getQuestions();
        timeLim = hours.getTimeLim();
      }
      StringBuilder questionsStr = getQuestions(questions);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("questions", questionsStr.toString())
              .put("time", timeLim).build();
      return new ModelAndView(variables, "taOnHours.html");
    }
  }
  private StringBuilder getQuestions(List<Question> questions) {
    String qStartTags = "<div class=\"row question\">";
    String qEndTags = "</div><br><hr>";
    StringBuilder qs = new StringBuilder();
    for (Question q : questions) {
      qs.append(qStartTags
          + q.content()
          + qEndTags);
    }
    return qs;
  }
  
  private class SetHoursTimeLimit implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      QueryParamsMap qm = req.queryMap();
      String newTimeLimit = qm.value("newTimeLimit");
      Hours hours = runningHours.getHoursForCourse(courseId);
      hours.setTimeLim(Integer.parseInt(newTimeLimit));
//      System.out.println(courseId);
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
      String currProject = "fix"; //TODO: KAMILLE: make the front end store the curr project at some point
      try {
        questionObject = db.addQuestion(assessmentName, question, courseId, currProject);
      } catch (SQLException e) {
        System.out.println("ERROR: sql exception in adding question");
        return 0;
      }
      return 1;
    }
  }
  
//  private class GetQuestionHandler implements TemplateViewRoute {
//    @Override
//    public ModelAndView handle(final Request req, final Response res) {
//      String courseId = req.params(":courseId");
//      List<Question> questions;
//      try {
//        questions = db.getCourseQuestions(courseId);
//      } catch (Exception e) {
//        System.err.println("ERROR: "+e);
//      }
//      Map<String, Object> variables =
//          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").build();
//      return new ModelAndView(variables, "taCreateClass.html");
//    }
//  }
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
