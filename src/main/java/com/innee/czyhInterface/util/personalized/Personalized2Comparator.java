package com.innee.czyhInterface.util.personalized;

import java.util.Comparator;

import com.innee.czyhInterface.dto.m.RecommendedRankingDTO;

public class Personalized2Comparator implements Comparator<RecommendedRankingDTO> {

	@Override
	public int compare(RecommendedRankingDTO rdA, RecommendedRankingDTO rdB) {
		if (rdA.getPrice().compareTo(rdB.getPrice())==1) {
			return -1;
		} else if (rdA.getPrice().compareTo(rdB.getPrice())==-1) {
			return 1;
		} else {
			return 0;
		}

	}
}