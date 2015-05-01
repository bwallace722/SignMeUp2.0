package edu.brown.cs.signMeUpBeta.analytics;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class AnalyticsWebHandler {

  private static final int DEFAULT_PORT = 1235;
  private static final Gson GSON = new Gson();
  private DatabaseQuery dbq;

  public AnalyticsWebHandler(String databaseURL) {
    try {
      dbq = new DatabaseQuery(databaseURL);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void runServer() {

    Spark.externalStaticFileLocation("web");
    Spark.setPort(DEFAULT_PORT);


    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/analytics_home", new AnalyticsHomeHandler(), freeMarker);
    Spark.post("/analytics_query", new QueryHandler(dbq)); 
  }

  /**
   * This class handles the front page of the website.
   * @author baw
   *
   */
  public static class AnalyticsHomeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = null;
      return new ModelAndView(variables, "analytics.ftl");
    }
  }


  public static class QueryHandler implements Route {
    private DatabaseQuery dbq;
    
    public QueryHandler(DatabaseQuery dbq) {
      this.dbq = dbq;
    }

    @Override
    public Object handle(final Request req, final Response res) {

      try {

        QueryParamsMap qm = req.queryMap();
        String[] parameters =
            GSON.fromJson(qm.value("filterParameters"), String[].class);

        List<String[]> selectOptions;
        selectOptions = dbq.getSelectOptions(parameters);


        List<String[]> weekData =
            dbq.getWeekData(parameters);

        List<Integer[]> studentFrequency =
            dbq.getStudentsVisits(parameters);

        Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
            .put("selectOptions", selectOptions)
            .put("weekData", weekData)
            .put("studentFrequency", studentFrequency).build();

        return GSON.toJson(variables);

      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
      }
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("web");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf(
          "ERROR: Unable use %s for template loading.\n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }
}
