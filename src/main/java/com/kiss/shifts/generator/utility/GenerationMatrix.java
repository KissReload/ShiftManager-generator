package com.kiss.shifts.generator.utility;

import java.util.Arrays;
import java.util.List;

import com.kiss.shifts.utils.ShiftsType;

public class GenerationMatrix {

	public static final List<ShiftTypePattern> matrix = Arrays.asList( 
			
			ShiftTypePattern.fromArray(ShiftsType.M, ShiftsType.M, ShiftsType.M, ShiftsType.R,
			ShiftsType.N, ShiftsType.N, ShiftsType.N, ShiftsType.LN, ShiftsType.R, ShiftsType.R, ShiftsType.P,
			ShiftsType.P, ShiftsType.P, ShiftsType.R, ShiftsType.R ),

			ShiftTypePattern.fromArray( ShiftsType.P, ShiftsType.R, ShiftsType.R, ShiftsType.M, ShiftsType.M, ShiftsType.M, ShiftsType.R,
					ShiftsType.N, ShiftsType.N, ShiftsType.N, ShiftsType.LN, ShiftsType.R, ShiftsType.R, ShiftsType.P,
					ShiftsType.P ),

			ShiftTypePattern.fromArray( ShiftsType.R, ShiftsType.P, ShiftsType.P, ShiftsType.P, ShiftsType.R, ShiftsType.R, ShiftsType.M,
					ShiftsType.M, ShiftsType.M, ShiftsType.R, ShiftsType.N, ShiftsType.N, ShiftsType.N, ShiftsType.LN,
					ShiftsType.R ),

			ShiftTypePattern.fromArray( ShiftsType.N, ShiftsType.LN, ShiftsType.R, ShiftsType.R, ShiftsType.P, ShiftsType.P, ShiftsType.P,
					ShiftsType.R, ShiftsType.R, ShiftsType.M, ShiftsType.M, ShiftsType.M, ShiftsType.R, ShiftsType.N,
					ShiftsType.N ),
			
			ShiftTypePattern.fromArray( ShiftsType.R, ShiftsType.N, ShiftsType.N, ShiftsType.N, ShiftsType.LN, ShiftsType.R, ShiftsType.R,
					ShiftsType.P, ShiftsType.P, ShiftsType.P, ShiftsType.R, ShiftsType.R, ShiftsType.M, ShiftsType.M,
					ShiftsType.M )

	);

}
