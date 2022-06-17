import java.util.ArrayList;
import java.util.List;

public class generator {
    private final int n;
    private int[][][] values;
    private String[] clauses;
    private long numberOfValues;
    private long numberOfClauses;

    public generator(int n, int selection) {
        this.n = n;
        setValues();
        setClausesV2(selection);
        setNumberOfValues(selection);
        setNumberOfClauses();
    }

    public static String convert_output(String[] string) {
        StringBuilder output = new StringBuilder();
        for (int i = 1; i < string.length; i++) {
            if (string[i].replaceAll("[^a-zA-Z]", "").equals("True")) {
                output.append(i);
            }
            if (string[i].replaceAll("[^a-zA-Z]", "").equals("False")) {
                output.append("-");
                output.append(i);
            }
            output.append(" ");
        }
        return output.toString();
    }

    // Getters
    public int getN() {
        return n;
    }

    public int[][][] getValues() {
        return values;
    }

    public long getNumberOfValues() {
        return numberOfValues;
    }

    // Setters
    private void setNumberOfValues(int selection) {
        if (selection == 4 || selection == 5) {
            this.numberOfValues = n * n * n + (n - 1) * (n - 1) * n * n;
        } else if (selection == 7 || selection == 9) {
            this.numberOfValues = n * n * n + (n - 1) * n * ((n - 1) * n + 1) + 1; //n^3 * numOfDuos * (numOfDuos + 1) + 1
        } else {
            this.numberOfValues = n * n * n;
        }
    }

    public long getNumberOfClauses() {
        return this.numberOfClauses;
    }

    public String[] getClauses() {
        return this.clauses;
    }

    private void setValues() {
        this.values = new int[n][n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    values[i][j][k] = i * n * n + j * n + k + 1;
                }
            }
        }
    }

    private void setNumberOfClauses() {
        this.numberOfClauses = this.getClauses().length;
    }

    private String[] setClauses(String[]... arrays) {
        int length = 0;
        for (String[] array : arrays) {
            length += array.length;
        }
        String[] result = new String[length];
        int pos = 0;
        for (String[] array : arrays) {
            for (String element : array) {
                result[pos] = element;
                pos++;
            }
        }

        //List<String> list = Arrays.asList(result);
        //Collections.shuffle(list);
        //list.toArray(result);

        return result;
    }

    public void setClausesV2(int selection) {
        switch (selection) {
            case 0: { //default LS
                this.clauses = setClauses(possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace());
                break;
            }
            case 1: { //right unit square
                this.clauses = setClauses(rightUnitSquare(), possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace());
                break;
            }
            case 2: { //left unit square
                this.clauses = setClauses(leftUnitSquare(), possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace());
                break;
            }
            case 3: {//both unit square
                this.clauses = setClauses(bothUnitSquare(), possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace());
                break;
            }
            case 4: { //RCLS
                this.clauses = setClauses(noSameValuesInRow(), noSameValuesInColumn(), noSameValuesInOnePlace(), clausesRCLS(), possibleRowPlacement(),
                        possibleColumnPlacement());
                break;
            }
            case 5: { //RCLS right unit square
                this.clauses = setClauses(rightUnitSquare(), noSameValuesInRow(), noSameValuesInColumn(), noSameValuesInOnePlace(), clausesRCLS(),
                        possibleRowPlacement(), possibleColumnPlacement());
                break;
            }

            case 6: { //post-comutative
                this.clauses = setClauses(possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace(), postComutative());
                break;
            }
            case 7: { //tseitin
                this.clauses = setClauses(noSameValuesInRow(), noSameValuesInColumn(), noSameValuesInOnePlace(), clausesRCLSTseitin(),
                        possibleRowPlacement(), possibleColumnPlacement());
                break;
            }
            case 8: { //comutative
                this.clauses = setClauses(rightUnitSquare(), possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace(), comutative());

                break;
            }
            case 9: {
                //post-comutative right unit square
                this.clauses = setClauses(rightUnitSquare(), possibleRowPlacement(), possibleColumnPlacement(), noSameValuesInRow(),
                        noSameValuesInColumn(), noSameValuesInOnePlace(), postComutative());

                break;
            }

        }
    }

    // Generating clauses
    private String[] possibleRowPlacement() {
        String[] out = new String[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (out[i * n + j] == null) {
                        out[i * n + j] = values[i][j][k] + " ";
                        //out[i*this.getN()+j] = "solver.add_clause( ( "+this.getValues()[i][j][k];
                    } else {
                        out[i * n + j] = out[i * n + j] + values[i][j][k] + " ";
                        //out[i*this.getN()+j] = out[i*this.getN()+j]+", "+this.getValues()[i][j][k];
                    }
                }
                out[i * n + j] = out[i * n + j] + "0";
                //out[i*this.getN()+j] = out[i*this.getN()+j]+") )";
            }
        }
        return out;
    }

    private String[] possibleColumnPlacement() {
        String[] out = new String[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (out[i * n + j] == null) {
                        out[i * n + j] = values[i][k][j] + " ";
                        //out[i*this.getN()+j] = "solver.add_clause( ( "+this.getValues()[i][k][j];
                    } else {
                        out[i * n + j] = out[i * n + j] + values[i][k][j] + " ";
                        //out[i*this.getN()+j] = out[i*this.getN()+j]+", "+this.getValues()[i][k][j];
                    }
                }
                out[i * n + j] = out[i * n + j] + "0";
                //out[i*this.getN()+j] = out[i*this.getN()+j]+") )";
            }
        }
        return out;
    }

    private String[] noSameValuesInOnePlace() {
        String[] out = new String[Math.toIntExact(n * n * combinations())];
        long tmp = n * n * combinations() - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        out[Math.toIntExact(tmp)] = "-" + values[k][i][j] + " -" + values[l][i][j] + " 0";
                        //out[Math.toIntExact(tmp)]="solver.add_clause( ( "+this.getValues()[k][i][j]+", "+this.getValues()[l][i][j]+") )";
                        tmp--;
                    }
                }
            }
        }
        return out;
    }

    private String[] noSameValuesInRow() {
        String[] out = new String[Math.toIntExact(n * n * combinations())];
        long tmp = n * n * combinations() - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        out[Math.toIntExact(tmp)] = "-" + values[i][j][k] + " -" + values[i][j][l] + " 0";
                        //out[Math.toIntExact(tmp)]="solver.add_clause( ( "+this.getValues()[i][j][k]+", "+this.getValues()[i][j][l]+") )";
                        tmp--;
                    }
                }
            }
        }
        return out;
    }

    private String[] noSameValuesInColumn() {
        String[] out = new String[Math.toIntExact(n * n * combinations())];
        long tmp = n * n * combinations() - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        out[Math.toIntExact(tmp)] = "-" + values[i][k][j] + " -" + values[i][l][j] + " 0";
                        //out[Math.toIntExact(tmp)]="solver.add_clause( ( "+this.getValues()[i][k][j]+", "+this.getValues()[i][l][j]+") )";
                        tmp--;
                    }
                }
            }
        }
        return out;
    }

    // Result
    public int[][] getResult(String output) {
        String[] parts = output.split(" ");
        int[][] result = new int[n][n];
        for (String part : parts) {
            int tmp = Integer.parseInt(part);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        if (tmp == values[i][j][k]) {
                            result[j][k] = i + 1;
                        }
                    }
                }
            }
        }
        return result;
    }

    // Printing Functions
    public void printClauses() {
        for (String s : clauses) {
            System.out.println(s);
            //System.out.print(s + "\\n");
        }
    }

    public void printResult(String output) {
        for (int[] x : getResult(output)) {
            for (int y : x) {
                if (y < 10) {
                    System.out.print(" " + y + " ");
                } else {
                    System.out.print(y + " ");
                }
            }
            System.out.println();
        }
    }


    // Helpers
    private long combinations() {
        return n * (n - 1) / 2;
    }

    private List<int[]> generateDuos() {
        List<int[]> combinations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    combinations.add(new int[]{i, j});
                }
            }
        }
        return combinations;
    }

    // Additions
    private String[] rightUnitSquare() {
        String[] out = new String[n];
        for (int i = 0; i < n; i++) {
            out[i] = values[i][i][0] + " 0";
        }
        return out;
    }

    private String[] leftUnitSquare() {
        String[] out = new String[n];
        for (int i = 0; i < n; i++) {
            out[i] = values[i][0][i] + " 0";
        }
        return out;
    }

    private String[] bothUnitSquare() {
        String[] out = new String[n * 2 - 1];
        for (int i = 0; i < n; i++) {
            out[i] = values[i][i][0] + " 0";
        }
        for (int i = 1; i < n; i++) {
            out[n + i - 1] = values[i][0][i] + " 0";
        }
        return out;
    }

    private String[] clausesRCLS() {
        int numOfDuos = n * (n - 1);
        int newVarsStart = n * n * n + 1;
        int[] newVars = new int[numOfDuos * numOfDuos];
        String[] clauses = new String[numOfDuos * numOfDuos * 2 + numOfDuos];

        for (int i = 0; i < numOfDuos * numOfDuos; i++) {
            newVars[i] = newVarsStart + i;
        }
        int tmp_c = 0;
        int tmp_v = 0;
        for (int[] combination : generateDuos()) {
            int index1 = combination[0];
            int index2 = combination[1];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - 1; j++) {
                    clauses[tmp_c] = "-" + newVars[tmp_v] + " " + values[index1][i][j] + " 0";
                    tmp_c++;
                    clauses[tmp_c] = "-" + newVars[tmp_v] + " " + values[index2][i][j + 1] + " 0";
                    tmp_c++;
                    tmp_v++;
                }
            }
        }

        tmp_v = 0;
        for (int i = 0; i < generateDuos().size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n * (n - 1); j++) {
                sb.append(newVars[tmp_v]).append(" ");
                tmp_v++;
            }
            sb.append("0");
            clauses[tmp_c] = sb.toString();
            tmp_c++;
        }
        return clauses;
    }


    private String[] clausesRCLSTseitin() {
        int numOfDuos = n * (n - 1);  // generateDuos().size();
        int newVarsStart = n * n * n + 1;
        int[] newVars = new int[numOfDuos * (numOfDuos + 1) + 1];
        String[] clauses = new String[numOfDuos * (numOfDuos * 3 + numOfDuos + 1) + numOfDuos + 2];

        for (int i = 0; i < newVars.length; i++) { // fill new vars with their values
            newVars[i] = newVarsStart + i;
        }
        int tmp_c = 0; // shows the next free new clause
        int tmp_v = 0; // shows the next free new var

        for (int[] combination : generateDuos()) {
            int index1 = combination[0];
            int index2 = combination[1];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - 1; j++) {
                    //inner AND -> KNF
                    clauses[tmp_c++] = "-" + newVars[tmp_v] + " " + values[index1][i][j] + " 0";
                    clauses[tmp_c++] = "-" + newVars[tmp_v] + " " + values[index2][i][j + 1] + " 0";
                    clauses[tmp_c++] = newVars[tmp_v] +  " -" + values[index1][i][j] + " -" + values[index2][i][j + 1] + " 0";
                    tmp_v++;
                }
            }

            // OR -> KNF
            int lastVarInx = tmp_v++;

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("-" + newVars[lastVarInx]); // last new var in the round is the "outcome" of the whole DNF
            for (int i = lastVarInx - numOfDuos; i < lastVarInx; i++) {
                stringBuilder.append(" " + newVars[i]);
            }
            stringBuilder.append(" 0");
            clauses[tmp_c++] = stringBuilder.toString();

            for (int i = lastVarInx - numOfDuos; i < lastVarInx; i++) {
                clauses[tmp_c++] = newVars[lastVarInx]  + " -" + newVars[i] + " 0";
            }
        }

        //outer AND -> KNF
        int finalNewVarInx = newVars.length - 1;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(newVars[finalNewVarInx]); // final new var is the "outcome" of the whole RCLS formula

        for (int i = numOfDuos; i < finalNewVarInx; i += numOfDuos + 1) {
            clauses[tmp_c++] = "-" + newVars[finalNewVarInx] + " " + newVars[i]  + " 0";
            stringBuilder.append(" -" + newVars[i]);
        }

        stringBuilder.append(" 0");
        clauses[tmp_c++] = stringBuilder.toString();

        //the "outcome" of the whole RCLS formula must be true
        clauses[tmp_c++] = newVars[finalNewVarInx] + " 0";
        return clauses;
    }


    private String[] postComutative() {
        String[] out = new String[Math.toIntExact(n * n * n)];
        long tmp = n * n * n - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    out[Math.toIntExact(tmp)] = "-" + values[k][i][j] + " " + values[j][i][k] + " 0";
                    tmp--;
                }
            }
        }
        return out;
    }


    private String[] comutative() {
        String[] out = new String[Math.toIntExact(n * n * n)];
        long tmp = (n * n * n) - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    out[Math.toIntExact(tmp)] = "-" + values[k][i][j] + " " + values[k][j][i] + " 0";
                    tmp--;
                }
            }
        }
        return out;
    }

}

