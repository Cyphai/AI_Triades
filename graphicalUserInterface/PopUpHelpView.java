package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.StyledDocument;

import translation.Messages;

public class PopUpHelpView extends JPanel {

	private static final long serialVersionUID = 980015872109265231L;

	private JPanel panelMin;
	private JPanel panelMax;
	private JComponent panelMaxContent;
	private StyledDocument editedNote;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	private Color backgroundUneditableColor;

	public PopUpHelpView() {
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.gray));
		build();

		panelMaxContent = null;
		editedNote = null;
		textPane = null;
		backgroundUneditableColor = new Color(248,248,160);

	}

	private void build() {
		// Déclarations + initialisations
		panelMax = new JPanel(new BorderLayout());
		panelMin = new JPanel();
		JPanel pTitle = new JPanel();
		BoxLayout layoutTitle = new BoxLayout(pTitle, BoxLayout.X_AXIS);
		BoxLayout layoutMin = new BoxLayout(panelMin, BoxLayout.Y_AXIS);
		JToolBar jtbMax = new JToolBar(JToolBar.HORIZONTAL);
		jtbMax.setFloatable(false);
		JToolBar jtbMin = new JToolBar(JToolBar.HORIZONTAL);
		jtbMin.setFloatable(false);
		JButton jbMinimize = new JButton(IconDatabase.iconArrowUpRight);
		jbMinimize.setFocusable(false);
		JButton jbMaximize = new JButton(IconDatabase.iconArrowDownLeft);
		jbMaximize.setFocusable(false);

		// Construction barre de titre max
		pTitle.setLayout(layoutTitle);
		pTitle.setBackground(Color.LIGHT_GRAY);
		jbMinimize.setBackground(Color.LIGHT_GRAY);
		jbMinimize.setFocusable(false);
		jbMinimize.setToolTipText(Messages.getString("PopUpHelpView.1")); //$NON-NLS-1$
		jbMinimize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panelMin.setVisible(true);
				panelMax.setVisible(false);
			}

		});
		jtbMax.add(jbMinimize);
		jtbMax.setBackground(Color.LIGHT_GRAY);
		pTitle.add(jtbMax);
		pTitle.add(Box.createHorizontalStrut(5));
		pTitle.add(new JLabel(IconDatabase.iconHelp));
		pTitle.add(Box.createHorizontalStrut(5));
		pTitle.add(new JLabel(Messages.getString("PopUpHelpView.0"))); //$NON-NLS-1$
		pTitle.add(Box.createHorizontalStrut(3));
		jtbMax.setFloatable(false);

		// Construction panel max
		panelMax.add(pTitle, BorderLayout.SOUTH);
		panelMax.setVisible(false);

		// Construction panel min
		panelMin.setVisible(true);
		jbMaximize.setBackground(Color.LIGHT_GRAY);
		jbMaximize.setFocusable(false);
		jbMaximize.setToolTipText(Messages.getString("PopUpHelpView.2")); //$NON-NLS-1$
		jbMaximize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {



				if (Program.isTriades() && !Program.myMainFrame.getDataPack().getProgramSettings().isHelpEditing() && editedNote != null && editedNote.getLength() == 0)
				{
					DialogHandlerFrame.showErrorDialog(Program.myMainFrame, Messages.getString("Version_1_0_1.PopUpHelpView.3")); //$NON-NLS-1$
				}
				else
				{
					checkEditableState();
					panelMin.setVisible(false);
					panelMax.setVisible(true);

				}
			}

		});
		jtbMin.add(Box.createHorizontalStrut(2));
		jtbMin.add(jbMaximize);
		jtbMin.add(new JLabel(IconDatabase.iconHelp));
		jtbMin.setBackground(Color.LIGHT_GRAY);
		panelMin.setLayout(layoutMin);
		panelMin.add(jtbMin);
		panelMin.add(Box.createGlue());

		// Construction panel total
		add(panelMax, BorderLayout.CENTER);
		add(panelMin, BorderLayout.EAST);


	}

	public void setView(JPanel panel) {
		if (panelMaxContent != null)
			panelMax.remove(panelMaxContent);

		panelMax.add(panel, BorderLayout.CENTER);
		panelMaxContent = panel;
	}

	protected void setHelpText(String text)
	{
		if (panelMaxContent != null)
			panelMax.remove(panelMaxContent);
		JTextArea textArea = new JTextArea(text);
		scrollPane = new JScrollPane(textArea);
		panelMax.add(scrollPane, BorderLayout.CENTER);
		panelMaxContent = scrollPane;
		scrollPane.setPreferredSize(new Dimension(400,400));
		scrollPane.setMinimumSize(new Dimension(300,250));
		textArea.setEditable(false);
		textArea.setBackground(backgroundUneditableColor);
		textArea.setLineWrap(true);
	}

	public void setNavigationHelp()
	{
		String navigationHelp = Messages.getString("Version_1_0_2.PopUpHelpView.3"); //$NON-NLS-1$
		navigationHelp += Messages.getString("Version_1_0_2.PopUpHelpView.91"); //$NON-NLS-1$
		navigationHelp += Messages.getString("Version_1_0_2.PopUpHelpView.94"); //$NON-NLS-1$
		navigationHelp += Messages.getString("Version_1_0_2.PopUpHelpView.95"); //$NON-NLS-1$
		navigationHelp += Messages.getString("Version_1_0_2.PopUpHelpView.96"); //$NON-NLS-1$
		setHelpText(navigationHelp);
	}	
	
	public void setActorListHelp()
	{
		String actorListHelp = Messages.getString("Version_1_0_2.PopUpHelpView.97"); //$NON-NLS-1$
		actorListHelp += Messages.getString("Version_1_0_2.PopUpHelpView.98"); //$NON-NLS-1$
		actorListHelp += Messages.getString("Version_1_0_2.PopUpHelpView.99"); //$NON-NLS-1$
		actorListHelp += Messages.getString("Version_1_0_2.PopUpHelpView.100"); //$NON-NLS-1$
		actorListHelp += Messages.getString("Version_1_0_2.PopUpHelpView.101"); //$NON-NLS-1$
		actorListHelp += Messages.getString("Version_1_0_2.PopUpHelpView.102"); //$NON-NLS-1$
		setHelpText(actorListHelp);
	}
	

	public void setNoteMode(StyledDocument noteContent)
	{
		if (panelMaxContent != null)
			panelMax.remove(panelMaxContent);

		editedNote = noteContent;
		textPane = new JTextPane(noteContent);
		//textPane.setFont(getFont().deriveFont(getFont().getSize()+20));
		setBig(noteContent.getLength() > 0);
		panelMaxContent = textPane;

		scrollPane = new JScrollPane(textPane);
		scrollPane.setOpaque(true);

		scrollPane.setMaximumSize(new Dimension(400,500));
		panelMax.add(scrollPane, BorderLayout.CENTER);

		textPane.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				checkEditableState();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2)
				{
					if (!textPane.isEditable())
					{
						DialogHandlerFrame.showErrorDialog(Program.myMainFrame, Messages.getString("Version_1_0_1.PopUpHelpView.91")); //$NON-NLS-1$
					}
				}


			}
		});

		textPane.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				checkEditableState();

			}

			@Override
			public void componentResized(ComponentEvent e) {
				checkEditableState();

			}

		});

	}

	protected Dimension boundsDimension(Dimension source, int widhtMax, int heightMax)
	{
		Dimension result = new Dimension(source);
		if (result.getWidth() + 5  > widhtMax)
		{

			result.width = widhtMax;
		}
		else
			result.width += 5;

		if (result.getHeight()+5 > heightMax)
		{

			result.height = heightMax;
		}
		else
			result.height += 5;
		return result;

	}

	protected void checkEditableState()
	{

		boolean currentValue = Program.myMainFrame.getDataPack().getProgramSettings().isHelpEditing();
		if (textPane != null)
		{

			textPane.setEditable(currentValue);
			if (textPane.isEditable())
			{
				textPane.setBackground(Color.white);
				scrollPane.setPreferredSize(new Dimension(400,400));
				scrollPane.setMinimumSize(new Dimension(400,250));
				scrollPane.setBackground(Color.white);
				textPane.getCaret().setVisible(true);
			}
			else
			{


				textPane.setBackground(backgroundUneditableColor);

				scrollPane.setMinimumSize(boundsDimension(textPane.getPreferredSize(), 300, 150));
				scrollPane.setPreferredSize(boundsDimension(textPane.getPreferredSize(), 400,400));
				textPane.getCaret().setVisible(false);
			}
			validate();
			Program.myMainFrame.repaint();
		}
	}

	@Override
	public void repaint()
	{
		if (textPane != null && textPane.isEditable() != Program.myMainFrame.getDataPack().getProgramSettings().isHelpEditing())
			checkEditableState();
		super.repaint();
	}

	public void setBig(boolean b) {
		panelMax.setVisible(b);
		panelMin.setVisible(!b);
	}

	public boolean isBig() {
		return panelMax.isVisible();
	}

	//	public static JPanel showText(String title, String message) {
	//		// Déclarations et initialisations
	//		JPanel panel = new JPanel();
	//		String[] strSet = message.split("\n"); //$NON-NLS-1$
	//
	//		// Layout + border
	//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	//		if (title != null)
	//			panel.setBorder(BorderFactory.createTitledBorder(title));
	//		else
	//			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
	//					.createLineBorder(panel.getBackground(), 2), BorderFactory
	//					.createCompoundBorder(BorderFactory
	//							.createLineBorder(new Color(169, 211, 247)),
	//							BorderFactory.createEmptyBorder(3, 4, 3, 4))));
	//
	//		// Construction panel
	//		for (int i = 0; i < strSet.length; i++) {
	//			panel.add(new JLabel(strSet[i]));
	//		}
	//
	//		return panel;
	//	}
	//
	//	public static JPanel showDataPackHelp() {
	//		// Declarations et initialisations
	//		JPanel panel = new JPanel();
	//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	//		JPanel pAddActors = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.4"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.5") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.6") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.7") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.8")); //$NON-NLS-1$
	//		JPanel pDeleteActors = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.9"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.10") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.11") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.12") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.13") + Messages.getString("PopUpHelpView.14")); //$NON-NLS-1$ //$NON-NLS-2$
	//		JPanel pAddGroups = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.15"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.16") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.17")); //$NON-NLS-1$
	//		JPanel pDeleteGroups = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.18"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.19") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.20") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.21") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.22") + Messages.getString("PopUpHelpView.23")); //$NON-NLS-1$ //$NON-NLS-2$
	//		JPanel pAddActivities = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.24"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.25") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.26") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.27")); //$NON-NLS-1$
	//		JPanel pAddBrickTypes = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.28"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.29") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.30")); //$NON-NLS-1$
	//		JPanel pAddActionTime = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.31"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.32") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.33")); //$NON-NLS-1$
	//		JPanel pBricksAndActivities = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.34"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.35") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.36") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.37")); //$NON-NLS-1$
	//
	//		// Ajouts au panel
	//		panel.add(getPanelTitle());
	//		panel.add(pAddActors);
	//		panel.add(pDeleteActors);
	//		panel.add(pAddGroups);
	//		panel.add(pDeleteGroups);
	//		panel.add(pAddActivities);
	//		panel.add(pAddBrickTypes);
	//		panel.add(pAddActionTime);
	//		panel.add(pBricksAndActivities);
	//
	//		return panel;
	//	}
	//
	//	public static JPanel showBrickHelp() {
	//		// Declarations et initialisations
	//		JPanel panel = new JPanel();
	//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	//		JPanel pAddActors = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.38"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.39") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.40")); //$NON-NLS-1$
	//		JPanel pAddRelation = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.41"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.42") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.43") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.44") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.45") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.46") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.47") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.48")); //$NON-NLS-1$
	//		JPanel pEditRelation = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.49"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.50") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.51") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.52") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.53") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.54") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.55")); //$NON-NLS-1$
	//
	//		// Ajouts au panel
	//		panel.add(pAddActors);
	//		panel.add(pAddRelation);
	//		panel.add(pEditRelation);
	//
	//		return panel;
	//	}
	//
	//	public static JPanel showModelHelp() {
	//		// Declarations et initialisations
	//		JPanel panel = new JPanel();
	//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	//		JPanel pAddActors = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.56"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.57") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.58")); //$NON-NLS-1$
	//		JPanel pAddRelation = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.59"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.60") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.61") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.62") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.63") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.64") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.65") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.66")); //$NON-NLS-1$
	//		JPanel pEditRelation = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.67"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.68") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.69") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.70") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.71") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.72") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.73")); //$NON-NLS-1$
	//
	//		// Ajouts au panel
	//		panel.add(pAddActors);
	//		panel.add(pAddRelation);
	//		panel.add(pEditRelation);
	//
	//		return panel;
	//	}
	//
	//	public static JPanel showSelectionActorsHelp() {
	//		// Declarations et initialisations
	//		JPanel panel = new JPanel();
	//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	//		JPanel pAddActors = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.74"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.75") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.76")); //$NON-NLS-1$
	//		JPanel pAddGroup = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.77"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.78") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.79")); //$NON-NLS-1$
	//
	//		// Ajouts au panel
	//		panel.add(pAddActors);
	//		panel.add(pAddGroup);
	//
	//		return panel;
	//	}
	//
	//	public static JPanel showSchemaGlobalHelp() {
	//		// Declarations et initialisations
	//		JPanel panel = new JPanel();
	//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	//		JPanel pEditRelation = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.80"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.81") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.82") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.83") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.84") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.85") //$NON-NLS-1$
	//				+ Messages.getString("PopUpHelpView.86")); //$NON-NLS-1$
	//		JPanel pMove = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.87"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.88")); //$NON-NLS-1$
	//		JPanel pMoveElement = getQuestionAnswerPanel(
	//				Messages.getString("PopUpHelpView.89"), //$NON-NLS-1$
	//				Messages.getString("PopUpHelpView.90")); //$NON-NLS-1$
	//
	//		// Ajouts au panel
	//		panel.add(pEditRelation);
	//		panel.add(pMove);
	//		panel.add(pMoveElement);
	//
	//		return panel;
	//	}
	//
	//	private static JPanel getQuestionAnswerPanel(String strQuestion,
	//			String strAnswer) {
	//		// Declarations et initialisations
	//		final JPanel panel = new JPanel(new BorderLayout());
	//		final JPanel pQuestion = new JPanel();
	//		final JPanel pAnswers = new JPanel();
	//		final JLabel icon = new JLabel(IconDatabase.iconMaximize);
	//		JLabel question = new JLabel(strQuestion);
	//		String[] strSet = strAnswer.split("\n"); //$NON-NLS-1$
	//
	//		pQuestion.setLayout(new BoxLayout(pQuestion, BoxLayout.X_AXIS));
	//		pAnswers.setLayout(new BoxLayout(pAnswers, BoxLayout.Y_AXIS));
	//		pAnswers.setVisible(false);
	//		pAnswers.setBackground(Color.WHITE);
	//		pAnswers.setBorder(BorderFactory.createCompoundBorder(BorderFactory
	//				.createLineBorder(panel.getBackground(), 2), BorderFactory
	//				.createCompoundBorder(BorderFactory.createLineBorder(new Color(
	//						169, 211, 247)), BorderFactory.createEmptyBorder(3, 4,
	//								3, 4))));
	//
	//		// Question
	//		pQuestion.add(Box.createHorizontalStrut(3));
	//		pQuestion.add(icon);
	//		pQuestion.add(Box.createHorizontalStrut(3));
	//		pQuestion.add(question);
	//		pQuestion.addMouseListener(new MouseListener() {
	//			@Override
	//			public void mouseClicked(MouseEvent e) {
	//				if (pAnswers.isVisible()) {
	//					icon.setIcon(IconDatabase.iconMaximize);
	//					pAnswers.setVisible(false);
	//				} else {
	//					icon.setIcon(IconDatabase.iconUnfold);
	//					pAnswers.setVisible(true);
	//				}
	//				pQuestion.setBackground(panel.getBackground());
	//			}
	//
	//			@Override
	//			public void mouseEntered(MouseEvent e) {
	//				pQuestion.setBackground(new Color(169, 211, 247));
	//
	//			}
	//
	//			@Override
	//			public void mouseExited(MouseEvent e) {
	//				pQuestion.setBackground(panel.getBackground());
	//			}
	//
	//			@Override
	//			public void mousePressed(MouseEvent e) {
	//			}
	//
	//			@Override
	//			public void mouseReleased(MouseEvent e) {
	//			}
	//
	//		});
	//
	//		// Answers
	//		for (int i = 0; i < strSet.length; i++) {
	//			pAnswers.add(new JLabel(strSet[i]));
	//		}
	//
	//		// Total
	//		panel.add(pQuestion, BorderLayout.NORTH);
	//		panel.add(pAnswers, BorderLayout.CENTER);
	//
	//		return panel;
	//	}
	//
	//	private static JPanel getPanelTitle() {
	//		JPanel pTitle = new JPanel(new BorderLayout());
	//		JLabel labelTitle = new JLabel(
	//				Messages.getString("PopUpHelpView.92")); //$NON-NLS-1$
	//		labelTitle.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	//
	//		pTitle.setBackground(Color.white);
	//		pTitle.add(
	//				new JLabel(Messages.getString("PopUpHelpView.93")), //$NON-NLS-1$
	//				BorderLayout.CENTER);
	//		pTitle.add(new JSeparator(), BorderLayout.SOUTH);
	//
	//		return pTitle;
	//	}
}
