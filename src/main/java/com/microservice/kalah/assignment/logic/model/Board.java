package com.microservice.kalah.assignment.logic.model;

import com.microservice.kalah.assignment.logic.GameConfiguration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Board {

	private final List<BoardCell> cells;

	@Getter
	private PlayerEnum turn;

	
	private boolean finished = false;

	private final int numberOfPits;


	public Board() {
		numberOfPits = GameConfiguration.getInstance().getPitsCount();
		cells = new ArrayList<>();
		int total = 2 * numberOfPits;
		IntStream.range(0, total)
				.mapToObj(BoardCell::makeRelevantBoardCell)
				.forEach(cells::add);

		for (int index = 0; index < total; index++) {
			BoardCell currentCell = cells.get(index);
			int nextIndex = (index + 1) % (total);
			int oppositeIndex = total - 2 - index;
			currentCell.setNextCell(cells.get(nextIndex));
			if (currentCell.isHome()) {
				continue;
			}
			currentCell.setOppositeCell(cells.get(oppositeIndex));
		}

		this.turn = PlayerEnum.PLAYER1;
	}

	public Board(List<Integer> cells, String turn) {
		this();
		for (int index = 0; index < this.cells.size(); index++) {
			int stoneCount = cells.get(index);
			this.cells.get(index).setStoneCount(stoneCount);
		}
		this.turn = PlayerEnum.valueOf(turn.toUpperCase());
	}

	public BoardCell getCellByIndex(int index) {
		return cells.get(index);
	}

	public void toggleTurn() {
		turn = turn.toggle();
	}


	public BoardCell getHome(PlayerEnum player) {
		if (player == PlayerEnum.PLAYER1) {
			return getCellByIndex(numberOfPits - 1);
		}
		return getCellByIndex(2 * numberOfPits - 1);
	}

	public boolean isFinished() {
		boolean side1IsEmpty = getBoardCellStreamOwnedBy(PlayerEnum.PLAYER1).allMatch(it -> it.isEmpty());
		boolean side2IsEmpty = getBoardCellStreamOwnedBy(PlayerEnum.PLAYER2).allMatch(it -> it.isEmpty());
		return side1IsEmpty || side2IsEmpty;
	}

	public void terminateBoard() {
		int side1StoneCount = getBoardCellStreamOwnedBy(PlayerEnum.PLAYER1)
				.map(it -> it.getStoneCount())
				.reduce(0, Integer::sum);
		int side2StoneCount = getBoardCellStreamOwnedBy(PlayerEnum.PLAYER2)
				.map(it -> it.getStoneCount())
				.reduce(0, Integer::sum);

		getHome(PlayerEnum.PLAYER1).incrementStoneBy(side1StoneCount);
		getHome(PlayerEnum.PLAYER2).incrementStoneBy(side2StoneCount);

		getBoardCellStreamOwnedBy(PlayerEnum.PLAYER1).forEach(it -> it.emptyStones());
		getBoardCellStreamOwnedBy(PlayerEnum.PLAYER2).forEach(it -> it.emptyStones());

		finished = true;

	}

	private Stream<BoardCell> getBoardCellStreamOwnedBy(PlayerEnum player) {
		int start = (player == PlayerEnum.PLAYER1) ? 0 : numberOfPits;
		int offset = start + numberOfPits - 1;
		return IntStream.range(start, offset)
				.mapToObj(cells::get);
	}

	public List<Integer> getCells() {
		return cells.stream().map(it -> it.getStoneCount())
				.collect(Collectors.toList());
	}

	public Stream<BoardCell> getNextFillableBordCellsStreamForPlayer(BoardCell boardCell, PlayerEnum player, int count) {
		Stream.Builder<BoardCell> builder = Stream.builder();
		BoardCell next = boardCell;
		int counter = 0;
		while (true) {
			next = next.getNextCell();
			if (next.isHome() && next.getOwner().equals(player.toggle())) {
				continue;
			}
			builder.add(next);
			if (++counter == count) {
				break;
			}
		}
		return builder.build();
	}

	public Map<String, String> getCellsStatus(){
		return cells.stream().collect(Collectors.toMap(o -> String.valueOf(cells.indexOf(o)+1), boardCell -> String.valueOf(boardCell.getStoneCount())));
	}

}
