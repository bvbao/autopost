package bao.autopost.gui.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bao.autopost.util.SettingsUtil;

public class SettingDialog extends JDialog {

	private static JDialog instance;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JTextField txtMaxTask;
	private JCheckBox chkBoxAutoSubmit;
	private JTextField txtTypes;

	public SettingDialog(JFrame parent) {
		super(parent, true);		
		instance = this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Settings");
		setBounds(100, 100, 316, 221);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Username");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			txtUsername = new JTextField();
			GridBagConstraints gbc_txtUsername = new GridBagConstraints();
			gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
			gbc_txtUsername.gridx = 1;
			gbc_txtUsername.gridy = 0;
			contentPanel.add(txtUsername, gbc_txtUsername);
			txtUsername.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password");
			GridBagConstraints gbc_lblPassword = new GridBagConstraints();
			gbc_lblPassword.anchor = GridBagConstraints.WEST;
			gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
			gbc_lblPassword.gridx = 0;
			gbc_lblPassword.gridy = 1;
			contentPanel.add(lblPassword, gbc_lblPassword);
		}
		{
			txtPassword = new JTextField();
			txtPassword.setColumns(10);
			GridBagConstraints gbc_txtPassword = new GridBagConstraints();
			gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
			gbc_txtPassword.gridx = 1;
			gbc_txtPassword.gridy = 1;
			contentPanel.add(txtPassword, gbc_txtPassword);
		}
		{
			JLabel lblMaxTask = new JLabel("Max Task");
			GridBagConstraints gbc_lblMaxTask = new GridBagConstraints();
			gbc_lblMaxTask.anchor = GridBagConstraints.WEST;
			gbc_lblMaxTask.insets = new Insets(0, 0, 5, 5);
			gbc_lblMaxTask.gridx = 0;
			gbc_lblMaxTask.gridy = 2;
			contentPanel.add(lblMaxTask, gbc_lblMaxTask);
		}
		{
			txtMaxTask = new JTextField();
			GridBagConstraints gbc_txtMaxTask = new GridBagConstraints();
			gbc_txtMaxTask.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtMaxTask.insets = new Insets(0, 0, 5, 0);
			gbc_txtMaxTask.gridx = 1;
			gbc_txtMaxTask.gridy = 2;
			contentPanel.add(txtMaxTask, gbc_txtMaxTask);
			txtMaxTask.setColumns(10);
		}
		{
			JLabel lblSiteTypes = new JLabel("Site types");
			GridBagConstraints gbc_lblSiteTypes = new GridBagConstraints();
			gbc_lblSiteTypes.anchor = GridBagConstraints.WEST;
			gbc_lblSiteTypes.insets = new Insets(0, 0, 5, 5);
			gbc_lblSiteTypes.gridx = 0;
			gbc_lblSiteTypes.gridy = 3;
			contentPanel.add(lblSiteTypes, gbc_lblSiteTypes);
		}
		{
			txtTypes = new JTextField();
			txtTypes.setColumns(10);
			GridBagConstraints gbc_txtTypes = new GridBagConstraints();
			gbc_txtTypes.insets = new Insets(0, 0, 5, 0);
			gbc_txtTypes.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTypes.gridx = 1;
			gbc_txtTypes.gridy = 3;
			contentPanel.add(txtTypes, gbc_txtTypes);
		}
		{
			JLabel lblAutoSubmit = new JLabel("Auto submission");
			GridBagConstraints gbc_lblAutoSubmit = new GridBagConstraints();
			gbc_lblAutoSubmit.anchor = GridBagConstraints.WEST;
			gbc_lblAutoSubmit.insets = new Insets(0, 0, 0, 5);
			gbc_lblAutoSubmit.gridx = 0;
			gbc_lblAutoSubmit.gridy = 4;
			contentPanel.add(lblAutoSubmit, gbc_lblAutoSubmit);
		}
		{
			chkBoxAutoSubmit = new JCheckBox("");
			GridBagConstraints gbc_chkBoxAutoSubmit = new GridBagConstraints();
			gbc_chkBoxAutoSubmit.anchor = GridBagConstraints.WEST;
			gbc_chkBoxAutoSubmit.gridx = 1;
			gbc_chkBoxAutoSubmit.gridy = 4;
			contentPanel.add(chkBoxAutoSubmit, gbc_chkBoxAutoSubmit);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String[] keys = new String[5];
						String username = txtUsername.getText();
						String password = txtPassword.getText();
						String maxTask = txtMaxTask.getText();
						String selected = String.valueOf(chkBoxAutoSubmit.isSelected());
						String types = txtTypes.getText();
						keys[0] = SettingsUtil.DEFAULT_USERNAME_KEY;
						keys[1] = SettingsUtil.DEFAULT_PASSWORD_KEY;
						keys[2] = SettingsUtil.MAX_TASK;
						keys[3] = SettingsUtil.TYPES;
						keys[4] = SettingsUtil.AUTO_SUBMISSION;
						String[] values = new String[] { username, password, maxTask, types, selected };
						SettingsUtil.savePropertiesFile(keys, values);
						instance.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadSettings();
	}
	
	private void loadSettings(){
		String[] credentials = SettingsUtil.getUsernameAndPassword();
		txtUsername.setText(credentials[0]);
		txtPassword.setText(credentials[1]);
		txtMaxTask.setText(String.valueOf(SettingsUtil.getMaxThread()));
		txtTypes.setText(SettingsUtil.getSiteTypes());
		chkBoxAutoSubmit.setSelected(SettingsUtil.getAutoSubmit());
	}

}
