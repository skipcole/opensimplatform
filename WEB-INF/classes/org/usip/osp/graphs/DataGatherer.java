package org.usip.osp.graphs;

import java.sql.*;

import org.jfree.data.category.*;
import org.jfree.data.general.DefaultPieDataset;

import org.usip.osp.persistence.*;

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
public class DataGatherer {

    public static DefaultPieDataset getPieData(String chart_name,
            String game_round, String getDataSQL) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        // dataset.setValue("Category 1", 43.2);
        // dataset.setValue("Category 2", 27.9);
        // dataset.setValue("Category 3", 79.5);

        /*
        if (chart_name.equalsIgnoreCase("governors_spending")) {

            getDataSQL += " AND `running_game_id` = " + running_game_id
                    + " AND `game_round` = " + game_round;

            try {
                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rst = stmt.executeQuery(getDataSQL);

                while (rst.next()) {
                    double d = new Double(rst.getString("value")).doubleValue();
                    dataset.setValue(rst.getString("value_label"), d);

                }

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        */
        return dataset;
    }

    public static DefaultCategoryDataset getChartData(String chartType,
            String game_round, String getDataSQL) {

        DefaultCategoryDataset cd = new DefaultCategoryDataset();

        if (false) {
            cd.addValue(new Integer(1), "words", new Integer(1));
            cd.addValue(new Integer(5), "words", new Integer(2));
            cd.addValue(new Integer(10), "words", new Integer(3));

        } else if (chartType.equalsIgnoreCase("LineChart")) {
            try {

                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();

                ResultSet rst = stmt.executeQuery(getDataSQL);

                while (rst.next()) {
                    cd.addValue(new Double(rst.getString(2)).doubleValue(),
                            "hard coded stuff", new Integer(rst.getString(1)));
                }

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (chartType.equalsIgnoreCase("myfirst")) {
            try {
                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rst = stmt.executeQuery(getDataSQL);

                while (rst.next()) {
                    cd.addValue(rst.getDouble(2), "Population Satisfaction",
                            new Integer(rst.getInt(1)));
                }

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } 
        /*
        else if (chartType.equalsIgnoreCase("cleanup_expenditure")) {

            String selectSQL = "SELECT game_round, value FROM liberia_game_values "
                    + "WHERE value_label = 'Oil Company Payment to Cleanup Fund' "
                    + "AND running_game_id = "
                    + running_game_id
                    + " ORDER BY game_round";

            try {
                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rst = stmt.executeQuery(selectSQL);

                while (rst.next()) {

                    String textValue = rst.getString(2);
                    Integer II = new Integer(textValue);

                    cd.addValue(II.doubleValue(), "Spending per Round",
                            new Integer(rst.getInt(1)));
                }

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (chartType.equalsIgnoreCase("framework1")) {

            String selectSQL = "SELECT game_round, value FROM framework_game_values "
                    + "WHERE value_label = 'historical_values' "
                    + "AND running_game_id = "
                    + running_game_id
                    + " ORDER BY game_round";

            try {
                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rst = stmt.executeQuery(selectSQL);

                int last_round = 0;
                double last_value = 0.0;
                int final_round = 0;
                double final_value = 0.0;

                while (rst.next()) {

                    String textValue = rst.getString(2);
                    Integer II = new Integer(textValue);

                    int this_round = rst.getInt(1);
                    double this_value = II.doubleValue();

                    if (this_round > (1 + last_round)) {
                        for (int round_num = last_round + 1; round_num == (this_round - 1); ++round_num) {
                            cd.addValue(last_value, "Spending per Round",
                                    new Integer(round_num));
                        }
                    }

                    cd.addValue(this_value, "Spending per Round", new Integer(
                            this_round));

                    final_round = this_round;
                    final_value = this_value;
                    last_round = this_round;
                    last_value = this_value;
                }

                int int_round = new Integer(game_round).intValue();
                if (final_round < int_round) {

                    for (int round_num = final_round + 1; round_num == int_round; ++round_num) {
                        cd.addValue(final_value, "Spending per Round",
                                new Integer(round_num));
                    }

                }

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */

        return cd;

    }
}
