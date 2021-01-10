package jigsaw;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class IntegerOnlyDocument extends PlainDocument
{
	private int mInputLimit = 0;
	
	public IntegerOnlyDocument(int inputLimit)
	{
		this.mInputLimit = inputLimit;
		return;
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a)
	{
		if(!this.lengthIsValid(str)){
			return;
		}
		if(!this.isNumeric(str)){
			return;
		}
		try{
			super.insertString(offs, str, a);
		}catch(BadLocationException ble){
			ble.printStackTrace();
		}
		return;
	}
	
	private boolean lengthIsValid(String input)
	{
		if(this.getLength() + input.length() <= this.mInputLimit){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isNumeric(String input)
	{
		try{
			Integer.parseInt(input);
			return true;
		}catch(Exception ex){
			
		}
		return false;
	}
}
