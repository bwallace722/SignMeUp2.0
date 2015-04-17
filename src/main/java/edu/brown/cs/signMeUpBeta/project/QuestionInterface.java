package edu.brown.cs.signMeUpBeta.project;

import java.util.List;

public interface QuestionInterface {
  List<SubCategoryInterface> subCategories();
  void addSubCategory(SubCategoryInterface newSubCategory);
  void setSubCategories(List<SubCategoryInterface> newSubCategories);
  
  String type();
  void setType();
  
  AssessmentInterface assignment();
  void setAssignment();
}
