package jigsaw;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;

/**
 * General graph and trigonometry functions.
 * 
 * Version 2.0 returns the class's floating point data type to double instead of float.  Clamp functions added.
 * 
 * Version 1.9 adds centeroid().
 * Version 1.8 adds getLineIntersectI() and getLineIntersectF().
 * Version 1.6 adds angleDifference() and absAngleDifference() functions for better angle comparisons.
 * Version 1.5 replaces buggy directionToFacePoint() with getShortestTurn() which is easier to understand.
 * Version 1.4 reintroduced the "norm" function for normalizing radian values; necessary for radian value comparison
 * Version 1.3 adds getAngleA function.
 * Version 1.2 fixes buggy code by removing "normalizing" and "abs" functions that acted on radian values.
 * Version 1.1 includes function to find intersecting points of two circles.
 * 
 * @author John McCullock
 * @version 2.o 2017-01-26
 */
public class MathUtil
{
	public static final double QUARTER_PI = Math.PI * 0.25;
	public static final double HALF_PI = Math.PI * 0.5;
	public static final double PI2 = Math.PI * 2.0;
	
	/**
	 * Finds the distance between two points using Pythagorean Theorem.
	 * @param x1 double x value for first point.
	 * @param y1 double y value for first point.
	 * @param x2 double x value for second point.
	 * @param y2 double y value for second point.
	 * @return double distance between first and second point.
	 */
	public static double distance(final double x1, final double y1, final double x2, final double y2)
 	{
		return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
 	}
	
	/**
	 * Finds the difference between angles on the side less that Pi.  This function side-steps the problem
	 * where the angles span across the 0-2Pi divide.
	 * @param theta1 double first angle.
	 * @param theta2 double second angle.
	 * @return double
	 */
	public static double angleDifference(double theta1, double theta2)
	{
		return ((theta2 - theta1) + Math.PI) % PI2 - Math.PI;
	}
	
	/**
	 * Similar to angleDifference function, but return the absolute value.  This function side-steps the 
	 * problem where the angles span across the 0-2Pi divide.
	 * @param theta1 double first angle.
	 * @param theta2 double second angle.
	 * @return double
	 */
	public static double absAngleDifference(double theta1, double theta2)
	{
		return Math.abs(((theta2 - theta1) + Math.PI) % PI2 - Math.PI);
	}
	
	/**
	 * 
	 * @param x1 double x value for start point.
	 * @param y1 double y value for start point.
	 * @param x2 double x value for destination point.
	 * @param y2 double y value for destination point.
	 * @return double angle in radians.
	 */
	public static double getAngleFromPoints(final double x1, final double y1, final double x2, final double y2)
	{
		return Math.atan2(-(y2 - y1), x2 - x1);
	}
	
	/**
	 * Determines if a clockwise or counterclockwise turn is shortest to a target angle.
	 * @param current double current angle in radians.
	 * @param target double target angle in radians.
	 * @return double Positive results means turn counterclockwise, negative means turn clockwise.
	 */
	public static double getShortestTurn(double current, double target)
	{
		double difference = target - current;
		while (difference < -Math.PI) difference += PI2;
		while (difference > Math.PI) difference -= PI2;
		return difference;
	}
	
	public static double distanceToStartTurn(double radius, double radiusFactor, double turnAngle, double turnExponent)
	{
		//double diameter = radius * 2f;
		//double timesRadiusFactor = diameter * radiusFactor;
		//double exponent = (double)Math.pow(turnExponent, turnAngle);
		//double total = timesRadiusFactor * exponent;
		return ((radius * 2f) * radiusFactor) * (double)Math.pow(turnExponent, turnAngle);
	}
	
	/**
	 * Useful for radian comparisons.
	 * 
	 * Takes any radian value and shifts it to a value between 0.0 and 2PI.
	 * 
	 * @param angle double angle to be evaluated.
	 * @return double value between 0.0 and 2PI.
	 */
	public static double norm(double angle)
	{
		angle = angle % (Math.PI * 2.0f);
		return angle = angle < 0 ? angle + (Math.PI * 2.0f) : angle;
	}
	
	/**
	 * Finds the angleA opposite to sideA of a triangle where the lengths of all three sides are known.
	 * @param opposite double length of sideA, which is opposite the angle being searched for (angleA).
	 * @param sideB double length of sideB.
	 * @param sideC double length of sideC.
	 * @return double angle in radians of angleA, or zero if any parameters were zero.
	 */
	public static double getAngleA(double opposite, double sideB, double sideC)
	{
		if(opposite == 0.0f || sideB == 0.0f || sideC == 0.0f){
			return 0.0f;
		}
		return Math.acos((opposite * opposite - sideB * sideB - sideC * sideC) / (-2.0f * sideB * sideC));
	}
	
	/**
	 * Generates a random double between a high and low radian values.
	 * @param low double radian value.
	 * @param high double radian value.
	 * @return double.
	 */
	public static double getRandomFloat(double low, double high)
	{
		return (Math.random() * (high - low)) + low;
	}
	
	public static int getExclusiveRandomInt(int high, int low, int[] excluding)
	{
		int result = 0;
		boolean done = false;
		while(!done)
		{
			boolean found = false;
			result = (int)Math.round(new Random().nextDouble() * (high - low)) + low;
			for(int i = 0; i < excluding.length; i++)
			{
				if(excluding[i] == result){
					found = true;
					break;
				}
			}
			if(!found){
				done = true;
			}
		}
		return result;
	}
	
	public static int getExclusiveRandomInt(int high, int low, Vector<Integer> excluding)
	{
		int result = 0;
		boolean done = false;
		while(!done)
		{
			boolean found = false;
			result = (int)Math.round(new Random().nextDouble() * (high - low)) + low;
			for(int i = 0; i < excluding.size(); i++)
			{
				if(excluding.get(i) == result){
					found = true;
					break;
				}
			}
			if(!found){
				done = true;
			}
		}
		return result;
	}
	
	/**
	 * Finds the point where two lines intersect.  If the lines don't intersect, the returned point may contain NaN or Infinity.
	 * Found at: http://www.dreamincode.net/forums/topic/217676-intersection-point-of-two-lines/
	 * @param x1 double x of first point on first line.
	 * @param y1 double y of first point on first line.
	 * @param x2 double x of second point on first line.
	 * @param y2 double y of second point on first line.
	 * @param x3 double x of first point on second line.
	 * @param y3 double y of first point on second line.
	 * @param x4 double x of second point on second line.
	 * @param y4 double y of second point on second line.
	 * @return Point2D.Double
	 */
	public static Point2D.Double getLineIntersectF(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
	{
		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		double x = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
		double y = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
		return new Point2D.Double(x, y);
	}
	
	/**
	 * Finds the point where two lines intersect.  If the lines don't intersect, the returned point may contain NaN or Infinity.
	 * Found at: http://www.dreamincode.net/forums/topic/217676-intersection-point-of-two-lines/
	 * @param x1 double x of first point on first line.
	 * @param y1 double y of first point on first line.
	 * @param x2 double x of second point on first line.
	 * @param y2 double y of second point on first line.
	 * @param x3 double x of first point on second line.
	 * @param y3 double y of first point on second line.
	 * @param x4 double x of second point on second line.
	 * @param y4 double y of second point on second line.
	 * @return Point
	 */
	public static Point getLineIntersectI(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
	{
		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		int x = (int)Math.round(((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d);
		int y = (int)Math.round(((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d);
		return new Point(x, y);
	}
	
	/**
	 * Do line segments (x1, y1)--(x2, y2) and (x3, y3)--(x4, y4) intersect?
  	 * Found at: http://ptspts.blogspot.com/2010/06/how-to-determine-if-two-line-segments.html
  	 * This design avoids as many multiplications and divisions as possible.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @return boolean true if lines intersect, false otherwise.
	 */
	public static boolean LineSegmentsIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
 	{
 	  int d1 = ComputeDirection(x3, y3, x4, y4, x1, y1);
 	  int d2 = ComputeDirection(x3, y3, x4, y4, x2, y2);
 	  int d3 = ComputeDirection(x1, y1, x2, y2, x3, y3);
 	  int d4 = ComputeDirection(x1, y1, x2, y2, x4, y4);
 	  return (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
 	          ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) ||
 	         (d1 == 0 && IsOnSegment(x3, y3, x4, y4, x1, y1)) ||
 	         (d2 == 0 && IsOnSegment(x3, y3, x4, y4, x2, y2)) ||
 	         (d3 == 0 && IsOnSegment(x1, y1, x2, y2, x3, y3)) ||
 	         (d4 == 0 && IsOnSegment(x1, y1, x2, y2, x4, y4));
 	}
 	
 	private static int ComputeDirection(double xi, double yi, double xj, double yj, double xk, double yk)
 	{
		double a = (xk - xi) * (yj - yi);
		double b = (xj - xi) * (yk - yi);
		return a < b ? -1 : a > b ? 1 : 0;
	}
 	
 	private static boolean IsOnSegment(double xi, double yi, double xj, double yj, double xk, double yk)
 	{
 		return (xi <= xk || xj <= xk) && (xk <= xi || xk <= xj) && (yi <= yk || yj <= yk) && (yk <= yi || yk <= yj);
 	}
 	
 	/**
 	 * Finds possible intersecting points of two circles.  The length of the resulting array describes how many intersecting points were found.
 	 * 
 	 * Will return empty array if:
 	 *  - the circles do not intersect.
 	 *  - the circles coincide.
 	 *  - one circle contains the other.
 	 * 
 	 * Will return only one intersection point if the circle edges merely touch.
 	 * 
 	 * Will return two intersection points if either circle overlaps the other.
 	 * 
 	 * @param radius1 double radius of the first circle.
 	 * @param center1x double x-coordinate of first circle's center.
 	 * @param center1y double y-coordinate of first circle's center.
 	 * @param radius2 double radius of the second circle.
 	 * @param center2x double x-coordinate of second circle's center.
 	 * @param center2y double y-coordinate of second circle's center.
 	 * @return Point2D.Double[] array containing any possible intersecting points.
 	 */
 	public static Point2D.Double[] circleIntersects(double radius1, final double center1x, final double center1y, double radius2, final double center2x, final double center2y)
	{
		Point2D.Double[] results = null;
		
		double d = MathUtil.distance(center1x, center1y, center2x, center2y);
		
		// Determine possible solutions:
		if(d > radius1 + radius2){
			// Circles do not intersect.
			return new Point2D.Double[0];
		}else if(d == 0.0f && radius1 == radius2){
			// Circles coincide.
			return new Point2D.Double[0];
		}else if(d + Math.min(radius1, radius2) < Math.max(radius1, radius2)){
			// One circle contains the other.
			return new Point2D.Double[0];
		}else{
			double a = ((radius1 * radius1) - (radius2 * radius2) + (d * d)) / (2.0f * d);
			double h = Math.sqrt((radius1 * radius1) - (a * a));
			
			// Find p2
			Point2D.Double p2 = new Point2D.Double(center1x + (a * (center2x - center1x)) / d,
												center1y + (a * (center2y - center1y)) / d);
			
			results = new Point2D.Double[2];
			
			results[0] = new Point2D.Double(p2.x + (h * (center2y - center1y) / d),
											p2.y - (h * (center2x - center1x) / d));
			
			results[1] = new Point2D.Double(p2.x - (h * (center2y - center1y) / d),
											p2.y + (h * (center2x - center1x) / d));
			
		}
		return results;
	}
 	
 	/**
 	 * Compares the distance between the first point and two others, returning the closest.
 	 * @param x1 double x-value of the first point.
 	 * @param y1 double y-value of the first point.
 	 * @param x2 double x-value of a second point to compare to the first.
 	 * @param y2 double y-value of a second point to compare to the first.
 	 * @param x3 double x-value of a third point to compare to the first.
 	 * @param y3 double y-value of a third point to compare to the first.
 	 * @return java.awt.Point of the closest: either the second or third point.
 	 */
 	public static Point2D.Double minPoint(double x1, double y1, double x2, double y2, double x3, double y3)
	{
		if(MathUtil.distance(x1, y1, x2, y2) < MathUtil.distance(x1, y1, x3, y3)){
			return new Point2D.Double(x2, y2);
		}else{
			return new Point2D.Double(x3, y3);
		}
	}
 	
 	public static Point2D.Double rotatePoint(double x, double y, double centerX, double centerY, double radians)
 	{
 		double dx = x - centerX;
 		double dy = y - centerY;
 		double newX = centerX + (dx * Math.cos(radians) - dy * Math.sin(radians));
 		double newY = centerY + (dx * Math.sin(radians) + dy * Math.cos(radians));
 		return new Point2D.Double(newX, newY);
 	}
 	
 	public static Point centeroid(Vector<Point> points)
 	{
 		int xTotal = 0;
		int yTotal = 0;
		for(Point p : points)
		{
			xTotal += p.x;
			yTotal += p.y;
		}
		return new Point((int)Math.round(xTotal / (double)points.size()), (int)Math.round(yTotal / (double)points.size()));
 	}
 	
 	public static int clamp(int leftBound, int rightBound, int value)
 	{
 		return (value < leftBound) ? leftBound : (value > rightBound) ? rightBound : value;
 	}
 	
 	public static long clamp(long leftBound, long rightBound, long value)
 	{
 		return (value < leftBound) ? leftBound : (value > rightBound) ? rightBound : value;
 	}
 	
 	public static float clamp(float leftBound, float rightBound, float value)
 	{
 		return (value < leftBound) ? leftBound : (value > rightBound) ? rightBound : value;
 	}
 	
 	public static double clamp(double leftBound, double rightBound, double value)
 	{
 		return (value < leftBound) ? leftBound : (value > rightBound) ? rightBound : value;
 	}
}
