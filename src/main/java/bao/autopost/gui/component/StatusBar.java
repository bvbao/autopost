package bao.autopost.gui.component;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

	private JLabel lbTotalSite;
	private JLabel lbSuccess;
	private JLabel lbFailed;
	private JLabel lbCanceled;
	private JLabel lbRunning;
	private int nRunning;
	private int nFailed;
	private int nSuccess;
	private int nCanceled;

	public StatusBar() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		JLabel lblNewLabel = new JLabel("Total:");
		add(lblNewLabel);

		lbTotalSite = new JLabel("0");
		add(lbTotalSite);

		JLabel lblNewLabel_1 = new JLabel("|");
		add(lblNewLabel_1);

		JLabel lblNewLabel_5 = new JLabel("Running:");
		add(lblNewLabel_5);

		lbRunning = new JLabel("0");
		add(lbRunning);

		JLabel label_1 = new JLabel("|");
		add(label_1);

		JLabel lblNewLabel_2 = new JLabel("Success:");
		add(lblNewLabel_2);

		lbSuccess = new JLabel("0");
		add(lbSuccess);

		JLabel label = new JLabel("|");
		add(label);

		JLabel lblNewLabel_3 = new JLabel("Failed:");
		add(lblNewLabel_3);

		lbFailed = new JLabel("0");
		add(lbFailed);

		JLabel label_2 = new JLabel("|");
		add(label_2);

		JLabel lblNewLabel_4 = new JLabel("Canceled:");
		add(lblNewLabel_4);

		lbCanceled = new JLabel("0");
		add(lbCanceled);
	}

	public void setTotalSite(int total) {
		lbTotalSite.setText(String.valueOf(total));
	}

	public void reset() {
		lbSuccess.setText("0");
		lbRunning.setText("0");
		lbFailed.setText("0");
		lbCanceled.setText("0");
	}

	public synchronized void increaseRunning() {
		nRunning++;
		lbRunning.setText(String.valueOf(nRunning));
	}

	public synchronized void decreaseRunning() {
		nRunning--;
		lbRunning.setText(String.valueOf(nRunning));
	}

	public synchronized void increaseSuccess() {
		nSuccess++;
		lbSuccess.setText(String.valueOf(nSuccess));
	}

	public synchronized void increaseFailed() {
		nFailed++;
		lbFailed.setText(String.valueOf(nFailed));
	}

	public synchronized void increaseCanceled() {
		nCanceled++;
		lbCanceled.setText(String.valueOf(nCanceled));
	}
}
