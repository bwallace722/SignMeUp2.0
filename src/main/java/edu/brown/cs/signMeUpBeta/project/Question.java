package edu.brown.cs.signMeUpBeta.project;

import java.util.List;

public interface Question {
  List<SubCategory> subCategories();
  void addSubCategory(SubCategory newSubCategory);
  void setSubCategories(List<SubCategory> newSubCategories);
  
  String type();
  void setType();
  
  Assignment assignment();
  void setAssignment();
}
