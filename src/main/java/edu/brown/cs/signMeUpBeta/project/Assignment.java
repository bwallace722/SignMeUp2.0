package edu.brown.cs.signMeUpBeta.project;

import java.util.List;

public interface Assignment {
  List<String> questions();
  void setQuestions();
  String name();
  void getName();
  void setName(String newProject);
}
