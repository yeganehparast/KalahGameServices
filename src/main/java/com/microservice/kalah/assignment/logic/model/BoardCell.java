package com.microservice.kalah.assignment.logic.model;

import com.microservice.kalah.assignment.logic.GameConfiguration;
import lombok.Getter;
import lombok.Setter;


@Getter
public class BoardCell {

	@Setter
	private int stoneCount;

	private final boolean isHome;

	private final PlayerEnum owner;

	@Setter
	private BoardCell nextCell;

	@Setter
	private BoardCell oppositeCell;

	private static final int numberOfPits = GameConfiguration.getInstance().getPitsCount();

	private static final int initialStonesCount = GameConfiguration.getInstance().getInitialStones();


	private BoardCell(int stoneCount, boolean isHome, PlayerEnum owner) {
		this.stoneCount = stoneCount;
		this.isHome = isHome;
		this.owner = owner;
	}

	public static BoardCell makeRelevantBoardCell(int index) {
		PlayerEnum owner = index < numberOfPits ? PlayerEnum.PLAYER1 : PlayerEnum.PLAYER2;
		boolean isHome = ((index % numberOfPits) == (numberOfPits - 1));
		int numberOfStones = isHome ? 0 : initialStonesCount;

		return new BoardCell(numberOfStones, isHome, owner);
	}


	public void incrementStoneCountByOne() {
		stoneCount++;
	}

	public void emptyStones() {
		stoneCount = 0;
	}

	public boolean isEmpty() {
		return stoneCount == 0;
	}

	public void incrementStoneBy(int number) {
		stoneCount += number;
	}

	public boolean isSingletone() {
		return stoneCount == 1;
	}
}
