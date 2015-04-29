package edu.brown.cs.signMeUpBeta.onhours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import edu.brown.cs.signMeUpBeta.student.Account;

public class Queue {
  private PriorityBlockingQueue<Account> pq;
  //private double cutOff;
  private Map<String, Integer> studentCheckMap;
  private Map<String, Long> signUpTime;
  private Map<String, Double> priorityMult;
  
  public Queue() {
    pq = new PriorityBlockingQueue(2, new PriorityComp());
    studentCheckMap = new ConcurrentHashMap<String, Integer>();
    signUpTime = new ConcurrentHashMap<String, Long>();
    priorityMult = new ConcurrentHashMap<String, Double>();
    //cutOff = Double.POSITIVE_INFINITY;
  }
  private class PriorityComp implements Comparator<Account> {
    @Override
    public int compare(Account a, Account b) {
      long currTime = (new Date()).getTime();
      double aPrior = priorityMult.get(a.getLogin())*(currTime - signUpTime.get(a.getLogin()));
      double bPrior = priorityMult.get(b.getLogin())*(currTime - signUpTime.get(b.getLogin()));
      
      return (int)(bPrior - aPrior);
    }
  }
  public List<String> getStudentsInOrder() {
//    List<String> toReturn = new ArrayList<String>();
//    System.out.println("2");
//    Account[] toSort = new Account[pq.size()];
//    toSort = pq.toArray(toSort);
//    System.out.println("3");
//    Arrays.sort(toSort, new PriorityComp());
//    System.out.println("4");
//    for (Account a : toSort) {
//      System.out.println(a.getLogin());
//      toReturn.add(a.getLogin());
//    }
//    return toReturn;
    
    List<String> toReturn = new ArrayList<String>();
    PriorityBlockingQueue<Account> copy = new PriorityBlockingQueue<Account>(pq);
    while (!copy.isEmpty()) {
      Account curr = copy.poll();
      System.out.println(curr.getLogin());
      toReturn.add(curr.getLogin());
    }
    return toReturn;
  }
  
  public void add(Account s, double pMult) {
    signUpTime.put(s.getLogin(), (new Date()).getTime());
    studentCheckMap.put(s.getLogin(), 0);
    priorityMult.put(s.getLogin(), pMult);
    pq.add(s);
  }
  
  public void remove(Account s) {
    pq.remove(s);
    signUpTime.remove(s.getLogin());
    studentCheckMap.remove(s.getLogin());
    priorityMult.remove(s.getLogin());
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
