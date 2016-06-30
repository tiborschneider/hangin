package com.tiborschneider.hangin;

/**
 * Created by tibor on 29.06.16.
 */
public class DialogueQueue {

    private static int queueSize = 10;
    private int firstElement = 0;
    private int nextFreeElement = 0;
    private Dialogue[] queue = new Dialogue[queueSize];

    public boolean isEmpty()
    {
        return (firstElement == nextFreeElement);
    }

    public boolean isFull()
    {
        return (firstElement == rotate(nextFreeElement, 1));
    }

    public Dialogue peakNext()
    {
        return queue[firstElement];
    }

    public Dialogue getNext()
    {
        if (isEmpty())
            return null;
        Dialogue ret = queue[firstElement];
        firstElement = rotate(firstElement, 1);
        return ret;
    }

    public boolean enqueue(Dialogue aDialogue)
    {
        if (isFull())
            return false;
        queue[nextFreeElement] = aDialogue;
        nextFreeElement = rotate(nextFreeElement,1);
        return true;
    }

    public boolean clear()
    {
        firstElement = 0;
        nextFreeElement = 0;
        return true;
    }

    private static int rotate(int start, int offset)
    {
        if (start + offset < 0) {
            return start + offset + queueSize;
        } else if (start + offset >= queueSize) {
            return start + offset - queueSize;
        } else {
            return start + offset;
        }
    }
}
