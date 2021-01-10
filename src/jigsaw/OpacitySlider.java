package jigsaw;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class OpacitySlider extends JPanel
{
	private final int DEFAULT_GRIP_WIDTH = 11;
	private final Color DEFAULT_BORDER_COLOR = new Color(0, 0, 0, 255);
	private final float DEFAULT_BORDER_THICKNESS = 1f;
	private final int DEFAULT_MINIMUM_VALUE = 0;
	private final int DEFAULT_MAXIMUM_VALUE = 100;
	private final Color UNCHOSEN_TRANSPARENCY_CHECKER_BACKGROUND = Color.LIGHT_GRAY;
	private final Color UNCHOSEN_TRANSPARENCY_CHECKER_FOREGROUND = Color.WHITE;
	
	private OpacitySlider.Listener mListener = null;
	private Path2D.Double mGrip = null;
	private boolean mIsDragging = false;
	private Font mFont = null;
	private BasicStroke mStroke = null;
	private Color mCheckerBackground = UNCHOSEN_TRANSPARENCY_CHECKER_BACKGROUND;
	private Color mCheckerForeground = UNCHOSEN_TRANSPARENCY_CHECKER_FOREGROUND;
	private BufferedImage mBackground = null;
	private int mMinimum = DEFAULT_MINIMUM_VALUE;
	private int mMaximum = DEFAULT_MAXIMUM_VALUE;
	private int mValue = 0;
	private int mLeftLabelX = 0;
	private int mRightLabelX = 0;
	private int mLabelY = 0;
	private int mCrossover = 0;
	private ComponentListener mComponentListener = null;
	private MouseListener mMouseListener = null;
	private MouseMotionListener mMouseMotionListener = null;
	
	public OpacitySlider(OpacitySlider.Listener listener)
	{
		this.mListener = listener;
		this.initializeLayout();
		return;
	}
	
	private void initializeLayout()
	{
		this.mComponentListener = this.createComponentListener();
		this.mMouseListener = this.createMouseListener();
		this.mMouseMotionListener = this.createMouseMotionListener();
		
		this.setLayout(new BorderLayout());
		this.mStroke = new BasicStroke(DEFAULT_BORDER_THICKNESS);
		this.setBackground(Color.WHITE);
		this.setOpaque(true);
		this.mFont = this.determineDefaultFont();
		
		this.engageListeners();
		return;
	}
	
	private void revalidateGrip()
	{
		if(this.getWidth() <= 0 || this.getHeight() <= 0){
			return;
		}
		this.mGrip = new Path2D.Double();
		double x = this.getWidth() * (this.mValue / this.getWidth());
		double half = Math.floor(DEFAULT_GRIP_WIDTH / 2.0);
		this.mGrip.moveTo(x, 0.0);
		this.mGrip.lineTo(x, this.getHeight() / 2.0);
		this.mGrip.lineTo(x + half, this.getHeight() - 1);
		this.mGrip.lineTo(x - half, this.getHeight() - 1);
		this.mGrip.lineTo(x, this.getHeight() / 2.0);
		return;
	}
	
	private void revalidateFontValues()
	{
		int height = this.getFontMetrics(this.mFont).getAscent();
		int width = this.getFontMetrics(this.mFont).stringWidth(this.getTestValue());
		int increment = this.getFontMetrics(this.mFont).stringWidth("0");
		this.mRightLabelX = (this.getWidth() - this.getBorder().getBorderInsets(this).right) - (width + increment);
		this.mCrossover = this.mRightLabelX - increment;
		this.mLeftLabelX = this.mCrossover - (width + increment);
		this.mLabelY = (int)Math.round((this.getHeight() / 2.0) + (height / 2.0));
		//System.out.println(this.mRightLabelX + ", " + this.mCrossover + ", " + this.mLeftLabelX);
		return;
	}
	
	private String getTestValue()
	{
		StringBuilder builder = new StringBuilder();
		int value = String.valueOf(this.mMaximum).length();
		for(int i = 0; i < value; i++)
		{
			builder.append("0");
		}
		return builder.toString();
	}
	
	private Font determineDefaultFont()
	{
		return new JLabel().getFont();
	}
	
	private void createBackgroundImage()
	{
		if(this.getWidth() < 10 || this.getHeight() < 10){
			return;
		}
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		int totalWidth = this.getWidth() - (this.getBorder().getBorderInsets(this).left + this.getBorder().getBorderInsets(this).right);
		BufferedImage image = gc.createCompatibleImage(totalWidth, this.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		Patterns.paintCheckerPattern(g2d, 0, 0, image.getWidth(), image.getHeight(), 5, this.mCheckerBackground, this.mCheckerForeground);
		
		double increment = 255 / (double)image.getWidth();
		for(int i = 0; i < image.getWidth(); i++)
		{
			int alpha = (int)Math.floor(i * increment);
			g2d.setPaint(new Color(255, 255, 255, alpha));
			g2d.fillRect(i, 0, 1, this.getHeight());
		}
		
		g2d.dispose();
		this.mBackground = image;
		return;
	}
	
	private void moveGrip(Point p)
	{
		int x = MathUtil.clamp(0, this.getWidth() - 1, p.x);
		
		AffineTransform at = new AffineTransform();
		at.translate(x - this.mGrip.getBounds().getCenterX(), 0);
		this.mGrip.transform(at);
		
		int oldValue = this.mValue;
		this.mValue = (int)Math.floor((this.mMaximum + 1) *(this.mGrip.getBounds().getCenterX() / (double)this.getWidth()));
		if(oldValue != this.mValue){
			this.mListener.opacitySliderValueChanged(this.mValue);
		}
		return;
	}
	
	private void moveGrip(int value)
	{
		value = MathUtil.clamp(this.mMinimum, this.mMaximum, value);
		double x = this.getWidth() * (value / (double)this.mMaximum);
		
		AffineTransform at = new AffineTransform();
		at.translate(x - this.mGrip.getBounds().getCenterX(), 0);
		this.mGrip.transform(at);
		return;
	}
	
	private ComponentListener createComponentListener()
	{
		return new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				revalidateGrip();
				moveGrip(mValue);
				revalidateFontValues();
				createBackgroundImage();
				OpacitySlider.this.repaint();
				return;
			}
			
			public void componentShown(ComponentEvent e)
			{
				revalidateGrip();
				moveGrip(mValue);
				revalidateFontValues();
				createBackgroundImage();
				OpacitySlider.this.repaint();
				return;
			}
		};
	}
	
	private MouseListener createMouseListener()
	{
		return new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				mIsDragging = true;
				moveGrip(e.getPoint());
				OpacitySlider.this.repaint();
				return;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				mIsDragging = false;
				OpacitySlider.this.repaint();
				return;
			}
		};
	}
	
	private MouseMotionListener createMouseMotionListener()
	{
		return new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				if(!mIsDragging){
					return;
				}
				moveGrip(e.getPoint());
				OpacitySlider.this.repaint();
				return;
			}
			
			public void mouseMoved(MouseEvent e)
			{
				return;
			}
		};
	}
	
	public void engageListeners()
	{
		this.addComponentListener(this.mComponentListener);
		this.addMouseListener(this.mMouseListener);
		this.addMouseMotionListener(this.mMouseMotionListener);
		return;
	}
	
	public void disengageListeners()
	{
		this.removeComponentListener(this.mComponentListener);
		this.removeMouseListener(this.mMouseListener);
		this.removeMouseMotionListener(this.mMouseMotionListener);
		return;
	}
	
	public void setValue(int value)
	{
		this.mValue = value;
		if(this.mGrip != null){
			this.moveGrip(value);
		}
		return;
	}
	
	public int getValue()
	{
		return this.mValue;
	}
	
	@Override
	public void setFont(Font font)
	{
		this.mFont = font;
		super.setFont(font);
		this.revalidateFontValues();
		return;
	}
	
	public Font getFont()
	{
		return this.mFont;
	}
	
	public void setMinimum(int minimum)
	{
		this.mMinimum = minimum;
		this.revalidateFontValues();
		return;
	}
	
	public int getMinimum()
	{
		return this.mMinimum;
	}
	
	public void setMaximum(int maximum)
	{
		this.mMaximum = maximum;
		this.revalidateFontValues();
		return;
	}
	
	public int getMaximum()
	{
		return this.mMaximum;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(this.mGrip == null){
			return;
		}
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		if(this.mBackground != null){
			g2d.drawImage(this.mBackground, 0, 0, null);
		}
		g2d.setPaint(DEFAULT_BORDER_COLOR);
		g2d.setStroke(this.mStroke);
		g2d.draw(this.mGrip);
		g2d.setFont(this.mFont);
		if(this.mGrip.getBounds().getCenterX() <= this.mCrossover){
			g2d.drawString(String.valueOf(this.mValue), this.mRightLabelX, this.mLabelY);
		}else{
			g2d.drawString(String.valueOf(this.mValue), this.mLeftLabelX, this.mLabelY);
		}
		this.paintBorder(g2d);
		g2d.dispose();
		return;
	}
	
	public interface Listener
	{
		abstract void opacitySliderValueChanged(int value);
	}
}
