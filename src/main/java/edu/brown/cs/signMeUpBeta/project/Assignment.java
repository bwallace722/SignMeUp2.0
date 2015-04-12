package edu.brown.cs.kb25.project;

import java.util.List;

public interface Assignment {
  List<SubCategory> subCategories();
  void addSubCategory(SubCategory newSubCategory);
  void setSubCategories(List<SubCategory> newSubCategories);
  String name();
  void setName(String newProject);
  String type();
  void setType();
}
