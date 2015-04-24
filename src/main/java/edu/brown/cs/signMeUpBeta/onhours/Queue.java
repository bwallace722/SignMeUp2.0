package edu.brown.cs.signMeUpBeta.onhours;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.signMeUpBeta.student.Account;

public class Queue {
  private PriorityQueue<Account> pq;
  private double cutOff;
  private Map<String, Integer> studentCheckMap;
  public Queue() {
    pq = new PriorityQueue(new PriorityComp());
    studentCheckMap = new ConcurrentHashMap<String, Integer>();
    cutOff = Double.POSITIVE_INFINITY;
  }
  private class PriorityComp implements Comparator<Account> {
    public PriorityComp() {}
    @Override
    public int compare(Account a, Account b) {
      double aPrior = a.priority();
      double bPrior = b.priority();
      if (aPrior > bPrior) {
        return 1;
      } else if (aPrior < bPrior) {
        return -1;
      } else {
        return 0;
      }
    }
  }
  public void add(Account s) {
    pq.add(s);
    studentCheckMap.put(s.getLogin(), 0);
  }
  public void remove(Account s) {
    pq.remove(s);
    studentCheckMap.remove(s.getLogin());
  }
  public int calledToHours(String login) {
    return studentCheckMap.get(login);
  }
  public int callOffQueue(String login) {
    if (studentCheckMap.containsKey(login)) {
      studentCheckMap.put(login, 1);
      return 1;
    }
    return 0;
  }
  // public void switchOrder(Account s1, Account s2) {}
  public double getPriority(Account s1) {
    return s1.priority();
  }
  public void setCutOff(double time) {
    cutOff = time;
  }
}
