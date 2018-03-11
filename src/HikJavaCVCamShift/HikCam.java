package HikJavaCVCamShift;

import java.awt.EventQueue;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import HikJavaCVCamShift.HCNetSDK.FRealDataCallBack_V30;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.util.Date;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class HikCam extends javax.swing.JFrame {

	//private JFrame frame;

	static HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
	static PlayCtrl playCtrl = PlayCtrl.INSTANCE;

	HCNetSDK.NET_DVR_IPPARACFG m_strIpparacfg; // IP����
	HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceinfo;// �豸��Ϣ
	HCNetSDK.NET_DVR_CLIENTINFO m_strClientinfo;// �û�����

	boolean bRealPlay;// �Ƿ�Ԥ��
	String m_sDeviceIP;// �ѵ�¼�豸IP

	NativeLong lUserID;// �û����
	NativeLong lPreviewHandle;// Ԥ�����
	NativeLongByReference m_lPort;// �ص�Ԥ��ʱ���ſ�˿�ָ��

	FRealDataCallBack fRealDataCallBack;
	
	int m_iTreeNodeNum;//ͨ�����ڵ���Ŀ
	DefaultMutableTreeNode m_DeviceRoot;//ͨ�������ڵ�
	
	// Variables declaration - do not modify 
	                    
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonRealPlay;
    private javax.swing.JComboBox jComboBoxCallback;
    private javax.swing.JLabel jLabelIPAddress;
    private javax.swing.JLabel jLabelPassWord;
    private javax.swing.JLabel jLabelPortNumber;
    private javax.swing.JLabel jLabelUserName;
    private javax.swing.JMenuBar jMenuBarConfig;
    private javax.swing.JMenu jMenuConfig;
    //private javax.swing.JMenuItem jMenuItemAlarmCfg;
    private javax.swing.JMenuItem jMenuItemBasicConfig;
    private javax.swing.JMenuItem jMenuItemChannel;
    private javax.swing.JMenuItem jMenuItemCheckTime;
    private javax.swing.JMenuItem jMenuItemDefault;
    private javax.swing.JMenuItem jMenuItemDeviceState;
    //private javax.swing.JMenuItem jMenuItemFormat;
    private javax.swing.JMenuItem jMenuItemIPAccess;
    private javax.swing.JMenuItem jMenuItemNetwork;
    private javax.swing.JMenuItem jMenuItemPlayBackRemote;
    private javax.swing.JMenuItem jMenuItemPlayTime;
    private javax.swing.JMenuItem jMenuItemReboot;
    //private javax.swing.JMenuItem jMenuItemRemoveAlarm;
    //private javax.swing.JMenuItem jMenuItemSerialCfg;
    private javax.swing.JMenuItem jMenuItemShutDown;
    //private javax.swing.JMenuItem jMenuItemUpgrade;
    private javax.swing.JMenuItem jMenuItemUserConfig;
    //private javax.swing.JMenuItem jMenuItemVoiceCom;
    private javax.swing.JMenu jMenuManage;
    private javax.swing.JMenu jMenuPlayBack;
    //private javax.swing.JMenu jMenuSetAlarm;
    //private javax.swing.JMenu jMenuVoice;
    private javax.swing.JPanel jPanelRealplayArea;
    private javax.swing.JPanel jPanelUserInfo;
    private javax.swing.JPasswordField jPasswordFieldPassword;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuListen;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuSetAlarm;
    private javax.swing.JScrollPane jScrollPaneTree;
    //private javax.swing.JScrollPane jScrollPanelAlarmList;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPaneHorizontal;
    private javax.swing.JSplitPane jSplitPaneVertical;
    //private javax.swing.JTable jTableAlarm;
    private javax.swing.JTextField jTextFieldIPAddress;
    private javax.swing.JTextField jTextFieldPortNumber;
    private javax.swing.JTextField jTextFieldUserName;
    private javax.swing.JTree jTreeDevice;
    private java.awt.Panel panelRealplay;
    // End of variables declaration  

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				boolean initSuc = hcNetSDK.NET_DVR_Init();

				if (!initSuc) {
					JOptionPane.showMessageDialog(null, "��ʼ��ʧ��!");
					//System.out.println(playCtrl);
				}
				
				HikCam window = new HikCam();				
				centerWindow(window);
				window.setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HikCam() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);// ��ֹ�����Ŵ���awt����
		initialize();
		lUserID=new NativeLong(-1);
		lPreviewHandle=new NativeLong(-1);
		m_lPort=new NativeLongByReference(new NativeLong(-1));
		fRealDataCallBack=new FRealDataCallBack();
		m_iTreeNodeNum=0;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	/**
	 * 
	 */
	private void initialize() {
		//frame = new JFrame();
		//frame.setBounds(100, 100, 450, 300);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jSplitPaneHorizontal = new javax.swing.JSplitPane();
        jPanelUserInfo = new javax.swing.JPanel();
        jButtonRealPlay = new javax.swing.JButton();
        jButtonLogin = new javax.swing.JButton();
        jLabelUserName = new javax.swing.JLabel();
        jLabelIPAddress = new javax.swing.JLabel();
        jTextFieldPortNumber = new javax.swing.JTextField();
        jTextFieldIPAddress = new javax.swing.JTextField();
        jLabelPortNumber = new javax.swing.JLabel();
        jLabelPassWord = new javax.swing.JLabel();
        jPasswordFieldPassword = new javax.swing.JPasswordField();
        jTextFieldUserName = new javax.swing.JTextField();
        jButtonExit = new javax.swing.JButton();
        jScrollPaneTree = new javax.swing.JScrollPane();
        jTreeDevice = new javax.swing.JTree();
        jComboBoxCallback = new javax.swing.JComboBox();
        jSplitPaneVertical = new javax.swing.JSplitPane();
        jPanelRealplayArea = new javax.swing.JPanel();
        panelRealplay = new java.awt.Panel();
        //jScrollPanelAlarmList = new javax.swing.JScrollPane();
        //jTableAlarm = new javax.swing.JTable();
        jMenuBarConfig = new javax.swing.JMenuBar();
        jMenuConfig = new javax.swing.JMenu();
        jMenuItemBasicConfig = new javax.swing.JMenuItem();
        jMenuItemNetwork = new javax.swing.JMenuItem();
        jMenuItemChannel = new javax.swing.JMenuItem();
        //jMenuItemAlarmCfg = new javax.swing.JMenuItem();
        //jMenuItemSerialCfg = new javax.swing.JMenuItem();
        jMenuItemUserConfig = new javax.swing.JMenuItem();
        jMenuItemIPAccess = new javax.swing.JMenuItem();
        jMenuPlayBack = new javax.swing.JMenu();
        jMenuItemPlayBackRemote = new javax.swing.JMenuItem();
        jMenuItemPlayTime = new javax.swing.JMenuItem();
        //jMenuSetAlarm = new javax.swing.JMenu();
        jRadioButtonMenuSetAlarm = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuListen = new javax.swing.JRadioButtonMenuItem();
        //jMenuItemRemoveAlarm = new javax.swing.JMenuItem();
        jMenuManage = new javax.swing.JMenu();
        jMenuItemCheckTime = new javax.swing.JMenuItem();
        //jMenuItemFormat = new javax.swing.JMenuItem();
        //jMenuItemUpgrade = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemReboot = new javax.swing.JMenuItem();
        jMenuItemShutDown = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemDefault = new javax.swing.JMenuItem();
        jMenuItemDeviceState = new javax.swing.JMenuItem();
        //jMenuVoice = new javax.swing.JMenu();
        //jMenuItemVoiceCom = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HikCameraShift");
        setFont(new java.awt.Font("����", 0, 10));

        jSplitPaneHorizontal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSplitPaneHorizontal.setDividerLocation(155);
        jSplitPaneHorizontal.setDividerSize(2);

        jPanelUserInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 255, 255), null));

        jButtonRealPlay.setText("Ԥ��");
        jButtonRealPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRealPlayActionPerformed(evt);
            }
        });

        jButtonLogin.setText("ע��");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jLabelUserName.setText("�û���");

        jLabelIPAddress.setText("IP��ַ");

        jTextFieldPortNumber.setText("8000");

        jTextFieldIPAddress.setText("192.168.1.65");

        jLabelPortNumber.setText("�˿�");

        jLabelPassWord.setText("����");

        jPasswordFieldPassword.setText("Aa11111111");

        jTextFieldUserName.setText("admin");

        jButtonExit.setText("�˳�");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jTreeDevice.setModel(this.initialTreeModel());
        jScrollPaneTree.setViewportView(jTreeDevice);

        jComboBoxCallback.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ֱ��Ԥ��", "�ص�Ԥ��" }));

        javax.swing.GroupLayout jPanelUserInfoLayout = new javax.swing.GroupLayout(jPanelUserInfo);
        jPanelUserInfo.setLayout(jPanelUserInfoLayout);
        jPanelUserInfoLayout.setHorizontalGroup(
            jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelIPAddress)
                .addGap(14, 14, 14)
                .addComponent(jTextFieldIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelUserName)
                .addGap(14, 14, 14)
                .addComponent(jTextFieldUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelPassWord)
                .addGap(26, 26, 26)
                .addComponent(jPasswordFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRealPlay))
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelPortNumber)
                .addGap(26, 26, 26)
                .addComponent(jTextFieldPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelUserInfoLayout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addContainerGap())
            .addComponent(jScrollPaneTree, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxCallback, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanelUserInfoLayout.setVerticalGroup(
            jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUserInfoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelIPAddress)
                    .addComponent(jTextFieldIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelUserName)
                    .addComponent(jTextFieldUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPassWord)
                    .addComponent(jPasswordFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPortNumber)
                    .addComponent(jTextFieldPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLogin)
                    .addComponent(jButtonRealPlay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneTree, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxCallback, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jButtonExit)
                .addContainerGap())
        );

        jSplitPaneHorizontal.setLeftComponent(jPanelUserInfo);

        jSplitPaneVertical.setDividerLocation(579);
        jSplitPaneVertical.setDividerSize(2);
        jSplitPaneVertical.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanelRealplayArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 255, 102)));

        panelRealplay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRealplayMousePressed(evt);
            }
        });

        javax.swing.GroupLayout panelRealplayLayout = new javax.swing.GroupLayout(panelRealplay);
        panelRealplay.setLayout(panelRealplayLayout);
        panelRealplayLayout.setHorizontalGroup(
            panelRealplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 704, Short.MAX_VALUE)
        );
        panelRealplayLayout.setVerticalGroup(
            panelRealplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelRealplayAreaLayout = new javax.swing.GroupLayout(jPanelRealplayArea);
        jPanelRealplayArea.setLayout(jPanelRealplayAreaLayout);
        jPanelRealplayAreaLayout.setHorizontalGroup(
            jPanelRealplayAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRealplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelRealplayAreaLayout.setVerticalGroup(
            jPanelRealplayAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRealplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPaneVertical.setTopComponent(jPanelRealplayArea);

        //jScrollPanelAlarmList.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        //jTableAlarm.setModel(this.initialTableModel());
        //jScrollPanelAlarmList.setViewportView(jTableAlarm);

        //jSplitPaneVertical.setRightComponent(jScrollPanelAlarmList);

        jSplitPaneHorizontal.setRightComponent(jSplitPaneVertical);

        jMenuBarConfig.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jMenuConfig.setText("����");

        jMenuItemBasicConfig.setText("������Ϣ");
        jMenuItemBasicConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemBasicConfigMousePressed(evt);
            }
        });
        jMenuConfig.add(jMenuItemBasicConfig);

        jMenuItemNetwork.setText("�������");
        jMenuItemNetwork.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemNetworkMousePressed(evt);
            }
        });
        jMenuConfig.add(jMenuItemNetwork);

        jMenuItemChannel.setText("ͨ������");
        jMenuItemChannel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemChannelMousePressed(evt);
            }
        });
        jMenuConfig.add(jMenuItemChannel);

        //jMenuItemAlarmCfg.setText("��������");
        //jMenuItemAlarmCfg.addMouseListener(new java.awt.event.MouseAdapter() {
         //   public void mousePressed(java.awt.event.MouseEvent evt) {
          //      jMenuItemAlarmCfgMousePressed(evt);
           // }
       // });
        //jMenuConfig.add(jMenuItemAlarmCfg);

//        jMenuItemSerialCfg.setText("���ڲ���");
//        jMenuItemSerialCfg.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                jMenuItemSerialCfgMousePressed(evt);
//            }
//        });
//        jMenuConfig.add(jMenuItemSerialCfg);

        jMenuItemUserConfig.setText("�û�����");
        jMenuItemUserConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemUserConfigMousePressed(evt);
            }
        });
        jMenuConfig.add(jMenuItemUserConfig);

        jMenuItemIPAccess.setText("IP��������");
        jMenuItemIPAccess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIPAccessActionPerformed(evt);
            }
        });
        jMenuConfig.add(jMenuItemIPAccess);

        jMenuBarConfig.add(jMenuConfig);

        jMenuPlayBack.setText("�ط�");

        jMenuItemPlayBackRemote.setText("���ļ�");
        jMenuItemPlayBackRemote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemPlayBackRemoteMousePressed(evt);
            }
        });
        jMenuPlayBack.add(jMenuItemPlayBackRemote);

        jMenuItemPlayTime.setText("��ʱ��");
        jMenuItemPlayTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemPlayTimeMousePressed(evt);
            }
        });
        jMenuPlayBack.add(jMenuItemPlayTime);

        jMenuBarConfig.add(jMenuPlayBack);

        //jMenuSetAlarm.setBorder(null);
        //jMenuSetAlarm.setText("����");

        jRadioButtonMenuSetAlarm.setText("������");
        jRadioButtonMenuSetAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuSetAlarmActionPerformed(evt);
            }
        });
        //jMenuSetAlarm.add(jRadioButtonMenuSetAlarm);

        jRadioButtonMenuListen.setText("������");
        jRadioButtonMenuListen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuListenActionPerformed(evt);
            }
        });
        //jMenuSetAlarm.add(jRadioButtonMenuListen);

        //jMenuItemRemoveAlarm.setText("��ձ�����Ϣ");
        //jMenuItemRemoveAlarm.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                jMenuItemRemoveAlarmMousePressed(evt);
//            }
//        });
//        jMenuSetAlarm.add(jMenuItemRemoveAlarm);
//
//        jMenuBarConfig.add(jMenuSetAlarm);

        jMenuManage.setText("����");

        jMenuItemCheckTime.setText("Уʱ");
        jMenuItemCheckTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemCheckTimeMousePressed(evt);
            }
        });
        jMenuManage.add(jMenuItemCheckTime);

//        jMenuItemFormat.setText("��ʽ��");
//        jMenuItemFormat.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                jMenuItemFormatMousePressed(evt);
//            }
//        });
//        jMenuManage.add(jMenuItemFormat);
//
//        jMenuItemUpgrade.setText("����");
//        jMenuItemUpgrade.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                jMenuItemUpgradeMousePressed(evt);
//            }
//        });
        //jMenuManage.add(jMenuItemUpgrade);
        jMenuManage.add(jSeparator1);

        jMenuItemReboot.setText("����");
        jMenuItemReboot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemRebootMousePressed(evt);
            }
        });
        jMenuManage.add(jMenuItemReboot);

        jMenuItemShutDown.setText("�ر�");
        jMenuItemShutDown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemShutDownMousePressed(evt);
            }
        });
        jMenuManage.add(jMenuItemShutDown);
        jMenuManage.add(jSeparator2);

        jMenuItemDefault.setText("�ָ�Ĭ�ϲ���");
        jMenuItemDefault.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemDefaultMousePressed(evt);
            }
        });
        jMenuManage.add(jMenuItemDefault);

        jMenuItemDeviceState.setText("�豸״̬");
        jMenuItemDeviceState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemDeviceStateMousePressed(evt);
            }
        });
        jMenuManage.add(jMenuItemDeviceState);

        jMenuBarConfig.add(jMenuManage);

        /*jMenuVoice.setText("����");

        jMenuItemVoiceCom.setText("�����Խ�");
        jMenuItemVoiceCom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItemVoiceComMousePressed(evt);
            }
        });*/
        //jMenuVoice.add(jMenuItemVoiceCom);

        //jMenuBarConfig.add(jMenuVoice);

        setJMenuBar(jMenuBarConfig);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneHorizontal, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneHorizontal, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold> 

	protected void jMenuItemCheckTimeMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jRadioButtonMenuListenActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jRadioButtonMenuSetAlarmActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemPlayTimeMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemPlayBackRemoteMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemIPAccessActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemUserConfigMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemChannelMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemNetworkMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemBasicConfigMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}
	//��������ע�ᡱ������Ӧ����
	//ע���¼�豸
	private void jButtonLoginActionPerformed(ActionEvent evt) {
		//ע��֮ǰ��������ע���û���Ԥ������²���ע��
	       if (bRealPlay)
	        {
	            JOptionPane.showMessageDialog(this, "ע�����û�����ֹͣ��ǰԤ��!");
	            return;
	        }

	        if (lUserID.longValue() > -1)
	        {
	            //��ע��
	            hcNetSDK.NET_DVR_Logout_V30(lUserID);
	            lUserID = new NativeLong(-1);
	            m_iTreeNodeNum = 0;
	            m_DeviceRoot.removeAllChildren();
	        }
	        //ע��
	        m_sDeviceIP = jTextFieldIPAddress.getText();//�豸ip��ַ
	        m_strDeviceinfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
	        int iPort = Integer.parseInt(jTextFieldPortNumber.getText());
	        lUserID = hcNetSDK.NET_DVR_Login_V30(m_sDeviceIP,
	                (short) iPort, jTextFieldUserName.getText(), new String(jPasswordFieldPassword.getPassword()), m_strDeviceinfo);

	        long userID = lUserID.longValue();
	        if (userID == -1)
	        {
	            m_sDeviceIP = "";//��¼δ�ɹ�,IP��Ϊ��
	            JOptionPane.showMessageDialog(HikCam.this, "ע��ʧ��");
	        }
	        else
	        {
	            CreateDeviceTree();
	        }
		
	}

	protected void jMenuItemRebootMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemShutDownMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemDefaultMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	protected void jMenuItemDeviceStateMousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	public DefaultTreeModel initialTreeModel() {
        m_DeviceRoot = new DefaultMutableTreeNode("Device");
        DefaultTreeModel myDefaultTreeModel = new DefaultTreeModel(m_DeviceRoot);//ʹ�ø��ڵ㴴��ģ��
        return myDefaultTreeModel;
	}
	/**
	 * "���Ŵ���"˫����Ӧ����
	 * ˫��ȫ��Ԥ����ǰԤ��ͨ��
	 * @param evt
	 */

	protected void panelRealplayMousePressed(MouseEvent evt) {
		if (!bRealPlay) {
			return;			
		}
		//��굥���¼�Ϊ˫��
		if (evt.getClickCount()==2) {
			//�½�JWindowȫ��Ԥ��
			final JWindow wndJWindow=new JWindow();
			//��ȡ��Ļ�ߴ�
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			wndJWindow.setSize(screenSize);
			wndJWindow.setVisible(true);
			
			final HWND hwnd=new HWND(Native.getComponentPointer(wndJWindow));
			m_strClientinfo.hPlayWnd=hwnd;
			final NativeLong lRealHandle=hcNetSDK.NET_DVR_RealPlay_V30(lUserID, m_strClientinfo, null, null, true);
			
			//JWindow ����˫����Ӧ������˫��ʱֹͣԤ�����˳�ȫ��
			wndJWindow.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent evt){
					if (evt.getClickCount()==2) {
						//ֹͣԤ��
						hcNetSDK.NET_DVR_StopRealPlay(lRealHandle);
						wndJWindow.dispose();
					}
				}
			});
		}
		
		
	}

	protected void jButtonExitActionPerformed(ActionEvent evt) {
        //�����Ԥ��,��ֹͣԤ��, �ͷž��
        if (lPreviewHandle.longValue() > -1)
        {
           hcNetSDK.NET_DVR_StopRealPlay(lPreviewHandle);
//           if(framePTZControl != null)
//           {
//            framePTZControl.dispose();
//           }
        }
//        if (lListenHandle.intValue() != -1)
//        {
//            hcNetSDK.NET_DVR_StopListen_V30(lListenHandle);
//        }

        //����Ѿ�ע��,ע��
        if (lUserID.longValue() > -1)
        {
            hcNetSDK.NET_DVR_Logout_V30(lUserID);
        }
        //cleanup SDK
        hcNetSDK.NET_DVR_Cleanup();
        this.dispose();
	}

	/***
	 * ������"Ԥ��"��ť������Ӧ����
	 * ������������ȡͨ���ţ��򿪲��Ŵ��ڣ���ʼ��ͨ��Ԥ��
	 * @param evt
	 */
	private void jButtonRealPlayActionPerformed(ActionEvent evt) {
		System.out.println(panelRealplay.getWidth());
		System.out.println(panelRealplay.getHeight());
		if (lUserID.intValue()==-1) {
			JOptionPane.showMessageDialog(this, "����ע��");
			return;			
		}
		//���Ԥ������û�򿪣�����Ԥ��
		if (!bRealPlay) {
			//��ȡ���ھ��
			HWND hwnd=new HWND(Native.getComponentPointer(panelRealplay));
			
			//��ȡͨ����
			int iChannelNum =getChannelNumber();//ͨ����
			if (iChannelNum==-1) {
				JOptionPane.showMessageDialog(this, "��ѡ��ҪԤ����ͨ��");
				return;				
			}
			
			m_strClientinfo=new HCNetSDK.NET_DVR_CLIENTINFO();
			m_strClientinfo.lChannel=new NativeLong(iChannelNum);
			
			//�ڴ��ж��Ƿ�ص�Ԥ����0�����ص���1���ص�
			if (jComboBoxCallback.getSelectedIndex()==0) {
				m_strClientinfo.hPlayWnd=hwnd;
				lPreviewHandle=hcNetSDK.NET_DVR_RealPlay_V30(lUserID, m_strClientinfo, null, null, true);
				
			}else if (jComboBoxCallback.getSelectedIndex()==1) {
				m_strClientinfo.hPlayWnd=null;
				lPreviewHandle=hcNetSDK.NET_DVR_RealPlay_V30(lUserID, m_strClientinfo, fRealDataCallBack, null, true);
				
			}
			
			long previewSucValue=lPreviewHandle.longValue();
			
			//Ԥ��ʧ��ʱ��
			if (previewSucValue==-1) {
				JOptionPane.showMessageDialog(this, "Ԥ��ʧ��");
				return;				
			}
			//Ԥ���ɹ�:
			jButtonRealPlay.setText("ֹͣ");
			bRealPlay=true;			
		}else {//�����Ԥ����ֹͣԤ�����رմ���
			hcNetSDK.NET_DVR_StopRealPlay(lPreviewHandle);
			jButtonRealPlay.setText("Ԥ��");
			bRealPlay=false;
			if (m_lPort.getValue().intValue()!=-1) {
				playCtrl.PlayM4_Stop(m_lPort.getValue());
				m_lPort.setValue(new NativeLong(-1));				
			}
			panelRealplay.repaint();
			
		}
		
	}

	//���豸����ȡͨ����
	private int getChannelNumber() {

		int iChannelNum=-1;
		TreePath tp=jTreeDevice.getSelectionPath();//��ȡѡ�нڵ�·��
		if (tp!=null) {//�ж�·���Ƿ���Ч����ͨ���Ƿ�ѡ��
			String sChannelName=((DefaultMutableTreeNode)tp.getLastPathComponent()).toString();
			if (sChannelName.charAt(0)=='C') { //Camera��ͷ��ʾģ��ͨ��
				//���ַ����л�ȡͨ����
				iChannelNum=Integer.parseInt(sChannelName.substring(6));//Camera 01
								
			}else {
				if (sChannelName.charAt(0)=='I') {//IPCamera��ͷ��ʾIPͨ��
					iChannelNum=Integer.parseInt(sChannelName.substring(8))+32;
				}else {
					return -1;
				}
			}			
		}else {
			return -1;
		}
		return iChannelNum;
	}
	
    /*************************************************
    ����:    centerWindow
    ��������:��������
     *************************************************/
  public static void centerWindow(Container window) {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int w = window.getSize().width;
    int h = window.getSize().height;
    int x = (dim.width - w) / 2;
    int y = (dim.height - h) / 2;
    window.setLocation(x, y);
  }
	
	 /*************************************************
    ����:    CreateDeviceTree
    ��������:�����豸ͨ����
*************************************************/
  private void CreateDeviceTree()
    {
	  IntByReference ibrBytesReturned = new IntByReference(0);//��ȡIP�������ò���
        boolean bRet = false;

        m_strIpparacfg = new HCNetSDK.NET_DVR_IPPARACFG();
        m_strIpparacfg.write();
        Pointer lpIpParaConfig = m_strIpparacfg.getPointer();
        bRet = hcNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0), lpIpParaConfig, m_strIpparacfg.size(), ibrBytesReturned);
        m_strIpparacfg.read();

        DefaultTreeModel TreeModel = ((DefaultTreeModel) jTreeDevice.getModel());//��ȡ��ģ��
        if (!bRet)
        {
            //�豸��֧��,���ʾû��IPͨ��
            for (int iChannum = 0; iChannum < m_strDeviceinfo.byChanNum; iChannum++)
            {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Camera" + (iChannum + m_strDeviceinfo.byStartChan));
                TreeModel.insertNodeInto(newNode, m_DeviceRoot,iChannum);
            }
        }
            else
        {
            //�豸֧��IPͨ��
            for (int iChannum = 0; iChannum < m_strDeviceinfo.byChanNum; iChannum++)
            {
                if(m_strIpparacfg.byAnalogChanEnable[iChannum] == 1)
                {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Camera" + (iChannum + m_strDeviceinfo.byStartChan));
                TreeModel.insertNodeInto(newNode, m_DeviceRoot,m_iTreeNodeNum);
                m_iTreeNodeNum ++;
                }
            }
            for(int iChannum =0; iChannum < HCNetSDK.MAX_IP_CHANNEL; iChannum++)
             if (m_strIpparacfg.struIPChanInfo[iChannum].byEnable == 1)
             {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("IPCamera" + (iChannum + m_strDeviceinfo.byStartChan));
                TreeModel.insertNodeInto(newNode, m_DeviceRoot, m_iTreeNodeNum);
             }
        }
                TreeModel.reload();//����ӵĽڵ���ʾ������
                jTreeDevice.setSelectionInterval(1, 1);//ѡ�е�һ���ڵ�
    }
	

	/**
	 * �ڲ��ࣺFRealDataCallBack ʵ��Ԥ���ص�����
	 * 
	 * @author PC
	 *
	 */
	class FRealDataCallBack implements FRealDataCallBack_V30 {

		// Ԥ���ص�
		@Override
		public void invoke(NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) {
			HWND hwnd = new HWND(Native.getComponentPointer(panelRealplay));
			switch (dwDataType) {
			case HCNetSDK.NET_DVR_SYSHEAD:// ϵͳͷ
				if (!playCtrl.PlayM4_GetPort(m_lPort)) {// ��ȡ���ſ�δʹ��ͨ����
					break;
				}
				if (dwBufSize > 0) {
					if (!playCtrl.PlayM4_SetStreamOpenMode(m_lPort.getValue(), PlayCtrl.STREAME_REALTIME)) {// ����ʵʱ������ģʽ
						break;
					}
					if (!playCtrl.PlayM4_OpenStream(m_lPort.getValue(),	pBuffer, dwBufSize, 1024 * 1024)) {// �����ӿ�
						break;
					}
					if (!playCtrl.PlayM4_Play(m_lPort.getValue(), hwnd)) {// ���ſ�ʼ
						break;
					}
				}
			case HCNetSDK.NET_DVR_STREAMDATA:// ��������
				if ((dwBufSize > 0) && (m_lPort.getValue().intValue() != -1)) {
					if (!playCtrl.PlayM4_InputData(m_lPort.getValue(), pBuffer,	dwBufSize)) {// ������
						break;
					}
				}
			}
		}
	}

}
