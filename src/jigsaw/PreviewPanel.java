package jigsaw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

@SuppressWarnings("serial")
public class PreviewPanel extends JPanel
{
	private final String ZOOM_IN_BUTTON_TOOLTIP = "Zoom in";
	private final String ZOOM_OUT_BUTTON_TOOLTIP = "Zoom out";
	private final String ZOOM_1_TO_1_TOOLTIP = "Zoom 1 to 1";
	
	private JPanel mCanvas = null;
	private JScrollBar mHScroll = null;
	private JScrollBar mVScroll = null;
	private BufferedImage mProjection = null;
	private Viewport mViewport = new Viewport();
	private ZoomIterator mZoom = null;
	private JButton mZoomInButton = null;
	private JButton mZoomOutButton = null;
	private JButton mZoomResetButton = null;
	
	public PreviewPanel()
	{
		return;
	}
	
	public void initializeLayout()
	{
		this.setLayout(new GridBagLayout());
		this.mZoom = new ZoomIterator();
		this.mZoom.set1To1();
		
		this.initializeListeners();
		
		this.add(this.createCanvas(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.createVerticalScrollBar(), new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.createHorizontalScrollBar(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.createZoomBar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		return;
	}
	
	private void initializeListeners()
	{
		
		return;
	}
	
	private JPanel createCanvas()
	{	
		this.mCanvas = new JPanel()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				if(mProjection == null){
					return;
				}
				Graphics2D g2d = (Graphics2D)g;
				
				g2d.drawImage(mProjection, 0, 0, null);
				
				g2d.dispose();
				return;
			}
		};
		this.mCanvas.setLayout(new BorderLayout());
		return this.mCanvas;
	}
	
	private JScrollBar createHorizontalScrollBar()
	{
		this.mHScroll = new JScrollBar(JScrollBar.HORIZONTAL);
		return this.mHScroll;
	}
	
	private JScrollBar createVerticalScrollBar()
	{
		this.mVScroll = new JScrollBar(JScrollBar.VERTICAL);
		return this.mVScroll;
	}
	
	private JPanel createZoomBar()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new GridBagLayout());
		
		aPanel.add(this.mZoomInButton, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		aPanel.add(this.mZoomOutButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		aPanel.add(this.mZoomResetButton, new GridBagConstraints(2, 0, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		return aPanel;
	}
	
	public void initializeZoomInButton(ImageIcon icon)
	{
		this.mZoomInButton = new JButton();
		this.mZoomInButton.setIcon(icon);
		this.mZoomInButton.setToolTipText(ZOOM_IN_BUTTON_TOOLTIP);
		
		return;
	}
	
	public void initializeZoomOutButton(ImageIcon icon)
	{
		this.mZoomOutButton = new JButton();
		this.mZoomOutButton.setIcon(icon);
		this.mZoomOutButton.setToolTipText(ZOOM_OUT_BUTTON_TOOLTIP);
		
		return;
	}
	
	public void initializeZoomResetButton(ImageIcon icon)
	{
		this.mZoomResetButton = new JButton();
		this.mZoomResetButton.setIcon(icon);
		this.mZoomResetButton.setToolTipText(ZOOM_1_TO_1_TOOLTIP);
		
		return;
	}
	
	public JPanel getCanvas()
	{
		return this.mCanvas;
	}
	
	public void setProjection(BufferedImage image)
	{
		this.mProjection = image;
		return;
	}
	
	public void increaseZoom()
	{
		this.mZoom.increase();
		return;
	}
	
	public void decreaseZoom()
	{
		this.mZoom.decrease();
		return;
	}
	
	public void resetZoom()
	{
		this.mZoom.set1To1();
		return;
	}
	
	public double getZoom()
	{
		return this.mZoom.getZoom();
	}
	
	public JButton getZoomInButton()
	{
		return this.mZoomInButton;
	}
	
	public JButton getZoomOutButton()
	{
		return this.mZoomOutButton;
	}
	
	public JButton getZoomResetButton()
	{
		return this.mZoomResetButton;
	}
	
	public JScrollBar getHScroll()
	{
		return this.mHScroll;
	}
	
	public JScrollBar getVScroll()
	{
		return this.mVScroll;
	}
	
	private class Viewport
	{
		private int mX = 0;
		private int mY = 0;
		private int mWidth = 0;
		private int mHeight = 0;
		
		public Viewport() { return; }
		
		public Viewport(int x, int y, int width, int height)
		{
			this.mX = x;
			this.mY = y;
			this.mWidth = width;
			this.mHeight = height;
			return;
		}
		
		public void setX(int x)
		{
			this.mX = x;
			//System.out.println("Display: " + GraphicsPane.this.mCanvas.getWidth() + ", " + GraphicsPane.this.mCanvas.getHeight());
			//GraphicsPaneListener.notifyViewportChange(GraphicsPane.this, GraphicsPane.this.mID, this.mX, this.mY, this.mWidth, this.mHeight);
			return;
		}
		
		public int getX()
		{
			return this.mX;
		}
		
		public void setY(int y)
		{
			this.mY = y;
			//System.out.println("Display: " + GraphicsPane.this.mCanvas.getWidth() + ", " + GraphicsPane.this.mCanvas.getHeight());
			//GraphicsPaneListener.notifyViewportChange(GraphicsPane.this, GraphicsPane.this.mID, this.mX, this.mY, this.mWidth, this.mHeight);
			return;
		}
		
		public int getY()
		{
			return this.mY;
		}
		
		public void setWidth(int width)
		{
			this.mWidth = width;
			//System.out.println("Display: " + GraphicsPane.this.mCanvas.getWidth() + ", " + GraphicsPane.this.mCanvas.getHeight());
			//GraphicsPaneListener.notifyViewportChange(GraphicsPane.this, GraphicsPane.this.mID, this.mX, this.mY, this.mWidth, this.mHeight);
			return;
		}
		
		public int getWidth()
		{
			return this.mWidth;
		}
		
		public void setHeight(int height)
		{
			this.mHeight = height;
			//System.out.println("Display: " + GraphicsPane.this.mCanvas.getWidth() + ", " + GraphicsPane.this.mCanvas.getHeight());
			//GraphicsPaneListener.notifyViewportChange(GraphicsPane.this, GraphicsPane.this.mID, this.mX, this.mY, this.mWidth, this.mHeight);
			return;
		}
		
		public int getHeight()
		{
			return this.mHeight;
		}
		
		public void setLocation(int x, int y)
		{
			this.mX = x;
			this.mY = y;
			//GraphicsPaneListener.notifyViewportChange(GraphicsPane.this, GraphicsPane.this.mID, this.mX, this.mY, this.mWidth, this.mHeight);
			return;
		}
		
		public void setSize(int width, int height)
		{
			this.mWidth = width;
			this.mHeight = height;
			//GraphicsPaneListener.notifyViewportChange(GraphicsPane.this, GraphicsPane.this.mID, this.mX, this.mY, this.mWidth, this.mHeight);
			return;
		}
	}
}
