package jigsaw;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.UIManager.*;

@SuppressWarnings("serial")
public class TestApp extends JFrame
{
	private JigsawDialog mDialog = null;
	
	public TestApp()
	{
		this.initializeMain();
		return;
	}
	
	private void initializeMain()
	{
		this.initializeLookAndFeel();
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new GridBagLayout());
		
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent e)
			{
				test1();
				return;
			}
		});
		
		this.setFocusable(true); // required for keyboard events.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(basePanel);
		this.setMinimumSize(new Dimension(1028, 800));
		this.setPreferredSize(new Dimension(1028, 800));
		this.setResizable(true);
		this.setVisible(true);
		this.pack();
		return;
	}
	
	private void test1()
	{
		try{
			BufferedImage source = this.loadImage("tigers.png");
			
			this.mDialog = new JigsawDialog(this, Dialog.ModalityType.APPLICATION_MODAL, source);
			
			ImageIcon zoomIn = new ImageIcon(this.loadImage("zoom-in.png"));
			ImageIcon zoomOut = new ImageIcon(this.loadImage("zoom-out.png"));
			ImageIcon zoomReset = new ImageIcon(this.loadImage("one_to_one.png"));
			BufferedImage resizeGrip = this.loadImage("resize_grip.png");
			this.mDialog.initializeLayout(zoomIn, zoomOut, zoomReset, resizeGrip);
			
			// Just kill it when the test is done.
			System.exit(0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private BufferedImage loadImage(String path)
	{
		BufferedImage aBMP = null;
		try{
			File aFile = new File(path);
			aBMP = ImageIO.read(aFile);
		}catch(IOException ioe){
			ioe.printStackTrace();
			System.exit(1);
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(1);
		}
		return aBMP;
	}
	
	private void initializeLookAndFeel()
	{
		try{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if("Nimbus".equals(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			try{
				JFrame.setDefaultLookAndFeelDecorated(true);
			}catch(Exception ex2){
				ex2.printStackTrace();
				System.exit(1);
			}
		}
		return;
	}
	
	public static void main(String[] args)
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new TestApp();
			}
		});
		return;
	}
}
