package edu.brown.cs.signMeUpBeta.main;

import java.sql.SQLException;
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
  }
  
  public int startHours(String courseID) {
    if (!queues.containsKey(courseID)) {
      queues.put(courseID, new Queue());
      List<Question> questions;
      try {
        questions = db.getCourseQuestions(courseID);
      } catch (SQLException e) {
        System.err.println("FUCK");
        return 0;
      }
      hours.put(courseID, new Hours(questions));
    }
    return 1;
  }
  
  public void endHours(String courseID) {
    if (queues.containsKey(courseID)) {
      queues.remove(courseID);
      hours.remove(courseID);
    }
  }
  
  public Queue getQueueForCourse(String courseID) {
    return queues.get(courseID);
  }
  
  public Hours getHoursForCourse(String courseID) {
    if(hours.get(courseID) == null) {
      return null;
    }
    return hours.get(courseID);
  }

}
