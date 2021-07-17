/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private Point[] myPoints;
    private Point[] collinear;
    private LineSegment[] lineSegments;
    private Selected[] selecteds;

    public FastCollinearPoints(Point[] points) {

        if (points == null) throw new IllegalArgumentException("Array points is null");
        myPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) myPoints[i] = points[i];

        for (int i = 0; i < myPoints.length; i++) {
            if (myPoints[i] == null)
                throw new IllegalArgumentException("Point is null");
        }

        Arrays.sort(myPoints);

        for (int i = 0; i < myPoints.length - 1; i++) {
            if (myPoints[i].compareTo(myPoints[i + 1]) == 0)
                throw new IllegalArgumentException("Repeated points");
        }

        lineSegments = new LineSegment[0];
        selecteds = new Selected[0];

        for (int i = 0; i < myPoints.length; i++) {
            Point[] remainingPoints = Arrays.copyOfRange(myPoints, i + 1, myPoints.length);
            Arrays.sort(remainingPoints, myPoints[i].slopeOrder());
            for (int j = 0; j < remainingPoints.length; ) {

                collinear = new Point[2];
                double slope = myPoints[i].slopeTo(remainingPoints[j]);
                collinear[0] = myPoints[i];
                collinear[1] = remainingPoints[j];
                j++;
                while (j < remainingPoints.length
                        && myPoints[i].slopeTo(remainingPoints[j]) == slope) {
                    add(remainingPoints[j]);
                    j++;
                }
                if (collinear.length >= 4) {
                    Point[] sortedCollinear = new Point[collinear.length];
                    for (int k = 0; k < collinear.length; k++) {
                        sortedCollinear[k] = collinear[k];
                    }
                    Arrays.sort(sortedCollinear);
                    Selected selected = new Selected(sortedCollinear[0],
                                                     sortedCollinear[sortedCollinear.length - 1],
                                                     slope);

                    if (!isAlreadySelected(selected)) {
                        add(new LineSegment(sortedCollinear[0],
                                            sortedCollinear[sortedCollinear.length - 1]));
                        add(selected);
                    }
                }
            }
        }
    }

    private class Selected {
        Point x;
        Point y;
        double slope;

        public Selected(Point x, Point y, double slope) {
            this.x = x;
            this.y = y;
            this.slope = slope;
        }
    }

    private boolean isAlreadySelected(Selected selected) {

        if (selecteds.length == 0) {
            return false;
        }

        for (int i = 0; i < selecteds.length; i++) {
            if (selected.slope == selecteds[i].slope) {
                double slope = selecteds[i].x.slopeTo(selected.y);
                if (slope == selected.slope)
                    return true;
            }
        }

        return false;
    }

    private void add(Selected selected) {
        if (selected == null) throw new NullPointerException();
        resize(selecteds, selecteds.length + 1);
        selecteds[selecteds.length - 1] = selected;
    }

    private void resize(Selected[] s, int capacity) {
        Selected[] copy = new Selected[capacity];
        for (int i = 0; i < selecteds.length; i++) copy[i] = s[i];
        selecteds = copy;
    }

    private void add(Point x) {
        if (x == null) throw new NullPointerException();
        resize(collinear, collinear.length + 1);
        collinear[collinear.length - 1] = x;
    }

    private void resize(Point[] x, int capacity) {
        Point[] copy = new Point[capacity];
        for (int i = 0; i < x.length; i++) copy[i] = x[i];
        collinear = copy;
    }

    private void add(LineSegment x) {
        if (x == null) throw new NullPointerException();
        resize(lineSegments, lineSegments.length + 1);
        lineSegments[lineSegments.length - 1] = x;
    }

    private void resize(LineSegment[] x, int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < x.length; i++) copy[i] = x[i];
        lineSegments = copy;
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        LineSegment[] copySegment = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) copySegment[i] = lineSegments[i];
        return copySegment;
    }

    public static void main(String[] args) {
        In in = new In("input50.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
