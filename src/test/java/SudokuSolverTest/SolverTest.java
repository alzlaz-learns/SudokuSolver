package SudokuSolverTest;

import SudokuSolver.Solver;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;

public class SolverTest {
    private final Solver solver = new Solver();

    @Test
    public void testSquareCount(){
        String columns = "123456789";
        String rows = "ABCDEFGHI";
        var countRes = solver.getSqCombinations().size();
        Assertions.assertEquals(81, countRes);
    }

    @Test
    public void testUnitListCount(){
        var countRes = solver.getUnitList().size();
        Assertions.assertEquals(27, countRes);
    }

    @Test
    public void testAllUnitCount(){
        solver.getUnits().keySet().stream().forEach((key) -> {
                Assertions.assertEquals(3, solver.getUnits().get(key).size());
            });
    }

    @Test
    public void testAllPeerCount(){
        solver.peers.keySet().stream().forEach((key)-> {
                Assertions.assertEquals(20, solver.peers.get(key).size());
            });
    }

}

