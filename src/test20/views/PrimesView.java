package test20.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import javax.inject.Inject;


public class PrimesView extends ViewPart implements NumCallBack {
	public static final String ID = "test20.views.SampleView";
	private Table primeTable;
	private Table SGTable;
	private int state;	// PRIME_NUM or SG_NUM
	private final int PRIME_NUM = 1;
	private final int SG_NUM = 2;
	private String[] buttons = {"Простые числа", "Числа Софи Жермен", "Стоп", "Очистить"};
	private final int ACT_PRIMES = 0;
	private final int ACT_SGPRIMES = 1;
	private final int ACT_STOP = 2;
	private final int ACT_CLEAR = 3;
	PrimeNumbers num = new PrimeNumbers(); 			// prime numbers calculator
	PrimeNumbersSG numSG = new PrimeNumbersSG(); 	// Sophie Germain prime numbers 
	@Inject IWorkbench workbench;
	private TableViewer viewer;
	private Action doubleClickAction;
	 
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		num.registerCallback(this); 	// prime numbers callback
		numSG.registerCallBack(this);	// SG prime numbers callback
		// Calculate items
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER_SOLID | SWT.FULL_SELECTION);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(buttons);
		viewer.setLabelProvider(new ViewLabelProvider());
		// Table
		primeTable = new Table(parent, SWT.BORDER);
		SGTable = new Table(parent, SWT.BORDER);
	    TableColumn tc1 = new TableColumn(primeTable, SWT.CENTER);
	    TableColumn tc2 = new TableColumn(SGTable, SWT.CENTER);
	    tc1.setText("Простые числа");
	    tc2.setText("Числа Софи Жермен");
	    tc1.setWidth(150);
	    tc2.setWidth(150);
	    primeTable.setHeaderVisible(true);
	    SGTable.setHeaderVisible(true);
	    // Actions
	    workbench.getHelpSystem().setHelp(viewer.getControl(), "test10-e3view.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookDoubleClickAction();
	}
	
	// Item has been clicked
	private void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				if (obj.toString().equals(buttons[ACT_PRIMES])) { // prime numbers
					if (!num.getIsStopped()) {
						return;	// in process
					}
					primeTable.removeAll();
					SGTable.removeAll();
					state = PRIME_NUM;
					Thread t = new Thread(num);
					t.run();
				}
				if (obj.toString().equals(buttons[ACT_SGPRIMES])) { // Sophie Germain prime numbers
					if (!num.getIsStopped()) {
						return;	// in process
					}
					primeTable.removeAll();
					SGTable.removeAll();
					state = SG_NUM;
					Thread t = new Thread(num);
					t.run();
				}
				if (obj.toString().equals(buttons[ACT_STOP])) { // stop computing
					num.stopAll();
				}
				if (obj.toString().equals(buttons[ACT_CLEAR])) { // clear all
					num.stopAll();
					primeTable.removeAll();
					SGTable.removeAll();
				}
			}
		};
	}
	
	// Got prime number
	public void callingBack(int number) {
		TableItem item = new TableItem(primeTable, SWT.NONE);
		item.setText(Integer.toString(number));
	    if (state==SG_NUM) {
	    	numSG.setNumberToCheck(number);
	    	Thread tSG = new Thread(numSG);
	    	tSG.run();
	    }
	}
	
	// Got Sophie Germain number
	public void callingBackSG(int number) {
		String numStr = Integer.toString(number);
		TableItem item = new TableItem(SGTable, SWT.NONE);
		item.setText(numStr);
		// Remove from table
		int count = primeTable.getItemCount();
		for (int i=0; i<count; i++) {
			if (primeTable.getItem(i).getText().equals(numStr)) {
				System.out.println(item.getText(i) + " == " + Integer.toString(number) + " -- " + i);
				primeTable.remove(i);
				return;
			}
		}
	}
	
	// Set listener
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
