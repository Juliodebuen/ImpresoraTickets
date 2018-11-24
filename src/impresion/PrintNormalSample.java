package impresion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jpos.*;
import jpos.util.JposPropertiesConst;

public class PrintNormalSample{
	public static void main(String args[]){
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "src/impresion/jpos.xml");
		PrintNormalFrame pnf;
		pnf = new PrintNormalFrame();
		pnf.setVisible(true);
	}
}

class PrintNormalFrame extends Frame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button printButton = null;
	Button closeButton = null;
	POSPrinter ptr = null;
	
	PrintNormalFrame(){
		int width = 250;
		int height = 70;
		
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		this.setLayout(fl);
		printButton = new Button("PrintNormal");
		printButton.addActionListener(this);
		closeButton = new Button("Close");
		closeButton.addActionListener(this);
		this.add(printButton);
		this.add(closeButton);
		this.setTitle("PrintNormal Sample");
		Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();		
		this.setSize(width, height);
		this.setLocation(dimen.width/2-width/2, dimen.height/2 - height/2);		

		String logicalName = "SRP-350plusIII";
		
		ptr = new POSPrinter();
		try {
			ptr.open(logicalName);
			ptr.claim(0);
			ptr.setDeviceEnabled(true);
		} catch (JposException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == printButton){
			try {
				byte ESCSquence[] = new byte[]{0x1B, 0x7C};
				
				ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "---------------------------\r\n");
				ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "JavaPOS POSPrinter\n");
				ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "Sample Program\n");
				ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "---------------------------\r\n");
				//	Feed 7 lines
				ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, new String(ESCSquence) + "7lF");
				ptr.cutPaper(2);
			} catch (JposException jposEvent) {
				jposEvent.printStackTrace();
			}
		}
		else if(e.getSource() == closeButton)
			try {
				ptr.setDeviceEnabled(false);
				ptr.release();
				ptr.close();
				System.exit(0);
			} catch (JposException e1) {
				e1.printStackTrace();
				System.exit(0);				
			}			
	}
}
