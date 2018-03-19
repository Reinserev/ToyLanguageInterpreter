package exceptions;

public class FatalError extends RuntimeException
{
    // Constructor that accepts a message
    public FatalError(String message)
    {
        super("A fatal error has occurred. Details: "+message);
    }
}
