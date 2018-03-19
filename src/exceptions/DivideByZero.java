package exceptions;

public class DivideByZero extends RuntimeException
{    // Constructor that accepts a message
    public DivideByZero()
    {
        super("An attempt to divide by zero has been made.");
    }
}
