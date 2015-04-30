package edu.brown.cs.signMeUpBeta.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.onhours.Hours;
import edu.brown.cs.signMeUpBeta.onhours.Queue;
import edu.brown.cs.signMeUpBeta.project.Question;

public class RunningHours {
  
  private ConcurrentHashMap<String, Queue> queues;
  private ConcurrentHashMap<String, Hours> hours;
  private Database db;
  
  public RunningHours(Database db) {
    this.db = db;
    queues = new ConcurrentHashMap<String, Queue>();
    hours = new ConcurrentHashMap<String, Hours>();
  }
  
  public int startHours(String courseID) {
    if (!queues.containsKey(courseID)) {
      queues.put(courseID, new Queue());
      String currProject = "";
      List<Question> questions;
      try {
        currProject = db.getCurrProject(courseID);
        questions = db.getQuestions(courseID, currProject);
      } catch (SQLException e) {
        questions = new ArrayList<Question>();
      }
      hours.put(courseID, new Hours(currProject, questions));
    }
    return 1;
  }
  
  public void endHours(String courseID) {
    queues.remove(courseID);
    hours.remove(courseID);
  }
  
  public Queue getQueueForCourse(String courseID) {
    return queues.get(courseID);
  }
  
  public Hours getHoursForCourse(String courseID) {
    return hours.get(courseID);
  }

}
