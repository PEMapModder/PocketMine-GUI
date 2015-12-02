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

import lombok.Getter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JNumberFilter extends PlainDocument{
	@Getter protected String acceptedChars = null;
	@Getter protected boolean negativeAccepted = false;

	public JNumberFilter(){
		acceptedChars = "0123456789";
	}

	public JNumberFilter setNegativeAccepted(boolean negativeAccepted){
		this.negativeAccepted = negativeAccepted;
		return this;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException{
		if(str == null){
			return;
		}
		for(int i = 0; i < str.length(); i++){
			if(!acceptedChars.contains(String.valueOf(str.charAt(i)))){
				return;
			}
		}
		if(negativeAccepted && str.contains("-")){
			if(str.indexOf("-") != 0 || offset != 0){
				return;
			}
		}
		super.insertString(offset, str, attr);
	}
}
