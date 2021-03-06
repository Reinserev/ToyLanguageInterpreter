package model.util;

public class Pair<A,B>
{
    private A left;
    private B right;

    public Pair(A left, B right)
    {
        this.left = left;
        this.right = right;
    }

    public A getLeft() { return left; }
    public B getRight() { return right; }

    public String toString()
    {
        return "("+left.toString()+","+right.toString()+")";
    }
}