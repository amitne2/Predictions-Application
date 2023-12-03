package grid;

import entity.instance.EntityInstanceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridInstances {
    private int rows;
    private int cols;
    EntityInstanceImpl[][] entityPhysicalSpace;

    public GridInstances(int rows, int cols) {
        this.rows= rows;
        this.cols = cols;
        this.entityPhysicalSpace = new EntityInstanceImpl[rows][cols];
    }

    public boolean isThisPositionFree(int row, int col) {
        if(entityPhysicalSpace[row][col] == null)
            return true;
        else
            return false;
    }

    public void freeLocationOnGrid(Position position) {
        entityPhysicalSpace[position.getRowLocation()][position.getColLocation()] = null;
    }

    public Position randomLocationForInstanceOnGrid(Position positionOnGrid) {
        Random random = new Random();
        List<Position> availablePositions = getPotentialPosition(positionOnGrid);
        if(availablePositions.size() == 0)
            return positionOnGrid;
        else
            return availablePositions.get(availablePositions.size()-1);
       /* if(availablePositions.size() == 0)
            return positionOnGrid;
        else if (availablePositions.size() == 1)
                return availablePositions.get(0);
        else {
            int randomIndex = random.nextInt(availablePositions.size());
            return availablePositions.get(randomIndex);
        }*/
    }

    private List<Position> getPotentialPosition(Position position) {
        List<Position> potentialPosition = new ArrayList<>();
        int direction = 0;
        boolean isValidPosition;
        while(direction!=4) {
            int currRow = position.getRowLocation();
            int currCol = position.getColLocation();
            switch (direction) {
                case 0: //UP
                    if (currRow == 0) //round
                        currRow = rows - 1;
                    else
                        currRow = currRow - 1;

                    isValidPosition = isThisPositionFree(currRow, currCol);
                    if (isValidPosition) {
                        Position positionToAdd = new Position(currRow, currCol);
                        potentialPosition.add(positionToAdd);
                    }
                    break;
                case 1: //DOWN
                    if (currRow == rows - 1)
                        currRow = 0;
                    else
                        currRow = currRow + 1;

                    isValidPosition = isThisPositionFree(currRow, currCol);
                    if (isValidPosition) {
                        Position positionToAdd = new Position(currRow, currCol);
                        potentialPosition.add(positionToAdd);
                    }
                    break;
                case 2: //LEFT
                    if (currCol == 0)
                        currCol = cols - 1;
                    else
                        currCol = currCol - 1;

                    isValidPosition = isThisPositionFree(currRow, currCol);
                    if (isValidPosition) {
                        Position positionToAdd = new Position(currRow, currCol);
                        potentialPosition.add(positionToAdd);
                    }
                    break;
                case 3: //RIGHT
                    if (currCol == cols - 1)
                        currCol = 0;
                    else
                        currCol = currCol + 1;


                    isValidPosition = isThisPositionFree(currRow, currCol);
                    if (isValidPosition) {
                        Position positionToAdd = new Position(currRow, currCol);
                        potentialPosition.add(positionToAdd);
                    }
                    break;
            }
            direction++;
        }
        return potentialPosition;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public EntityInstanceImpl getInstanceOnPosition(int row, int col) {
        return entityPhysicalSpace[row][col];
    }

    public void setInstanceOnLocation(EntityInstanceImpl entityInstance) {
        int row = entityInstance.getPositionOnGrid().getRowLocation();
        int col = entityInstance.getPositionOnGrid().getColLocation();
        entityPhysicalSpace[row][col] = entityInstance;
    }
}
