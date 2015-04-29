package edu.brown.cs.signMeUpBeta.onhours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.student.Account;

public class Queue {
  private PriorityQueue<Account> pq;
  //private double cutOff;
  private Map<String, Integer> studentCheckMap;
  private Map<String, Long> signUpTime;
  
  public Queue() {
    pq = new PriorityQueue(new PriorityComp());
    studentCheckMap = new ConcurrentHashMap<String, Integer>();
    //cutOff = Double.POSITIVE_INFINITY;
  }
  private class PriorityComp implements Comparator<Account> {
    @Override
    public int compare(Account a, Account b) {
      long currTime = new Date().getTime();
      double aPrior = a.priorityMultiplier()*(currTime - signUpTime.get(a.getLogin()));
      double bPrior = b.priorityMultiplier()*(currTime - signUpTime.get(b.getLogin()));
      if (aPrior > bPrior) {
        return 1;
      } else if (aPrior < bPrior) {
        return -1;
      } else {
        return 0;
      }
    }
  }
  public List<String> getStudentsInOrder() {
    List<String> toReturn = new ArrayList<String>();
    Account[] toSort = (Account[]) pq.toArray();
    Arrays.sort(toSort, new PriorityComp());
    for (Account a : toSort) {
      System.out.println(a.getLogin());
      toReturn.add(a.getLogin());
    }
    return toReturn;
  }
  
  public void add(Account s) {
    pq.add(s);
    signUpTime.put(s.getLogin(), new Date().getTime());
    studentCheckMap.put(s.getLogin(), 0);
  }
  
  public void remove(Account s) {
    pq.remove(s);
    signUpTime.remove(s.getLogin());
    studentCheckMap.remove(s.getLogin());
  }
  public int calledToHours(String login) {
    return studentCheckMap.get(login);
  }
  public int callOffQueue(String login) {
    if (studentCheckMap.containsKey(login)) {
      studentCheckMap.put(login, 1);
      pq.remove(login);
      return 1;
    }
    return 0;
  }
  // public void switchOrder(Account s1, Account s2) {}
//  public double getPriority(Account s1) {
//    return s1.priority();
//  }
//  public void setCutOff(double time) {
//    cutOff = time;
//  }
}
