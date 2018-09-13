package com.kiss.shifts.generator;

import static com.kiss.shifts.generator.utility.GenerationMatrix.matrix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.kiss.shifts.utils.Shift;
import com.kiss.shifts.utils.ShiftWorker;
import com.kiss.shifts.utils.ShiftsType;

public class prove {

	public static void main(String[] args) {
		List<ShiftWorker> workers = getWorkers();

		Collections.shuffle(workers);

		System.out.println(
				"-----------------------------------------------------------------------------------------------------------");

		workers = assignShifts(workers);

		System.out.println(
				"-----------------------------------------------------------------------------------------------------------");

		workers = assignShifts(workers);
		System.out.println(
				"-----------------------------------------------------------------------------------------------------------");

		workers = assignShifts(workers);
		System.out.println(
				"-----------------------------------------------------------------------------------------------------------");
		workers = assignShifts(workers);

		workers.stream().map(worker -> {
			return String.format("%s: %s", String.format("%1$15s", worker.getName()),
					worker.getShifts().stream().map(shift -> String.format("%1$4s", shift.getType().getSymbol()))
							.collect(Collectors.joining(", ")));
		}).forEach(str -> {
			System.out.println(str);
		});

	}



	public static List<ShiftWorker> getWorkers() {
		List<ShiftWorker> workers = new ArrayList<>();
		workers.add(new ShiftWorker("Marco"));
		workers.add(new ShiftWorker("Luigi"));
		workers.add(new ShiftWorker("Maria"));
		workers.add(new ShiftWorker("Rocco"));
		workers.add(new ShiftWorker("Thomas"));
		workers.add(new ShiftWorker("Gennaro"));
		workers.add(new ShiftWorker("Topolino"));
		workers.add(new ShiftWorker("Pippo"));
		workers.add(new ShiftWorker("Pluto"));
		workers.add(new ShiftWorker("Paperino"));
		workers.add(new ShiftWorker("Claudio"));
		workers.add(new ShiftWorker("Daniele"));

		return workers;
	}

}


