package exceptions;

public class InvalidPositionException extends RuntimeException
{
    // Constructor that accepts a message
    public InvalidPositionException(String message)
    {
        super("An attempt to get data from an invalid index / key has been made! Sender: "+message);
    }
}
