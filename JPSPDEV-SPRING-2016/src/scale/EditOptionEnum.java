package scale;

/* Enumeration of all edit option we can choose*/

public enum EditOptionEnum {
	EditOptionSetName(1), EditOptionPrice(2);
	
	private int value;
	
	private EditOptionEnum(int value){
		this.setValue(value);
	}
	
	public int getValue(){
		return value;
	}
	
	private void setValue(int value){
		this.value = value;
	}
	

}
