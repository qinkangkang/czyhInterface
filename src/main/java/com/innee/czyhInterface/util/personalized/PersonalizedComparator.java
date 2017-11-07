package com.innee.czyhInterface.util.personalized;

import java.util.Comparator;

import com.innee.czyhInterface.dto.m.RecommendedRankingDTO;

public class PersonalizedComparator implements Comparator<RecommendedRankingDTO> {

	@Override
	public int compare(RecommendedRankingDTO rdA, RecommendedRankingDTO rdB) {
		if (rdA.getScore() > rdB.getScore()) {
			return -1;
		} else if (rdA.getScore() < rdB.getScore()) {
			return 1;
		} else {
			return 0;
		}
	}
}