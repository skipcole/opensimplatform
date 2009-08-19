package org.usip.osp.graphs;

import java.io.IOException;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartDisplayHelper {

	public static String hello = "Hello World";
	
    public static JFreeChart getDemoChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "S1", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(4.0, "S1", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(15.0, "S1", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(14.0, "S1", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-5.0, "S2", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-7.0, "S2", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(14.0, "S2", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-3.0, "S2", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(6.0, "S3", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(17.0, "S3", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-12.0, "S3", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(7.0, "S3", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(7.0, "S4", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(15.0, "S4", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(11.0, "S4", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(0.0, "S4", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-8.0, "S5", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-6.0, "S5", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(10.0, "S5", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-9.0, "S5", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(9.0, "S6", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(8.0, "S6", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(null, "S6", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(6.0, "S6", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-10.0, "S7", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(9.0, "S7", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(7.0, "S7", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(7.0, "S7", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(11.0, "S8", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(13.0, "S8", "C2"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(9.0, "S8", "C3"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(9.0, "S8", "C4"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(-3.0, "S9", "C1"); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.addValue(7.0, "S9", "C2");
        dataset.addValue(11.0, "S9", "C3");
        dataset.addValue(-10.0, "S9", "C4");
        JFreeChart chart = ChartFactory.createBarChart("Bar Chart", "Category",
                "Value", dataset, PlotOrientation.VERTICAL, true, true, false);

        return chart;

    }
    
    /**
     * Attempts to print out png file and close output stream.
     * 
     * @param out
     * @param cci
     * @throws IOException
     */
    public static void writeChartAsPNG(OutputStream out, Chart cci) throws IOException {

        try {

            ChartUtilities.writeChartAsPNG(out, cci.getThis_chart(), cci.getWidth(),
                    cci.getHeight());

        } catch (Exception e) {

            out.write(e.getMessage().getBytes());

        } finally {
            out.close();
        }
    }
}
