package edu.brown.cs.signMeUpBeta.onhours;

import java.util.Comparator;
import java.util.PriorityQueue;

import edu.brown.cs.signMeUpBeta.student.Account;

public class Queue {
  
  private PriorityQueue<Account> pq;
  private double cutOff;
  
  public Queue() {
    pq = new PriorityQueue(new PriorityComp());
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
  }
  
  public void remove(Account s) {
    pq.remove(s);
  }
  
//  public void switchOrder(Account s1, Account s2) {}
  
  public double getPriority(Account s1) {
    return s1.priority();
  }
  
  public void setCutOff(double time) {
    cutOff = time;
  }
}
