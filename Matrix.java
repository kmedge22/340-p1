import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Matrix {

    private int rows;
    private int columns;
    private double[][] data;

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.columns = data[0].length;
        this.data = data;
    }

    /**
     * Overloaded constructor which given a filename, reads in the matrix data from the file
     * @param filename of matrix data
     */
    public Matrix(String filename) { readItIn(filename); }

    /**
     * Compares two matrices
     *
     * @param otherMatrix Matrix to be compared
     * @return Boolean
     */
    public boolean equals(Matrix otherMatrix) {
//        boolean equal = Boolean.TRUE;
        double epsilon = .001;
        if (!isSameSize(otherMatrix)) {
            return Boolean.FALSE;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (Math.abs((this.data[i][j]) - otherMatrix.data[i][j]) > epsilon) {
                    return Boolean.FALSE;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the dimensions of two matrices are equivalent
     * @param otherMatrix Matrix to be compared
     * @return True or False
     */
    private boolean isSameSize(Matrix otherMatrix) {
        if (rows == otherMatrix.rows && columns == otherMatrix.columns) {
            return true;
        }
        return false;
    }

    /**
     * Transposes a given matrix
     * @return new Matrix
     */
    public Matrix transpose() {
        double[][] newMatrix = new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newMatrix[j][i] = data[i][j];
            }
        }
        return new Matrix(newMatrix);
    }

    /**
     * Reads in a matrix from txt file. Determines size of rows and columns. Creates
     * new double[][] with the dimensions from txt file. Splits tokens by whitespace.
     * iterates though  array, returns new double initialized from given String.
     * Sets matrix data to value.
     *
     * @param filename file path as string
     */
    private void readItIn(String filename) {
        ArrayList<String> lines; // = new ArrayList<>();
        int e = 0;
        lines = readData(filename);
        rows = lines.size();
        columns = (lines.get(0).split(" ")).length;
        data = new double[rows][columns];
        for (String ln : lines) {
            String[] x = ln.split(" ");
            int m = 0;
            for (String element : x) {
                String token = element.trim();
                double value = Double.parseDouble(token);
                data[e][m] = value;
                m++;
            }
            e++;
        }
    }

    /**
     * Adds two matrices together
     * @param otherMatrix Matrix to be added
     * @return new Matrix
     */
    public Matrix add(Matrix otherMatrix) {
        double[][] k = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                k[i][j] = this.data[i][j] + otherMatrix.data[i][j];
            }
        }
        return new Matrix(k);
    }

    /**
     * Mulitiplies two matrices. Checks the dimensions of the matrices to see if they
     * can be multiplied. Separately sends the rows of this.data and otherMatrix to multHelper.
     * @param otherMatrix Matrix to be multiplied
     * @return new Matrix
     */
    public Matrix mult(Matrix otherMatrix) {
        double[][] k = new double[this.rows][otherMatrix.columns];
        if (canBeMultiplied(otherMatrix))
            for (int i = 0; i < rows; i++) {
                k[i] = multHelper(this.data[i], otherMatrix.data);
            }
        return new Matrix(k);
    }

    /**
     * calculates the dot product for matrix multiplication and returns
     * a double[] to mult() method that contains the rows of the final matrix
     * @param row row of "this" Matrix
     * @param otherDouble all rows and columns of other matrix
     * @return double[]
     */
    private double[] multHelper(double[] row, double[][] otherDouble) {
        double[] k = new double[otherDouble[0].length];
        for (int i = 0; i < otherDouble[0].length; i++) {
            double total = 0;
            for (int j = 0; j < otherDouble.length; j++) {
                total += (row[j] * otherDouble[j][i]);
            }
            k[i] = total;
        }
        return k;
    }

    /**
     * Scalar Multiplication of matrices
     * @param value constant you want to multiply matrix by
     * @return new Matrix
     */
    public Matrix mult(double value) {
        double[][] k = new double[rows][columns];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                k[i][j] = value * data[i][j];
            }
        }
        return new Matrix(k);
    }

    /**
     * Determines if two matrices have dimensions that allow them to be multiplied
     * @param otherMatrix Matrix to be compared
     * @return True or False
     */
    private Boolean canBeMultiplied(Matrix otherMatrix) {
        if (this.columns == otherMatrix.rows) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Load file-helper
     * Used from CSC 331 with permission
     * @param filename filepath as string
     * @return ArayList of Strings
     */
    private static ArrayList<String> readData(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        Scanner s = null;
        File infile = new File(filename);
        try {
            s = new Scanner(infile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (s != null ? s.hasNext() : false) {
            lines.add(s.nextLine());
        }

        return lines;
    }


    public int getRows() {
        return rows;
    }


    public int getColumns() {
        return columns;
    }


    /**
     * Square Identity Matrix
     * @return
     */
    public Matrix createSquareIdentity(int sizeN){
        double [][] k = new double[sizeN][sizeN];
        for (int i = 0; i < sizeN; i++) {
            for (int j = 0; j < sizeN; j++) {
                if(i == j){
                    k[i][j] = 1;
                }
                else {
                    k[i][j] = 0;
                }
            }

        }

        Matrix out = new Matrix(k);
        System.out.println(out.toString());

        return out;
    }

    /**
     * Given a double[][] matrix and a double[], will augment the two and return as a Matrix
     * Tested on multiple sized doubles[][] and one size colToAdd
     * @param matrix as double[][]
     * @param colToAdd as double[] one column only
     * @return augmented Matrix
     */
    public Matrix buildAugmentedMatrix(double[][] matrix, double[] colToAdd){
        int cols = matrix[0].length;
        int rows = matrix.length;

        double[][] augment = new double[rows][cols+1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                augment[i][j] = matrix[i][j];
            }
        }
        System.out.println("test");
        for (int i = 0; i < rows; i++) {
            System.out.println(augment[i][cols]);
            augment[i][cols] = colToAdd[i];
        }
        return new Matrix(augment);

    }

    public Matrix getMaxRowInPlace(Matrix matrix) throws ExceptionHandled{
        int e = 1;
        int pivotRow = findMaxOfFirstCol(matrix);

        // Will be -1 if findMaxOfFirstColumn finds that there are all zeros in first column
        if (pivotRow == -1){
            System.out.println("Cpj == 0, terminating");
            throw new ExceptionHandled("Can't find unique solution");
        }

        //if tempmax is in the first row, do nothing; else, switch the row temp max is on with the first row
        if(pivotRow != 0){
            double tempRowFirst[] = matrix.data[0];
            double tempRowOther[] = matrix.data[pivotRow];
            for (int i = 0; i < matrix.columns+1; i++) {
                matrix.data[0]=tempRowOther;
                matrix.data[pivotRow]=tempRowFirst;
            }
        }
        return new Matrix(data);
    }

    /**
     * Given a Matrix, finds the max number in the first column
     * **may have to adjust to work on all rows**
     * @param matrix
     * @return the row number of the max number
     */
    public int findMaxOfFirstCol(Matrix matrix){
        //TODO adjust to work on any given row
        //find max of first column
        //tempMax will be pivot point
        double tempMax = matrix.data[0][0];
        int pivotRow = 0;
        for (int i = 0; i < matrix.rows ; i++) {
            if (Math.abs(matrix.data[i][0]) > Math.abs(tempMax)){
                tempMax = matrix.data[i][0];
                pivotRow = i;
            }
        }
        if (tempMax == 0){
            System.out.println("Cpj == 0, terminating");
            return -1;
        }
        return pivotRow;
    }

    /**
     * Divides an indicated row of given Matrix by a divisor
     * @param matrix
     * @param rowNum row to be divided
     * @param divisor what to divide row by
     * @return matrix with each element in the row indicated divided by the divisor
     */
    public Matrix divideRow(Matrix matrix, int rowNum, double divisor){
        for (int i = 0; i < matrix.data[rowNum].length; i++) {
            matrix.data[rowNum][i]= matrix.data[rowNum][i]/divisor;
        }
        return matrix;
    }



    @Override
    public String toString() {
        String x = "";
        for (int i = 0; i < rows; i++) {
            x += Arrays.toString(data[i]) + "\n";

        }
        return x;
    }

    public static void main(String[] args) {
        Matrix t = new Matrix("Resources/m1.txt");
        t.createSquareIdentity(2);
    }
}
