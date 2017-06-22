package view;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FtpMain {

	private JFrame frame;
	@SuppressWarnings("unused")
	private JTextField textField;
	Ftputil ftputil = new Ftputil();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FtpMain window = new FtpMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FtpMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame("FTP�ͻ���");
		frame.setBounds(100, 100, 555, 437);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0)); 
		
		JPanel panel2 = new JPanel();
		GridLayout gl_panel2 = new GridLayout(3,2);
		gl_panel2.setVgap(15);
		gl_panel2.setHgap(10);
		panel2.setLayout(gl_panel2);
		panel.add(panel2);
		
		//������label
		JLabel label = new JLabel("��������");
		label.setFont(new Font("����", Font.PLAIN, 14));
		//�������ı���
		JTextField textField = new JTextField();
		
		//�˿�label
		JLabel label4 = new JLabel("�˿�:");
		label4.setFont(new Font("����", Font.PLAIN, 14));
		//�˿��ı���
		JTextField textField4 = new JTextField();
		//��½��label
		JLabel label2 = new JLabel("��½����");
		label2.setFont(new Font("����", Font.PLAIN, 14));
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		//��½���ı���
		JTextField textField2 = new JTextField();
		//����label
		JLabel label3 = new JLabel("\u767B\u9646\u53E3\u4EE4:");
		label3.setFont(new Font("����", Font.PLAIN, 14));
		
		//�����ı���
		JTextField textField3 = new JTextField();
		
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.add(label, BorderLayout.WEST);
		panel3.add(textField);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BorderLayout());
		panel4.add(label4,BorderLayout.WEST);
		panel4.add(textField4);
		
		JPanel panel5 = new JPanel();
		panel5.setBorder(null);
		panel5.setLayout(new BorderLayout());
		panel5.add(label2,BorderLayout.WEST);
		panel5.add(textField2);
		
		JPanel panel6 = new JPanel();
		panel6.setLayout(new BorderLayout());
		panel6.add(label3,BorderLayout.WEST);
		panel6.add(textField3);
		
		JButton button2 = new JButton("��ʼ����");
		
		
		JButton button = new JButton("�Ͽ�����");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ftputil.Dukaiconnect()==1){
					JOptionPane.showMessageDialog(null,"�Ͽ����ӳɹ�");
					button.setEnabled(false);
					button2.setEnabled(true);
					textField.setEnabled(true);
					textField2.setEnabled(true);
					textField3.setEnabled(true);
					textField4.setEnabled(true);
				}	
			}
		});
		
		panel2.add(panel3);
		panel2.add(panel4);
		panel2.add(panel5);
		panel2.add(panel6);
		panel2.add(button2);
		panel2.add(button);
		
		//�°벿��
		JPanel bomJPanel = new JPanel();
		frame.getContentPane().add(bomJPanel);
		bomJPanel.setLayout(new GridLayout(1, 2, 0, 20));
		
		JPanel leftjJPanel = new JPanel();
		
		JLabel telJLabel = new JLabel("�ļ��б�                              ");
		JList<String> jList = new JList<String>();
		jList.setVisibleRowCount(15);
	    
		
		leftjJPanel.add(telJLabel);
		leftjJPanel.add(new JScrollPane(jList));
		//
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(4,1,20,20));
		JButton button3 = new JButton("ˢ��");
		button3.setPreferredSize(new Dimension(5, 5));
		JButton button4 = new JButton("�ϴ�");
		button4.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				if(ftputil.socket==null)
					JOptionPane.showMessageDialog(null,"δ��½�����½������");
				else{
				String path = null;
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("��ѡ��Ҫ�ϴ����ļ�...");
				fc.setApproveButtonText("ȷ��");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
				    path=fc.getSelectedFile().getPath();
				    System.out.println(path);
			}
				File file = new File(path);
				System.out.println(file.getPath());
				try {
					if(ftputil.stor(file))
					   JOptionPane.showMessageDialog(null,"�ϴ��ɹ���");
					else {
						JOptionPane.showMessageDialog(null,"�ϴ�ʧ�ܣ�");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
			}});
		
		JButton button5 = new JButton("����");
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = jList.getSelectedValue();//��ȡѡ����ļ���
				System.out.println(filename);
				File file = new File(filename);
				String name = file.getName();
				String ftppath = "F:\\ftpserver\\"+name;
				String localpath = "F:\\client\\"+name;
				System.out.println(localpath);
				try {
					int i = ftputil.downloadFile(localpath, ftppath);
					if(i==1)
						JOptionPane.showMessageDialog(null, "�����ļ��ɹ���");
					else {
						JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton button6 = new JButton("�˳�");;
		/*
		JPanel panel7 = new JPanel();
		panel7.setLayout(new FlowLayout());
		JFileChooser jFileChooser = new JFileChooser("��ѡ���ļ�");
		jFileChooser.setPreferredSize(new Dimension(50,30));
		panel7.add(jFileChooser);
		panel7.add(button4);
		*/
		//
		rightPanel.add(button3);
		rightPanel.add(button4);
		rightPanel.add(button5);
		rightPanel.add(button6);
		
		bomJPanel.add(leftjJPanel);
		bomJPanel.add(rightPanel);
		button2.addMouseListener(new MouseAdapter() {//��ʼ���Ӱ�ť������
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField.getText()==null||textField.getText().equals(""))
					JOptionPane.showMessageDialog(null, "��������ַ����Ϊ��");
				else if(textField4.getText()==null||textField4.getText().equals(""))
					JOptionPane.showMessageDialog(null, "�˿ڲ���Ϊ��");
				else if(textField2.getText()==null||textField2.getText().equals(""))
					JOptionPane.showMessageDialog(null, "��½������Ϊ��");
				else if(textField3.getText()==null||textField3.getText().equals(""))
					JOptionPane.showMessageDialog(null, "��½�����Ϊ��");
				else{
					try {
						int i = ftputil.connectServer(textField.getText(),textField2.getText(),textField3.getText(),Integer.valueOf(textField4.getText()));
						if(i==1){
							JOptionPane.showMessageDialog(null,"���ӳɹ���");
							String dir = "F:\\ftpserver";
							//System.out.println(dir);
							ArrayList<File> list = ftputil.showFile(dir);
							DefaultListModel<String> dim = new DefaultListModel<String>();//����Jlistģ��
							for (File file : list) {
								dim.addElement(file.getPath());
							}
							jList.setModel(dim);
							button.setEnabled(true);
							button2.setEnabled(false);
							textField.setEnabled(false);
							textField2.setEnabled(false);
							textField3.setEnabled(false);
							textField4.setEnabled(false);
							}
						else {
							JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		button3.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				if(ftputil.socket==null)
					JOptionPane.showMessageDialog(null,"δ��½�����½������");
				else{
				String dir = "F:\\ftpserver";
				//System.out.println(dir);
				ArrayList<File> list = ftputil.showFile(dir);
				/*
				StringBuffer sBuffer = new StringBuffer();
                for (File file : list) {
					sBuffer.append(file.getAbsolutePath()+"\n");
				}
                textArea.setText(sBuffer.toString());*/
				DefaultListModel<String> dim = new DefaultListModel<String>();//����Jlistģ��
				for (File file : list) {
					dim.addElement(file.getPath());
				}
				jList.setModel(dim);
				}}
			
		});
	}

}
