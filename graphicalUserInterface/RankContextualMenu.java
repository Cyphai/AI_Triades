package graphicalUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import translation.Messages;

import models.Brick;
import models.BrickVertex.VerticeRank;
import models.BrickVertex;

public class RankContextualMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 920753966653532282L;

	
	
	public RankContextualMenu(final BrickVertex bV, final Brick brick)
	{
		
		JRadioButtonMenuItem primary = new JRadioButtonMenuItem(Messages.getString("RankContextualMenu.0")); //$NON-NLS-1$
		primary.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				bV.setRank(VerticeRank.primary);
			}
		});
		
		JRadioButtonMenuItem secondary = new JRadioButtonMenuItem(Messages.getString("RankContextualMenu.1")); //$NON-NLS-1$
		secondary.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bV.setRank(VerticeRank.secondary);
			}
		});
		
		JRadioButtonMenuItem remaining = new JRadioButtonMenuItem(Messages.getString("RankContextualMenu.2")); //$NON-NLS-1$
		remaining.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bV.setRank(VerticeRank.remaining);
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(primary);
		group.add(secondary);
		group.add(remaining);
		
		if (bV.getRank() == null)
			bV.setRank(VerticeRank.remaining);
		
		
		switch (bV.getRank())
		{
		case primary :
			primary.setSelected(true);
			break;
		case secondary : 
			secondary.setSelected(true);
			break;
		case remaining :
			remaining.setSelected(true);
			break;			
		}
				
		add(primary);
		add(secondary);
		add(remaining);
		
		
	}
	
	
}
