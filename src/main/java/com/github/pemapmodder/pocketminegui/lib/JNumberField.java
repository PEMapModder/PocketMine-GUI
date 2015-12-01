package com.github.pemapmodder.pocketminegui.lib;

/*
 * This file is part of PocketMine-GUI.
 *
 * PocketMine-GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PocketMine-GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PocketMine-GUI.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * From <a href="http://stackoverflow.com/a/10927828">StackOverflow</a>
 * @author Rin
 */
public class JNumberField extends JTextField{
	private static final char DOT = '.';
	private static final char NEGATIVE = '-';
	private static final String BLANK = "";
	private static final int DEF_PRECISION = 2;

	public static final int NUMERIC = 2;
	public static final int DECIMAL = 3;

	public static final String FM_NUMERIC = "0123456789";
	public static final String FM_DECIMAL = FM_NUMERIC + DOT;

	private int maxLength = 0;
	private int format = NUMERIC;
	private String negativeChars = BLANK;
	private String allowedChars = null;
	private boolean allowNegative = false;
	private int precision = 0;

	protected PlainDocument numberFieldFilter;

	public JNumberField(){
		this(10, NUMERIC);
	}

	public JNumberField(int maxLen){
		this(maxLen, NUMERIC);
	}

	public JNumberField(int maxLen, int format){
		setAllowNegative(true);
		setMaxLength(maxLen);
		setFormat(format);

		numberFieldFilter = new JNumberFieldFilter();
		super.setDocument(numberFieldFilter);
	}

	public void setMaxLength(int maxLen){
		if(maxLen > 0){
			maxLength = maxLen;
		}else{
			maxLength = 0;
		}
	}

	public int getMaxLength(){
		return maxLength;
	}

	public void setPrecision(int precision){
		if(format == NUMERIC){
			return;
		}
		if(precision >= 0){
			this.precision = precision;
		}else{
			this.precision = DEF_PRECISION;
		}
	}

	public int getPrecision(){
		return precision;
	}

	public Number getNumber(){
		if(format == NUMERIC){
			return new Integer(getText());
		}
		return new Double(getText());
	}

	public void setNumber(Number value){
		setText(String.valueOf(value));
	}

	public int getInt(){
		return Integer.parseInt(getText());
	}

	public void setInt(int value){
		setText(String.valueOf(value));
	}

	public float getFloat(){
		return new Float(getText());
	}

	public void setFloat(float value){
		setText(String.valueOf(value));
	}

	public double getDouble(){
		return new Double(getText());
	}

	public void setDouble(double value){
		setText(String.valueOf(value));
	}

	public int getFormat(){
		return format;
	}

	public void setFormat(int format){
		switch(format){
			case NUMERIC:
			default:
				this.format = NUMERIC;
				precision = 0;
				allowedChars = FM_NUMERIC;
				break;

			case DECIMAL:
				this.format = DECIMAL;
				precision = DEF_PRECISION;
				allowedChars = FM_DECIMAL;
				break;
		}
	}

	public void setAllowNegative(boolean value){
		allowNegative = value;

		if(value){
			negativeChars = "" + NEGATIVE;
		}else{
			negativeChars = BLANK;
		}
	}

	public boolean isAllowNegative(){
		return allowNegative;
	}

	@Override
	public void setDocument(Document document){
	}

	class JNumberFieldFilter extends PlainDocument{
		public JNumberFieldFilter(){
			super();
		}

		@Override
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException{
			String text = getText(0, offset) + str + getText(offset, getLength() - offset);

			if(str == null){
				return;
			}

			for(int i = 0; i < str.length(); i++){
				if((allowedChars + negativeChars).indexOf(str.charAt(i)) == -1){
					return;
				}
			}

			int precisionLength = 0, dotLength = 0, minusLength = 0;
			int textLength = text.length();

			try{
				if(format == NUMERIC){
					if(!(text.equals(negativeChars) && text.length() == 1)){
						new Long(text);
					}
				}else if(format == DECIMAL){
					if(!(text.equals(negativeChars) && text.length() == 1)){
						new Double(text);
					}

					int dotIndex = text.indexOf(DOT);
					if(dotIndex != -1){
						dotLength = 1;
						precisionLength = textLength - dotIndex - dotLength;

						if(precisionLength > precision){
							return;
						}
					}
				}
			}catch(Exception ex){
				return;
			}

			if(text.startsWith("" + NEGATIVE)){
				if(!allowNegative){
					return;
				}else{
					minusLength = 1;
				}
			}

			if(maxLength < textLength - dotLength - precisionLength - minusLength){
				return;
			}

			super.insertString(offset, str, attr);
		}
	}
}