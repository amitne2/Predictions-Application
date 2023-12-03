package grid;

public class Position {

    private int rowLocation;
    private int colLocation;

    public Position(int row, int col) {
        this.rowLocation = row;
        this.colLocation = col;
    }

    public int getColLocation() {
        return colLocation;
    }

    public int getRowLocation() {
        return rowLocation;
    }

    public void setLocation(Position position) {
        this.rowLocation = position.rowLocation;
        this.colLocation = position.colLocation;
    }

}
