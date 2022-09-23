package SolverRunner;
/*
* The part of the program that stitches everything together and runs it.
*/
import FileLoader.ReadFile;
import SudokuSolver.Solver;

public class Main {
    public static void main(String[] args) {
        ReadFile rf = new ReadFile("src/sudoku.txt");
        String txt = rf.getConvertedContents();
        String test = "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
        String easy = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
        String hard = ".....6....59.....82....8....45........3........6..3.54...325..6..................";
        Solver s = new Solver(txt);


    }
}
