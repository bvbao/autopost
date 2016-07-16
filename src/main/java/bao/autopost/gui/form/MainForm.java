package bao.autopost.gui.form;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import bao.autopost.gui.component.StatusBar;
import bao.autopost.gui.table.WebsiteTable;
import bao.autopost.listeners.PostTaskListener;
import bao.autopost.listeners.TableListener;
import bao.autopost.model.Category;
import bao.autopost.model.Content;
import bao.autopost.model.TaskStatus;
import bao.autopost.model.Website;
import bao.autopost.service.AutoPostService;
import bao.autopost.service.DataService;

public class MainForm extends JFrame {

	private static final Logger logger = Logger.getLogger(MainForm.class);
	private JPanel contentPane;
	private JTextField txtContent;
	private WebsiteTable tableSite;
	private JMenu mnSettings;
	private JMenuItem mnItemAccount;
	private JMenuItem mnItemConfig;
	private AutoPostService service = new AutoPostService();
	private JComboBox<Category> cbbCategory;
	private JToolBar toolBar;
	private JComboBox<Content> cbbContentType;
	private JTextField txtTitle;
	private boolean categoryChanged = false;
	private JFrame instance;
	private JButton btnEdit;
	private JButton btnDelete;
	private JButton btnRunSelected;
	private JButton btnRunAll;
	private StatusBar statusBar;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainForm frame = null;
				try {
					frame = new MainForm();
					frame.setVisible(true);
				} catch (Exception e) {
					logger.error("Error while open main form", e);
					if (frame != null) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Unknown error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	public MainForm() throws JAXBException {
		instance = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 359);

		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
			service = (AutoPostService) context.getBean("autoPostService");
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				service.stopAllTasks();
				service.save();
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
				try {
					int total = service.getAllSites().size();
					tableSite.setData(service.getAllSites());
					statusBar.setTotalSite(total);
				} catch (JAXBException e) {
					logger.error("Could not load sites", e);
				}
			}
		});

		createMainLayout();
		createMenu();
		createToolBar();
		createInputArea();
		createTable();
		createStatusBar();
		createActionButton();
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		mnItemAccount = new JMenuItem("Account");
		mnSettings.add(mnItemAccount);

		mnItemConfig = new JMenuItem("Configuration");
		mnSettings.add(mnItemConfig);
	}

	private void createTable() throws JAXBException {
		GridBagConstraints gbc_siteTable = new GridBagConstraints();
		gbc_siteTable.weighty = 1.0;
		gbc_siteTable.weightx = 1.0;
		gbc_siteTable.fill = GridBagConstraints.BOTH;
		gbc_siteTable.insets = new Insets(0, 0, 5, 0);
		gbc_siteTable.gridwidth = 3;
		gbc_siteTable.gridx = 0;
		gbc_siteTable.gridy = 4;
		tableSite = new WebsiteTable();
		contentPane.add(tableSite, gbc_siteTable);
		tableSite.addListener(new TableListener() {
			@Override
			public void onRowSelected(Object obj) {
				performSiteSelection(obj);
			}

			@Override
			public void onAllRowsDeselected() {
				if (tableSite.getSelectedRows().isEmpty()) {
					btnDelete.setEnabled(false);
					btnRunSelected.setEnabled(false);
					btnEdit.setEnabled(false);
				}
			}
		});
	}

	private void performSiteSelection(Object obj) {
		btnEdit.setEnabled(true);
		btnDelete.setEnabled(true);
		if (tableSite.getCurrentStatusOfSelectedWebsite().equals(TaskStatus.Canceling)) {
			btnRunSelected.setEnabled(false);
		} else {
			btnRunSelected.setEnabled(true);
		}
		if (tableSite.getCurrentStatusOfSelectedWebsite().equals(TaskStatus.Running)) {
			btnRunSelected.setText("Stop");
		} else {
			btnRunSelected.setText("Run");
		}
		Website site = (Website) obj;
		categoryChanged = false;
		cbbCategory.removeAllItems();
		for (Category cat : site.getCategories()) {
			cbbCategory.addItem(cat);
		}
		cbbCategory.setSelectedItem(site.getSelectedCategory() != null ? site.getSelectedCategory() : 0);
	}

	private void createStatusBar() {
		statusBar = new StatusBar();
		FlowLayout flowLayout = (FlowLayout) statusBar.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		statusBar.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_statusBar = new GridBagConstraints();
		gbc_statusBar.weightx = 1.0;
		gbc_statusBar.fill = GridBagConstraints.BOTH;
		gbc_statusBar.gridwidth = 3;
		gbc_statusBar.gridx = 0;
		gbc_statusBar.gridy = 5;
		contentPane.add(statusBar, gbc_statusBar);
	}

	private void createActionButton() {
		final JButton btnStopAll = new JButton("Stop All");
		btnStopAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				service.stopAllTasks();
			}
		});

		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WebsiteForm form = new WebsiteForm(instance, tableSite);
				form.setVisible(true);
			}
		});
		toolBar.add(btnNew);

		btnEdit = new JButton("Edit");
		btnEdit.setEnabled(false);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WebsiteForm form = new WebsiteForm(tableSite.getFirstSelectedRow(), instance, tableSite);
				form.setVisible(true);
			}
		});
		toolBar.add(btnEdit);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
						JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.NO_OPTION) {
					return;
				}
				tableSite.deleteSelectedRows();
				btnDelete.setEnabled(false);
				btnEdit.setEnabled(false);
				DataService.setDataChanged(true);
			}
		});
		btnDelete.setEnabled(false);
		toolBar.add(btnDelete);
		toolBar.add(btnStopAll);

		btnRunAll = new JButton("Run All");
		toolBar.add(btnRunAll);
		btnRunAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					run(service.getAllSites());
				} catch (Exception e) {
					logger.error("Error while start running all websites", e);
					JOptionPane.showMessageDialog(instance, e.getMessage(), "Unknown error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnRunSelected = new JButton("Run");
		btnRunSelected.setEnabled(false);
		toolBar.add(btnRunSelected);
		btnRunSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (btnRunSelected.getText().equals("Run")) {
					try {
						run(tableSite.getSelectedRows());
						btnRunSelected.setText("Stop");
					} catch (Exception e) {
						logger.error("Error while start running selected websites", e);
						JOptionPane.showMessageDialog(instance, e.getMessage(), "Unknown error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					service.stopTask(tableSite.getSelectedRows());
					btnRunSelected.setText("Run");
				}
			}
		});
	}

	private void run(Collection<Website> websites) throws Exception {
		if (txtTitle.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(instance, "Please enter title", "Empty title", JOptionPane.ERROR_MESSAGE);
			return;
		}
		service.post(websites, txtTitle.getText(), new PostTaskListener() {
			@Override
			public void taskStatusChanged(TaskStatus status, Website site) {
				tableSite.updateTaskStatus(site, status);
				if (status.equals(TaskStatus.Running)) {
					statusBar.increaseRunning();
				} else {
					if (!status.equals(TaskStatus.Canceling)) {
						statusBar.decreaseRunning();
					}
					if (status.equals(TaskStatus.Successful)) {
						statusBar.increaseSuccess();
					}
					if (status.equals(TaskStatus.Failed)) {
						statusBar.increaseFailed();
					}
					if (status.equals(TaskStatus.Canceled)) {
						statusBar.increaseCanceled();
					}
				}
			}

			@Override
			public void taskFinished() {

			}
		});
	}

	private void createMainLayout() {
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 47, 341, 80 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 177, 12, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 1.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
	}

	private void createInputArea() {

		JLabel lblTitle = new JLabel("Title");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.WEST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 1;
		contentPane.add(lblTitle, gbc_lblTitle);

		txtTitle = new JTextField();
		txtTitle.setColumns(10);
		GridBagConstraints gbc_txtTitle = new GridBagConstraints();
		gbc_txtTitle.insets = new Insets(0, 0, 5, 5);
		gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTitle.gridx = 1;
		gbc_txtTitle.gridy = 1;
		contentPane.add(txtTitle, gbc_txtTitle);
		JLabel lbContent = new JLabel("Content");
		GridBagConstraints gbc_lbContent = new GridBagConstraints();
		gbc_lbContent.fill = GridBagConstraints.BOTH;
		gbc_lbContent.anchor = GridBagConstraints.WEST;
		gbc_lbContent.insets = new Insets(0, 0, 5, 5);
		gbc_lbContent.gridx = 0;
		gbc_lbContent.gridy = 2;
		contentPane.add(lbContent, gbc_lbContent);

		txtContent = new JTextField();
		GridBagConstraints gbc_txtContent = new GridBagConstraints();
		gbc_txtContent.weightx = 1.0;
		gbc_txtContent.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContent.insets = new Insets(0, 0, 5, 5);
		gbc_txtContent.gridx = 1;
		gbc_txtContent.gridy = 2;
		contentPane.add(txtContent, gbc_txtContent);
		txtContent.setColumns(10);
		txtContent.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				Content content = (Content) cbbContentType.getSelectedItem();
				content.setValue(txtContent.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				Content content = (Content) cbbContentType.getSelectedItem();
				content.setValue(txtContent.getText());
			}
		});

		cbbContentType = new JComboBox<Content>();
		GridBagConstraints gbc_cbbContentType = new GridBagConstraints();
		gbc_cbbContentType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbContentType.insets = new Insets(0, 0, 5, 0);
		gbc_cbbContentType.gridx = 2;
		gbc_cbbContentType.gridy = 2;
		contentPane.add(cbbContentType, gbc_cbbContentType);
		cbbContentType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Content content = (Content) cbbContentType.getSelectedItem();
				txtContent.setText(content.getValue());
			}
		});
		for (Content content : service.getAllContents()) {
			cbbContentType.addItem(content);
		}

		cbbCategory = new JComboBox<Category>();
		GridBagConstraints gbc_cbbCategory = new GridBagConstraints();
		gbc_cbbCategory.gridwidth = 2;
		gbc_cbbCategory.insets = new Insets(0, 0, 5, 0);
		gbc_cbbCategory.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbCategory.gridx = 1;
		gbc_cbbCategory.gridy = 3;
		contentPane.add(cbbCategory, gbc_cbbCategory);
		cbbCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (categoryChanged) {
					Category cat = (Category) cbbCategory.getSelectedItem();
					tableSite.updateCategoryForSelectedWebsite(cat);
				}
			}
		});
		cbbCategory.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				categoryChanged = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		JLabel lblCategory = new JLabel("Category");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.anchor = GridBagConstraints.EAST;
		gbc_lblCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 3;
		contentPane.add(lblCategory, gbc_lblCategory);
	}

	private void createToolBar() {
		toolBar = new JToolBar();
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.WEST;
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridwidth = 3;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		contentPane.add(toolBar, gbc_toolBar);
	}
}
