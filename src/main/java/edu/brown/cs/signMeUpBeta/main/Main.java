package edu.brown.cs.signMeUpBeta.main;

import java.io.IOException;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import edu.brown.cs.signMeUpBeta.analytics.DatabaseQuery;
import edu.brown.cs.signMeUpBeta.classSetup.Database;
import edu.brown.cs.signMeUpBeta.handlers.AllHandlers;


public class Main {
  
  /**
   * This is the main method that instantiates the program.
   * @param args the input arguments given by the user.
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new Main(args).run();
  }
  
  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }
  
  
  /**
   * run kicks off the program.
   * Baring in mind that this method is only called once to initialize.
   * this method is not run everytime.
   * @throws Exception
   * @throws IOException
   */
  private void run() throws Exception {
    if (args.length != 0) {
      OptionParser parser = new OptionParser();
      OptionSet options = parser.parse(args);
      Class.forName("org.sqlite.JDBC");
      OptionSpec<String> strings = parser.nonOptions().ofType(String.class);
      options = parser.parse(args);
      List<String> params = options.valuesOf(strings);
      String dbPath = params.get(0);
      
      Database db = new Database(dbPath);
      //TODO: make sure the databases align enough to use this:
      DatabaseQuery dbq = new DatabaseQuery(dbPath);
      RunningHours hours = new RunningHours(db);
      AllHandlers gui = new AllHandlers(db, dbq, hours);
      
    } else {
      System.out.println("ERROR: database path not found.");
    }
  }

}
