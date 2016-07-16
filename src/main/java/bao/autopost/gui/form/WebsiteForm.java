package bao.autopost.gui.form;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import bao.autopost.factory.WebsiteFactory;
import bao.autopost.gui.table.CategoryTable;
import bao.autopost.gui.table.WebsiteTable;
import bao.autopost.listeners.TableListener;
import bao.autopost.model.Category;
import bao.autopost.model.Website;
import bao.autopost.service.DataService;
import bao.autopost.util.SettingsUtil;

public class WebsiteForm extends JDialog {

	private static final Logger logger = Logger.getLogger(WebsiteForm.class);
	private static final String URL_LABLE = "URL";
	private static final String USERNAME_LABLE = "Username";
	private static final String PASSWORD_LABLE = "Password";
	private static final String USERNAME_XPATH_LABLE = "Username Xpath";
	private static final String PASSWORD_XPATH_LABLE = "Password Xpath";
	private static final String SHOW_LOGIN_LABLE = "Show Login Xpath";
	private static final String LOGIN_XPATH_LABLE = "Login Xpath";
	private static final String TITLE_XPATH_LABLE = "Title Xpath";
	private static final String CONTENT_XPATH_LABLE = "Content Xpath";
	private static final String SUBMIT_XPATH_LABLE = "Submit Xpath";

	private JPanel contentPane;
	private JPanel panel;
	private JDialog instance;
	
	private WebsiteTable tableSite;
	private CategoryTable tableCategory = new CategoryTable();

	private JTextField txtPassword;
	private JTextField txtUsername;
	private JTextField txtURL;
	private JTextField txtUsernameXpath;
	private JTextField txtPasswordXpath;
	private JTextField txtShowLoginXpath;
	private JTextField txtLoginXpath;
	private JTextField txtIframeXpath;
	private JTextField txtTitleXpath;
	private JTextField txtContentXpath;
	private JTextField txtSubmitXpath;
	private JTextField txtCatURL;
	private JTextField txtCatName;
	private JComboBox<String> cbbType;
	private JButton btnDelete;	

	private Website website;
	private List<Category> newCategories = new ArrayList<Category>();
	private List<Category> deletedCategories = new ArrayList<Category>();
	private boolean isBtnOKClicked;
	
	private List<JTextField> txtFields;

	public Website getWebsite() {
		return website;
	}

	/**
	 * @wbp.parser.constructor
	 */
	public WebsiteForm(Website website, JFrame parent, WebsiteTable tableSite) {
		this(parent, tableSite);
		this.website = website;
		txtURL.setEditable(false);
		loadWebsite();
	}

	private void loadWebTypes() {
		String[] types = SettingsUtil.getWebsitetypes();
		for (String type : types) {
			cbbType.addItem(type);
		}
	}

	public WebsiteForm(JFrame parent, WebsiteTable tableSite) {
		super(parent, true);
		this.tableSite = tableSite;
		instance = this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 439, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 66, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lbType = new JLabel("Type");
		lbType.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbType = new GridBagConstraints();
		gbc_lbType.anchor = GridBagConstraints.WEST;
		gbc_lbType.insets = new Insets(0, 0, 5, 5);
		gbc_lbType.gridx = 0;
		gbc_lbType.gridy = 0;
		contentPane.add(lbType, gbc_lbType);

		cbbType = new JComboBox<String>();
		GridBagConstraints gbc_cbbType = new GridBagConstraints();
		gbc_cbbType.insets = new Insets(0, 0, 5, 5);
		gbc_cbbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbType.gridx = 1;
		gbc_cbbType.gridy = 0;
		contentPane.add(cbbType, gbc_cbbType);

		JLabel lbURL = new JLabel(URL_LABLE);
		GridBagConstraints gbc_lbURL = new GridBagConstraints();
		gbc_lbURL.anchor = GridBagConstraints.WEST;
		gbc_lbURL.insets = new Insets(0, 0, 5, 5);
		gbc_lbURL.gridx = 0;
		gbc_lbURL.gridy = 1;
		contentPane.add(lbURL, gbc_lbURL);

		txtURL = new JTextField();
		txtURL.setName(URL_LABLE);
		GridBagConstraints gbc_txtURL = new GridBagConstraints();
		gbc_txtURL.gridwidth = 2;
		gbc_txtURL.insets = new Insets(0, 0, 5, 0);
		gbc_txtURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtURL.gridx = 1;
		gbc_txtURL.gridy = 1;
		contentPane.add(txtURL, gbc_txtURL);
		txtURL.setColumns(10);

		JLabel lbUsername = new JLabel(USERNAME_LABLE);
		GridBagConstraints gbc_lbUsername = new GridBagConstraints();
		gbc_lbUsername.anchor = GridBagConstraints.WEST;
		gbc_lbUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lbUsername.gridx = 0;
		gbc_lbUsername.gridy = 2;
		contentPane.add(lbUsername, gbc_lbUsername);

		txtUsername = new JTextField();
		txtUsername.setName(USERNAME_LABLE);
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.gridwidth = 2;
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 2;
		contentPane.add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);

		JLabel lbPassword = new JLabel(PASSWORD_LABLE);
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.anchor = GridBagConstraints.WEST;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 3;
		contentPane.add(lbPassword, gbc_lblPassword);

		txtPassword = new JTextField();
		txtPassword.setName(PASSWORD_LABLE);
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 2;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 1;
		gbc_txtPassword.gridy = 3;
		contentPane.add(txtPassword, gbc_txtPassword);

		JLabel lbUsernameXpath = new JLabel(USERNAME_XPATH_LABLE);
		GridBagConstraints gbc_lbUsernameXpath = new GridBagConstraints();
		gbc_lbUsernameXpath.anchor = GridBagConstraints.WEST;
		gbc_lbUsernameXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbUsernameXpath.gridx = 0;
		gbc_lbUsernameXpath.gridy = 4;
		contentPane.add(lbUsernameXpath, gbc_lbUsernameXpath);

		txtUsernameXpath = new JTextField();
		txtUsernameXpath.setName(USERNAME_XPATH_LABLE);
		txtUsernameXpath.setColumns(10);
		GridBagConstraints gbc_txtUsernameXpath = new GridBagConstraints();
		gbc_txtUsernameXpath.gridwidth = 2;
		gbc_txtUsernameXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsernameXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsernameXpath.gridx = 1;
		gbc_txtUsernameXpath.gridy = 4;
		contentPane.add(txtUsernameXpath, gbc_txtUsernameXpath);

		JLabel lbPasswordXpath = new JLabel(PASSWORD_XPATH_LABLE);
		GridBagConstraints gbc_lbPasswordXpath = new GridBagConstraints();
		gbc_lbPasswordXpath.anchor = GridBagConstraints.WEST;
		gbc_lbPasswordXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbPasswordXpath.gridx = 0;
		gbc_lbPasswordXpath.gridy = 5;
		contentPane.add(lbPasswordXpath, gbc_lbPasswordXpath);

		txtPasswordXpath = new JTextField();
		txtPasswordXpath.setName(PASSWORD_XPATH_LABLE);
		txtPasswordXpath.setColumns(10);
		GridBagConstraints gbc_txtPasswordXpath = new GridBagConstraints();
		gbc_txtPasswordXpath.gridwidth = 2;
		gbc_txtPasswordXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtPasswordXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPasswordXpath.gridx = 1;
		gbc_txtPasswordXpath.gridy = 5;
		contentPane.add(txtPasswordXpath, gbc_txtPasswordXpath);

		JLabel lbShowLogin = new JLabel(SHOW_LOGIN_LABLE);
		GridBagConstraints gbc_lbShowLogin = new GridBagConstraints();
		gbc_lbShowLogin.anchor = GridBagConstraints.WEST;
		gbc_lbShowLogin.insets = new Insets(0, 0, 5, 5);
		gbc_lbShowLogin.gridx = 0;
		gbc_lbShowLogin.gridy = 6;
		contentPane.add(lbShowLogin, gbc_lbShowLogin);

		txtShowLoginXpath = new JTextField();
		txtShowLoginXpath.setName(SHOW_LOGIN_LABLE);
		txtShowLoginXpath.setColumns(10);
		GridBagConstraints gbc_txtShowLoginXpath = new GridBagConstraints();
		gbc_txtShowLoginXpath.gridwidth = 2;
		gbc_txtShowLoginXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtShowLoginXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtShowLoginXpath.gridx = 1;
		gbc_txtShowLoginXpath.gridy = 6;
		contentPane.add(txtShowLoginXpath, gbc_txtShowLoginXpath);

		JLabel lbIframeXpath = new JLabel("Iframe Xpath");
		GridBagConstraints gbc_lbIframeXpath = new GridBagConstraints();
		gbc_lbIframeXpath.anchor = GridBagConstraints.WEST;
		gbc_lbIframeXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbIframeXpath.gridx = 0;
		gbc_lbIframeXpath.gridy = 7;
		contentPane.add(lbIframeXpath, gbc_lbIframeXpath);

		txtIframeXpath = new JTextField();
		txtIframeXpath.setText((String) null);
		txtIframeXpath.setName("Show Login Xpath");
		txtIframeXpath.setColumns(10);
		GridBagConstraints gbc_txtIframeXpath = new GridBagConstraints();
		gbc_txtIframeXpath.gridwidth = 2;
		gbc_txtIframeXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtIframeXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIframeXpath.gridx = 1;
		gbc_txtIframeXpath.gridy = 7;
		contentPane.add(txtIframeXpath, gbc_txtIframeXpath);

		JLabel lbLoginXpath = new JLabel(LOGIN_XPATH_LABLE);
		GridBagConstraints gbc_lbLoginXpath = new GridBagConstraints();
		gbc_lbLoginXpath.anchor = GridBagConstraints.WEST;
		gbc_lbLoginXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbLoginXpath.gridx = 0;
		gbc_lbLoginXpath.gridy = 8;
		contentPane.add(lbLoginXpath, gbc_lbLoginXpath);

		txtLoginXpath = new JTextField();
		txtLoginXpath.setName(LOGIN_XPATH_LABLE);
		txtLoginXpath.setColumns(10);
		GridBagConstraints gbc_txtLoginXpath = new GridBagConstraints();
		gbc_txtLoginXpath.gridwidth = 2;
		gbc_txtLoginXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtLoginXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLoginXpath.gridx = 1;
		gbc_txtLoginXpath.gridy = 8;
		contentPane.add(txtLoginXpath, gbc_txtLoginXpath);

		JLabel lblTitleXpath = new JLabel(TITLE_XPATH_LABLE);
		GridBagConstraints gbc_lbTitleXpath = new GridBagConstraints();
		gbc_lbTitleXpath.anchor = GridBagConstraints.WEST;
		gbc_lbTitleXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbTitleXpath.gridx = 0;
		gbc_lbTitleXpath.gridy = 9;
		contentPane.add(lblTitleXpath, gbc_lbTitleXpath);

		txtTitleXpath = new JTextField();
		txtTitleXpath.setName(TITLE_XPATH_LABLE);
		GridBagConstraints gbc_txtTitleXpath = new GridBagConstraints();
		gbc_txtTitleXpath.gridwidth = 2;
		gbc_txtTitleXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtTitleXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTitleXpath.gridx = 1;
		gbc_txtTitleXpath.gridy = 9;
		contentPane.add(txtTitleXpath, gbc_txtTitleXpath);
		txtTitleXpath.setColumns(10);

		JLabel lbContentXpath = new JLabel(CONTENT_XPATH_LABLE);
		GridBagConstraints gbc_lbContentXpath = new GridBagConstraints();
		gbc_lbContentXpath.anchor = GridBagConstraints.WEST;
		gbc_lbContentXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbContentXpath.gridx = 0;
		gbc_lbContentXpath.gridy = 10;
		contentPane.add(lbContentXpath, gbc_lbContentXpath);

		txtContentXpath = new JTextField();
		txtContentXpath.setName(CONTENT_XPATH_LABLE);
		GridBagConstraints gbc_txtContentXpath = new GridBagConstraints();
		gbc_txtContentXpath.gridwidth = 2;
		gbc_txtContentXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtContentXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContentXpath.gridx = 1;
		gbc_txtContentXpath.gridy = 10;
		contentPane.add(txtContentXpath, gbc_txtContentXpath);
		txtContentXpath.setColumns(10);

		JLabel lbSubmitXpath = new JLabel(SUBMIT_XPATH_LABLE);
		GridBagConstraints gbc_lbSubmitXpath = new GridBagConstraints();
		gbc_lbSubmitXpath.anchor = GridBagConstraints.WEST;
		gbc_lbSubmitXpath.insets = new Insets(0, 0, 5, 5);
		gbc_lbSubmitXpath.gridx = 0;
		gbc_lbSubmitXpath.gridy = 11;
		contentPane.add(lbSubmitXpath, gbc_lbSubmitXpath);

		txtSubmitXpath = new JTextField();
		txtSubmitXpath.setName(SUBMIT_XPATH_LABLE);
		GridBagConstraints gbc_txtSubmitXpath = new GridBagConstraints();
		gbc_txtSubmitXpath.gridwidth = 2;
		gbc_txtSubmitXpath.insets = new Insets(0, 0, 5, 0);
		gbc_txtSubmitXpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSubmitXpath.gridx = 1;
		gbc_txtSubmitXpath.gridy = 11;
		contentPane.add(txtSubmitXpath, gbc_txtSubmitXpath);
		txtSubmitXpath.setColumns(10);

		JLabel lblCategories = new JLabel("Categories");
		GridBagConstraints gbc_lbCategories = new GridBagConstraints();
		gbc_lbCategories.anchor = GridBagConstraints.WEST;
		gbc_lbCategories.insets = new Insets(0, 0, 5, 5);
		gbc_lbCategories.gridx = 0;
		gbc_lbCategories.gridy = 12;
		contentPane.add(lblCategories, gbc_lbCategories);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 13;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 66, 116, 276, 0 };
		gbl_panel.rowHeights = new int[] { 114, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 0.5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(tableCategory, gbc_scrollPane);
		tableCategory.addListener(new TableListener() {
			@Override
			public void onRowSelected(Object site) {
				btnDelete.setEnabled(true);
			}

			@Override
			public void onAllRowsDeselected() {
				if(tableCategory.getSelectedRows().isEmpty()){
					btnDelete.setEnabled(false);
				}
			}
		});

		txtCatName = new JTextField();
		txtCatName.setHorizontalAlignment(SwingConstants.LEFT);
		txtCatName.setColumns(10);
		GridBagConstraints gbc_txtCatName = new GridBagConstraints();
		gbc_txtCatName.gridwidth = 2;
		gbc_txtCatName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCatName.insets = new Insets(0, 0, 5, 2);
		gbc_txtCatName.gridx = 0;
		gbc_txtCatName.gridy = 1;
		panel.add(txtCatName, gbc_txtCatName);

		txtCatURL = new JTextField();
		GridBagConstraints gbc_txtCatURL = new GridBagConstraints();
		gbc_txtCatURL.anchor = GridBagConstraints.WEST;
		gbc_txtCatURL.insets = new Insets(0, 0, 5, 0);
		gbc_txtCatURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCatURL.gridx = 2;
		gbc_txtCatURL.gridy = 1;
		panel.add(txtCatURL, gbc_txtCatURL);
		txtCatURL.setColumns(10);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = txtCatName.getText().trim();
				String url = txtCatURL.getText().trim();
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(instance, "Please enter category name", "Empty category name",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (url.isEmpty()) {
					JOptionPane.showMessageDialog(instance, "Please enter category url", "Empty category url",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				Category category = new Category();
				category.setName(name);
				category.setUrl(url);
				try {
					tableCategory.addRow(category);
					newCategories.add(category);
				} catch (Exception e) {
					if (e.getMessage().equals("Object existed")) {
						JOptionPane.showMessageDialog(instance, "Category existed", "Duplication error",
								JOptionPane.ERROR_MESSAGE);
					}
				}				
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 2;
		panel.add(btnAdd, gbc_btnAdd);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 9;

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Category> cats = tableCategory.deleteSelectedRows();
				deletedCategories.addAll(cats);
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 0);
		gbc_btnDelete.anchor = GridBagConstraints.WEST;
		gbc_btnDelete.weightx = 50.0;
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 2;
		panel.add(btnDelete, gbc_btnDelete);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performAddOrUpdateWebsite();
			}
		});
		GridBagConstraints gbc_btnOK = new GridBagConstraints();
		gbc_btnOK.anchor = GridBagConstraints.EAST;
		gbc_btnOK.gridx = 2;
		gbc_btnOK.gridy = 14;
		contentPane.add(btnOK, gbc_btnOK);

		loadWebTypes();
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (!isBtnOKClicked && website != null) {
					website.getCategories().removeAll(newCategories);
					website.getCategories().addAll(deletedCategories);
				}
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

		});

		txtFields = Arrays.asList(txtURL, txtUsername, txtPassword, txtUsernameXpath, txtPasswordXpath,
				txtShowLoginXpath, txtLoginXpath, txtTitleXpath, txtContentXpath, txtSubmitXpath);
		for (final JTextField txtField : txtFields) {
			txtField.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					if (!txtField.getText().isEmpty()) {
						txtField.setBackground(Color.white);
					} else {
						txtField.setBackground(Color.red);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}	
	}

	private void performAddOrUpdateWebsite() {
		boolean isNewSite = false;
		if (website == null) {
			try {
				website = WebsiteFactory.createWebsite((String) cbbType.getSelectedItem());
				isNewSite = true;
			} catch (Exception ex) {
				isBtnOKClicked = false;
				logger.error("Cannot create website due to " + ex.getMessage(), ex);
				return;
			}
		}

		boolean isValid = true;
		String url = txtURL.getText().trim();
		String username = txtUsername.getText().trim();
		String password = txtPassword.getText().trim();
		String usernameXpath = txtUsernameXpath.getText().trim();
		String passwordXpath = txtPasswordXpath.getText().trim();
		String showLoginFormXpath = txtShowLoginXpath.getText().trim();
		String loginXpath = txtLoginXpath.getText().trim();
		String titleXpath = txtTitleXpath.getText().trim();
		String contentXpath = txtContentXpath.getText().trim();
		String submitXpath = txtSubmitXpath.getText().trim();

		for (JTextField tf : txtFields) {
			if (tf.getText().trim().isEmpty()) {
				tf.setBackground(Color.red);
				isValid = false;
			}
		}
		
		List<Category> categories = tableCategory.getData();
		if(categories == null || categories.isEmpty()){
			JOptionPane.showMessageDialog(instance, "At least one category is required", "Empty category",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (isValid) {
			website.setUrl(url);
			website.setUsername(username);
			website.setPassword(password);
			website.setUsernameXpath(usernameXpath);
			website.setPasswordXpath(passwordXpath);
			website.setShowLoginFormXpath(showLoginFormXpath);
			website.setLoginXpath(loginXpath);
			website.setTitleXpath(titleXpath);
			website.setContentXpath(contentXpath);
			website.setSubmitXpath(submitXpath);
			website.setIframeXpath(txtIframeXpath.getText().trim());
			website.setCategories(tableCategory.getData());
			isBtnOKClicked = true;
			if (isNewSite) {
				try {
					tableSite.addRow(website);
				} catch (Exception e) {
					if (e.getMessage().equals("Object existed")) {
						JOptionPane.showMessageDialog(instance, "Site existed", "Duplication error",
								JOptionPane.ERROR_MESSAGE);
						website = null;
						return;
					}
				}
			}	
			DataService.setDataChanged(true);
			instance.dispose();
		}
	}

	private void loadWebsite() {
		cbbType.setSelectedItem(website.getType());
		txtURL.setText(website.getUrl());
		txtUsername.setText(website.getUsername());
		txtPassword.setText(website.getPassword());
		txtUsernameXpath.setText(website.getUsernameXpath());
		txtPasswordXpath.setText(website.getPasswordXpath());
		txtShowLoginXpath.setText(website.getShowLoginFormXpath());
		txtLoginXpath.setText(website.getLoginXpath());
		if (website.getIframeXpath() != null) {
			txtIframeXpath.setText(website.getIframeXpath());
		}
		txtTitleXpath.setText(website.getTitleXpath());
		txtContentXpath.setText(website.getContentXpath());
		txtSubmitXpath.setText(website.getSubmitXpath());
		tableCategory.setData(website.getCategories());		
	}
}
