package scale;

import exception.AutoException;
import model.Automobile;

public class EditOption extends Thread {
	/* Automobile to edit */
	private Automobile editAuto;
	
	/* Option to edit*/
	private int editOptionEnum;
	
	/* Arguments for operation
	 * args[0] - modelName
	 * args[1] - optionSet name
	 * args[2] - new optionSet name or option name
	 * args[3] - new option price*/
	private String[] args;
	
	/* Required thread ID */
	private int threadID;
	
	/* EditOption() constructor */
	
	public EditOption(int threadID, Automobile editAuto,
			EditOptionEnum editOptionEnum, String[] args){
		this.editAuto = editAuto;
		this.threadID = threadID;
		this.editOptionEnum = editOptionEnum.getValue();
		this.args = args;
	}
	
	/* Function for thread, using editOptionEnum, choose what to do */
	
	public void run(){
		switch(editOptionEnum){
		case 1:
			ThreadUpdateOptionSetName();
			break;
		case 2:
			
			try{
			ThreadUpdateOptionPrice();
		}catch (NumberFormatException | AutoException ae){
			System.out.println("Error ..." + ae.toString());
		}
			break;
		default:
				break;
		}
	}
	
	/* ThreadOptionSetName - update option set name */
	public void ThreadUpdateOptionSetName(){
		
		/* Update option set name testing synchronization between threads*/
		String[] ThreadOptionSetName = {args[1], "Op1", "Op2", "Op3", "Op4",
				"Op5", "Op6", "Op7", args[1]};
		
		synchronized(editAuto){
			for (int i = 0; i<ThreadOptionSetName.length; i++){
				/* Wait for a random amount of time between each check*/
				randomWait();
				
				try{
					/* Update option Set name
					 * [i] is the old name
					 * [i+1] is the new name*/
					
					editAuto.updateOptionSetName(ThreadOptionSetName[i],
							ThreadOptionSetName[i+1]);
					System.out.println("Thread" + threadID +":" + "Change Option Set from"
							+ ThreadOptionSetName[i] + "To" + 
							editAuto.getOptionSetName(ThreadOptionSetName[i+1]));
				}catch (AutoException ae){
					System.out.println("Thread" + threadID +"Error ..." + ae.toString());
				}
				
			}
		
		}
		
	}
	
	/* ThreadUpdateOptionPrice - update the option price*/
	
	public void ThreadUpdateOptionPrice() throws NumberFormatException, AutoException {
		synchronized(editAuto){
			for (int i = 0; i < 10; i++){
				randomWait();
				/*args[1] is old option set name, args[2] is new name, args[3] is new price*/
				
				editAuto.updateOptionPrice(args[1], args[2], i* Float.parseFloat(args[3]));
				System.out.println("Thread" + threadID + " " + args[1] + " "
						+ args[2] + "Price: " + editAuto.getOptionPrice(args[1], args[2]));
			}
		}
		
	}
	

	public void randomWait(){
		try {
			Thread.currentThread();
			Thread.sleep((long) (3000 * Math.random()));
		}catch (InterruptedException e){
			System.out.println("Interrrupted!");
		}
	}
	
}
