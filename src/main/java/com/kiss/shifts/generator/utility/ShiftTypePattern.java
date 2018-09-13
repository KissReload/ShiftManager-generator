package com.kiss.shifts.generator.utility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.kiss.shifts.utils.ShiftsType;

public class ShiftTypePattern  extends LinkedList<ShiftsType>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ShiftTypePattern (){
		super();
	}
	
	public ShiftTypePattern (List<ShiftsType> list){
		this.addAll(list);
	}
	
	public static ShiftTypePattern fromArray(ShiftsType... shiftsTypes){
		return new ShiftTypePattern(Arrays.asList(shiftsTypes));
	}

}
