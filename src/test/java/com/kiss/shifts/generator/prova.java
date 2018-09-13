package com.kiss.shifts.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class prova {

	public static void main(String[] args) {
		String p="1,2";
		
		List<String> pippo =  Arrays.asList(p.split(","));
		System.out.println(pippo);
		
		System.out.println(HashMap.class);
		
		Map<String,Object> c= new HashMap<>();
				
		System.out.println(c.getClass());

	}

}
