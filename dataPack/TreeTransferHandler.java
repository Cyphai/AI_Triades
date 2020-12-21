package dataPack;

import java.awt.datatransfer.DataFlavor;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class TreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 8808467211310175768L;

	public TreeTransferHandler() {
		super(null);
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		System.out.println("Youpi 1"); //$NON-NLS-1$
		return true;
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		System.out.println("Youpi 2"); //$NON-NLS-1$
		return true;
	}
}
