package SudokuSolver;
/*
* A java Implementation of Peter Norvig's Sudoku Solver
* found here https://norvig.com/sudoku.html
*/
import java.util.*;

public class Solver {
    private final String digits = "123456789";
    private final String rows = "ABCDEFGHI";
    private String columns;
    private ArrayList<String> squares;
    private ArrayList<ArrayList<String>> unitList;
    private HashMap<String, ArrayList<ArrayList<String>>> units;
    public HashMap<String, ArrayList<String>> peers;
    public HashMap <String, ArrayList<Character>> res;

    public Solver(){
        this.columns = this.digits;
        this.squares = generateCombination(columns, rows);
        this.unitList = generateUnitList();
        this.units = generateUnits();
        this.peers = generatePeers();
    }

    public Solver(String inp){
//        System.out.println(inp);
        this.columns = this.digits;
        this.squares = generateCombination(columns, rows);
        this.unitList = generateUnitList();
        this.units = generateUnits();
        this.peers = generatePeers();
        this.res = search(parseGrid(inp));
        display(this.res);

    }
    public HashMap<String, ArrayList<ArrayList<String>>> getUnits(){
        /*
         *Getter function used for implementations of test and visualization of attributes
         */
        return this.units;
    }
    public ArrayList<String> getSqCombinations(){
        /*
         *Getter function used for implementations of test and visualization of attributes
         */
        return this.squares;
    }

    public ArrayList<ArrayList<String>> getUnitList(){
        /*
        *Getter function used for implementations of test and visualization of attributes
        */
        return this.unitList;
    }
    private ArrayList<String> generateCombination(String X, String Y){
        /*
        *  Method that takes 2 strings as parameters
        *  and returns an ArrayList<String> sqList
        *  e.g. generateCombination("A", "1") returns ["A1"]
        */
        ArrayList<String> sqList = new ArrayList<>();
        String sq;
        for (int y = 0; y < Y.length(); y++){
            for (int x = 0; x < X.length(); x++){
                sq = String.valueOf(Y.charAt(y)) + String.valueOf(X.charAt(x));
                sqList.add(sq);
            }
        }
        return sqList;
    }
    private ArrayList<ArrayList<String>> generateUnitList(){
        /*
        *  Method that takes no parameters
        *  calls the attributes
        */
        ArrayList<ArrayList<String>> generatedUnitList = new ArrayList<ArrayList<String>>();
        for (int r = 0; r < this.rows.length(); r++ ) {
            generatedUnitList.add(
                    generateCombination(
                            this.columns, String.valueOf(this.rows.charAt(r))));
        }
        for (int c = 0; c < this.columns.length(); c++ ) {
            generatedUnitList.add(
                    generateCombination(
                            String.valueOf(this.columns.charAt(c)), this.rows));
        }
        String[] boxRows = {"ABC", "DEF","GHI"};
        String[] boxColumns = {"123", "456", "789"};
        for(String r: boxRows){
            for(String c: boxColumns) {
                generatedUnitList.add(
                        generateCombination(c,r));
            }
        }
        return generatedUnitList;
    }
    private HashMap generateUnits(){
        HashMap<String, ArrayList<ArrayList<String>>> units = new HashMap<String, ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> valueList;

        for (String s: this.squares){
            valueList = new ArrayList<>();
            for(ArrayList<String> u: this.unitList){
                if (u.contains(s)) {
                    valueList.add(u);
                }
            }
            units.put(s, valueList);
        }
        return units;
    }
    private HashMap generatePeers(){
        HashMap<String, ArrayList<String>> peerMap = new HashMap<>();

        for (String s: this.squares){
            Set<String> peerSet = new HashSet<>();
            for(ArrayList<String> u: this.units.get(s)){
                peerSet.addAll(u);
            }
            peerSet.removeIf(i -> Objects.equals(i, s));
            peerMap.put(s, new ArrayList<>(peerSet));
        }

        return peerMap;
    }
    //creates a map to compare existing values to.
    private HashMap generatePossibleValues(){
        HashMap<String, ArrayList<Character>> vals = new HashMap<>();
        ArrayList<Character> digis = new ArrayList<>();
        for (char d : this.digits.toCharArray()) {
            digis.add(d);
        }
        for (String s : this.squares) {
            vals.put(s, digis);
        }
        return vals;
    }

    private HashMap eliminate(HashMap<String, ArrayList<Character>> values, String s, Character d){
        if(!values.get(s).contains(d)){
            return values;
        }
        ArrayList<Character> values2 = (ArrayList<Character>) values.get(s).clone();
        values2.remove(d);
        values.put(s, values2);
        //    ## (1) If a square s is reduced to one value d2, then eliminate d2 from the peers.
        if(values.get(s).size() == 0){
            return null;
        } else if (values.get(s).size() == 1) {
            Character d2 = values.get(s).get(0);
            for(String s2: this.peers.get(s)){
                if(eliminate(values, s2, d2) == null){
                    return null;
                }
            }
        }
        //## (2) If a unit u is reduced to only one place for a value d, then put it there.
        for(ArrayList<String> unit: this.units.get(s)){
            ArrayList<String> dPlaces = new ArrayList<>();
            for(String s2: unit){
                if(values.get(s2).contains(d)){
                    dPlaces.add(s2);
                }
            }
            if(dPlaces.size() == 0){
                return null;
            } else if (dPlaces.size() == 1) {
                if(assign(values, dPlaces.get(0), d)==null){
                    return null;
                }
            }
        }
        return values;
    }
    private HashMap assign(HashMap<String, ArrayList<Character>> values, String s, Character d) {
        ArrayList<Character> values2 = (ArrayList) values.get(s).clone();
        values2.remove(d);
        for (Character d2: values2){
            if(eliminate(values, s, d2)==null){
                return null;
            }
        }
        return values;
    }
    /*
    * utility method to display Sudoku grid
     */
    public void display(HashMap<String, ArrayList<Character>> values){
        int i = 1;
        for (String p: this.squares){
            if((i != 0) && (i % 27 == 0) && (i % 81 != 0)){
                System.out.println(values.get(p).get(0));
                System.out.println("------+-------+-------");
            }else if((i != 0) && (i % 9 == 0) && (i % 27 != 0)){
                System.out.print(values.get(p).get(0));
                System.out.println();
            }else if((i != 0) && (i % 3 == 0) && (i % 81 != 0)){
                System.out.print(values.get(p).get(0) + " | ");
            }
            else {
                System.out.print(values.get(p).get(0)+ " ");
            }
            i++;
        }
    }

    private HashMap parseGrid(String inp){
        HashMap <String, ArrayList<Character>> values = generatePossibleValues();
        HashMap <String, Character> inputMap = gridValues(inp);
        for (HashMap.Entry<String, Character> inputEntry: inputMap.entrySet()){
            if(this.digits.contains(Character.toString(inputEntry.getValue())) ) {
                if(assign(values, inputEntry.getKey(), inputEntry.getValue())==null) {
                    return null;
                }
            }
        }
        return values;
    }

    private HashMap<String, ArrayList<Character>> search(HashMap<String, ArrayList<Character>> values){
        if(values == null){
            return null;
        }
        boolean complete = true;
        for(ArrayList<Character> val: values.values()){
            if(val.size() != 1){
                complete = false;
                break;
            }
        }
        if (complete) {
            return values;
        }
        String min = "";
        int count = 10;
        for(String s: this.squares){
            int size = values.get(s).size();
            if( size > 1 && size < count){
                min = s;
                count = size;
            }
        }

        ArrayList<Character> possibleChoice = values.get(min);

        for(int i = 0; i < possibleChoice.size(); i++){
            Character d = possibleChoice.get(i);
            HashMap<String, ArrayList<Character>> cpy = (HashMap<String, ArrayList<Character>>) values.clone();
            HashMap<String, ArrayList<Character>> res = search(assign(cpy, min, d));
            if (res != null) {
                return res;
            }
        }
        return null;
    }
    private HashMap gridValues(String inp){
        ArrayList<Character> chars = new ArrayList<>();
        char[] stringChar = inp.replaceAll("[^\\d]", "0").toCharArray();
        for(char ch: stringChar){
            chars.add(ch);
        }
        HashMap <String, Character> vals = new HashMap<String, Character>();
        for (int i = 0; i < this.squares.size(); i++){
            vals.put(this.squares.get(i), chars.get(i));
        }
        return vals;
    }
}
