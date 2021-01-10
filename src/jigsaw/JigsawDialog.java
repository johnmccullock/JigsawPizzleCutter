package jigsaw;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class JigsawDialog extends JDialog implements OpacitySlider.Listener, ResizeGrip.ResizableClient
{
	private static final Insets DIALOG_MARGINS = new Insets(20, 2, 2, 2);
	private static final Insets COMPONENT_MARGINS = new Insets(6, 6, 6, 6);
	private static final Insets TEXT_FIELD_INSETS = new Insets(2, 4, 2, 4);
	private static final int DEFAULT_ROW_COL_MINIMUM = 1;
	private static final int DEFAULT_ROW_COL_MAXIMUM = 100;
	private static final String ROW_COL_EXCEPTION_CAPTION = "Row or column value is invalid.  Expecting value between 1 and 100.";
	private static final String SHOW_IMAGE_OPTION_CAPTION = "Show Image";
	private static final boolean DEFAULT_SHOW_IMAGE = false;
	private static final int DEFAULT_OPACITY_SLIDER_MINIMUM = 0;
	private static final int DEFAULT_OPACITY_SLIDER_MAXIMUM = 100;
	private static final int DEFAULT_OPACITY_SLIDER_VALUE = 50;
	private static final String RESET_BUTTON_CAPTION = "Reset";
	private static final String RESET_BUTTON_TOOLTIP = "Discard all changes and return to default shape.";
	private static final String RESET_BUTTON_COMMAND = "reset";
	private static final String OKAY_BUTTON_CAPTION = "Okay";
	private static final String OKAY_BUTTON_COMMAND = "okay";
	private static final String OKAY_BUTTON_TOOLTIP = "Accept these options and apply.";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";
	private static final String CANCEL_BUTTON_COMMAND = "cancel";
	private static final String CANCEL_BUTTON_TOOLTIP = "Cancel this action.";
	private static final String HELP_BUTTON_CAPTION = "Help";
	private static final String HELP_BUTTON_COMMAND = "help";
	private static final String HELP_BUTTON_TOOLTIP = "Show helpful information for this dialog window.";
	private static final Insets BUTTON_MARGINS = new Insets(2, 2, 2, 2);
	private static final float BUTTON_WIDTH_FACTOR = 2.0f;
	private static final float BUTTON_HEIGHT_FACTOR = 2.0f;
	private static final Color TEXTFIELD_ERROR_STATE_COLOR = new Color(255, 64, 64, 255);
	private static final Color TEXTFIELD_NORMAL_BACKGROUND = UIManager.getColor("TextField.background");
	private static final int DEFAULT_ROWS_VALUE = 10;
	private static final int DEFAULT_COLS_VALUE = 10;
	private static final int DEFAULT_TABHOOKRECT_WIDTH = 1000;
	private static final int DEFAULT_TABHOOKRECT_HEIGHT = 1000;
	
	private JTextField mRowsField = null;
	private DocumentListener mRowsDocumentListener = null;
	private JTextField mColsField = null;
	private DocumentListener mColsDocumentListener = null;
	private JCheckBox mShowImageOption = null;
	private ItemListener mShowImageOptionItemListener = null;
	private OpacitySlider mOpacitySlider = null;
	private JSplitPane mSplitPane = null;
	private EditorPanel mEditorPanel = null;
	private PreviewPanel mPreviewPanel = null;
	private MouseListener mEditorMouseListener = null;
	private MouseMotionListener mEditorMouseMotionListener = null;
	private MouseWheelListener mEditorMouseWheelListener = null;
	private MouseListener mPreviewMouseListener = null;
	private MouseMotionListener mPreviewMouseMotionListener = null;
	private MouseWheelListener mPreviewMouseWheelListener = null;
	private ActionListener mEditorZoomInButtonListener = null;
	private ActionListener mEditorZoomOutButtonListener = null;
	private ActionListener mEditorZoomResetButtonListener = null;
	private ActionListener mPreviewZoomInButtonListener = null;
	private ActionListener mPreviewZoomOutButtonListener = null;
	private ActionListener mPreviewZoomResetButtonListener = null;
	private AdjustmentListener mEditorHScrollAdjustmentListener = null;
	private AdjustmentListener mEditorVScrollAdjustmentListener = null;
	private AdjustmentListener mPreviewHScrollAdjustmentListener = null;
	private AdjustmentListener mPreviewVScrollAdjustmentListener = null;
	private JButton mResetButton = null;
	private JButton mOkayButton = null;
	private JButton mCancelButton = null;
	private JButton mHelpButton = null;
	
	private GraphicsConfiguration mGC = null;
	private BufferedImage mSource = null;
	private TabHookRect mRect = null;
	private TabHookGrip mCurrent = null;
	private Point2D.Double mEditorMouse = null;
	private TabHookFrame mPreviewFrame = null;
	private Dimension mPreview1to1 = new Dimension();
	private JigsawConfirmDialog mJigsawConfirmDialog = null;
	
	public JigsawDialog(JFrame parent, Dialog.ModalityType modality, BufferedImage source)
	{
		super(parent, modality);
		this.mSource = source;
		return;
	}
	
	public void initializeLayout(ImageIcon zoomIn, ImageIcon zoomOut, ImageIcon zoomReset, BufferedImage resizeGrip)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		this.mGC = gd.getDefaultConfiguration();
		
		JPanel basePanel = new JPanel();
		basePanel.setBorder(BorderFactory.createEmptyBorder(DIALOG_MARGINS.top, DIALOG_MARGINS.left, DIALOG_MARGINS.bottom, DIALOG_MARGINS.right));
		basePanel.setLayout(new GridBagLayout());
		
		this.mRowsDocumentListener = this.createRowsDocumentListener();
		this.mColsDocumentListener = this.createColsDocumentListener();
		this.mShowImageOptionItemListener = this.createShowImageOptionItemListener();
		this.mEditorMouseListener = this.createEditorMouseListener();
		this.mEditorMouseMotionListener = this.createEditorMouseMotionListener();
		this.mEditorMouseWheelListener = this.createEditorMouseWheelListener();
		this.mEditorZoomInButtonListener = this.createEditorZoomInButtonListener();
		this.mEditorZoomOutButtonListener = this.createEditorZoomOutButtonListener();
		this.mEditorZoomResetButtonListener = this.createEditorZoomResetButtonListener();
		this.mEditorHScrollAdjustmentListener = this.createEditorHScrollAdjustmentListener();
		this.mEditorVScrollAdjustmentListener = this.createEditorVScrollAdjustmentListener();
		this.mPreviewMouseListener = this.createPreviewMouseListener();
		this.mPreviewMouseMotionListener = this.createPreviewMouseMotionListener();
		this.mPreviewMouseWheelListener = this.createPreviewMouseWheelListener();
		this.mPreviewZoomInButtonListener = this.createPreviewZoomInButtonListener();
		this.mPreviewZoomOutButtonListener = this.createPreviewZoomOutButtonListener();
		this.mPreviewZoomResetButtonListener = this.createPreviewZoomResetButtonListener();
		this.mPreviewHScrollAdjustmentListener = this.createPreviewHScrollAdjustmentListener();
		this.mPreviewVScrollAdjustmentListener = this.createPreviewVScrollAdjustmentListener();
		
		basePanel.add(this.createTopRow(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		this.mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.mSplitPane.setBorder(BorderFactory.createLoweredBevelBorder());
		this.mSplitPane.setLeftComponent(this.createEditorPanel(zoomIn, zoomOut, zoomReset));
		this.mSplitPane.setRightComponent(this.createPreviewPanel(zoomIn, zoomOut, zoomReset));
		basePanel.add(this.mSplitPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(20, COMPONENT_MARGINS.left, 0, COMPONENT_MARGINS.right), 0, 0));
		
		basePanel.add(this.createButtonRow(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(30, 0, 0, 0), 0, 0));
		
		basePanel.add(new ResizeGrip(this, resizeGrip), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		this.mRowsField.setText(String.valueOf(DEFAULT_ROWS_VALUE));
		this.mColsField.setText(String.valueOf(DEFAULT_COLS_VALUE));

		this.engageListeners();
		
		this.setResizable(true);
		this.setMinimumSize(new Dimension(1028, 800));
		this.setPreferredSize(new Dimension(1028, 800));
		
		this.addComponentListener(this.createDialogComponentListener());
		this.addWindowListener(this.createDialogWindowListener());
		this.setContentPane(basePanel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();
		return;
	}
	
	private JPanel createTopRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new GridBagLayout());
		int indent = new JTextField().getBorder().getBorderInsets(null).left;
		
		aPanel.add(this.createResetButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, COMPONENT_MARGINS.left, 0, COMPONENT_MARGINS.right), 0, 0));
		
		// Spacer.
		aPanel.add(new JLabel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		aPanel.add(new JLabel("Rows:"), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, COMPONENT_MARGINS.left + indent, 0, 0), 0, 0));
		aPanel.add(this.createRowsField(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, COMPONENT_MARGINS.left, 0, COMPONENT_MARGINS.right), 0, 0));
		
		aPanel.add(new JLabel("Cols:"), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, COMPONENT_MARGINS.left + indent, 0, 0), 0, 0));
		aPanel.add(this.createColsField(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, COMPONENT_MARGINS.left, 0, COMPONENT_MARGINS.right), 0, 0));
		
		aPanel.add(this.createShowImageOption(), new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, COMPONENT_MARGINS.left, 0, COMPONENT_MARGINS.right), 0, 0));
		
		aPanel.add(this.createOpacitySliderContainer(), new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, COMPONENT_MARGINS.left, 0, COMPONENT_MARGINS.right), 0, 0));
		return aPanel;
	}
	
	private JTextField createRowsField()
	{
		this.mRowsField = new JTextField();
		int minHeight = this.mRowsField.getFontMetrics(this.mRowsField.getFont()).getHeight();
		minHeight += TEXT_FIELD_INSETS.top + TEXT_FIELD_INSETS.bottom;
		minHeight += this.mRowsField.getBorder().getBorderInsets(null).top + this.mRowsField.getBorder().getBorderInsets(null).bottom;
		this.mRowsField.setMargin(TEXT_FIELD_INSETS);
		this.mRowsField.setMinimumSize(new Dimension(100, minHeight));
		this.mRowsField.setPreferredSize(new Dimension(100, minHeight));
		this.mRowsField.setHorizontalAlignment(JTextField.CENTER);
		this.mRowsField.setDocument(new IntegerOnlyDocument(3));
		this.mRowsField.addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				mRowsField.selectAll();
				return;
			}
			
			public void focusLost(FocusEvent e) { return; }
		});
		return this.mRowsField;
	}
	
	private JTextField createColsField()
	{
		this.mColsField = new JTextField();
		int minHeight = this.mColsField.getFontMetrics(this.mColsField.getFont()).getHeight();
		minHeight += TEXT_FIELD_INSETS.top + TEXT_FIELD_INSETS.bottom;
		minHeight += this.mColsField.getBorder().getBorderInsets(null).top + this.mColsField.getBorder().getBorderInsets(null).bottom;
		this.mColsField.setMargin(TEXT_FIELD_INSETS);
		this.mColsField.setMinimumSize(new Dimension(100, minHeight));
		this.mColsField.setPreferredSize(new Dimension(100, minHeight));
		this.mColsField.setHorizontalAlignment(JTextField.CENTER);
		this.mColsField.setDocument(new IntegerOnlyDocument(3));
		this.mColsField.addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				mColsField.selectAll();
				return;
			}
			
			public void focusLost(FocusEvent e) { return; }
		});
		return this.mColsField;
	}
	
	private JCheckBox createShowImageOption()
	{
		this.mShowImageOption = new JCheckBox();
		this.mShowImageOption.setText(SHOW_IMAGE_OPTION_CAPTION);
		return this.mShowImageOption;
	}
	
	private ItemListener createShowImageOptionItemListener()
	{
		return new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private JPanel createOpacitySliderContainer()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BorderLayout());
		aPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		Dimension minSize = this.mRowsField.getMinimumSize().width > this.mColsField.getMinimumSize().width ? this.mRowsField.getMinimumSize() : this.mColsField.getMinimumSize();
		Dimension prefSize = this.mRowsField.getPreferredSize().width > this.mColsField.getPreferredSize().width ? this.mRowsField.getPreferredSize() : this.mColsField.getPreferredSize();
		
		aPanel.setMinimumSize(minSize);
		aPanel.setPreferredSize(prefSize);
		
		this.mOpacitySlider = new OpacitySlider(JigsawDialog.this);
		this.mOpacitySlider.setMinimum(DEFAULT_OPACITY_SLIDER_MINIMUM);
		this.mOpacitySlider.setMaximum(DEFAULT_OPACITY_SLIDER_MAXIMUM);
		this.mOpacitySlider.setValue(DEFAULT_OPACITY_SLIDER_VALUE);
		
		aPanel.add(this.mOpacitySlider, BorderLayout.CENTER);
		return aPanel;
	}
	
	private JButton createResetButton()
	{
		this.mResetButton = new JButton(RESET_BUTTON_CAPTION);
		this.mResetButton.setToolTipText(RESET_BUTTON_TOOLTIP);
		this.mResetButton.setActionCommand(RESET_BUTTON_COMMAND);
		this.mResetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mSplitPane.setDividerLocation(0.5);
				resetTabHookRect();
				initializePreviewBaseSize();
				
				resetEditor();
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		});
		return this.mResetButton;
	}
	
	private DocumentListener createRowsDocumentListener()
	{
		return new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				if(checkRowColumnValue(mRowsField)){
					this.resetPreview();
				}
				return;
			}
			
			public void insertUpdate(DocumentEvent e)
			{
				if(checkRowColumnValue(mRowsField)){
					this.resetPreview();
				}
				return;
			}
			
			public void removeUpdate(DocumentEvent e)
			{
				if(checkRowColumnValue(mRowsField)){
					this.resetPreview();
				}
				return;
			}
			
			private void resetPreview()
			{
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private DocumentListener createColsDocumentListener()
	{
		return new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				if(checkRowColumnValue(mRowsField)){
					this.resetPreview();
				}
				return;
			}
			
			public void insertUpdate(DocumentEvent e)
			{
				if(checkRowColumnValue(mRowsField)){
					this.resetPreview();
				}
				return;
			}
			
			public void removeUpdate(DocumentEvent e)
			{
				if(checkRowColumnValue(mRowsField)){
					this.resetPreview();
				}
				return;
			}
			
			private void resetPreview()
			{
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private MouseListener createEditorMouseListener()
	{
		return new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				return;
			}
			
			public void mouseExited(MouseEvent e)
			{
				return;
			}
			
			public void mousePressed(MouseEvent e)
			{
				TabHookData item = createShapeData();
				item.scale(mEditorPanel.getZoom(), mRect.getCenterX(), mRect.getCenterY());
				double xCenter = (mEditorPanel.getCanvas().getWidth() / 2.0);
				double yCenter = (mEditorPanel.getCanvas().getHeight() / 2.0);
				item.translate(xCenter, yCenter);
				
				item.translate(-mEditorPanel.getHScroll().getValue(), -mEditorPanel.getVScroll().getValue());
				for(int i = 0; i < 9; i++)
				{
					if(e.getX() - item.north[i].x >= -5 && e.getX() - item.north[i].x <= 5 && e.getY() - item.north[i].y >= -5 && e.getY() - item.north[i].y <= 5){
						mCurrent = mRect.getNorth().getGrip(i);
						mEditorMouse = new Point2D.Double(e.getX(), e.getY());
					}
					if(e.getX() - item.east[i].x >= -5 && e.getX() - item.east[i].x <= 5 && e.getY() - item.east[i].y >= -5 && e.getY() - item.east[i].y <= 5){
						mCurrent = mRect.getEast().getGrip(i);
						mEditorMouse = new Point2D.Double(e.getX(), e.getY());
					}
					if(e.getX() - item.south[i].x >= -5 && e.getX() - item.south[i].x <= 5 && e.getY() - item.south[i].y >= -5 && e.getY() - item.south[i].y <= 5){
						mCurrent = mRect.getSouth().getGrip(i);
						mEditorMouse = new Point2D.Double(e.getX(), e.getY());
					}
					if(e.getX() - item.west[i].x >= -5 && e.getX() - item.west[i].x <= 5 && e.getY() - item.west[i].y >= -5 && e.getY() - item.west[i].y <= 5){
						mCurrent = mRect.getWest().getGrip(i);
						mEditorMouse = new Point2D.Double(e.getX(), e.getY());
					}
				}
				return;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				mCurrent = null;
				mEditorMouse = null;
				return;
			}
		};
	}
	
	private MouseMotionListener createEditorMouseMotionListener()
	{
		return new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				if(mCurrent == null){
					return;
				}
				if(mEditorMouse == null){
					return;
				}
				mCurrent.setX(mCurrent.getX() + ((e.getX() - mEditorMouse.x) / mEditorPanel.getZoom()));
				mCurrent.setY(mCurrent.getY() + ((e.getY() - mEditorMouse.y) / mEditorPanel.getZoom()));
				
				mEditorMouse.x = e.getX();
				mEditorMouse.y = e.getY();
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
			
			public void mouseMoved(MouseEvent e)
			{
//				TabHookData item = createShapeData();
//				item.scale(mEditorPanel.getZoom(), mRect.getCenterX(), mRect.getCenterY());
//				double xCenter = (mEditorPanel.getCanvas().getWidth() / 2.0);
//				double yCenter = (mEditorPanel.getCanvas().getHeight() / 2.0);
//				item.translate(xCenter, yCenter);
//				
//				item.translate(-mEditorPanel.getHScroll().getValue(), -mEditorPanel.getVScroll().getValue());
//				for(int i = 0; i < 9; i++)
//				{
//					if(e.getX() - item.north[i].x >= -5 && e.getX() - item.north[i].x <= 5 && e.getY() - item.north[i].y >= -5 && e.getY() - item.north[i].y <= 5){
//						System.out.println("hit");
//					}
//					if(e.getX() - item.east[i].x >= -5 && e.getX() - item.east[i].x <= 5 && e.getY() - item.east[i].y >= -5 && e.getY() - item.east[i].y <= 5){
//						System.out.println("hit");
//					}
//					if(e.getX() - item.south[i].x >= -5 && e.getX() - item.south[i].x <= 5 && e.getY() - item.south[i].y >= -5 && e.getY() - item.south[i].y <= 5){
//						System.out.println("hit");
//					}
//					if(e.getX() - item.west[i].x >= -5 && e.getX() - item.west[i].x <= 5 && e.getY() - item.west[i].y >= -5 && e.getY() - item.west[i].y <= 5){
//						System.out.println("hit");
//					}
//				}
				return;
			}
		};
	}
	
	private MouseWheelListener createEditorMouseWheelListener()
	{
		return new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if(e.getWheelRotation() < 0){
					mEditorPanel.increaseZoom();
				}else{
					mEditorPanel.decreaseZoom();
				}
				recalculateEditorScrollBars();
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private MouseListener createPreviewMouseListener()
	{
		return new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				return;
			}
			
			public void mouseExited(MouseEvent e)
			{
				return;
			}
			
			public void mousePressed(MouseEvent e)
			{
				return;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				return;
			}
		};
	}
	
	private MouseMotionListener createPreviewMouseMotionListener()
	{
		return new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				return;
			}
			
			public void mouseMoved(MouseEvent e)
			{
				return;
			}
		};
	}
	
	private MouseWheelListener createPreviewMouseWheelListener()
	{
		return new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if(e.getWheelRotation() < 0){
					mPreviewPanel.increaseZoom();
				}else{
					mPreviewPanel.decreaseZoom();
				}
				resetTabHookFrame();
				recalculatePreviewScrollBars();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private ActionListener createEditorZoomInButtonListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mEditorPanel.increaseZoom();
				recalculateEditorScrollBars();
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private ActionListener createEditorZoomOutButtonListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mEditorPanel.decreaseZoom();
				recalculateEditorScrollBars();
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private ActionListener createEditorZoomResetButtonListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mEditorPanel.resetZoom();
				recalculateEditorScrollBars();
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private ActionListener createPreviewZoomInButtonListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mPreviewPanel.increaseZoom();
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private ActionListener createPreviewZoomOutButtonListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mPreviewPanel.decreaseZoom();
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private ActionListener createPreviewZoomResetButtonListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mPreviewPanel.resetZoom();
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private AdjustmentListener createEditorHScrollAdjustmentListener()
	{
		return new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private AdjustmentListener createEditorVScrollAdjustmentListener()
	{
		return new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				drawEditorProjection();
				mEditorPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private AdjustmentListener createPreviewHScrollAdjustmentListener()
	{
		return new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private AdjustmentListener createPreviewVScrollAdjustmentListener()
	{
		return new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		};
	}
	
	private EditorPanel createEditorPanel(ImageIcon zoomIn, ImageIcon zoomOut, ImageIcon zoomReset)
	{
		this.mEditorPanel = new EditorPanel();
		this.mEditorPanel.initializeZoomInButton(zoomIn);
		this.mEditorPanel.initializeZoomOutButton(zoomOut);
		this.mEditorPanel.initializeZoomResetButton(zoomReset);
		this.mEditorPanel.initializeLayout();
		this.mEditorPanel.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				resetEditor();
				drawPreviewProjection();
				return;
			}
		});
		return this.mEditorPanel;
	}
	
	private PreviewPanel createPreviewPanel(ImageIcon zoomIn, ImageIcon zoomOut, ImageIcon zoomReset)
	{
		this.mPreviewPanel = new PreviewPanel();
		this.mPreviewPanel.initializeZoomInButton(zoomIn);
		this.mPreviewPanel.initializeZoomOutButton(zoomOut);
		this.mPreviewPanel.initializeZoomResetButton(zoomReset);
		this.mPreviewPanel.initializeLayout();
		this.mPreviewPanel.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				resetTabHookFrame();
				drawPreviewProjection();
				mPreviewPanel.getCanvas().repaint();
				return;
			}
		});
		return this.mPreviewPanel;
	}
	
	private JPanel createButtonRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new GridBagLayout());
		
		JButton temp = new JButton("temp");
		int width = (int)Math.round(FontTool.maxLength(new String[]{OKAY_BUTTON_CAPTION, CANCEL_BUTTON_CAPTION, HELP_BUTTON_CAPTION}, temp.getFont()) * BUTTON_WIDTH_FACTOR);
		int height = (int)Math.round(temp.getFontMetrics(temp.getFont()).getHeight() * BUTTON_HEIGHT_FACTOR);
		
		aPanel.add(this.createOkayButton(width, height), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		aPanel.add(this.createCancelButton(width, height), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		aPanel.add(this.createHelpButton(width, height), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		return aPanel;
	}
	
	private JButton createOkayButton(int width, int height)
	{
		this.mOkayButton = new JButton(OKAY_BUTTON_CAPTION);
		this.mOkayButton.setToolTipText(OKAY_BUTTON_TOOLTIP);
		this.mOkayButton.setActionCommand(OKAY_BUTTON_COMMAND);
		this.mOkayButton.setMinimumSize(new Dimension(width, height));
		this.mOkayButton.setPreferredSize(new Dimension(width, height));
		this.mOkayButton.setMargin(BUTTON_MARGINS);
		this.mOkayButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int cols = Integer.parseInt(mColsField.getText());
				int rows = Integer.parseInt(mRowsField.getText());
				mJigsawConfirmDialog = new JigsawConfirmDialog(JigsawDialog.this, Dialog.ModalityType.APPLICATION_MODAL, rows, cols);
				handleConfirmationResponse(mJigsawConfirmDialog.open());
				mJigsawConfirmDialog.dispose();
				return;
			};
		});
		return this.mOkayButton;
	}
	
	private JButton createCancelButton(int width, int height)
	{
		this.mCancelButton = new JButton(CANCEL_BUTTON_CAPTION);
		this.mCancelButton.setToolTipText(CANCEL_BUTTON_TOOLTIP);
		this.mCancelButton.setActionCommand(CANCEL_BUTTON_COMMAND);
		this.mCancelButton.setMinimumSize(new Dimension(width, height));
		this.mCancelButton.setPreferredSize(new Dimension(width, height));
		this.mCancelButton.setMargin(BUTTON_MARGINS);
		this.mCancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				return;
			};
		});
		return this.mCancelButton;
	}
	
	private JButton createHelpButton(int width, int height)
	{
		this.mHelpButton = new JButton(HELP_BUTTON_CAPTION);
		this.mHelpButton.setToolTipText(HELP_BUTTON_TOOLTIP);
		this.mHelpButton.setActionCommand(HELP_BUTTON_COMMAND);
		this.mHelpButton.setMinimumSize(new Dimension(width, height));
		this.mHelpButton.setPreferredSize(new Dimension(width, height));
		this.mHelpButton.setMargin(BUTTON_MARGINS);
		this.mHelpButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				return;
			};
		});
		return this.mHelpButton;
	}
	
	private void handleConfirmationResponse(JigsawConfirmDialog.Response response)
	{
		if(response.equals(JigsawConfirmDialog.Response.OPEN_ALL)){
			
		}else if(response.equals(JigsawConfirmDialog.Response.SAVE)){
			this.openSaveFileDialog();
		}
		return;
	}
	
	private void openSaveFileDialog()
	{
		try{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileFilter(new FileNameExtensionFilter(".XML", "xml"));
			chooser.setAcceptAllFileFilterUsed(true);
			
			int response = chooser.showSaveDialog(JigsawDialog.this);
			if(response == JFileChooser.CANCEL_OPTION){
				return;
			}
			
			XMLTabHookDataWriter writer = new XMLTabHookDataWriter(this.createShapeData());
			String xml = writer.write();
			xml = writer.serializeXML(xml);
			
			ReadWriteFile.write(chooser.getSelectedFile().getAbsolutePath(), xml);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void engageListeners()
	{
		this.mRowsField.getDocument().addDocumentListener(this.mRowsDocumentListener);
		this.mColsField.getDocument().addDocumentListener(this.mColsDocumentListener);
		this.mShowImageOption.addItemListener(this.mShowImageOptionItemListener);
		this.mEditorPanel.getCanvas().addMouseListener(this.mEditorMouseListener);
		this.mEditorPanel.getCanvas().addMouseMotionListener(this.mEditorMouseMotionListener);
		this.mEditorPanel.getCanvas().addMouseWheelListener(this.mEditorMouseWheelListener);
		this.mEditorPanel.getZoomInButton().addActionListener(this.mEditorZoomInButtonListener);
		this.mEditorPanel.getZoomOutButton().addActionListener(this.mEditorZoomOutButtonListener);
		this.mEditorPanel.getZoomResetButton().addActionListener(this.mEditorZoomResetButtonListener);
		this.mEditorPanel.getHScroll().addAdjustmentListener(this.mEditorHScrollAdjustmentListener);
		this.mEditorPanel.getVScroll().addAdjustmentListener(this.mEditorVScrollAdjustmentListener);
		this.mPreviewPanel.getCanvas().addMouseListener(this.mPreviewMouseListener);
		this.mPreviewPanel.getCanvas().addMouseMotionListener(this.mPreviewMouseMotionListener);
		this.mPreviewPanel.getCanvas().addMouseWheelListener(this.mPreviewMouseWheelListener);
		this.mPreviewPanel.getZoomInButton().addActionListener(this.mPreviewZoomInButtonListener);
		this.mPreviewPanel.getZoomOutButton().addActionListener(this.mPreviewZoomOutButtonListener);
		this.mPreviewPanel.getZoomResetButton().addActionListener(this.mPreviewZoomResetButtonListener);
		this.mPreviewPanel.getHScroll().addAdjustmentListener(this.mPreviewHScrollAdjustmentListener);
		this.mPreviewPanel.getVScroll().addAdjustmentListener(this.mPreviewVScrollAdjustmentListener);
		return;
	}
	
	public void disengageListeners()
	{
		this.mRowsField.getDocument().removeDocumentListener(this.mRowsDocumentListener);
		this.mColsField.getDocument().removeDocumentListener(this.mColsDocumentListener);
		this.mShowImageOption.removeItemListener(this.mShowImageOptionItemListener);
		this.mEditorPanel.getCanvas().removeMouseListener(this.mEditorMouseListener);
		this.mEditorPanel.getCanvas().removeMouseMotionListener(this.mEditorMouseMotionListener);
		this.mEditorPanel.getCanvas().removeMouseWheelListener(this.mEditorMouseWheelListener);
		this.mEditorPanel.getZoomInButton().removeActionListener(this.mEditorZoomInButtonListener);
		this.mEditorPanel.getZoomOutButton().removeActionListener(this.mEditorZoomOutButtonListener);
		this.mEditorPanel.getZoomResetButton().removeActionListener(this.mEditorZoomResetButtonListener);
		this.mEditorPanel.getHScroll().removeAdjustmentListener(this.mEditorHScrollAdjustmentListener);
		this.mEditorPanel.getVScroll().removeAdjustmentListener(this.mEditorVScrollAdjustmentListener);
		this.mPreviewPanel.getCanvas().removeMouseListener(this.mPreviewMouseListener);
		this.mPreviewPanel.getCanvas().removeMouseMotionListener(this.mPreviewMouseMotionListener);
		this.mPreviewPanel.getCanvas().removeMouseWheelListener(this.mPreviewMouseWheelListener);
		this.mPreviewPanel.getZoomInButton().removeActionListener(this.mPreviewZoomInButtonListener);
		this.mPreviewPanel.getZoomOutButton().removeActionListener(this.mPreviewZoomOutButtonListener);
		this.mPreviewPanel.getZoomResetButton().removeActionListener(this.mPreviewZoomResetButtonListener);
		this.mPreviewPanel.getHScroll().removeAdjustmentListener(this.mPreviewHScrollAdjustmentListener);
		this.mPreviewPanel.getVScroll().removeAdjustmentListener(this.mPreviewVScrollAdjustmentListener);
		return;
	}
	
	private void resetTabHookRect()
	{
		this.mRect = new TabHookRect(0.0, 0.0, 250, 187.5, 10);
		return;
	}
	
	private void resetEditor()
	{
		this.mEditorPanel.getHScroll().setValue(0);
		this.mEditorPanel.getVScroll().setValue(0);
		
		this.drawEditorProjection();
		this.mEditorPanel.getCanvas().repaint();
		this.disengageListeners();
		this.recalculateEditorScrollBars();
		this.engageListeners();
		return;
	}
	
	private void drawEditorProjection()
	{
		if(mRect == null){
			return;
		}
		BufferedImage image = this.mGC.createCompatibleImage(this.mEditorPanel.getCanvas().getWidth(), this.mEditorPanel.getCanvas().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		TabHookData item = this.createShapeData();
		item.scale(this.mEditorPanel.getZoom(), this.mRect.getCenterX(), this.mRect.getCenterY());
		double xCenter = (this.mEditorPanel.getCanvas().getWidth() / 2.0);
		double yCenter = (this.mEditorPanel.getCanvas().getHeight() / 2.0);
		item.translate(xCenter, yCenter);
		
		item.translate(-this.mEditorPanel.getHScroll().getValue(), -this.mEditorPanel.getVScroll().getValue());
		
		g2d.setPaint(Color.BLACK);
		
		g2d.draw(item.getShape());
		
		for(int i = 0; i < 9; i++)
		{
			if(mRect.getNorth().getGripClass(i) == TabHookAnchor.class){
				g2d.setPaint(Color.BLACK);
			}else if(mRect.getNorth().getGripClass(i) == TabHookControl.class){
				g2d.setPaint(Color.RED);
			}
			g2d.fillRect((int)Math.round(item.north[i].x - 5), (int)Math.round(item.north[i].y - 5), 10, 10);
			
			if(mRect.getEast().getGripClass(i) == TabHookAnchor.class){
				g2d.setPaint(Color.BLACK);
			}else if(mRect.getEast().getGripClass(i) == TabHookControl.class){
				g2d.setPaint(Color.RED);
			}
			g2d.fillRect((int)Math.round(item.east[i].x - 5), (int)Math.round(item.east[i].y - 5), 10, 10);
			
			if(mRect.getSouth().getGripClass(i) == TabHookAnchor.class){
				g2d.setPaint(Color.BLACK);
			}else if(mRect.getSouth().getGripClass(i) == TabHookControl.class){
				g2d.setPaint(Color.RED);
			}
			g2d.fillRect((int)Math.round(item.south[i].x - 5), (int)Math.round(item.south[i].y - 5), 10, 10);
			
			if(mRect.getWest().getGripClass(i) == TabHookAnchor.class){
				g2d.setPaint(Color.BLACK);
			}else if(mRect.getWest().getGripClass(i) == TabHookControl.class){
				g2d.setPaint(Color.RED);
			}
			g2d.fillRect((int)Math.round(item.west[i].x - 5), (int)Math.round(item.west[i].y - 5), 10, 10);
		}
		
		this.mEditorPanel.setProjection(image);
		g2d.dispose();
		return;
	}
	
	private TabHookData createShapeData()
	{
		TabHookData data = this.mRect.getData(DEFAULT_TABHOOKRECT_WIDTH, DEFAULT_TABHOOKRECT_HEIGHT);
		double halfWidth = DEFAULT_TABHOOKRECT_WIDTH / 2.0;
		double halfHeight = DEFAULT_TABHOOKRECT_HEIGHT / 2.0;
		
		TabHookData item = new TabHookData();
		
		item.northwest.x = this.mRect.getCenterX() + (halfWidth * data.northwest.x);
		item.northwest.y = this.mRect.getCenterY() + (halfHeight * data.northwest.y);
		item.northeast.x = this.mRect.getCenterX() + (halfWidth * data.northeast.x);
		item.northeast.y = this.mRect.getCenterY() + (halfHeight * data.northeast.y);
		item.southeast.x = this.mRect.getCenterX() + (halfWidth * data.southeast.x);
		item.southeast.y = this.mRect.getCenterY() + (halfHeight * data.southeast.y);
		item.southwest.x = this.mRect.getCenterX() + (halfWidth * data.southwest.x);
		item.southwest.y = this.mRect.getCenterY() + (halfHeight * data.southwest.y);
		
		for(int i = 0; i < 9; i++)
		{
			item.north[i] = new Point2D.Double(this.mRect.getCenterX() + (halfWidth * data.north[i].getX()), this.mRect.getCenterY() + (halfHeight * data.north[i].getY()));
			item.east[i] = new Point2D.Double(this.mRect.getCenterX() + (halfWidth * data.east[i].getX()), this.mRect.getCenterY() + (halfHeight * data.east[i].getY()));
			item.south[i] = new Point2D.Double(this.mRect.getCenterX() + (halfWidth * data.south[i].getX()), this.mRect.getCenterY() + (halfHeight * data.south[i].getY()));
			item.west[i] = new Point2D.Double(this.mRect.getCenterX() + (halfWidth * data.west[i].getX()), this.mRect.getCenterY() + (halfHeight * data.west[i].getY()));
		}
		
		return item;
	}
	
	private void recalculateEditorScrollBars()
	{
		if(this.mRect == null){
			return;
		}
		TabHookData item = this.createShapeData();
		item.scale(mEditorPanel.getZoom(), this.mRect.getCenterX(), this.mRect.getCenterY());
		
		// Use zero as the center value.
		this.mEditorPanel.getHScroll().setUnitIncrement(1);
		this.mEditorPanel.getHScroll().setMinimum((int)Math.floor(-item.getXMax()));
		this.mEditorPanel.getHScroll().setMaximum((int)Math.floor(item.getXMax()));
		this.mEditorPanel.getHScroll().getModel().setExtent(0);
		this.mEditorPanel.getVScroll().setUnitIncrement(1);
		this.mEditorPanel.getVScroll().setMinimum((int)Math.floor(-item.getYMax()));
		this.mEditorPanel.getVScroll().setMaximum((int)Math.floor(item.getYMax()));
		this.mEditorPanel.getVScroll().getModel().setExtent(0);
		return;
	}
	
	private void recalculatePreviewScrollBars()
	{
		if(this.mRect == null){
			return;
		}
		
		// Use zero as the center value.
		this.mPreviewPanel.getHScroll().setUnitIncrement(1);
		this.mPreviewPanel.getHScroll().setMinimum(-(int)Math.round(this.mPreview1to1.width * this.mPreviewPanel.getZoom()));
		this.mPreviewPanel.getHScroll().setMaximum((int)Math.round(this.mPreview1to1.width * this.mPreviewPanel.getZoom()));
		this.mPreviewPanel.getHScroll().getModel().setExtent(0);
		this.mPreviewPanel.getVScroll().setUnitIncrement(1);
		this.mPreviewPanel.getVScroll().setMinimum(-(int)Math.round(this.mPreview1to1.height * this.mPreviewPanel.getZoom()));
		this.mPreviewPanel.getVScroll().setMaximum((int)Math.round(this.mPreview1to1.height * this.mPreviewPanel.getZoom()));
		this.mPreviewPanel.getVScroll().getModel().setExtent(0);
		return;
	}
	
	private void initializePreviewBaseSize()
	{
		double ratio = 300.0 / (double)this.mSource.getWidth();
		this.mPreview1to1.width = 300;
		this.mPreview1to1.height = (int)Math.round(this.mSource.getHeight() * ratio);
		
		this.disengageListeners();
		this.recalculatePreviewScrollBars();
		this.mPreviewPanel.getHScroll().setValue(0);
		this.mPreviewPanel.getVScroll().setValue(0);
		this.engageListeners();
		return;
	}
	
	private void resetTabHookFrame()
	{
		if(mRect == null){
			return;
		}
		try{
			int cols = Integer.parseInt(this.mColsField.getText());
			int rows = Integer.parseInt(this.mRowsField.getText());
			
			this.mPreviewFrame = new TabHookFrame((int)Math.round(this.mPreview1to1.width * this.mPreviewPanel.getZoom()), 
												(int)Math.round(this.mPreview1to1.height * this.mPreviewPanel.getZoom()), 
												rows, cols, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void drawPreviewProjection()
	{
		if(mRect == null){
			return;
		}
		if(this.mPreviewFrame == null){
			return;
		}
		
		TabHookData prototype = this.createShapeData();
		double x = ((this.mPreviewPanel.getCanvas().getWidth() / 2.0));
		double y = ((this.mPreviewPanel.getCanvas().getHeight() / 2.0));
		
		x -= (this.mPreview1to1.width * this.mPreviewPanel.getZoom()) / 2.0;
		y -= (this.mPreview1to1.height * this.mPreviewPanel.getZoom()) / 2.0;
		
		double cols = Integer.parseInt(this.mColsField.getText());
		double rows = Integer.parseInt(this.mRowsField.getText());
		
		// A TabHookRect's origin is at its center.  Adjust by the center of upper left rect.
		x += (this.mPreview1to1.width / cols) / 2.0;
		y += (this.mPreview1to1.height / rows) / 2.0;
		
		// Offset by the values of the scroll bars.
		x -= this.mPreviewPanel.getHScroll().getValue();
		y -= this.mPreviewPanel.getVScroll().getValue();
		
		this.mPreviewFrame.setPrototype(prototype);
		this.mPreviewFrame.layoutFrame(x, y, 187.5);
		
		BufferedImage image = this.mGC.createCompatibleImage(this.mPreviewPanel.getCanvas().getWidth(), this.mPreviewPanel.getCanvas().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		this.drawBackgroundImage(g2d, cols, rows);
		
		g2d.setPaint(Color.BLACK);
		for(int i = 0; i < this.mPreviewFrame.getFrame().length; i++)
		{
			g2d.draw(this.mPreviewFrame.getFrame()[i]);
		}
		
		this.mPreviewPanel.setProjection(image);
		g2d.dispose();
		return;
	}
	
	private void drawBackgroundImage(Graphics2D g2d, double cols, double rows)
	{
		if(!this.mShowImageOption.isSelected()){
			return;
		}
		
		double x = ((this.mPreviewPanel.getCanvas().getWidth() / 2.0));
		double y = ((this.mPreviewPanel.getCanvas().getHeight() / 2.0));
		
		x -= (this.mPreview1to1.width * this.mPreviewPanel.getZoom()) / 2.0;
		y -= (this.mPreview1to1.height * this.mPreviewPanel.getZoom()) / 2.0;
		
		x += (this.mPreview1to1.width / cols) / 2.0;
		y += (this.mPreview1to1.height / rows) / 2.0;
		
		// Offset by the values of the scroll bars.
		x -= this.mPreviewPanel.getHScroll().getValue();
		y -= this.mPreviewPanel.getVScroll().getValue();
		
		double wRatio = (this.mPreview1to1.width * this.mPreviewPanel.getZoom()) / this.mSource.getWidth();
		double hRatio = (this.mPreview1to1.height * this.mPreviewPanel.getZoom()) / this.mSource.getHeight();
		BufferedImage background = ImageOps.resizeNearestNeighbor(this.mSource, wRatio, hRatio);
		
		AlphaComposite original = (AlphaComposite)g2d.getComposite();
		float alpha = MathUtil.clamp(0f, 100f, this.mOpacitySlider.getValue()) / 100f;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		g2d.drawImage(background, 
						(int)Math.round(x - ((this.mPreview1to1.width * this.mPreviewPanel.getZoom()) / cols) / 2.0), 
						(int)Math.round(y - ((this.mPreview1to1.height * this.mPreviewPanel.getZoom()) / rows) / 2.0), 
						null);
		
		g2d.setComposite(original);
		return;
	}
	
	private boolean checkRowColumnValue(JTextField field)
	{
		boolean isValid = true;
		try{
			int value = Integer.parseInt(field.getText());
			
			if(value < DEFAULT_ROW_COL_MINIMUM){
				throw new Exception();
			}
			if(value > DEFAULT_ROW_COL_MAXIMUM){
				throw new Exception();
			}
			field.setBackground(TEXTFIELD_NORMAL_BACKGROUND);
		}catch(Exception ex){
			isValid = false;
			field.setBackground(TEXTFIELD_ERROR_STATE_COLOR);
			//ex.printStackTrace();
		}
		return isValid;
	}
	
	public void opacitySliderValueChanged(int value)
	{
		this.drawPreviewProjection();
		this.mPreviewPanel.getCanvas().repaint();
		return;
	}
	
	private ComponentListener createDialogComponentListener()
	{
		return new ComponentListener()
		{
			public void componentHidden(ComponentEvent e) { return; }
			public void componentMoved(ComponentEvent e) { return; }
			
			public void componentResized(ComponentEvent e)
			{
				resetEditor();
				drawPreviewProjection();
				return;
			}
			
			public void componentShown(ComponentEvent e)
			{
				return;
			}
		};
	}
	
	private WindowListener createDialogWindowListener()
	{
		return new WindowListener()
		{
			public void windowActivated(WindowEvent e) { return; }
			
			public void windowClosed(WindowEvent e)
			{
				return;
			}
			
			public void windowClosing(WindowEvent e)
			{
				return;
			}
			
			public void windowDeactivated(WindowEvent e) { return; }
			public void windowDeiconified(WindowEvent e) { return; }
			public void windowIconified(WindowEvent e) { return; }
			
			public void windowOpened(WindowEvent e)
			{
				mSplitPane.setDividerLocation(0.5);
				resetTabHookRect();
				initializePreviewBaseSize();
				return;
			}
		};
	}
}
