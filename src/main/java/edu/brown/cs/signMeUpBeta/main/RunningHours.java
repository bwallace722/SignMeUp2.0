package edu.brown.cs.signMeUpBeta.main;

import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.onhours.Hours;
import edu.brown.cs.signMeUpBeta.onhours.Queue;

public class RunningHours {
  
  private ConcurrentHashMap<String, Queue> queues;
  private ConcurrentHashMap<String, Hours> hours;
  
  public RunningHours() {
    queues = new ConcurrentHashMap<String, Queue>();
  }
  
  public void startHours(String courseID) {
    if (!queues.containsKey(courseID)) {
      queues.put(courseID, new Queue());
      hours.put(courseID, new Hours());
    }
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
    return hours.get(courseID);
  }

}
