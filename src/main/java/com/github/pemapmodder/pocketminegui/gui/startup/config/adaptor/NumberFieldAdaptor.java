package com.github.pemapmodder.pocketminegui.gui.startup.config.adaptor;

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

import lombok.AllArgsConstructor;

import javax.swing.JTextField;

@AllArgsConstructor
public class NumberFieldAdaptor implements OptionAdaptor<Number>{
	private JTextField field;

	@Override
	public Number getOption(){
		String text = field.getText();
		double dbl = Double.parseDouble(text);
		if(Integer.MIN_VALUE <= dbl && dbl <= Integer.MAX_VALUE){
			return (int) dbl;
		}
		return dbl;
	}
}
