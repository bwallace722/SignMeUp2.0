package edu.brown.cs.signMeUpBeta.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabaseFromCSV {

  public static void createTables(
      Connection conn, File csvFolder) throws SQLException {
    String[] tables = new String[]{
        ("CREATE TABLE course("
            + "id INT,"
            + "name TEXT,"
            + "PRIMARY KEY(id));"),

            ("CREATE TABLE category("
                + "id INT,"
                + "name TEXT,"
                + "course_id INT,"
                + "PRIMARY KEY(id),"
                + "FOREIGN KEY(course_id) REFERENCES course(id)"
                + "ON DELETE CASCADE ON UPDATE CASCADE);"),

                ("CREATE TABLE assignment("
                    + "id INT,"
                    + "name TEXT,"
                    + "category_id INT,"
                    + "PRIMARY KEY(id),"
                    + "FOREIGN KEY(category_id) REFERENCES category(id)"
                    + "ON DELETE CASCADE ON UPDATE CASCADE);"),

                    ("CREATE TABLE topic("
                        + "id INT,"
                        + "name TEXT,"
                        + "assignment_id INT,"
                        + "PRIMARY KEY(id),"
                        + "FOREIGN KEY(assignment_id) REFERENCES assignment(id)"
                        + "ON DELETE CASCADE ON UPDATE CASCADE);"),

                        ("CREATE TABLE question("
                            + "id INT,"
                            + "text TEXT,"
                            + "topic_id INT,"
                            + "PRIMARY KEY(id),"
                            + "FOREIGN KEY(topic_id) REFERENCES topic(id)"
                            + "ON DELETE CASCADE ON UPDATE CASCADE);"),


                            ("CREATE TABLE student("
                                + "id INT,"
                                + "name TEXT,"
                                + "login TEXT,"
                                + "PRIMARY KEY(id));"),

                                ("CREATE TABLE student_course("
                                    + "id INT,"
                                    + "student_id INT,"
                                    + "course_id INT,"
                                    + "PRIMARY KEY(id),"
                                    + "FOREIGN KEY(student_id) REFERENCES student(id)"
                                    + "ON DELETE CASCADE ON UPDATE CASCADE,"
                                    + "FOREIGN KEY(course_id) REFERENCES course(id)"
                                    + "ON DELETE CASCADE ON UPDATE CASCADE);"),

                                    //id,student_id,class_id,sign_up_time,check_off_time,question
                                    ("CREATE TABLE hours("
                                        + "id INT,"
                                        + "student_id INT,"
                                        + "sign_up_time INT,"
                                        + "check_off_time INT,"
                                        + "question_id INT,"
                                        + "PRIMARY KEY(id),"
                                        + "FOREIGN KEY(student_id) REFERENCES student(id)"
                                        + "ON DELETE CASCADE ON UPDATE CASCADE,"
                                        + "FOREIGN KEY(question_id) REFERENCES question(id)"
                                        + "ON DELETE CASCADE ON UPDATE CASCADE);"),
    };

    PreparedStatement prep;
    for (String statement : tables) {
      try {
        prep = conn.prepareStatement(statement);
        prep.executeUpdate();
      } catch (SQLException e) {
        System.out.println("couldn't create table: " + e.getMessage());
      }
    }
  }

  public static void populateTables(
      Connection conn, File csvFolder) throws IOException, SQLException {
    BufferedReader bfr;
    String csv = ".csv";

    for (File file : csvFolder.listFiles()) {
      if (file.isFile()) {
        String fileName = file.getName();
        //check if it's a .csv file:
        if (fileName.substring(
            fileName.length() - csv.length(), fileName.length()).equals(csv)) {
          try {
            String tableName =
                fileName.substring(0, fileName.length() - csv.length());
            bfr = new BufferedReader(new FileReader(file));
            //set up the prepared statement for each of the lines
            String line = bfr.readLine();
            String[] values = line.split(",");
            int numValues = values.length;
            StringBuffer statement = new StringBuffer();
            statement.append("INSERT OR IGNORE INTO " + tableName + " VALUES (?");
            for (int i = 0; i < values.length - 1; i++) {
              statement.append(",?");
            }
            statement.append(");");
            PreparedStatement prep = conn.prepareStatement(statement.toString());
            while ((line = bfr.readLine()) != null) {
              try {
                values = line.split(",");
                if (values.length == numValues) {
                  for (int i = 0; i < values.length; i++) {
                    prep.setString(i + 1, values[i]);
                  }
                  prep.executeUpdate();
                } else {
                  throw new SQLException(
                      "statement has the wrong number of values.");
                }
              } catch (SQLException e) {
                System.out.println(
                    "The following line could not be added to " + tableName + ":");
                System.out.println(line);
                System.out.println(e.getMessage());
              }
            }
            System.out.println(fileName + " added to the database");
          } catch (SQLException e) {
            System.out.println("couldn't populate table: " + e.getMessage());
          }
        }else {
          System.out.println(fileName + " skipped: it's not a .csv");
        }
      }
    }
  }


  public static void main(String[] args) {
    String usage = "usage: <pathToDatabase> <pathToCSVs>";
    try {
      //TODO: specify path
      if (args.length != 2) {
        System.out.println(usage);
        System.exit(1);
      }
      String pathToDatabase = args[0];
      String pathToCSVs = args[1];

      // this line loads the driver manager class, and must be
      // present for everything else to work properly
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + pathToDatabase;
      Connection conn = DriverManager.getConnection(urlToDB);
      // these two lines tell the database to enforce foreign
      // keys during operations, and should be present
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");

      File csvFolder = new File(pathToCSVs);

      createTables(conn, csvFolder);

      populateTables(conn, csvFolder);

      conn.close();

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("ERROR: " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
