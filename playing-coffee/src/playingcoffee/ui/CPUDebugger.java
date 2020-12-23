package playingcoffee.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import playingcoffee.core.CPU;
import playingcoffee.core.MMU;
import playingcoffee.core.Registers;

import javax.swing.JCheckBox;

public class CPUDebugger {

	private JFrame frmCpuDebugger;
	private JList<String> registerList;
	
	private JCheckBox chckbxZero;
	private JCheckBox chckbxNegative;
	private JCheckBox chckbxHalfCarry;
	private JCheckBox chckbxCarry;
	
	private MMU mmu;
	private CPU cpu;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CPUDebugger window = new CPUDebugger();
					window.frmCpuDebugger.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CPUDebugger() {
		initialize();
		
		mmu = new MMU();
		cpu = new CPU(mmu);
		
		mmu.loadBootRom();
		
		updateValues();
	}
	
	private void updateValues() {
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.addElement("AF: 0x" + Integer.toHexString(cpu.getRegisters().readAF()));
		model.addElement("BC: 0x" + Integer.toHexString(cpu.getRegisters().readBC()));
		model.addElement("DE: 0x" + Integer.toHexString(cpu.getRegisters().readDE()));
		model.addElement("HL: 0x" + Integer.toHexString(cpu.getRegisters().readHL()));
		model.addElement("PC: 0x" + Integer.toHexString(cpu.getRegisters().pc));
		model.addElement("SP: 0x" + Integer.toHexString(cpu.getRegisters().sp));
		
		chckbxZero.setSelected(cpu.getRegisters().getFlag(Registers.FLAG_Z));
		chckbxNegative.setSelected(cpu.getRegisters().getFlag(Registers.FLAG_N));
		chckbxHalfCarry.setSelected(cpu.getRegisters().getFlag(Registers.FLAG_H));
		chckbxCarry.setSelected(cpu.getRegisters().getFlag(Registers.FLAG_C));
		
		registerList.setModel(model);
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		frmCpuDebugger = new JFrame();
		frmCpuDebugger.setTitle("CPU Debugger");
		frmCpuDebugger.setBounds(100, 100, 350, 350);
		frmCpuDebugger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnStep = new JButton("Step");
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do { cpu.cycle(); } while (cpu.getCycles() != 0);
				updateValues();
			}
		});
		
		JButton btnReset = new JButton("Reset");
		
		JLabel lblRegisters = new JLabel("Registers:");
		
		registerList = new JList<String>();
		
		chckbxZero = new JCheckBox("Zero");
		chckbxNegative = new JCheckBox("Negative");
		chckbxHalfCarry = new JCheckBox("Half Carry");
		chckbxCarry = new JCheckBox("Carry");
		
		GroupLayout groupLayout = new GroupLayout(frmCpuDebugger.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblRegisters)
						.addComponent(btnStep)
						.addComponent(registerList, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
							.addComponent(btnReset)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxZero)
								.addComponent(chckbxNegative)
								.addComponent(chckbxHalfCarry)
								.addComponent(chckbxCarry))
							.addGap(73))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblRegisters)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(registerList, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
							.addGap(51)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnReset)
								.addComponent(btnStep)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxZero)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxNegative)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxHalfCarry)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxCarry)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		frmCpuDebugger.getContentPane().setLayout(groupLayout);
	}
}