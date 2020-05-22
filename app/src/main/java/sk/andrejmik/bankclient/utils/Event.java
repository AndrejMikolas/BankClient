package sk.andrejmik.bankclient.utils;

public class Event<T>
{
    private T content;
    private boolean hasBeenHandled = false;
    
    /**
     * Returns the content and prevents its use again.
     */
    public T getContentIfNotHandled()
    {
        if (hasBeenHandled)
        {
            return null;
        }
        else
        {
            hasBeenHandled = true;
            return content;
        }
    }
    
    /**
     * Returns the content, even if it's already been handled.
     */
    public T peekContent()
    {
        return content;
    }
    
    /**
     * Type of events when loading data
     */
    public enum LoadEvent
    {
        STARTED, COMPLETE, UNKNOWN_ERROR, NETWORK_ERROR, NO_MORE, NOT_FOUND
    }
}
