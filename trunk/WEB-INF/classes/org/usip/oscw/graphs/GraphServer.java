package org.usip.oscw.graphs;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.*;
import org.jfree.data.general.DefaultPieDataset;

import org.usip.oscw.persistence.MysqlDatabase;


/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class GraphServer extends HttpServlet {

    /**
     * Processes a GET request.
     * 
     * @param request
     *            the request.
     * @param response
     *            the response.
     * 
     * @throws ServletException
     *             if there is a servlet related problem.
     * @throws IOException
     *             if there is an I/O problem.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OutputStream out = response.getOutputStream();

        prepResonse(response);

        String chart_id = request.getParameter("chart_id");
        String sim_id = request.getParameter("sim_id");
        String game_round = request.getParameter("game_round");
        //String running_game_id = request.getParameter("running_game_id");
        String game_values_table = request.getParameter("game_values_table");

        Chart cci = new Chart();
        
        if(chart_id != null){
            cci = getChartByNameAndRound(chart_id, game_round, sim_id, game_values_table);
        } else {
            cci.this_chart = getDemoChart();
        }

        writeChartAsPNG(out, cci);

    }

    public void prepResonse(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
    }

    /**
     * Attempts to print out png file and close output stream.
     * 
     * @param out
     * @param cci
     * @throws IOException
     */
    public void writeChartAsPNG(OutputStream out, Chart cci) throws IOException {

        try {

            ChartUtilities.writeChartAsPNG(out, cci.this_chart, cci.width,
                    cci.height);

        } catch (Exception e) {

            out.write(e.getMessage().getBytes());

        } finally {
            out.close();
        }
    }

    public static Chart getChartByNameAndRound(String chart_id,
            String game_round, String sim_id, String values_table) {

        Chart cci = new Chart();

        String selectChartInfo = "SELECT * FROM `charts` where sf_id = '"
                + chart_id + "'";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectChartInfo);

            if (rst.next()) {
                String chart_type = rst.getString("type");
                String chart_title = rst.getString("title");
                String x_axis_title = rst.getString("x_axis_title");
                String y_axis_title = rst.getString("y_axis_title");
                cci.height = rst.getInt("height");
                cci.width = rst.getInt("width");

                String howToGetData = rst.getString("first_data_source");

                howToGetData = howToGetData.replace("[simulation_id]", sim_id);
                howToGetData = howToGetData.replace("[sim_value_table_name]", values_table);
                
                Statement stmt2 = connection.createStatement();
                ResultSet rst2 = stmt.executeQuery(howToGetData);

                JFreeChart chart = null;


                if (chart_type.equalsIgnoreCase("LineChart")) {
                    
                    DefaultCategoryDataset cd = DataGatherer.getChartData(
                            chart_type, game_round,
                            howToGetData);

                    chart = ChartFactory.createLineChart(chart_title, // chart
                            // title
                            x_axis_title, // domain axis label
                            y_axis_title, // range axis label
                            cd, // data
                            PlotOrientation.VERTICAL, // orientation
                            false, // include legend
                            true, // tooltips
                            false // urls
                            );

                } else if (chart_type.equalsIgnoreCase("BarChart")) {

                    DefaultPieDataset dataset = DataGatherer.getPieData(
                            chart_id, game_round,
                            howToGetData);

                    chart = ChartFactory.createPieChart(chart_title, dataset,
                            true, // legend?
                            true, // tooltips?
                            false // URLs?
                            );
                }

                cci.this_chart = chart;

            } // End of loop if found chart info

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cci;
    }

    public static JFreeChart getLineChart() {
        DefaultCategoryDataset cd = new DefaultCategoryDataset();

        cd.addValue(20, "Population Satisfaction", new Integer(1));
        cd.addValue(40, "Population Satisfaction", new Integer(2));
        cd.addValue(60, "Population Satisfaction", new Integer(3));
        cd.addValue(80, "Population Satisfaction", new Integer(4));

        // create the chart...
        JFreeChart chart = ChartFactory.createLineChart(
                "Population Satisfaction", // chart title
                "Game Round", // domain axis label
                "Satisfaction Level", // range axis label
                cd, // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                true, // tooltips
                false // urls
                );

        return chart;
    }

    public static JFreeChart getDemoChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "S1", "C1");
        dataset.addValue(4.0, "S1", "C2");
        dataset.addValue(15.0, "S1", "C3");
        dataset.addValue(14.0, "S1", "C4");
        dataset.addValue(-5.0, "S2", "C1");
        dataset.addValue(-7.0, "S2", "C2");
        dataset.addValue(14.0, "S2", "C3");
        dataset.addValue(-3.0, "S2", "C4");
        dataset.addValue(6.0, "S3", "C1");
        dataset.addValue(17.0, "S3", "C2");
        dataset.addValue(-12.0, "S3", "C3");
        dataset.addValue(7.0, "S3", "C4");
        dataset.addValue(7.0, "S4", "C1");
        dataset.addValue(15.0, "S4", "C2");
        dataset.addValue(11.0, "S4", "C3");
        dataset.addValue(0.0, "S4", "C4");
        dataset.addValue(-8.0, "S5", "C1");
        dataset.addValue(-6.0, "S5", "C2");
        dataset.addValue(10.0, "S5", "C3");
        dataset.addValue(-9.0, "S5", "C4");
        dataset.addValue(9.0, "S6", "C1");
        dataset.addValue(8.0, "S6", "C2");
        dataset.addValue(null, "S6", "C3");
        dataset.addValue(6.0, "S6", "C4");
        dataset.addValue(-10.0, "S7", "C1");
        dataset.addValue(9.0, "S7", "C2");
        dataset.addValue(7.0, "S7", "C3");
        dataset.addValue(7.0, "S7", "C4");
        dataset.addValue(11.0, "S8", "C1");
        dataset.addValue(13.0, "S8", "C2");
        dataset.addValue(9.0, "S8", "C3");
        dataset.addValue(9.0, "S8", "C4");
        dataset.addValue(-3.0, "S9", "C1");
        dataset.addValue(7.0, "S9", "C2");
        dataset.addValue(11.0, "S9", "C3");
        dataset.addValue(-10.0, "S9", "C4");
        JFreeChart chart = ChartFactory.createBarChart("Bar Chart", "Category",
                "Value", dataset, PlotOrientation.VERTICAL, true, true, false);

        return chart;

    }
    
    public void cachingDebris(){
        /*
         * Since inadvertant caching seems like it will be a problem, I have
         * removed the caching I originally put in.
         * 
         * String nocache = request.getParameter("nocache"); //if ((nocache !=
         * null) && (nocache.equalsIgnoreCase("true"))) {
         * 
         * Hashtable charts = (Hashtable) getServletContext().getAttribute(
         * "charts");
         * 
         * if (charts == null) { charts = new Hashtable();
         * getServletContext().setAttribute("charts", charts); } else {
         *  // Try to pull it out of cache cci = (Chart) charts.get(chart_name +
         * "_" + game_round);
         *  // If not in cache, get it from db. if (cci == null){ cci =
         * getChartByNameAndRound(chart_name, game_round, running_game_id); }
         *  // Store it back in the cache charts.put(chart_name + "_" +
         * game_round, cci);
         *  }
         */
    }
}
