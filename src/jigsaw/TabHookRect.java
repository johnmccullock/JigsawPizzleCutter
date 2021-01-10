package jigsaw;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TabHookRect
{
	public static final Point2D.Double NORTHWEST = new Point2D.Double(-0.75, -1.0);
	public static final Point2D.Double NORTHEAST = new Point2D.Double(0.75, -1.0);
	public static final Point2D.Double SOUTHEAST = new Point2D.Double(0.75, 1.0);
	public static final Point2D.Double SOUTHWEST = new Point2D.Double(-0.75, 1.0);
	
	public static final Point2D.Double CTRL_NORTH = new Point2D.Double(0.0, -1.0);
	public static final Point2D.Double CTRL_EAST = new Point2D.Double(0.75, 0.0);
	public static final Point2D.Double CTRL_SOUTH = new Point2D.Double(0.0, 1.0);
	public static final Point2D.Double CTRL_WEST = new Point2D.Double(-0.75, 0.0);
	
	public static final Point2D.Double NORTH0 = new Point2D.Double(-0.25, -1.0);
	public static final Point2D.Double NORTH1 = new Point2D.Double(-0.125, -1.0);
	public static final Point2D.Double NORTH2 = new Point2D.Double(-0.125, -0.8333);
	public static final Point2D.Double NORTH3 = new Point2D.Double(-0.125, -0.666);
	public static final Point2D.Double NORTH4 = new Point2D.Double(0.0, -0.666);
	public static final Point2D.Double NORTH5 = new Point2D.Double(0.125, -0.666);
	public static final Point2D.Double NORTH6 = new Point2D.Double(0.125, -0.8333);
	public static final Point2D.Double NORTH7 = new Point2D.Double(0.125, -1.0);
	public static final Point2D.Double NORTH8 = new Point2D.Double(0.25, -1.0);
	
	public static final Point2D.Double EAST0 = new Point2D.Double(0.75, -0.333);
	public static final Point2D.Double EAST1 = new Point2D.Double(0.75, -0.1666);
	public static final Point2D.Double EAST2 = new Point2D.Double(0.875, -0.1666);
	public static final Point2D.Double EAST3 = new Point2D.Double(1.0, -0.1666);
	public static final Point2D.Double EAST4 = new Point2D.Double(1.0, 0.0);
	public static final Point2D.Double EAST5 = new Point2D.Double(1.0, 0.1666);
	public static final Point2D.Double EAST6 = new Point2D.Double(0.875, 0.1666);
	public static final Point2D.Double EAST7 = new Point2D.Double(0.75, 0.1666);
	public static final Point2D.Double EAST8 = new Point2D.Double(0.75, 0.333);
	
	public static final Point2D.Double SOUTH0 = new Point2D.Double(0.25, 1.0);
	public static final Point2D.Double SOUTH1 = new Point2D.Double(0.125, 1.0);
	public static final Point2D.Double SOUTH2 = new Point2D.Double(0.125, 0.8333);
	public static final Point2D.Double SOUTH3 = new Point2D.Double(0.125, 0.666);
	public static final Point2D.Double SOUTH4 = new Point2D.Double(0.0, 0.666);
	public static final Point2D.Double SOUTH5 = new Point2D.Double(-0.125, 0.666);
	public static final Point2D.Double SOUTH6 = new Point2D.Double(-0.125, 0.833);
	public static final Point2D.Double SOUTH7 = new Point2D.Double(-0.125, 1.0);
	public static final Point2D.Double SOUTH8 = new Point2D.Double(-0.25, 1.0);
	
	public static final Point2D.Double WEST0 = new Point2D.Double(-0.75, 0.333);
	public static final Point2D.Double WEST1 = new Point2D.Double(-0.75, 0.1666);
	public static final Point2D.Double WEST2 = new Point2D.Double(-0.875, 0.1666);
	public static final Point2D.Double WEST3 = new Point2D.Double(-1.0, 0.1666);
	public static final Point2D.Double WEST4 = new Point2D.Double(-1.0, 0.0);
	public static final Point2D.Double WEST5 = new Point2D.Double(-1.0, -0.1666);
	public static final Point2D.Double WEST6 = new Point2D.Double(-0.875, -0.1666);
	public static final Point2D.Double WEST7 = new Point2D.Double(-0.75, -0.1666);
	public static final Point2D.Double WEST8 = new Point2D.Double(-0.75, -0.333);
	
	private Point2D.Double mCenter = new Point2D.Double();
	private TabHookAnchor mNW = null;
	private TabHookAnchor mNE = null;
	private TabHookAnchor mSE = null;
	private TabHookAnchor mSW = null;
	private TabHookControl mCtrlN = null; // Control point between mNW and mNE.
	private TabHookControl mCtrlE = null; // Control point between mNE and mSE.
	private TabHookControl mCtrlS = null; // Control point between mSE and mSW.
	private TabHookControl mCtrlW = null; // Control point between mSW and mNW.
	private TabHook mNorth = null;
	private TabHook mEast = null;
	private TabHook mSouth = null;
	private TabHook mWest = null;
	
	public TabHookRect(double centerX, double centerY, double gripWidth)
	{
		this.mCenter.x = centerX;
		this.mCenter.y = centerY;
		this.mNW = new TabHookAnchor(0, 0, gripWidth);
		this.mNE = new TabHookAnchor(0, 0, gripWidth);
		this.mSE = new TabHookAnchor(0, 0, gripWidth);
		this.mSW = new TabHookAnchor(0, 0, gripWidth);
		this.mCtrlN = new TabHookControl(0, 0, gripWidth); // Control point between mNW and mNE.
		this.mCtrlE = new TabHookControl(0, 0, gripWidth); // Control point between mNE and mSE.
		this.mCtrlS = new TabHookControl(0, 0, gripWidth); // Control point between mSE and mSW.
		this.mCtrlW = new TabHookControl(0, 0, gripWidth); // Control point between mSW and mNW.
		this.mNorth = new TabHook(gripWidth);
		this.mEast = new TabHook(gripWidth);
		this.mSouth = new TabHook(gripWidth);
		this.mWest = new TabHook(gripWidth);
		return;
	}
	
	public TabHookRect(double centerX, double centerY, double width, double height, double gripWidth)
	{
		this.mCenter.x = centerX;
		this.mCenter.y = centerY;
		double halfWidth = width / 2.0;
		double halfHeight = height / 2.0;
		
		this.mNW = new TabHookAnchor(centerX + (halfWidth * NORTHWEST.x), centerY + (halfHeight * NORTHWEST.y), gripWidth);
		this.mNE = new TabHookAnchor(centerX + (halfWidth * NORTHEAST.x), centerY + (halfHeight * NORTHEAST.y), gripWidth);
		this.mSE = new TabHookAnchor(centerX + (halfWidth * SOUTHEAST.x), centerY + (halfHeight * SOUTHEAST.y), gripWidth);
		this.mSW = new TabHookAnchor(centerX + (halfWidth * SOUTHWEST.x), centerY + (halfHeight * SOUTHWEST.y), gripWidth);
		this.mCtrlN = new TabHookControl(centerX + (halfWidth * CTRL_NORTH.x), centerY + (halfHeight *CTRL_NORTH.y), gripWidth);
		this.mCtrlE = new TabHookControl(centerX + (halfWidth * CTRL_EAST.x), centerY + (halfHeight * CTRL_EAST.y), gripWidth);
		this.mCtrlS = new TabHookControl(centerX + (halfWidth * CTRL_SOUTH.x), centerY + (halfHeight * CTRL_SOUTH.y), gripWidth);
		this.mCtrlW = new TabHookControl(centerX + (halfWidth * CTRL_WEST.x), centerY + (halfHeight * CTRL_WEST.y), gripWidth);
		
		this.mNorth = new TabHook(gripWidth);
		this.mNorth.moveGrip(0, centerX + (halfWidth * NORTH0.x), centerY + (halfHeight * NORTH0.y));
		this.mNorth.moveGrip(1, centerX + (halfWidth * NORTH1.x), centerY + (halfHeight * NORTH1.y));
		this.mNorth.moveGrip(2, centerX + (halfWidth * NORTH2.x), centerY + (halfHeight * NORTH2.y));
		this.mNorth.moveGrip(3, centerX + (halfWidth * NORTH3.x), centerY + (halfHeight * NORTH3.y));
		this.mNorth.moveGrip(4, centerX + (halfWidth * NORTH4.x), centerY + (halfHeight * NORTH4.y));
		this.mNorth.moveGrip(5, centerX + (halfWidth * NORTH5.x), centerY + (halfHeight * NORTH5.y));
		this.mNorth.moveGrip(6, centerX + (halfWidth * NORTH6.x), centerY + (halfHeight * NORTH6.y));
		this.mNorth.moveGrip(7, centerX + (halfWidth * NORTH7.x), centerY + (halfHeight * NORTH7.y));
		this.mNorth.moveGrip(8, centerX + (halfWidth * NORTH8.x), centerY + (halfHeight * NORTH8.y));
		
		this.mEast = new TabHook(gripWidth);
		this.mEast.moveGrip(0, centerX + (halfWidth * EAST0.x), centerY + (halfHeight * EAST0.y));
		this.mEast.moveGrip(1, centerX + (halfWidth * EAST1.x), centerY + (halfHeight * EAST1.y));
		this.mEast.moveGrip(2, centerX + (halfWidth * EAST2.x), centerY + (halfHeight * EAST2.y));
		this.mEast.moveGrip(3, centerX + (halfWidth * EAST3.x), centerY + (halfHeight * EAST3.y));
		this.mEast.moveGrip(4, centerX + (halfWidth * EAST4.x), centerY + (halfHeight * EAST4.y));
		this.mEast.moveGrip(5, centerX + (halfWidth * EAST5.x), centerY + (halfHeight * EAST5.y));
		this.mEast.moveGrip(6, centerX + (halfWidth * EAST6.x), centerY + (halfHeight * EAST6.y));
		this.mEast.moveGrip(7, centerX + (halfWidth * EAST7.x), centerY + (halfHeight * EAST7.y));
		this.mEast.moveGrip(8, centerX + (halfWidth * EAST8.x), centerY + (halfHeight * EAST8.y));
		
		this.mSouth = new TabHook(gripWidth);
		this.mSouth.moveGrip(0, centerX + (halfWidth * SOUTH0.x), centerY + (halfHeight * SOUTH0.y));
		this.mSouth.moveGrip(1, centerX + (halfWidth * SOUTH1.x), centerY + (halfHeight * SOUTH1.y));
		this.mSouth.moveGrip(2, centerX + (halfWidth * SOUTH2.x), centerY + (halfHeight * SOUTH2.y));
		this.mSouth.moveGrip(3, centerX + (halfWidth * SOUTH3.x), centerY + (halfHeight * SOUTH3.y));
		this.mSouth.moveGrip(4, centerX + (halfWidth * SOUTH4.x), centerY + (halfHeight * SOUTH4.y));
		this.mSouth.moveGrip(5, centerX + (halfWidth * SOUTH5.x), centerY + (halfHeight * SOUTH5.y));
		this.mSouth.moveGrip(6, centerX + (halfWidth * SOUTH6.x), centerY + (halfHeight * SOUTH6.y));
		this.mSouth.moveGrip(7, centerX + (halfWidth * SOUTH7.x), centerY + (halfHeight * SOUTH7.y));
		this.mSouth.moveGrip(8, centerX + (halfWidth * SOUTH8.x), centerY + (halfHeight * SOUTH8.y));
		
		this.mWest = new TabHook(gripWidth);
		this.mWest.moveGrip(0, centerX + (halfWidth * WEST0.x), centerY + (halfHeight * WEST0.y));
		this.mWest.moveGrip(1, centerX + (halfWidth * WEST1.x), centerY + (halfHeight * WEST1.y));
		this.mWest.moveGrip(2, centerX + (halfWidth * WEST2.x), centerY + (halfHeight * WEST2.y));
		this.mWest.moveGrip(3, centerX + (halfWidth * WEST3.x), centerY + (halfHeight * WEST3.y));
		this.mWest.moveGrip(4, centerX + (halfWidth * WEST4.x), centerY + (halfHeight * WEST4.y));
		this.mWest.moveGrip(5, centerX + (halfWidth * WEST5.x), centerY + (halfHeight * WEST5.y));
		this.mWest.moveGrip(6, centerX + (halfWidth * WEST6.x), centerY + (halfHeight * WEST6.y));
		this.mWest.moveGrip(7, centerX + (halfWidth * WEST7.x), centerY + (halfHeight * WEST7.y));
		this.mWest.moveGrip(8, centerX + (halfWidth * WEST8.x), centerY + (halfHeight * WEST8.y));
		return;
	}
	
	public TabHookData getData(double width, double height)
	{
		TabHookData data = new TabHookData();
		double halfWidth = width / 2.0;
		double halfHeight = height / 2.0;
		
		data.northwest.x = (this.mNW.getX() - this.mCenter.x) / halfWidth;
		data.northwest.y = (this.mNW.getY() - this.mCenter.y) / halfHeight;
		data.northeast.x = (this.mNE.getX() - this.mCenter.x) / halfWidth;
		data.northeast.y = (this.mNE.getY() - this.mCenter.y) / halfHeight;
		data.southeast.x = (this.mSE.getX() - this.mCenter.x) / halfWidth;
		data.southeast.y = (this.mSE.getY() - this.mCenter.y) / halfHeight;
		data.southwest.x = (this.mSW.getX() - this.mCenter.x) / halfWidth;
		data.southwest.y = (this.mSW.getY() - this.mCenter.y) / halfHeight;
		data.ctrlnorth.x = (this.mCtrlN.getX() - this.mCenter.x) / halfWidth;
		data.ctrlnorth.y = (this.mCtrlN.getY() - this.mCenter.y) / halfHeight;
		data.ctrleast.x = (this.mCtrlE.getX() - this.mCenter.x) / halfWidth;
		data.ctrleast.y = (this.mCtrlE.getY() - this.mCenter.y) / halfHeight;
		data.ctrlsouth.x = (this.mCtrlS.getX() - this.mCenter.x) / halfWidth;
		data.ctrlsouth.y = (this.mCtrlS.getY() - this.mCenter.y) / halfHeight;
		data.ctrlwest.x = (this.mCtrlW.getX() - this.mCenter.x) / halfWidth;
		data.ctrlwest.y = (this.mCtrlW.getY() - this.mCenter.y) / halfHeight;
		for(int i = 0; i < 9; i++)
		{
			data.north[i] = new Point2D.Double((this.mNorth.getGripX(i) - this.mCenter.x) / halfWidth, (this.mNorth.getGripY(i) - this.mCenter.y) / halfHeight);
			data.east[i] = new Point2D.Double((this.mEast.getGripX(i) - this.mCenter.x) / halfWidth, (this.mEast.getGripY(i) - this.mCenter.y) / halfHeight);
			data.south[i] = new Point2D.Double((this.mSouth.getGripX(i) - this.mCenter.x) / halfWidth, (this.mSouth.getGripY(i) - this.mCenter.y) / halfHeight);
			data.west[i] = new Point2D.Double((this.mWest.getGripX(i) - this.mCenter.x) / halfWidth, (this.mWest.getGripY(i) - this.mCenter.y) / halfHeight);
		}
		return data;
	}
	
	public Path2D.Double getShape()
	{
		Path2D.Double p = new Path2D.Double();
		p.moveTo(this.mNW.getX(), this.mNW.getY());
		p.lineTo(this.mNorth.getGripX(0), this.mNorth.getGripY(0));
		p.curveTo(this.mNorth.getGripX(0), this.mNorth.getGripY(0), this.mNorth.getGripX(1), this.mNorth.getGripY(1), this.mNorth.getGripX(2), this.mNorth.getGripY(2));
		p.curveTo(this.mNorth.getGripX(2), this.mNorth.getGripY(2), this.mNorth.getGripX(3), this.mNorth.getGripY(3), this.mNorth.getGripX(4), this.mNorth.getGripY(4));
		p.curveTo(this.mNorth.getGripX(4), this.mNorth.getGripY(4), this.mNorth.getGripX(5), this.mNorth.getGripY(5), this.mNorth.getGripX(6), this.mNorth.getGripY(6));
		p.curveTo(this.mNorth.getGripX(6), this.mNorth.getGripY(6), this.mNorth.getGripX(7), this.mNorth.getGripY(7), this.mNorth.getGripX(8), this.mNorth.getGripY(8));
		
		p.lineTo(this.mNE.getX(), this.mNE.getY());
		p.lineTo(this.mEast.getGripX(0), this.mEast.getGripY(0));
		p.curveTo(this.mEast.getGripX(0), this.mEast.getGripY(0), this.mEast.getGripX(1), this.mEast.getGripY(1), this.mEast.getGripX(2), this.mEast.getGripY(2));
		p.curveTo(this.mEast.getGripX(2), this.mEast.getGripY(2), this.mEast.getGripX(3), this.mEast.getGripY(3), this.mEast.getGripX(4), this.mEast.getGripY(4));
		p.curveTo(this.mEast.getGripX(4), this.mEast.getGripY(4), this.mEast.getGripX(5), this.mEast.getGripY(5), this.mEast.getGripX(6), this.mEast.getGripY(6));
		p.curveTo(this.mEast.getGripX(6), this.mEast.getGripY(6), this.mEast.getGripX(7), this.mEast.getGripY(7), this.mEast.getGripX(8), this.mEast.getGripY(8));
		
		p.lineTo(this.mSE.getX(), this.mSE.getY());
		p.lineTo(this.mSouth.getGripX(0), this.mSouth.getGripY(0));
		p.curveTo(this.mSouth.getGripX(0), this.mSouth.getGripY(0), this.mSouth.getGripX(1), this.mSouth.getGripY(1), this.mSouth.getGripX(2), this.mSouth.getGripY(2));
		p.curveTo(this.mSouth.getGripX(2), this.mSouth.getGripY(2), this.mSouth.getGripX(3), this.mSouth.getGripY(3), this.mSouth.getGripX(4), this.mSouth.getGripY(4));
		p.curveTo(this.mSouth.getGripX(4), this.mSouth.getGripY(4), this.mSouth.getGripX(5), this.mSouth.getGripY(5), this.mSouth.getGripX(6), this.mSouth.getGripY(6));
		p.curveTo(this.mSouth.getGripX(6), this.mSouth.getGripY(6), this.mSouth.getGripX(7), this.mSouth.getGripY(7), this.mSouth.getGripX(8), this.mSouth.getGripY(8));
		
		p.lineTo(this.mSW.getX(), this.mSW.getY());
		p.lineTo(this.mWest.getGripX(0), this.mWest.getGripY(0));
		p.curveTo(this.mWest.getGripX(0), this.mWest.getGripY(0), this.mWest.getGripX(1), this.mWest.getGripY(1), this.mWest.getGripX(2), this.mWest.getGripY(2));
		p.curveTo(this.mWest.getGripX(2), this.mWest.getGripY(2), this.mWest.getGripX(3), this.mWest.getGripY(3), this.mWest.getGripX(4), this.mWest.getGripY(4));
		p.curveTo(this.mWest.getGripX(4), this.mWest.getGripY(4), this.mWest.getGripX(5), this.mWest.getGripY(5), this.mWest.getGripX(6), this.mWest.getGripY(6));
		p.curveTo(this.mWest.getGripX(6), this.mWest.getGripY(6), this.mWest.getGripX(7), this.mWest.getGripY(7), this.mWest.getGripX(8), this.mWest.getGripY(8));
		
		p.closePath();
		return p;
	}
	
	public void setNW(double x, double y)
	{
		this.mNW.setX(x);
		this.mNW.setY(y);
		return;
	}
	
	public Point2D.Double getNW()
	{
		return new Point2D.Double(this.mNW.getX(), this.mNW.getY());
	}
	
	public TabHookGrip getNWGrip()
	{
		return this.mNW;
	}
	
	public Rectangle2D.Double getNWBounds()
	{
		return this.mNW.getBounds();
	}
	
	public void setNE(double x, double y)
	{
		this.mNE.setX(x);
		this.mNE.setY(y);
		return;
	}
	
	public Point2D.Double getNE()
	{
		return new Point2D.Double(this.mNE.getX(), this.mNE.getY());
	}
	
	public TabHookGrip getNEGrip()
	{
		return this.mNE;
	}
	
	public Rectangle2D.Double getNEBounds()
	{
		return this.mNE.getBounds();
	}
	
	public void setSE(double x, double y)
	{
		this.mSE.setX(x);
		this.mSE.setY(y);
		return;
	}
	
	public Point2D.Double getSE()
	{
		return new Point2D.Double(this.mSE.getX(), this.mSE.getY());
	}
	
	public TabHookGrip getSEGrip()
	{
		return this.mSE;
	}
	
	public Rectangle2D.Double getSEBounds()
	{
		return this.mSE.getBounds();
	}
	
	public void setSW(double x, double y)
	{
		this.mSW.setX(x);
		this.mSW.setY(y);
		return;
	}
	
	public Point2D.Double getSW()
	{
		return new Point2D.Double(this.mSW.getX(), this.mSW.getY());
	}
	
	public TabHookGrip getSWGrip()
	{
		return this.mSW;
	}
	
	public Rectangle2D.Double getSWBounds()
	{
		return this.mSW.getBounds();
	}
	
	public void setCtrlN(double x, double y)
	{
		this.mCtrlN.setX(x);
		this.mCtrlN.setY(y);
		return;
	}
	
	public Point2D.Double getCtrlN()
	{
		return new Point2D.Double(this.mCtrlN.getX(), this.mCtrlN.getY());
	}
	
	public Rectangle2D.Double getCtrlNBounds()
	{
		return this.mCtrlN.getBounds();
	}
	
	public void setCtrlE(double x, double y)
	{
		this.mCtrlE.setX(x);
		this.mCtrlE.setY(y);
		return;
	}
	
	public Point2D.Double getCtrlE()
	{
		return new Point2D.Double(this.mCtrlE.getX(), this.mCtrlE.getY());
	}
	
	public Rectangle2D.Double getCtrlEBounds()
	{
		return this.mCtrlE.getBounds();
	}
	
	public void setCtrlS(double x, double y)
	{
		this.mCtrlS.setX(x);
		this.mCtrlS.setY(y);
		return;
	}
	
	public Point2D.Double getCtrlS()
	{
		return new Point2D.Double(this.mCtrlS.getX(), this.mCtrlS.getY());
	}
	
	public Rectangle2D.Double getCtrlSBounds()
	{
		return this.mCtrlS.getBounds();
	}
	
	public void setCtrlW(double x, double y)
	{
		this.mCtrlW.setX(x);
		this.mCtrlW.setY(y);
		return;
	}
	
	public Point2D.Double getCtrlW()
	{
		return new Point2D.Double(this.mCtrlW.getX(), this.mCtrlW.getY());
	}
	
	public Rectangle2D.Double getCtrlWBounds()
	{
		return this.mCtrlW.getBounds();
	}

	public void moveNorthGrip(int gripIndex, double x, double y)
	{
		this.mNorth.moveGrip(gripIndex, x, y);
		return;
	}
	
	public Rectangle2D.Double getNorthGripBounds(int gripIndex)
	{
		return this.mNorth.getGripBounds(gripIndex);
	}
	
	public void moveEastGrip(int gripIndex, double x, double y)
	{
		this.mEast.moveGrip(gripIndex, x, y);
		return;
	}
	
	public Rectangle2D.Double getEastGripBounds(int gripIndex)
	{
		return this.mEast.getGripBounds(gripIndex);
	}
	
	public void moveSouthGrip(int gripIndex, double x, double y)
	{
		this.mSouth.moveGrip(gripIndex, x, y);
		return;
	}
	
	public Rectangle2D.Double getSouthGripBounds(int gripIndex)
	{
		return this.mSouth.getGripBounds(gripIndex);
	}
	
	public void moveWestGrip(int gripIndex, double x, double y)
	{
		this.mWest.moveGrip(gripIndex, x, y);
		return;
	}
	
	public Rectangle2D.Double getWestGripBounds(int gripIndex)
	{
		return this.mWest.getGripBounds(gripIndex);
	}
	
	public double getCenterX()
	{
		return this.mCenter.x;
	}
	
	public double getCenterY()
	{
		return this.mCenter.y;
	}
	
	public TabHook getNorth()
	{
		return this.mNorth;
	}
	
	public TabHook getEast()
	{
		return this.mEast;
	}
	
	public TabHook getSouth()
	{
		return this.mSouth;
	}
	
	public TabHook getWest()
	{
		return this.mWest;
	}
	
	public Rectangle2D.Double getBounds()
	{
		double xMin = this.getXMin();
		double yMin = this.getYMin();
		return new Rectangle2D.Double(xMin, yMin, this.getXMax() - xMin, this.getYMax() - yMin);
	}
	
	private double getXMin()
	{
		double min = Math.min(this.mNW.getX(), this.mSW.getX());
		for(int i = 0; i < 9; i++)
		{
			min = Math.min(min, this.mWest.getGripX(i));
		}
		return min;
	}
	
	private double getYMin()
	{
		double min = Math.min(this.mNW.getY(), this.mNE.getY());
		for(int i = 0; i < 9; i++)
		{
			min = Math.min(min, this.mNorth.getGripY(i));
		}
		return min;
	}
	
	private double getXMax()
	{
		double max = Math.max(this.mNE.getX(), this.mSE.getX());
		for(int i = 0; i < 9; i++)
		{
			max = Math.max(max, this.mEast.getGripX(i));
		}
		return max;
	}
	
	private double getYMax()
	{
		double max = Math.max(this.mSW.getY(), this.mSE.getY());
		for(int i = 0; i < 9; i++)
		{
			max = Math.max(max, this.mSouth.getGripY(i));
		}
		return max;
	}
}
