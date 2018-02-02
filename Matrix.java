import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Matrix {

    private int rows;
    private int columns;
    private double[][] data;
    private double[][] x;
    private int r=0;

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.columns = data[0].length;
        this.data = data;
    }

    /**
     * Overloaded constructor which given a filename, reads in the matrix data from the file
     *
     * @param filename of matrix data
     */
    public Matrix(String filename) {
        readItIn(filename);
    }

    /**
     * Compares two matrices
     *
     * @param otherMatrix Matrix to be compared
     * @return Boolean
     */
    public boolean equals(Matrix otherMatrix) {
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
     *
     * @param otherMatrix Matrix to be compared
     * @return True or False
     */
    private boolean isSameSize(Matrix otherMatrix) {
        return rows == otherMatrix.rows && columns == otherMatrix.columns;
    }

    /**
     * Transposes a given matrix
     *
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
     *
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
     * Subtracts matrix one - matrix two
     * @param one double[][]
     * @param two double[][]
     * @return double[][] (matrix data)
     */
    public double[][] subtract(double[][] one, double[][] two) throws ExceptionHandled {
        int oneLength = one.length;
        int twoLength = one[0].length;
        if (one.length != two.length && one[0].length != two[0].length) {
            throw new ExceptionHandled("These two matricies have different dimensions, sorry Charlie");
        }
        double[][] tempData = new double[oneLength][twoLength];
        for (int i = 0; i < oneLength; i++)
            for (int j = 0; j < twoLength; j++)
                tempData[i][j] = one[i][j] - two[i][j];
        return tempData;
    }

    /**
     * Mulitiplies two matrices. Checks the dimensions of the matrices to see if they
     * can be multiplied. Separately sends the rows of this.data and otherMatrix to multHelper.
     *
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
     *
     * @param row         row of "this" Matrix
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
     *
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
     *
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
     *
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

        while (s != null && s.hasNext()) {
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

    public double[][] getX() {
        return x;
    }

    public void setX(double[][] x) {
        this.x = x;
    }

    public double[][] getData() {
        return this.data;
    }

    /**
     * Square Identity Matrix
     * @return matrix
     */
    public Matrix createSquareIdentity(int n) {
        double[][] temp = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    temp[i][j] = 1;
                } else {
                    temp[i][j] = 0;
                }
            }
        }

        Matrix out = new Matrix(temp);
        System.out.println(out.toString());

        return out;
    }

    /**
     * Given a double[][] matrix and a double[], will augment the two and return as a Matrix
     * Tested on multiple sized doubles[][] and one size colToAdd
     *
     * @param colToAdd as double[] one column only
     * @return augmented Matrix
     */
    public Matrix buildAugmentedMatrix(double[] colToAdd) {
        int cols = this.data[0].length;
        int rows = this.data.length;

        //Cnxn+1 = [A,b]
        double[][] augment = new double[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                augment[i][j] = this.data[i][j];
            }
        }

        //add b as last column
        for (int i = 0; i < rows; i++) {
            System.out.println(augment[i][cols]);
            augment[i][cols] = colToAdd[i];
        }
        return new Matrix(augment);
    }

    /**
     * @param column to find max in this given column
     * @return Matrix with the max row in the top position for the given columns values
     * @throws ExceptionHandled if no unique solution exists
     */
    public double[][] getMaxRowInPlace(int column) throws ExceptionHandled {
        int e = 1;

        //find the max of the first column
        int pivotRow = findMaxOfFirstCol(this, column);

        // -1 if findMaxOfFirstColumn finds that there are all zeros in column
        if (pivotRow == -1) {
            System.out.println("Cpj == 0, terminating");
            throw new ExceptionHandled("Can't find unique solution");
        }

        //if pivotPoint is in the given row, do nothing;
        // else, switch pivot row with row j
        if (pivotRow > column) {
            this.r +=1;
            double tempRowFirst[] = this.data[column];
            double tempRowOther[] = this.data[pivotRow];
            //for (int i = 0; i < this.columns + 1; i++) {
                this.data[column] = tempRowOther;
                this.data[pivotRow] = tempRowFirst;
//            }
        }
        return this.data;
    }

    /**
     * Given a Matrix, finds the max value in the given column
     * @param matrix
     * @return the row number of the max number
     */
    public int findMaxOfFirstCol(Matrix matrix, int column) {
        double pivotPoint = matrix.data[0][column];
        int pivotRow = 0;

        //find the max of param(column)
        for (int i = 0; i < matrix.rows; i++) {
            if (Math.abs(matrix.data[i][column]) > Math.abs(pivotPoint)) {
                pivotPoint = matrix.data[i][column];
                pivotRow = i;
            }
        }

        //if Cpj = 0, set E = 0 and exit by returning -1
        if (pivotPoint == 0) {
            System.out.println("Cpj == 0, terminating per the instructions");
            return -1;
        }
        return pivotRow;
    }

    /**
     * Divides an indicated row of given Matrix by a divisor
     *
     * @param matrix
     * @param rowNum  row to be divided
     * @param divisor what to divide row by
     * @return matrix with each element in the row indicated divided by the divisor
     */
    //TODO fix
    public double[][] divideRow(Matrix matrix, int rowNum, double divisor) {
        for (int i = 0; i < matrix.data[rowNum].length; i++) {
            matrix.data[rowNum][i] /= divisor;
        }
        return matrix.data;
    }

    public double[] getARow(int rowNumber){
        double[] out = new double[]{rows};
        for (int i = 0; i < rows; i++) {
            out[i]= this.data[rowNumber][i];
        }
        return out;
    }

    public double[] getAColumn(int colNumber){
        double[] out = new double[]{columns};
        for (int i = 0; i < rows; i++) {
            out[i]= this.data[i][colNumber];
        }
        return out;
    }

    /**
     * Gauss-Jordan Elimination - slide 14
     * @param data matrix
     * @return data (matrix)
     * @throws Exception if no unique solution
     */
    public double[][] gaussJordan(double[][] data) throws Exception {
        int length = data.length;

        //for j = 1 to the number of rows n
        for (int j = 0; j < length; j++) {
            this.data = getMaxRowInPlace(j);

            // Divide row j by Cjj
            double divisor = data[j][j];
            for (int i = 0; i < data[j].length; i++) {
                data[j][i] /= divisor;
            }

            //Subtract Cij* C[j] from C[i]
            for (int i = 0; i < data.length; i++) {

                //For each i != j
                if (i != j) {
                    double tempMultiply = data[i][j];
                    for (int colNum = 0; colNum < data[1].length; colNum++) {
                        double temp2 = (tempMultiply * data[j][colNum]);
                        data[i][colNum] -= temp2;
                    }
                }
            }
        }
        return data;
    }

    public double[][] gaussian(double[][] data) throws ExceptionHandled {
        int length = data.length;

        //for j = 1 to the number of rows n
        for (int j = 0; j < length; j++) {
            this.data = getMaxRowInPlace(j);

            //Subtract i - Cij/Cjj*row j
            for (int i = 0; i < data.length; i++) {

                //For each i != j
                if (i > j) {
                    double tempMultiply = data[i][j];
                    double tempDivisorCjj = data[j][j];
                    double tempDivision = tempMultiply/tempDivisorCjj;

                    for (int colNum = 0; colNum < data[1].length; colNum++) {
                        double temp2 = (tempDivision * data[j][colNum]);
                        data[i][colNum] -= temp2;
                    }
                }
            }
        }

        //part 2
        gaussianHelper(data);

        return data;
    }

    private void gaussianHelper(double[][] data) {
        int length = data.length;
        System.out.println(length);
        double sum;
        double[][] x = new double[length][1];

        //last column of x (init)
        x[rows-1][0] = data[rows-1][columns-1]/data[rows-1][columns-2];

        //Summation of Dij*xi
        for (int j = length - 2; j >= 0; j--) {
            sum = 0;
            double Djj = data[j][j];
            double divisor = 1/Djj;
            for (int i = j+1; i < length; i++) {
                 double Dji = data[j][i];
                 sum += (Dji * x[i][0]);
            }

            x[j][0]= (data[j][columns-1] - sum)* divisor;
        }

        setX(x);
    }

    public double[][] getInverse(double[][] matrix) throws Exception {
        int n = matrix.length;
        double [][] temp = new double[n][2*n];
        double[][]out = new double[n][n];

        for (int k = 0; k < n; k++) {
            for (int l = 0; l < n; l++) {

                temp[k][l]=matrix[k][l];
                Matrix z = new Matrix(temp);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                    if(j == i){
                        temp[i][j+n] = 1;

                    }
                    else{
                        temp[i][j+n]=0;
                    }


            }

        }

        Matrix y = new Matrix(temp);
        temp = y.gaussJordan(temp);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if(j == i){
                    out[i][j]=temp[i][j+n];

                }
                else{
                    out[i][j]=temp[i][j+n];
                }
            }
        }

        return out;
    }

    public double determinant(double[][] matrix) throws ExceptionHandled {
        double out = 1;
        int length = data.length;

        //for j = 1 to the number of rows n
        for (int j = 0; j < length; j++) {
            this.data = getMaxRowInPlace(j);

            //Subtract i - Cij/Cjj*row j
            for (int i = 0; i < data.length; i++) {

                //For each i != j
                if (i > j) {
                    double tempMultiply = data[i][j];
                    double tempDivisorCjj = data[j][j];
                    double tempDivision = tempMultiply/tempDivisorCjj;

                    for (int colNum = 0; colNum < data[1].length; colNum++) {
                        double temp2 = (tempDivision * data[j][colNum]);
                        data[i][colNum] -= temp2;
                    }
                }
            }
        }
        Matrix temp1 = new Matrix(data);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if(i==j){
                    out *= data[i][j];
                }
            }
        }
        double temp = Math.pow((-1),this.r);
        out = out * temp;

        return out;
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

    }
}
