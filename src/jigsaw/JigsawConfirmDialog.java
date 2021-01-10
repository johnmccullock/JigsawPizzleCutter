package jigsaw;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class JigsawConfirmDialog extends JDialog
{
	public static enum Response {OPEN_ALL, SAVE, NONE};
	
	private static final String DIALOG_TITLE = "Confirm Action";
	private static final Insets DIALOG_MARGINS = new Insets(30, 30, 20, 30);
	private static final Dimension DEFAULT_DIALOG_SIZE = new Dimension(500, 300);
	private static final String OPEN_ALL_OPTION_CAPTION = "Cut and open all puzzle pieces as new images.";
	private static final String OPEN_ALL_OPTION_DESCRIPTION1 = "CAUTION: ";
	private static final String OPEN_ALL_OPTION_DESCRIPTION2 = " rows and ";
	private static final String OPEN_ALL_OPTION_DESCRIPTION3 = " columns will result in ";
	private static final String OPEN_ALL_OPTION_DESCRIPTION4 = " new images opening!";
	private static final String SAVE_SHAPE_DATA_OPTION_CAPTION = "Save/export shape data as a file.";
	private static final String OKAY_BUTTON_CAPTION = "Okay";
	private static final String OKAY_BUTTON_COMMAND = "okay";
	private static final String OKAY_BUTTON_TOOLTIP = "Accept these options and apply.";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";
	private static final String CANCEL_BUTTON_COMMAND = "cancel";
	private static final String CANCEL_BUTTON_TOOLTIP = "Cancel this action.";
	private static final Insets BUTTON_MARGINS = new Insets(2, 2, 2, 2);
	private static final float BUTTON_WIDTH_FACTOR = 2.0f;
	private static final float BUTTON_HEIGHT_FACTOR = 2.0f;
	
	private JRadioButton mOpenAllInNewEditorsOption = null;
	private JRadioButton mSaveShapeData = null;
	private JButton mOkayButton = null;
	private JButton mCancelButton = null;
	
	private int mRows = 0;
	private int mColumns = 0;
	private Response mResponse = Response.NONE;
	
	public JigsawConfirmDialog(JDialog parent, Dialog.ModalityType modality, int rows, int columns)
	{
		super(parent, modality);
		this.mRows = rows;
		this.mColumns = columns;
		return;
	}
	
	public Response open()
	{
		this.setTitle(DIALOG_TITLE);
		JPanel basePanel = new JPanel();
		basePanel.setBorder(BorderFactory.createEmptyBorder(DIALOG_MARGINS.top, DIALOG_MARGINS.left, DIALOG_MARGINS.bottom, DIALOG_MARGINS.right));
		basePanel.setLayout(new GridBagLayout());
		basePanel.setOpaque(true);
		
		basePanel.add(this.createOpenAllInNewEditorsOption(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		basePanel.add(this.createCautionaryDescription(this.mOpenAllInNewEditorsOption.getFont(), basePanel.getBackground()), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 30, 10, 30), 0, 0));
			
		basePanel.add(this.createSaveShapeDataOption(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		ButtonGroup group = new ButtonGroup();
		group.add(this.mOpenAllInNewEditorsOption);
		group.add(this.mSaveShapeData);
		
		JButton temp = new JButton("temp");
		int width = (int)Math.round(FontTool.maxLength(new String[]{OKAY_BUTTON_CAPTION, CANCEL_BUTTON_CAPTION}, temp.getFont()) * BUTTON_WIDTH_FACTOR);
		int height = (int)Math.round(temp.getFontMetrics(temp.getFont()).getHeight() * BUTTON_HEIGHT_FACTOR);
		basePanel.add(this.createOkayButton(width, height), new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
		basePanel.add(this.createCancelButton(width, height), new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		
		this.addComponentListener(this.createDialogComponentListener());
		this.addWindowListener(this.createDialogWindowListener());
		
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setMinimumSize(DEFAULT_DIALOG_SIZE);
		this.setPreferredSize(DEFAULT_DIALOG_SIZE);
		this.setContentPane(basePanel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();
		
		return this.mResponse;
	}
	
	private JRadioButton createOpenAllInNewEditorsOption()
	{
		this.mOpenAllInNewEditorsOption = new JRadioButton();
		this.mOpenAllInNewEditorsOption.setText(OPEN_ALL_OPTION_CAPTION);
		return this.mOpenAllInNewEditorsOption;
	}
	
	private JTextArea createCautionaryDescription(Font font, Color background)
	{
		JTextArea text = new JTextArea();
		text.setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue()));
		text.setBorder(null);
		text.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize() + 5));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setOpaque(true);
		text.setEditable(false);
		text.setFocusable(false);
		StringBuilder builder = new StringBuilder();
		builder.append(OPEN_ALL_OPTION_DESCRIPTION1);
		builder.append(String.valueOf(this.mRows));
		builder.append(OPEN_ALL_OPTION_DESCRIPTION2);
		builder.append(String.valueOf(this.mColumns));
		builder.append(OPEN_ALL_OPTION_DESCRIPTION3);
		builder.append(String.valueOf(this.mRows * this.mColumns));
		builder.append(OPEN_ALL_OPTION_DESCRIPTION4);
		text.setText(builder.toString());
		return text;
	}
	
	private JRadioButton createSaveShapeDataOption()
	{
		this.mSaveShapeData = new JRadioButton();
		this.mSaveShapeData.setText(SAVE_SHAPE_DATA_OPTION_CAPTION);
		return this.mSaveShapeData;
	}
	
	private JButton createOkayButton(int width, int height)
	{
		this.mOkayButton = new JButton();
		this.mOkayButton.setText(OKAY_BUTTON_CAPTION);
		this.mOkayButton.setMinimumSize(new Dimension(width, height));
		this.mOkayButton.setMaximumSize(new Dimension(width, height));
		this.mOkayButton.setPreferredSize(new Dimension(width, height));
		this.mOkayButton.setMargin(BUTTON_MARGINS);
		this.mOkayButton.setActionCommand(OKAY_BUTTON_COMMAND);
		this.mOkayButton.setToolTipText(OKAY_BUTTON_TOOLTIP);
		this.mOkayButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mResponse = mOpenAllInNewEditorsOption.isSelected() ? Response.OPEN_ALL : Response.SAVE;
				setVisible(false);
				return;
			}
		});
		return this.mOkayButton;
	}
	
	private JButton createCancelButton(int width, int height)
	{
		this.mCancelButton = new JButton();
		this.mCancelButton.setText(CANCEL_BUTTON_CAPTION);
		this.mCancelButton.setMinimumSize(new Dimension(width, height));
		this.mCancelButton.setMaximumSize(new Dimension(width, height));
		this.mCancelButton.setPreferredSize(new Dimension(width, height));
		this.mCancelButton.setMargin(BUTTON_MARGINS);
		this.mCancelButton.setActionCommand(CANCEL_BUTTON_COMMAND);
		this.mCancelButton.setToolTipText(CANCEL_BUTTON_TOOLTIP);
		this.mCancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mResponse = Response.NONE;
				setVisible(false);
				return;
			}
		});
		return this.mCancelButton;
	}
	
	private ComponentListener createDialogComponentListener()
	{
		return new ComponentAdapter()
		{
			public void componentHidden(ComponentEvent e)
			{
				return;
			}
			
			public void componentResized(ComponentEvent e)
			{
				
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
		return new WindowAdapter()
		{
			public void windowClosed(WindowEvent e)
			{
				return;
			}
			
			public void windowClosing(WindowEvent e)
			{
				return;
			}
			
			public void windowOpened(WindowEvent e)
			{
				return;
			}
		};
	}
}
