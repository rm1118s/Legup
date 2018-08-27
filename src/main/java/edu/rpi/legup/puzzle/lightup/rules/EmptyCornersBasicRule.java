package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmptyCornersBasicRule extends BasicRule
{

    public EmptyCornersBasicRule()
    {
        super("Empty Corners",
                "Cells on the corners of a number must be empty if placing bulbs would prevent the number from being satisfied.",
                "edu/rpi/legup/images/lightup/rules/EmptyCorners.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement index of the puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        LightUpBoard initialBoard = (LightUpBoard)transition.getParents().get(0).getBoard();
        LightUpCell cell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpBoard finalBoard = (LightUpBoard)transition.getBoard();
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);

        if(!(cell.getType() == LightUpCellType.UNKNOWN && finalCell.getType() == LightUpCellType.EMPTY))
        {
            return "Must be an empty cell";
        }

        Point loc = finalCell.getLocation();
        List<LightUpCell> numberedCells = new ArrayList<>();
        LightUpCell upperRight = finalBoard.getCell(loc.x + 1, loc.y - 1);
        if(upperRight != null && upperRight.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(upperRight);
        }
        LightUpCell upperLeft = finalBoard.getCell(loc.x - 1, loc.y - 1);
        if(upperLeft != null && upperLeft.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(upperLeft);
        }
        LightUpCell lowerRight = finalBoard.getCell(loc.x + 1, loc.y + 1);
        if(lowerRight != null && lowerRight.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(lowerRight);
        }
        LightUpCell lowerLeft = finalBoard.getCell(loc.x - 1, loc.y + 1);
        if(lowerLeft != null && lowerLeft.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(lowerLeft);
        }
        if(numberedCells.isEmpty()) {
            return "There must be a numbered cell diagonal to this cell.";
        }

        TooFewBulbsContradictionRule tooFew = new TooFewBulbsContradictionRule();
        LightUpBoard bulbCaseBoard = finalBoard.copy();
        LightUpCell bulbCaseCell = (LightUpCell) bulbCaseBoard.getPuzzleElement(puzzleElement);
        bulbCaseCell.setData(LightUpCellType.BULB.value);
        bulbCaseBoard.fillWithLight();
        TreeTransition bulbTran = new TreeTransition(bulbCaseBoard);
        boolean createsContra = false;
        for(LightUpCell c : numberedCells) {
            createsContra |= tooFew.checkContradictionAt(bulbTran, c) == null;
        }
        if(createsContra) {
            return null;
        } else {
            return "Cell is not forced to be empty";
        }
    }

    private boolean isForcedEmpty(LightUpBoard board, LightUpCell cell, Point loc)
    {
        if(cell == null || cell.getType() != LightUpCellType.NUMBER)
        {
            return false;
        }

        Point cellLoc = cell.getLocation();

        if(cellLoc.x == loc.x - 1)
        {
            LightUpCell checkCell = null;
        }
        else
        {

        }

        int bulbs = 0;
        int bulbsNeeded = cell.getData();
        cell = board.getCell(cellLoc.x + 1, cellLoc.y);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(cellLoc.x, cellLoc.y + 1);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(cellLoc.x - 1, cellLoc.y);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(cellLoc.x, cellLoc.y - 1);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        return bulbs == bulbsNeeded;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard lightUpBoard = (LightUpBoard)node.getBoard().copy();
        return null;
    }
}
