package jigsaw;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Custom grip component for making resizing other components easier.  A lot of dialog windows and other windows have 
 * a small, triangular shaped grip in the lower left-hand corner.  Users often find it easier to drag and resize a 
 * window by this kind of grip.
 * 
 * The resizable component which uses this class needs to implement the ResizeGrip.ResizableClient interface.
 * 
 * @author John McCullock
 * @version 1.0 2017-03-31
 */

@SuppressWarnings("serial")
public class ResizeGrip extends JPanel
{
	private ResizeGrip.ResizableClient mListener = null;
	private BufferedImage mImage = null;
	private Point mMousePos = null;
	
	public ResizeGrip(ResizeGrip.ResizableClient listener, BufferedImage image)
	{
		this.mListener = listener;
		this.mImage = image;
		this.initializeLayout();
		return;
	}
	
	private void initializeLayout()
	{
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(this.mImage.getWidth(), this.mImage.getHeight()));
		this.setMaximumSize(new Dimension(this.mImage.getWidth(), this.mImage.getHeight()));
		this.setPreferredSize(new Dimension(this.mImage.getWidth(), this.mImage.getHeight()));
		this.addMouseListener(this.createMouseListener());
		this.addMouseMotionListener(this.createMouseMotionListener());
		return;
	}
	
	private MouseListener createMouseListener()
	{
		return new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				ResizeGrip.this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				return;
			}
			
			public void mouseExited(MouseEvent e)
			{
				ResizeGrip.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			
			public void mousePressed(MouseEvent e)
			{
				ResizeGrip.this.mMousePos = e.getLocationOnScreen();
				return;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				ResizeGrip.this.mMousePos = null;
				return;
			}
		};
	}
	
	private MouseMotionListener createMouseMotionListener()
	{
		return new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if(ResizeGrip.this.mMousePos == null){
					return;
				}
				int xDiff = e.getLocationOnScreen().x - ResizeGrip.this.mMousePos.x;
				int yDiff = e.getLocationOnScreen().y - ResizeGrip.this.mMousePos.y;
				int width = 0;
				int height  = 0;
				
				if(mListener.getWidth() + xDiff < mListener.getMinimumSize().width){
					width = mListener.getMinimumSize().width;
				}else if(mListener.getWidth() + xDiff > mListener.getMaximumSize().width){
					width = mListener.getMaximumSize().width;
				}else{
					width = mListener.getWidth() + xDiff;
					mMousePos.x = e.getLocationOnScreen().x;
				}
				
				if(mListener.getHeight() + yDiff < mListener.getMinimumSize().height){
					height = mListener.getMinimumSize().height;
				}else if(mListener.getHeight() +yDiff > mListener.getMaximumSize().height){
					height = mListener.getMaximumSize().height;
				}else{
					height = mListener.getHeight() + yDiff;
					mMousePos.y = e.getLocationOnScreen().y;
				}
				
				ResizeGrip.this.mListener.setSize(width, height);
				return;
			}
		};
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(this.mImage, 0, 0, null);
		g2d.dispose();
		return;
	}
	
	public interface ResizableClient
	{
		abstract int getHeight();
		abstract int getWidth();
		abstract void setSize(int width, int height);
		abstract Dimension getMinimumSize();
		abstract Dimension getMaximumSize();
	}
}
