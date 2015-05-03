package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.main.RunningHours;
import edu.brown.cs.signMeUpBeta.onhours.Hours;
import edu.brown.cs.signMeUpBeta.project.Question;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
    // Spark.post("/getQuestions/:courseId", new GetQuestionHandler(),
    // new FreeMarkerEngine());
    Spark.get("/onHours/:courseId", new TAOnHoursHandler(),
        new FreeMarkerEngine());
    Spark.get("/courseSetUp/:courseId", new TACourseSetUpHandler(),
        new FreeMarkerEngine());
    Spark.post("/saveAssignment", new SaveAssignment());
    Spark.post("/saveExam", new SaveExam());
    Spark.post("/saveLab", new SaveLab());
    Spark.post("/addCourse/:courseId", new AddCourseToDatabase());
    Spark.get("/createCourse/:login", new CreateCourseHandler(),
        new FreeMarkerEngine());
    Spark.get("/editCourse/:courseId", new EditCourseHandler(),
        new FreeMarkerEngine());
    Spark.post("/removeAssessmentItem", new RemoveAssessmentItem());
    Spark.post("/emailStudent", new EmailStudent());
  }
  private class EmailStudent implements Route {
    @Override
    public Object handle(Request req, Response res) {
      Properties props = new Properties();
      Session session = Session.getDefaultInstance(props, null);
      QueryParamsMap qm = req.queryMap();
      String messageBody = qm.value("message");
      String studentLogin = qm.value("studentLogin");
      String taEmail = qm.value("taEmail");
      String recipient;
      try {
        recipient = db.getStudentEmail(studentLogin);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(taEmail));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
            recipient));
        msg.setSubject("You're Up For Hours!");
        msg.setText(messageBody);
        Transport.send(msg);
      } catch (SQLException | MessagingException e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
    }
  }
  private class RemoveAssessmentItem implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String table = qm.value("table");
      String assignmentName = qm.value("assignmentName");
      try {
        db.removeAssignmentItem(table, assignmentName);
      } catch (SQLException e) {
        System.out.println("ERROR: "
            + e.getMessage());
        return 0;
      }
      return 1;
    }
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
  
  private class EditCourseHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      StringBuilder assList = new StringBuilder();
      String tableTags =
          "<table class=\"table table-hover courseInfoTable\">"
              + "<thead><tr><th>Name</th><th>Start Date</th><th>End Date</th>"
              + "</tr></thead><tbody id=\"courseInfoTableBody\">";
      String closeTableTags = "</tbody></table>";
      assList.append(tableTags);
      String startTags = "<tr class=\"clickable-row\" data-toggle=\"modal\" data-target=\"#assModal\">"
          + "<td class=\"itemName\">";
      String middleTags = "</td><td>";
      String endTags = "</td></tr>";
      List<String> allAss = new ArrayList<String>();
      try {
        allAss = db.getAllAssessments(courseId);
      } catch (Exception e) {
        System.err.println(e);
      }
      StringBuilder allAssTags = new StringBuilder();
      for(String s : allAss) {
        String[] sSplit = s.split(":");
        String name = sSplit[0];
        String[] date = sSplit[1].split(",");
        String start = date[0];
        String end = date[1];
        assList.append(startTags + name + middleTags + start + middleTags + end + endTags);
      }
      assList.append(closeTableTags);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0")
          .put("course", courseId)
          .put("allAss", assList.toString()).build();
      return new ModelAndView(variables, "taEditCourse.html");
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
      String assStartTag =
          "<div class=\"assignment row\"><div class=\"col-sm-3 col-sm-push-1\"><h5>";
      String assDateTag =
          "</h5></div><div class=\"col-sm-5 col-sm-push-1\"><h5>";
      String assEndTag = "</h5></div></div>";
      String courseId = req.params(":courseId");
      String currAss = "none";
      List<Question> questions = new ArrayList<Question>();
      List<String> allAss = new ArrayList<String>();
      try {
        currAss = db.getCurrAssessment(courseId);
        questions = db.getQuestions(courseId, currAss);
        allAss = db.getAllAssessments(courseId);
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
      StringBuilder allAssTags = new StringBuilder();
      for (String s : allAss) {
        String[] sSplit = s.split(":");
        String name = sSplit[0];
        String date = sSplit[1];
        allAssTags.append(assStartTag + name + assDateTag + date + assEndTag);

      }
      // System.out.println(courseId);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("allAss",
              allAssTags.toString()).put("currAss", currAss).put("questions",
              qs.toString()).put("course", courseId).build();
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
      Hours hours = runningHours.getHoursForCourse(courseId);
      List<Question> questions = null;
      int timeLim = 10;
      if (hours != null) {
        questions = hours.getQuestions();
        timeLim = hours.getTimeLim();
      }
      String currAss = "none";
      try {
        currAss = db.getCurrAssessment(courseId);
      } catch (Exception e) {
        System.err.println(e);
      }
      StringBuilder questionsStr = getQuestions(questions);
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("currAss", currAss).put("title",
              "SignMeUp 2.0").put("course", courseId).put("questions",
              questionsStr.toString()).put("time", timeLim).build();
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
      //TODO KIERAN: this method never returns one. where is it getting stuck?
      System.out.println("hi");
      return 1;
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
      String currProject = "fix";
      try {
        questionObject =
            db.addQuestion(assessmentName, question, courseId, currProject);
      } catch (SQLException e) {
        System.out.println("ERROR: sql exception in adding question");
        return 0;
      }
      return 1;
    }
  }
  // private class GetQuestionHandler implements TemplateViewRoute {
  // @Override
  // public ModelAndView handle(final Request req, final Response res) {
  // String courseId = req.params(":courseId");
  // List<Question> questions;
  // try {
  // questions = db.getCourseQuestions(courseId);
  // } catch (Exception e) {
  // System.err.println("ERROR: "+e);
  // }
  // Map<String, Object> variables =
  // new ImmutableMap.Builder().put("title", "SignMeUp 2.0").build();
  // return new ModelAndView(variables, "taCreateClass.html");
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
