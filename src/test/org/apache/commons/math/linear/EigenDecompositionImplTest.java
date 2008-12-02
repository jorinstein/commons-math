/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.linear;

import java.util.Arrays;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EigenDecompositionImplTest extends TestCase {

    private double[] refValues;
    private RealMatrix matrix;

    public EigenDecompositionImplTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EigenDecompositionImplTest.class);
        suite.setName("EigenDecompositionImpl Tests");
        return suite;
    }

    public void testDimension1() {
        RealMatrix matrix =
            new RealMatrixImpl(new double[][] {
                                   { 1.5 }
                               }, false);
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        assertEquals(1.5, ed.getEigenvalue(0), 1.0e-15);
    }

    public void testDimension2() {
        RealMatrix matrix =
            new RealMatrixImpl(new double[][] {
                                   {       59.0, 12.0 },
                                   { Double.NaN, 66.0 }
                               }, false);
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        assertEquals(75.0, ed.getEigenvalue(0), 1.0e-15);
        assertEquals(50.0, ed.getEigenvalue(1), 1.0e-15);
    }

    public void testDimension3() {
        RealMatrix matrix =
            new RealMatrixImpl(new double[][] {
                                   {    39632.0,    -4824.0, -16560.0 },
                                   { Double.NaN,     8693.0,   7920.0 },
                                   { Double.NaN, Double.NaN,  17300.0 }
                               }, false);
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        assertEquals(50000.0, ed.getEigenvalue(0), 3.0e-11);
        assertEquals(12500.0, ed.getEigenvalue(1), 3.0e-11);
        assertEquals( 3125.0, ed.getEigenvalue(2), 3.0e-11);
    }

    public void testDimension4WithSplit() {
        RealMatrix matrix =
            new RealMatrixImpl(new double[][] {
                                   {      0.784,     -0.288,       0.000,  0.000 },
                                   { Double.NaN,      0.616,       0.000,  0.000 },
                                   { Double.NaN, Double.NaN,       0.164, -0.048 },
                                   { Double.NaN, Double.NaN,  Double.NaN,  0.136 }
                               }, false);
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        assertEquals(1.0, ed.getEigenvalue(0), 1.0e-15);
        assertEquals(0.4, ed.getEigenvalue(1), 1.0e-15);
        assertEquals(0.2, ed.getEigenvalue(2), 1.0e-15);
        assertEquals(0.1, ed.getEigenvalue(3), 1.0e-15);
    }

    public void testAbsoluteSplit() {
        RealMatrix matrix =
            new RealMatrixImpl(new double[][] {
                                   {      0.784,     -0.288,       0.000,  0.000 },
                                   { Double.NaN,      0.616,       0.000,  0.000 },
                                   { Double.NaN, Double.NaN,       0.164, -0.048 },
                                   { Double.NaN, Double.NaN,  Double.NaN,  0.136 }
                               }, false);
        EigenDecompositionImpl ed = new EigenDecompositionImpl();
        ed.setAbsoluteSplitTolerance(1.0e-13);
        ed.decompose(matrix);
        assertEquals(1.0, ed.getEigenvalue(0), 1.0e-15);
        assertEquals(0.4, ed.getEigenvalue(1), 1.0e-15);
        assertEquals(0.2, ed.getEigenvalue(2), 1.0e-15);
        assertEquals(0.1, ed.getEigenvalue(3), 1.0e-15);
    }

    public void testDimension4WithoutSplit() {
        RealMatrix matrix =
            new RealMatrixImpl(new double[][] {
                                   {  0.5608, -0.2016,  0.1152, -0.2976 },
                                   { -0.2016,  0.4432, -0.2304,  0.1152 },
                                   {  0.1152, -0.2304,  0.3088, -0.1344 },
                                   { -0.2976,  0.1152, -0.1344,  0.3872 }
                               }, false);
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        assertEquals(1.0, ed.getEigenvalue(0), 1.0e-15);
        assertEquals(0.4, ed.getEigenvalue(1), 1.0e-15);
        assertEquals(0.2, ed.getEigenvalue(2), 1.0e-15);
        assertEquals(0.1, ed.getEigenvalue(3), 1.0e-15);
    }

    /** test dimensions */
    public void testDimensions() {
        final int m = matrix.getRowDimension();
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        assertEquals(m, ed.getV().getRowDimension());
        assertEquals(m, ed.getV().getColumnDimension());
        assertEquals(m, ed.getD().getColumnDimension());
        assertEquals(m, ed.getD().getColumnDimension());
        assertEquals(m, ed.getVT().getRowDimension());
        assertEquals(m, ed.getVT().getColumnDimension());
    }

    /** test eigenvalues */
    public void testEigenvalues() {
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        double[] eigenValues = ed.getEigenvalues();
        assertEquals(refValues.length, eigenValues.length);
        for (int i = 0; i < refValues.length; ++i) {
            assertEquals(refValues[i], eigenValues[i], 3.0e-15);
        }
    }

    /** test eigenvalues for a big matrix. */
    public void testBigMatrix() {
        Random r = new Random(17748333525117l);
        double[] bigValues = new double[200];
        for (int i = 0; i < bigValues.length; ++i) {
            bigValues[i] = 2 * r.nextDouble() - 1;
        }
        Arrays.sort(bigValues);
        EigenDecomposition ed = new EigenDecompositionImpl(createTestMatrix(r, bigValues));
        double[] eigenValues = ed.getEigenvalues();
        assertEquals(bigValues.length, eigenValues.length);
        for (int i = 0; i < bigValues.length; ++i) {
            assertEquals(bigValues[bigValues.length - i - 1], eigenValues[i], 2.0e-14);
        }
    }

    /** test eigenvectors */
    public void testEigenvectors() {
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        for (int i = 0; i < matrix.getRowDimension(); ++i) {
            double lambda = ed.getEigenvalue(i);
            RealVector v  = ed.getEigenvector(i);
            RealVector mV = matrix.operate(v);
            assertEquals(0, mV.subtract(v.mapMultiplyToSelf(lambda)).getNorm(), 1.0e-13);
        }
    }

    /** test A = VDVt */
    public void testAEqualVDVt() {
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        RealMatrix v  = ed.getV();
        RealMatrix d  = ed.getD();
        RealMatrix vT = ed.getVT();
        double norm = v.multiply(d).multiply(vT).subtract(matrix).getNorm();
        assertEquals(0, norm, 6.0e-13);
    }

    /** test that V is orthogonal */
    public void testVOrthogonal() {
        RealMatrix v = new EigenDecompositionImpl(matrix).getV();
        RealMatrix vTv = v.transpose().multiply(v);
        RealMatrix id  = MatrixUtils.createRealIdentityMatrix(vTv.getRowDimension());
        assertEquals(0, vTv.subtract(id).getNorm(), 2.0e-13);
    }

    /** test non invertible matrix */
    public void testNonInvertible() {
        Random r = new Random(9994100315209l);
        EigenDecomposition ed =
            new EigenDecompositionImpl(createTestMatrix(r, new double[] { 1.0, 0.0, -1.0, -2.0, -3.0 }));
        assertFalse(ed.isNonSingular());
        try {
            ed.getInverse();
            fail("an exception should have been thrown");
        } catch (InvalidMatrixException ime) {
            // expected behavior
        } catch (Exception e) {
            fail("wrong exception caught");
        }
    }

    /** test invertible matrix */
    public void testInvertible() {
        Random r = new Random(9994100315209l);
        RealMatrix m =
            createTestMatrix(r, new double[] { 1.0, 0.5, -1.0, -2.0, -3.0 });
        EigenDecomposition ed = new EigenDecompositionImpl(m);
        assertTrue(ed.isNonSingular());
        RealMatrix inverse = ed.getInverse();
        RealMatrix error =
            m.multiply(inverse).subtract(MatrixUtils.createRealIdentityMatrix(m.getRowDimension()));
        assertEquals(0, error.getNorm(), 4.0e-15);
    }

    /** test diagonal matrix */
    public void testDiagonal() {
        double[] diagonal = new double[] { -3.0, -2.0, 2.0, 5.0 };
        EigenDecomposition ed =
            new EigenDecompositionImpl(createDiagonalMatrix(diagonal, diagonal.length, diagonal.length));
        assertEquals(diagonal[0], ed.getEigenvalue(3), 2.0e-15);
        assertEquals(diagonal[1], ed.getEigenvalue(2), 2.0e-15);
        assertEquals(diagonal[2], ed.getEigenvalue(1), 2.0e-15);
        assertEquals(diagonal[3], ed.getEigenvalue(0), 2.0e-15);
    }

    /** test solve dimension errors */
    public void testSolveDimensionErrors() {
        EigenDecomposition ed = new EigenDecompositionImpl(matrix);
        RealMatrix b = new RealMatrixImpl(new double[2][2]);
        try {
            ed.solve(b);
            fail("an exception should have been thrown");
        } catch (IllegalArgumentException iae) {
            // expected behavior
        } catch (Exception e) {
            fail("wrong exception caught");
        }
        try {
            ed.solve(b.getColumn(0));
            fail("an exception should have been thrown");
        } catch (IllegalArgumentException iae) {
            // expected behavior
        } catch (Exception e) {
            fail("wrong exception caught");
        }
        try {
            ed.solve(new RealVectorImplTest.RealVectorTestImpl(b.getColumn(0)));
            fail("an exception should have been thrown");
        } catch (IllegalArgumentException iae) {
            // expected behavior
        } catch (Exception e) {
            fail("wrong exception caught");
        }
    }

    /** test solve */
    public void testSolve() {
        RealMatrix m = new RealMatrixImpl(new double[][] {
                { 91,  5, 29, 32, 40, 14 },
                {  5, 34, -1,  0,  2, -1 },
                { 29, -1, 12,  9, 21,  8 },
                { 32,  0,  9, 14,  9,  0 },
                { 40,  2, 21,  9, 51, 19 },
                { 14, -1,  8,  0, 19, 14 }
        });
        EigenDecomposition ed = new EigenDecompositionImpl(m);
        assertEquals(184041, ed.getDeterminant(), 2.0e-8);
        RealMatrix b = new RealMatrixImpl(new double[][] {
                { 1561, 269, 188 },
                {   69, -21,  70 },
                {  739, 108,  63 },
                {  324,  86,  59 },
                { 1624, 194, 107 },
                {  796,  69,  36 }
        });
        RealMatrix xRef = new RealMatrixImpl(new double[][] {
                { 1,   2, 1 },
                { 2,  -1, 2 },
                { 4,   2, 3 },
                { 8,  -1, 0 },
                { 16,  2, 0 },
                { 32, -1, 0 }
        });

        // using RealMatrix
        assertEquals(0, ed.solve(b).subtract(xRef).getNorm(), 2.0e-12);

        // using double[]
        for (int i = 0; i < b.getColumnDimension(); ++i) {
            assertEquals(0,
                         new RealVectorImpl(ed.solve(b.getColumn(i))).subtract(xRef.getColumnVector(i)).getNorm(),
                         2.0e-11);
        }

        // using RealMatrixImpl
        for (int i = 0; i < b.getColumnDimension(); ++i) {
            assertEquals(0,
                         ed.solve(b.getColumnVector(i)).subtract(xRef.getColumnVector(i)).getNorm(),
                         2.0e-11);
        }

        // using RealMatrix with an alternate implementation
        for (int i = 0; i < b.getColumnDimension(); ++i) {
            RealVectorImplTest.RealVectorTestImpl v =
                new RealVectorImplTest.RealVectorTestImpl(b.getColumn(i));
            assertEquals(0,
                         ed.solve(v).subtract(xRef.getColumnVector(i)).getNorm(),
                         2.0e-11);
        }

    }
    
    /**
     * Matrix with eigenvalues {8, -1, -1}
     */
    public void testRepeatedEigenvalue() {
        RealMatrix repeated = new RealMatrixImpl(new double[][] {
                {3,  2,  4},
                {2,  0,  2},
                {4,  2,  3}
        }); 
        EigenDecomposition ed = new EigenDecompositionImpl(repeated);
        checkEigenValues((new double[] {8, -1, -1}), ed, 1E-12);
        checkEigenVector((new double[] {2, 1, 2}), ed, 1E-12);
    }
    
    /**
     * Matrix with eigenvalues {2, 0, 12}
     */
    public void testDistinctEigenvalues() {
        RealMatrix distinct = new RealMatrixImpl(new double[][] {
                {3, 1, -4},  
                {1, 3, -4}, 
                {-4, -4, 8}
        });
        EigenDecomposition ed = new EigenDecompositionImpl(distinct);
        checkEigenValues((new double[] {2, 0, 12}), ed, 1E-12);
        checkEigenVector((new double[] {1, -1, 0}), ed, 1E-12);
        checkEigenVector((new double[] {1, 1, 1}), ed, 1E-12);
        checkEigenVector((new double[] {-1, -1, 2}), ed, 1E-12);
    }
    
    /**
     * Verifies that the given EigenDecomposition has eigenvalues equivalent to
     * the targetValues, ignoring the order of the values and allowing
     * values to differ by tolerance.
     */
    protected void checkEigenValues(double[] targetValues,
            EigenDecomposition ed, double tolerance) {
        double[] observed = ed.getEigenvalues();
        for (int i = 0; i < observed.length; i++) {
            assertTrue(isIncludedValue(observed[i], targetValues, tolerance));
            assertTrue(isIncludedValue(targetValues[i], observed, tolerance));
        }
    }
    
    /**
     * Returns true iff there is an entry within tolerance of value in
     * searchArray.
     */
    private boolean isIncludedValue(double value, double[] searchArray,
            double tolerance) {
       boolean found = false;
       int i = 0;
       while (!found && i < searchArray.length) {
           if (Math.abs(value - searchArray[i]) < tolerance) {
               found = true;
           }
           i++;
       }
       return found;
    }
    
    /**
     * Returns true iff eigenVector is a scalar multiple of one of the columns
     * of ed.getV().  Does not try linear combinations - i.e., should only be
     * used to find vectors in one-dimensional eigenspaces.
     */
    protected void checkEigenVector(double[] eigenVector,
            EigenDecomposition ed, double tolerance) {
        assertTrue(isIncludedColumn(eigenVector, ed.getV(), tolerance));
    }
    
    /**
     * Returns true iff there is a column that is a scalar multiple of column
     * in searchMatrix (modulo tolerance)
     */
    private boolean isIncludedColumn(double[] column, RealMatrix searchMatrix,
            double tolerance) {
        boolean found = false;
        int i = 0;
        while (!found && i < searchMatrix.getColumnDimension()) {
            double multiplier = 1d;
            boolean matching = true;
            int j = 0;
            while (matching && j < searchMatrix.getRowDimension()) {
                double colEntry = searchMatrix.getEntry(j, i);
                // Use the first entry where both are non-zero as scalar
                if (multiplier == 1d && Math.abs(colEntry) > 1E-14
                        && Math.abs(column[j]) > 1e-14) {
                    multiplier = colEntry / column[j];
                } 
                if (Math.abs(column[j] * multiplier - colEntry) > tolerance) {
                    matching = false;
                }
                j++;
            }
            found = matching;
            i++;
        }
        return found;
    }

    public void setUp() {
        refValues = new double[] {
                2.003, 2.002, 2.001, 1.001, 1.000, 0.001
        };
        matrix = createTestMatrix(new Random(35992629946426l), refValues);
    }

    public void tearDown() {
        refValues = null;
        matrix    = null;
    }

    private RealMatrix createTestMatrix(final Random r, final double[] eigenValues) {
        final int n = eigenValues.length;
        final RealMatrix v = createOrthogonalMatrix(r, n);
        final RealMatrix d = createDiagonalMatrix(eigenValues, n, n);
        return v.multiply(d).multiply(v.transpose());
    }

    public static RealMatrix createOrthogonalMatrix(final Random r, final int size) {

        final double[][] data = new double[size][size];

        for (int i = 0; i < size; ++i) {
            final double[] dataI = data[i];
            double norm2 = 0;
            do {

                // generate randomly row I
                for (int j = 0; j < size; ++j) {
                    dataI[j] = 2 * r.nextDouble() - 1;
                }

                // project the row in the subspace orthogonal to previous rows
                for (int k = 0; k < i; ++k) {
                    final double[] dataK = data[k];
                    double dotProduct = 0;
                    for (int j = 0; j < size; ++j) {
                        dotProduct += dataI[j] * dataK[j];
                    }
                    for (int j = 0; j < size; ++j) {
                        dataI[j] -= dotProduct * dataK[j];
                    }
                }

                // normalize the row
                norm2 = 0;
                for (final double dataIJ : dataI) {
                    norm2 += dataIJ * dataIJ;
                }
                final double inv = 1.0 / Math.sqrt(norm2);
                for (int j = 0; j < size; ++j) {
                    dataI[j] *= inv;
                }

            } while (norm2 * size < 0.01);
        }

        return new RealMatrixImpl(data, false);

    }

    public static RealMatrix createDiagonalMatrix(final double[] diagonal,
                                                  final int rows, final int columns) {
        final double[][] dData = new double[rows][columns];
        for (int i = 0; i < Math.min(rows, columns); ++i) {
            dData[i][i] = diagonal[i];
        }
        return new RealMatrixImpl(dData, false);
    }

}
