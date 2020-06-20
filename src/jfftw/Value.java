package jfftw;

abstract class Value {

    protected double[] arr;

    public int getSize() { return arr.length; }
    public double[] getArray() { return arr; }
    public void setArray(double [] d) { arr = d; };
    public int alignment() { return Interface.alignmentOf(this); }

}
