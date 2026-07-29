package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;

public class PainelDashboard extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PainelDashboard() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Dashboard");
		add(lblNewLabel, BorderLayout.NORTH);

	}

}
