package jigsaw;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class TabHookData
{
	public Point2D.Double northwest = new Point2D.Double();
	public Point2D.Double northeast = new Point2D.Double();
	public Point2D.Double southeast = new Point2D.Double();
	public Point2D.Double southwest = new Point2D.Double();
	public Point2D.Double ctrlnorth = new Point2D.Double();
	public Point2D.Double ctrleast = new Point2D.Double();
	public Point2D.Double ctrlsouth = new Point2D.Double();
	public Point2D.Double ctrlwest = new Point2D.Double();
	public Point2D.Double[] north = new Point2D.Double[9];
	public Point2D.Double[] east = new Point2D.Double[9];
	public Point2D.Double[] south = new Point2D.Double[9];
	public Point2D.Double[] west = new Point2D.Double[9];
	
	public TabHookData() { return; }
	
	public TabHookData(TabHookData that)
	{
		this.northwest.setLocation(that.northwest.x, that.northwest.y);
		this.northeast.setLocation(that.northeast.x, that.northeast.y);
		this.southeast.setLocation(that.southeast.x, that.southeast.y);
		this.southwest.setLocation(that.southwest.x, that.southwest.y);
		this.ctrlnorth.setLocation(that.ctrlnorth.x, that.ctrlnorth.y);
		this.ctrleast.setLocation(that.ctrleast.x, that.ctrleast.y);
		this.ctrlsouth.setLocation(that.ctrlsouth.x, that.ctrlsouth.y);
		this.ctrlwest.setLocation(that.ctrlwest.x, that.ctrlwest.y);
		for(int i = 0; i < 9; i++)
		{
			this.north[i] = new Point2D.Double(that.north[i].getX(), that.north[i].getY());
			this.east[i] = new Point2D.Double(that.east[i].getX(), that.east[i].getY());
			this.south[i] = new Point2D.Double(that.south[i].getX(), that.south[i].getY());
			this.west[i] = new Point2D.Double(that.west[i].getX(), that.west[i].getY());
		}
		return;
	}
	
	public double getXMin()
	{
		double min = Math.min(northwest.x, southwest.x);
		for(int i = 0; i < 9; i++)
		{
			min = Math.min(min, west[i].x);
		}
		return min;
	}
	
	public double getYMin()
	{
		double min = Math.min(northwest.y, northeast.y);
		for(int i = 0; i < 9; i++)
		{
			min = Math.min(min, north[i].y);
		}
		return min;
	}
	
	public double getXMax()
	{
		double max = Math.max(northeast.x, southeast.x);
		for(int i = 0; i < 9; i++)
		{
			max = Math.max(max, east[i].x);
		}
		return max;
	}
	
	public double getYMax()
	{
		double max = Math.max(southwest.y, southeast.y);
		for(int i = 0; i < 9; i++)
		{
			max = Math.max(max, south[i].y);
		}
		return max;
	}
	
	public void translate(double x, double y)
	{
		this.northwest.x += x;
		this.northwest.y += y;
		this.northeast.x += x;
		this.northeast.y += y;
		this.southeast.x += x;
		this.southeast.y += y;
		this.southwest.x += x;
		this.southwest.y += y;
		this.ctrlnorth.x += x;
		this.ctrlnorth.y += y;
		this.ctrleast.x += x;
		this.ctrleast.y += y;
		this.ctrlsouth.x += x;
		this.ctrlsouth.y += y;
		this.ctrlwest.x += x;
		this.ctrlwest.y += y;
		for(int i = 0; i < 9; i++)
		{
			if(this.north != null){
				this.north[i].x += x;
				this.north[i].y += y;
			}
			if(this.east != null){
				this.east[i].x += x;
				this.east[i].y += y;
			}
			if(this.south != null){
				this.south[i].x += x;
				this.south[i].y += y;
			}
			if(this.west != null){
				this.west[i].x += x;
				this.west[i].y += y;
			}
		}
		return;
	}
	
	public void scale(double value, double xCenter, double centerY)
	{
		this.northwest.x = value * (this.northwest.x - xCenter);
		this.northwest.y = value * (this.northwest.y - centerY);
		this.northeast.x = value * (this.northeast.x - xCenter);
		this.northeast.y = value * (this.northeast.y - centerY);
		this.southeast.x = value * (this.southeast.x - xCenter);
		this.southeast.y = value * (this.southeast.y - centerY);
		this.southwest.x = value * (this.southwest.x - xCenter);
		this.southwest.y = value * (this.southwest.y - centerY);
		this.ctrlnorth.x = value * (this.ctrlnorth.x - xCenter);
		this.ctrlnorth.y = value * (this.ctrlnorth.y - centerY);
		this.ctrleast.x = value * (this.ctrleast.x - xCenter);
		this.ctrleast.y = value * (this.ctrleast.y - centerY);
		this.ctrlsouth.x = value * (this.ctrlsouth.x - xCenter);
		this.ctrlsouth.y = value * (this.ctrlsouth.y - centerY);
		this.ctrlwest.x = value * (this.ctrlwest.x - xCenter);
		this.ctrlwest.y = value * (this.ctrlwest.y - centerY);
		for(int i = 0; i < 9; i++)
		{
			if(this.north != null){
				this.north[i].x = value * (this.north[i].x - xCenter);
				this.north[i].y = value * (this.north[i].y - centerY);
			}
			if(this.east != null){
				this.east[i].x = value * (this.east[i].x - xCenter);
				this.east[i].y = value * (this.east[i].y - centerY);
			}
			if(this.south != null){
				this.south[i].x = value * (this.south[i].x - xCenter);
				this.south[i].y = value * (this.south[i].y - centerY);
			}
			if(this.west != null){
				this.west[i].x = value * (this.west[i].x - xCenter);
				this.west[i].y = value * (this.west[i].y - centerY);
			}
		}
		return;
	}
	
	public Path2D.Double getShape()
	{
		Path2D.Double p = new Path2D.Double();
		p.moveTo(this.northwest.getX(), this.northwest.getY());
		if(this.north != null){
			p.lineTo(this.north[0].getX(), this.north[0].getY());
			p.curveTo(this.north[0].getX(), this.north[0].getY(), this.north[1].getX(), this.north[1].getY(), this.north[2].getX(), this.north[2].getY());
			p.curveTo(this.north[2].getX(), this.north[2].getY(), this.north[3].getX(), this.north[3].getY(), this.north[4].getX(), this.north[4].getY());
			p.curveTo(this.north[4].getX(), this.north[4].getY(), this.north[5].getX(), this.north[5].getY(), this.north[6].getX(), this.north[6].getY());
			p.curveTo(this.north[6].getX(), this.north[6].getY(), this.north[7].getX(), this.north[7].getY(), this.north[8].getX(), this.north[8].getY());
		}
		
		p.lineTo(this.northeast.getX(), this.northeast.getY());
		if(this.east != null){
			p.lineTo(this.east[0].getX(), this.east[0].getY());
			p.curveTo(this.east[0].getX(), this.east[0].getY(), this.east[1].getX(), this.east[1].getY(), this.east[2].getX(), this.east[2].getY());
			p.curveTo(this.east[2].getX(), this.east[2].getY(), this.east[3].getX(), this.east[3].getY(), this.east[4].getX(), this.east[4].getY());
			p.curveTo(this.east[4].getX(), this.east[4].getY(), this.east[5].getX(), this.east[5].getY(), this.east[6].getX(), this.east[6].getY());
			p.curveTo(this.east[6].getX(), this.east[6].getY(), this.east[7].getX(), this.east[7].getY(), this.east[8].getX(), this.east[8].getY());
		}
			
		p.lineTo(this.southeast.getX(), this.southeast.getY());
		if(this.south != null){
			p.lineTo(this.south[0].getX(), this.south[0].getY());
			p.curveTo(this.south[0].getX(), this.south[0].getY(), this.south[1].getX(), this.south[1].getY(), this.south[2].getX(), this.south[2].getY());
			p.curveTo(this.south[2].getX(), this.south[2].getY(), this.south[3].getX(), this.south[3].getY(), this.south[4].getX(), this.south[4].getY());
			p.curveTo(this.south[4].getX(), this.south[4].getY(), this.south[5].getX(), this.south[5].getY(), this.south[6].getX(), this.south[6].getY());
			p.curveTo(this.south[6].getX(), this.south[6].getY(), this.south[7].getX(), this.south[7].getY(), this.south[8].getX(), this.south[8].getY());
		}
			
		p.lineTo(this.southwest.getX(), this.southwest.getY());
		if(this.west != null){
			p.lineTo(this.west[0].getX(), this.west[0].getY());
			p.curveTo(this.west[0].getX(), this.west[0].getY(), this.west[1].getX(), this.west[1].getY(), this.west[2].getX(), this.west[2].getY());
			p.curveTo(this.west[2].getX(), this.west[2].getY(), this.west[3].getX(), this.west[3].getY(), this.west[4].getX(), this.west[4].getY());
			p.curveTo(this.west[4].getX(), this.west[4].getY(), this.west[5].getX(), this.west[5].getY(), this.west[6].getX(), this.west[6].getY());
			p.curveTo(this.west[6].getX(), this.west[6].getY(), this.west[7].getX(), this.west[7].getY(), this.west[8].getX(), this.west[8].getY());
		}
		
		p.closePath();
		return p;
	}
}
