package exceptions;

public class EmptyContainerException extends RuntimeException
{
    // Constructor that accepts a message
    public EmptyContainerException(String message)
    {
        super("An attempt to read from an empty container has been made! Sender: "+message);
    }
}
