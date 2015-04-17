package edu.brown.cs.signMeUpBeta.project;

import java.util.ArrayList;
import java.util.List;

public class Question {

  private List<SubCategoryInterface> subCategories;
//  private AssessmentInterface assessment;
  private String content, courseID, assessment;
  
  public Question(String courseID, String content, String assessment) {
    this.courseID = courseID;
    this.content = content;
    this.assessment = assessment;
    subCategories = new ArrayList<SubCategoryInterface>();
  }
  
  public List<SubCategoryInterface> subCategories() {
    return subCategories;
  }
  public void addSubCategory(SubCategoryInterface newSubCategory) {
    subCategories.add(newSubCategory);
  }
  public void setSubCategories(List<SubCategoryInterface> newSubCategories) {
    subCategories = newSubCategories;
  }
  
//  String type();
//  void setType();
  
//  public AssessmentInterface assignment() {}
  public String assessment() {
    return assessment;
  }
  public void setAssessment(String newAssessment) {
    assessment = newAssessment;
  }
  
  public String content() {
    return content;
  }
  public void setContent(String newContent) {
    content = newContent;
  }
}
