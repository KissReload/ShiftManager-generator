package com.kiss.shifts.generator.executors;

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
import java.util.stream.LongStream;

import com.kiss.shifts.generator.utility.ShiftTypePattern;
import com.kiss.shifts.utils.Shift;
import com.kiss.shifts.utils.ShiftWorker;
import com.kiss.shifts.utils.ShiftsType;

public class ShiftGenerator {

	/**
	 * Return the level of restriction of the last Shift of the worker N
	 * ShiftType is the most restricitive F, R, CDS and MAL are the most not
	 * restricitive ShiftType
	 * 
	 * @param worker
	 * @return The level of restriction of the last Shift of the worker
	 */
	public static Integer getRestrictionLevel(ShiftWorker worker) {
		ShiftsType last = worker.getShifts().isEmpty() ? ShiftsType.R
				: worker.getShifts().get(worker.getShifts().size() - 1).getType();

		switch (last) {
		case N:
			return 10;
		case P:
			return 9;
		case M:
			return 8;
		case F:
		case R:
		case CDS:
		case MAL:
			return 7;
		default:
			return 0;
		}
	}

	/**
	 * Elaborate and assign new shifts to the workers list
	 * 
	 * @param workers
	 * @return the updated list of workers
	 */
	public static List<ShiftWorker> assignShifts(List<ShiftWorker> workers, int attempt) {
		Map<Object, Integer> assignationCounters = initializeAssigniationsCounters(matrix);
		final List<ShiftWorker> sortedWorkers = sortWorkers(workers);
		List<ShiftWorker> workersAssigned = new ArrayList<>();

		try {
			workersAssigned = sortedWorkers.stream().map(worker -> {

				List<ShiftTypePattern> assignablePatterns = getAssignablePatterns(worker, matrix);

				ShiftTypePattern patternToAssign = getPatternToAssign(assignationCounters, sortedWorkers, worker,
						assignablePatterns);

				worker.setShifts(appendPattern(worker, patternToAssign));

				incrementAssigiationCounter(assignationCounters, patternToAssign);

				return worker;
			}).collect(Collectors.toList());
		} catch (WrongWayException e) {
			if (attempt > 0)
				return assignShifts(sortedWorkers, attempt - 1);
			else
				throw e;
		}

		return workersAssigned;

	}

	/**
	 * Elaborate and assign new shifts to the workers list
	 * 
	 * @param workers
	 * @return the updated list of workers
	 */
	public static List<ShiftWorker> assignShifts(List<ShiftWorker> workers) {
		return assignShifts(workers, 10);
	}

	/**
	 * Create and initialize a map that contains a counter of assigniations for
	 * each pattern
	 * 
	 * @param matrix
	 *            - list of all patterns
	 * @return a map that contains a counter of assigniations for each pattern
	 */
	private static Map<Object, Integer> initializeAssigniationsCounters(List<ShiftTypePattern> matrix) {
		Map<Object, Integer> assignationCounters = new HashMap<>();

		matrix.stream().forEach(pattern -> {
			assignationCounters.put(pattern, 0);
		});
		return assignationCounters;
	}

	/**
	 * Sort the list if workers based on his last generated shift
	 * 
	 * @param workers
	 *            the list of workers
	 * @return the list of workers sorted
	 */
	private static List<ShiftWorker> sortWorkers(List<ShiftWorker> workers) {
		return workers.stream().sorted((w1, w2) -> getRestrictionLevel(w2).compareTo(getRestrictionLevel(w1)))
				.collect(Collectors.toList());
	}

	/**
	 * Increment the counter on the assignation-counters map for the specific
	 * pattern
	 * 
	 * @param assignationCounters
	 * @param patternToAssign
	 */
	private static void incrementAssigiationCounter(Map<Object, Integer> assignationCounters,
			ShiftTypePattern patternToAssign) {
		assignationCounters.put(patternToAssign, assignationCounters.get(patternToAssign) + 1);
	}

	/**
	 * Generate new shifts based on the pattern and then append them to the
	 * worker shifts list
	 * 
	 * @param worker
	 * @param patternToAssign
	 * @return
	 */

	private static List<Shift> appendPattern(ShiftWorker worker, ShiftTypePattern patternToAssign) {
		List<Shift> shifts = patternToAssign.stream().map(type -> new Shift(Calendar.getInstance(), type))
				.collect(Collectors.toList());

		List<Shift> workerShifts = worker.getShifts();
		workerShifts.addAll(shifts);
		return workerShifts;
	}

	/**
	 * Retrieve a random pattern from the list of assignable patterns;
	 * 
	 * @param assignationCount
	 * @param sortedWorkers
	 * @param worker
	 * @param assignablePatterns
	 * @return
	 */
	private static ShiftTypePattern getPatternToAssign(Map<Object, Integer> assignationCount,
			final List<ShiftWorker> sortedWorkers, ShiftWorker worker, List<ShiftTypePattern> assignablePatterns) {

		List<ShiftTypePattern> probabilityList = createProbabilityList(sortedWorkers.size(), assignationCount,
				assignablePatterns);
		if (probabilityList.size() < 1)
			printError(assignationCount, sortedWorkers, worker, assignablePatterns);

		return probabilityList.get(new Random().nextInt(probabilityList.size()));
	}

	private static void printError(Map<Object, Integer> assignationCount, final List<ShiftWorker> sortedWorkers,
			ShiftWorker worker, List<ShiftTypePattern> assignablePatterns) {
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<< errore >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		System.out.println(String.format("Assignable Patterns: %s", assignablePatterns));
		System.out.println(String.format("Assignation Count: %s", assignationCount));
		System.out.println(String.format("Sorted Workers: %s", sortedWorkers.size()));
		System.out.println(String.format("Workers Shifts: %s",
				worker.getShifts().stream().map(x -> x.getType()).collect(Collectors.toList())));
		createProbabilityList(sortedWorkers.size(), assignationCount, assignablePatterns);
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		throw new WrongWayException();
	}

	/**
	 * Calculate the list of patterns that can be assigned to the worker
	 * 
	 * @param worker
	 *            - the worke who the pattern will be assigned
	 * @param patternList
	 *            - complete list of possible patterns
	 * @return
	 */

	public static List<ShiftTypePattern> getAssignablePatterns(ShiftWorker worker, List<ShiftTypePattern> patternList) {
		return patternList.parallelStream().filter(getFilter(worker.getShifts()))// Filter
																					// assignable
																					// pattern
				.collect(Collectors.toList());
	}

	/**
	 * Generate a list, with an occurrency of each pattern multiplied for the
	 * probability of each to be assigned to a worker
	 * 
	 * @param workersCount
	 *            Total number of workers
	 * @param assignationCount
	 *            Contains all the times of each patter has been assigned
	 * @param assignablePatterns
	 *            List of patterns that cal be assigned
	 * @return
	 */
	public static List<ShiftTypePattern> createProbabilityList(Integer workersCount,
			Map<Object, Integer> assignationCount, List<ShiftTypePattern> assignablePatterns) {
		List<ShiftTypePattern> probabilityList = new ArrayList<>();

		assignablePatterns.stream().forEach(pattern -> {

			int totAssign = assignationCount.entrySet().stream().mapToInt((e) -> e.getValue()).sum();
			long probability = getProbability(workersCount, matrix.size(), assignationCount.get(pattern), totAssign);
			LongStream.range(0, probability).forEach(n -> {
				probabilityList.add(pattern);
			});
		});

		Collections.shuffle(probabilityList);
		return probabilityList;

	}

	/**
	 * Calculate the probability that a pattern will be assigned to a worker
	 * 
	 * @param wokersCount
	 *            Total number of workers
	 * @param matrixSize
	 *            Totoal number of possible patterns
	 * @param assignationCount
	 *            Number of times that a specific patter has been assigned
	 * @param totalAssignationCount
	 *            Number of times that a patter has been assigned
	 * @return
	 */
	public static long getProbability(int wokersCount, int matrixSize, int assignationCount,
			int totalAssignationCount) {
		double r = ((double) wokersCount) / ((double) matrixSize);
		double p = ((Math.ceil(r) - assignationCount) * 100) / (wokersCount - totalAssignationCount);
		return p > 0 ? Math.round(p) : 0;
	}

	/**
	 * Create a function that filter assignables patterns based on worker's last
	 * shifts
	 * 
	 * @param lastShifts
	 *            List of the last worker's shift generated
	 * @returna function that filter Assignable pattern
	 */
	public static Predicate<ShiftTypePattern> getFilter(List<Shift> lastShifts) {

		ShiftTypePattern reverseShiftsList = lastShifts.stream().map(shift -> shift.getType())
				.collect(Collectors.toCollection(ShiftTypePattern::new));
		Collections.reverse(reverseShiftsList);

		switch (lastShifts.isEmpty() ? ShiftsType.F : lastShifts.get(lastShifts.size() - 1).getType()) {
		// If worker's last shift is F or R or CDS or MAL all patterns are
		// permitted
		case F:
		case R:
		case CDS:
		case MAL:
			return (ShiftTypePattern pattern) -> true;

		/*
		 * If worker's last shift is M : patterns that starts with M or P or N
		 * are permitted only if will be not generated a sequence of work day
		 * longer than five days, others patterns are always permitted
		 */
		case M:
			return (ShiftTypePattern pattern) -> {
				switch (pattern.get(0)) {
				case M:
				case P:
				case N:
					if (getContinuativeDays(pattern) + getContinuativeDays(reverseShiftsList) > 5)
						return false;
					return true;
				default:
					return true;
				}
			};

		/*
		 * If worker's last shift is P : patterns that starts with M are not
		 * permitted patterns that starts with P or N are permitted only if will
		 * be not generated a sequence of work day longer than five days, others
		 * patterns are always permitted
		 */
		case P:
			return (ShiftTypePattern pattern) -> {
				switch (pattern.get(0)) {
				case M:
					return false;
				case P:
				case N:
					if (getContinuativeDays(pattern) + getContinuativeDays(reverseShiftsList) > 5)
						return false;
					return true;
				default:
					return true;
				}
			};

		/*
		 * If worker's last shift is N : patterns that starts with M or P or R
		 * are not permitted patterns N or LN are permitted only if will be not
		 * generated a sequence of work day longer than five days, others
		 * patterns are always permitted
		 */
		case N:
			return (ShiftTypePattern pattern) -> {
				switch (pattern.get(0)) {
				case M:
				case P:
				case R:
					return false;
				case N:
				case LN:
					if (getContinuativeDays(pattern) + getContinuativeDays(reverseShiftsList) > 5)
						return false;
					return true;
				default:
					return true;
				}
			};

		/*
		 * If worker's last shift is N : patterns that starts with LN are not
		 * permitted patterns M or P or N are permitted only if will be not
		 * generated a sequence of work day longer than five days, others
		 * patterns are always permitted
		 */
		case LN:
			return (ShiftTypePattern pattern) -> {
				switch (pattern.get(0)) {
				case LN:
					return false;
				case M:
				case P:
				case N:
					if (getContinuativeDays(pattern) + getContinuativeDays(reverseShiftsList) > 5)
						return false;
					return true;
				default:
					return true;
				}
			};

		default:
			// The type of the last work shift was not recognized
			throw new RuntimeException("Something went wrong o previous elaboartions");
		}

	}

	/**
	 * count the number of continuative work day from the start of the array;
	 * 
	 * @param shifts
	 *            - List of shifts
	 * @return the number of continuative work day
	 */
	public static Integer getContinuativeDays(ShiftTypePattern shifts) {
		Integer continuativeDays = 0;
		for (ShiftsType day : shifts) {
			if (ShiftsType.P == day || ShiftsType.N == day || ShiftsType.M == day || ShiftsType.LN == day) {
				continuativeDays++;
			} else {
				break;
			}
		}
		return continuativeDays;

	}

}

class WrongWayException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}