/*
 *
 */

package org.inventivetalent.advancedslabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to help with the TabCompletion for Bukkit.
 *
 * @author D4rKDeagle
 */

class TabCompletionHelper {

	public static List<String> getPossibleCompletionsForGivenArgs(String[] args, String[] possibilitiesOfCompletion) {
		final String argumentToFindCompletionFor = args[args.length - 1];

		final List<String> listOfPossibleCompletions = new ArrayList<String>();
		for (int i = 0; i < possibilitiesOfCompletion.length; i++) {
			final String[] foundString = possibilitiesOfCompletion;
			try {
				if (foundString[i] != null && foundString[i].regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
					listOfPossibleCompletions.add(foundString[i]);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(listOfPossibleCompletions);

		return listOfPossibleCompletions;
	}

}
