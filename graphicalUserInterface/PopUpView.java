package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import translation.Messages;

public abstract class PopUpView extends JPanel {

	private static final long serialVersionUID = -3496894332815190012L;

	protected JPanel contentPane;

	protected JPanel panelMax;
	protected JPanel panelMin;
	protected JLabel labelTitle;

	public PopUpView() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.gray));
		build();
	}

	private void build() {
		// Déclarations + initialisations
		contentPane = new JPanel(new BorderLayout());
		panelMax = contentPane;
		panelMin = new JPanel();
		labelTitle = new JLabel("Titre"); //$NON-NLS-1$
		JPanel pTitle = new JPanel();
		BoxLayout layoutTitle = new BoxLayout(pTitle, BoxLayout.X_AXIS);
		BoxLayout layoutMin = new BoxLayout(panelMin, BoxLayout.Y_AXIS);
		JToolBar jtbMax = new JToolBar(JToolBar.HORIZONTAL);
		jtbMax.setFloatable(false);
		JToolBar jtbMin = new JToolBar(JToolBar.HORIZONTAL);
		jtbMin.setFloatable(false);
		JButton jbMinimize = new JButton(IconDatabase.iconArrowDownLeft);
		jbMinimize.setFocusable(false);
		JButton jbMaximize = new JButton(IconDatabase.iconArrowUpRight);
		jbMaximize.setFocusable(false);

		// Construction barre de titre max
		pTitle.setLayout(layoutTitle);
		pTitle.setBackground(Color.LIGHT_GRAY);
		pTitle.add(Box.createHorizontalStrut(3));
		pTitle.add(new JLabel(IconDatabase.iconConfigure));
		pTitle.add(Box.createHorizontalStrut(5));
		pTitle.add(labelTitle);
		pTitle.add(Box.createGlue());
		jtbMax.setFloatable(false);
		jbMinimize.setBackground(Color.LIGHT_GRAY);
		jbMinimize.setFocusable(false);
		jbMinimize.setToolTipText(Messages.getString("PopUpView.1")); //$NON-NLS-1$
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

		// Construction panel max
		panelMax.add(pTitle, BorderLayout.NORTH);
		panelMax.setVisible(false);

		// Construction panel min
		panelMin.setVisible(false);
		jtbMin.add(Box.createHorizontalStrut(2));
		jtbMin.add(new JLabel(IconDatabase.iconConfigure));
		jbMaximize.setBackground(Color.LIGHT_GRAY);
		jbMaximize.setFocusable(false);
		jbMaximize.setToolTipText(Messages.getString("PopUpView.2")); //$NON-NLS-1$
		jbMaximize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panelMin.setVisible(false);
				panelMax.setVisible(true);
			}

		});
		jtbMin.add(jbMaximize);
		jtbMin.setBackground(Color.LIGHT_GRAY);
		panelMin.setLayout(layoutMin);
		panelMin.add(Box.createGlue());
		panelMin.add(jtbMin);

		// Construction panel total
		add(panelMax, BorderLayout.CENTER);
		add(panelMin, BorderLayout.WEST);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
