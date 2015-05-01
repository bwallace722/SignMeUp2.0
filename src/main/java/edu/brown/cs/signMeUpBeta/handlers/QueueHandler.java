package edu.brown.cs.signMeUpBeta.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    Spark.get("/makeAppointment/:courseIdAndUserId",
        new MakeAppointmentHandler(), new FreeMarkerEngine());
    Spark.get("/signUpForHours/:courseAndUserId", new StudentSignUpForHours(),
        new FreeMarkerEngine());
    Spark.post("/checkAppointments", new CheckAppointmentHandler());
    Spark.post("/startHours/:courseId", new StartCourseHours());
    Spark.post("/addStudentToQueue", new AddStudentToQueue());
    Spark.post("/labCheckOff/:login", new AddLabCheckoffToQueue());
    Spark.post("/updateQueue/:courseId", new UpdateQueueHandler());
    Spark
        .post("/updateAppointments/:courseId", new UpdateAppointmentsHandler());
    Spark.post("/updateClinic/:courseId", new UpdateClinicHandler());
    Spark.post("/callStudent/:courseId", new CallStudentToHours());
    Spark.post("/callClinic", new CallClinicToHours());
    Spark.post("/checkQueue", new QueueChecker());
    Spark.post("/removeStudent", new RemoveStudent());
    Spark.post("/endHours/:courseId", new EndHours());
    Spark.post("/cancelAppointment", new CancelAppointment());
    Spark.post("/checkOffAppointment", new CheckOffAppointment());
  }
  public int addToQueue(String login, String courseId, String[] questions,
      String otherQ) {
    Queue queue = runningHours.getQueueForCourse(courseId);
    Hours hours = runningHours.getHoursForCourse(courseId);
    if (queue.alreadyOnQueue(login)) {
      return 2;
    }
    String currAss = hours.getCurrAssessment();
    try {
      if (db.getLastProject(login, courseId) != currAss) {
        db.resetNumQuestions(login, courseId);
      }
      db.updateStudentInfo(login, courseId, questions, currAss);
    } catch (Exception e) {
      System.err.println("ERROR: "
          + e);
    }
    int toReturn = 0;
    Account account;
    int numQuestions = 0;
    try {
      account = db.getAccount(login);
      numQuestions = db.getNumberQuestionsAsked(login, courseId);
    } catch (Exception e) {
      System.err.println("ERROR: sql error on add student to queue");
      return 0;
    }
    queue.add(account, (1 / (numQuestions + 1)));
    hours.updateQuestions(login,
        new ArrayList<String>(Arrays.asList(questions)));
    if (otherQ != null) {
      hours.incrementQuestion(otherQ);
    }
    toReturn = 1;
    return toReturn;
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
  private class CheckAppointmentHandler implements Route {
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
   * this handler gives the front-end an updated list of all the appointments.
   * @author kj13
   */
  private class UpdateAppointmentsHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String course = req.params(":courseId");
      Map<String, String> apts =
          runningHours.getHoursForCourse(course).getAppointments();
      StringBuilder aptStr = new StringBuilder();
      for (String key : apts.keySet()) {
        String login = apts.get(key);
        if (login != null) {
          aptStr.append(login
              + "~ "
              + key
              + ",");
        }
      }
      return aptStr.toString();
    }
  }
  /**
   * this handler gives the front-end an updated list of all the clinic
   * suggestions.
   * @author kj13
   */
  private class UpdateClinicHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String course = req.params(":courseId");
      Map<String, String> apts =
          runningHours.getHoursForCourse(course).getAppointments();
      StringBuilder clinicStr = new StringBuilder();
      Hours hours = runningHours.getHoursForCourse(course);
      List<String> popQs = hours.mostPopularQuestions();
      for (String q : popQs) {
        List<String> students = hours.studentsWhoAsked(q);
        StringBuilder studentStr = new StringBuilder();
        for (String s : students) {
          studentStr.append(s
              + ",");
        }
        clinicStr.append(q
            + "~"
            + studentStr.toString()
            + "!");
      }
      return clinicStr.toString();
    }
  }
  private class CallClinicToHours implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String courseId = qm.value("course");
      String q = qm.value("clinicQ");
      String[] questions = {q};
      String studentsString = qm.value("students");
      // TODO KIERAN
      String[] students = studentsString.split(",");
      for (String s : students) {
        addToQueue(s, courseId, questions, null);
      }
      return null;
    }
  }
  private class RemoveStudent implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String course = qm.value("course");
      String studentLogin = qm.value("studentLogin");
      Queue queue = runningHours.getQueueForCourse(course);
      Hours hours = runningHours.getHoursForCourse(course);
      if (queue == null) {
        return 0;
      }
      Account account;
      try {
        account = db.getAccount(studentLogin);
      } catch (Exception e) {
        System.err.println(e);
        return 0;
      }
      queue.remove(account);
      hours.removeStudent(studentLogin);
      System.out.println(queue.getStudentsInOrder().size());
      return 1;
    }
  }
  /**
   * This handler removes an appointment from the stored appointments.
   * @author omadarik
   */
  private class CancelAppointment implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String time = qm.value("time");
      String courseID = qm.value("course");
      Hours hrs = runningHours.getHoursForCourse(courseID);
      if (hrs.removeAppointment(time) != 1) {
        return 0;
      }
      return 1;
    }
  }
  /**
   * This handlers checks off a student from the stored appointments.
   * @author omadarik
   */
  private class CheckOffAppointment implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String time = qm.value("time");
      String courseID = qm.value("course");
      Hours hrs = runningHours.getHoursForCourse(courseID);
      if (hrs.checkOffAppointment(time) != 1) {
        return 0;
      }
      return 1;
    }
  }
  /**
   * This handler ends hours for the given course.
   * @author kj13
   */
  private class EndHours implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String course = req.params(":courseId");
      runningHours.endHours(course);
      Queue queue = runningHours.getQueueForCourse(course);
      if (queue != null) {
        System.out.println("Did not remove queue");
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
      int toReturn = 0;
      Queue queue = runningHours.getQueueForCourse(course);
      Account account;
      try {
        account = db.getAccount(login);
      } catch (Exception e) {
        System.err.println("ERROR: sql error on add lab checkoff");
        return 0;
      }
      if (queue == null) {
        // FUCK
      }
      queue.add(account, 2);
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
   * This class handles
   * @author omadarik
   */
  private class AddStudentToQueue implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String courseId = qm.value("course");
      String login = qm.value("login");
      String qList = qm.value("questions");
      String otherQ = qm.value("otherQ");
      System.out.println(qList);
      String[] questions = qList.split("/");
      Queue queue = runningHours.getQueueForCourse(courseId);
      Hours hours = runningHours.getHoursForCourse(courseId);
      if (queue.alreadyOnQueue(login)
          || hours.alreadyMadeAppointment(login)) {
        return 2;
      }
      String currAss = hours.getCurrAssessment();
      try {
        if (db.getLastProject(login, courseId) != currAss) {
          db.resetNumQuestions(login, courseId);
        }
        db.updateStudentInfo(login, courseId, questions, currAss);
      } catch (Exception e) {
        System.err.println("ERROR: "
            + e);
      }
      int toReturn = 0;
      Account account;
      int numQuestions = 0;
      try {
        account = db.getAccount(login);
        numQuestions = db.getNumberQuestionsAsked(login, courseId);
      } catch (Exception e) {
        System.err.println("ERROR: sql error on add student to queue");
        return 0;
      }
      queue.add(account, (1 / (numQuestions + 1)));
      hours.updateQuestions(login, new ArrayList<String>(Arrays
          .asList(questions)));
      hours.incrementQuestion(otherQ);
      toReturn = 1;
      return toReturn;
    }
  }
  private class UpdateQueueHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      String courseId = req.params(":courseId");
      // System.out.println(courseId);
      Queue currentQueue = runningHours.getQueueForCourse(courseId);
      List<String> toReturn = currentQueue.getStudentsInOrder();
      return toReturn;
    }
  }
  private class StartCourseHours implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String courseId = req.params(":courseId");
      Integer duration = Integer.parseInt(qm.value("duration"));
      Date currentDate = new Date();
      int toReturn = runningHours.startHours(courseId);
      Hours currHours = runningHours.getHoursForCourse(courseId);
      currHours.setUpAppointments(currentDate, duration);
      return toReturn;
    }
  }
  private class ConfirmAppointmentHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String courseId = qm.value("courseId");
      String login = qm.value("login");
      String time = qm.value("time");
      String qList = qm.value("questions");
      String otherQ = qm.value("otherQ");
      String[] questions = qList.split("/");
      Queue queue = runningHours.getQueueForCourse(courseId);
      Hours hours = runningHours.getHoursForCourse(courseId);
      if (queue.alreadyOnQueue(login)) {
        return 2;
      }
      String currAss = hours.getCurrAssessment();
      try {
        if (db.getLastProject(login, courseId) != currAss) {
          db.resetNumQuestions(login, courseId);
        }
        db.updateStudentInfo(login, courseId, questions, currAss);
      } catch (Exception e) {
        System.err.println("ERROR: "
            + e);
      }
      int toReturn = 0;
      toReturn = hours.scheduleAppointment(time, login);
      if (toReturn == 1) {
        hours.updateQuestions(login, new ArrayList<String>(Arrays
            .asList(questions)));
        hours.incrementQuestion(otherQ);
      }
      return toReturn;
    }
  }
  private class MakeAppointmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      String courseAndUserId = req.params(":courseIdAndUserId");
      String[] reqParams = courseAndUserId.split("~");
      String courseId = reqParams[0];
      String login = reqParams[1];
      String timesHTMLTags =
          "<button class=\"aptTime btn btn-success btn-lg\">";
      String closeTag = "</button>";
      Map<String, String> timesMap =
          runningHours.getHoursForCourse(courseId).getAppointments();
      // NEEDED: AVAILABLE APPOINTMENT TIMES
      List<String> availTimes = new ArrayList<String>();
      for (String time : timesMap.keySet()) {
        availTimes.add(time);
      }
      StringBuilder timesHTML = new StringBuilder();
      for (String a : availTimes) {
        String t = timesHTMLTags
            + a
            + closeTag;
        timesHTML.append(t);
      }
      // NEEDED: SUBQUESTIONS PER QUESTION
      Hours hours = runningHours.getHoursForCourse(courseId);
      List<Question> questionsList = new ArrayList<Question>();
      StringBuilder questions = new StringBuilder();
      boolean running = false;
      if (hours != null) {
        running = true;
        questionsList = hours.getQuestions();
        questions = getQuestions(questionsList);
      }
      String currAss = "none";
      try {
        currAss = db.getCurrAssessment(courseId);
      } catch (Exception e) {
        System.err.println(e);
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).put("aptTimes", timesHTML).put(
              "questions", questions.toString()).put("currAss", currAss).put(
              "running", running).build();
      return new ModelAndView(variables, "makeAppointment.html");
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
      String qStartTags = "<label ><input type=\"checkbox\" value=\"";
      String closeValTags = "\">";
      String qEndTags = "</label><br>";
      Hours hours = runningHours.getHoursForCourse(courseId);
      List<Question> questions = new ArrayList<Question>();
      boolean running = false;
      if (hours != null) {
        running = true;
        questions = hours.getQuestions();
      }
      StringBuilder qs = new StringBuilder();
      for (Question q : questions) {
        qs.append(qStartTags
            + q.content()
            + closeValTags
            + q.content()
            + qEndTags);
      }
      String currAss = "none";
      try {
        currAss = db.getCurrAssessment(courseId);
      } catch (Exception e) {
        System.err.println(e);
      }
      Map<String, Object> variables =
          new ImmutableMap.Builder().put("title", "SignMeUp 2.0").put("course",
              courseId).put("login", login).put("currAss", currAss).put(
              "questions", qs.toString()).put("running", running).build();
      return new ModelAndView(variables, "signUpForHours.html");
    }
  }
  private StringBuilder getQuestions(List<Question> questions) {
    String qStartTags = "<label><input type=\"checkbox\" value=\"";
    String closeValTags = "\">";
    String qEndTags = "</label><br>";
    StringBuilder qs = new StringBuilder();
    for (Question q : questions) {
      qs.append(qStartTags
          + q.content()
          + closeValTags
          + q.content()
          + qEndTags);
    }
    return qs;
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
