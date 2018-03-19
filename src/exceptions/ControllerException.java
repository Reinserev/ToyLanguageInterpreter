package exceptions;

public class ControllerException extends RuntimeException
{
    // Constructor that accepts a message
    public ControllerException(String message)
    {
        super(message);
    }
}
