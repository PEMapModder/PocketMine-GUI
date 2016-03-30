package com.github.pemapmodder.pocketminegui.utils;

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

import org.apache.commons.lang3.ArrayUtils;

import java.util.Iterator;

public class Ring<T> implements Iterable<T>{
	private T[] array;
	/**
	 * This is an array offset
	 */
	private int start = 0;
	/**
	 * This is an internal offset
	 */
	private int size = 0;

	public Ring(T[] buffer){
		array = buffer.clone();
	}

	public void add(T t){
		if(size < array.length){
			array[size++] = t;
		}else{
			array[start++] = t;
		}
	}

	public T get(int offset){
		if(offset >= size){
			throw new IndexOutOfBoundsException();
		}
		return array[internalOffset(offset)];
	}

	private int internalOffset(int offset){
		return (offset + start) % array.length;
	}

	@Override
	public Iterator<T> iterator(){
		return new RingIterator();
	}

	private class RingIterator implements Iterator<T>{
		private T[] backup;
		private int start;
		private int next = 0;

		public RingIterator(){
			assert start == 0 || size == array.length;
			backup = ArrayUtils.subarray(array, 0, size);
			start = Ring.this.start;
		}

		@Override
		public boolean hasNext(){
			return next < backup.length;
		}

		@Override
		public T next(){
			T ret = backup[(next + start) % backup.length];
			next++; // I really want to put it in the same assigument, but then it would become next+++start...
			return ret;
		}
	}
}
