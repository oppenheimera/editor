package editor;

public class UndoRedoBuffer<T> {
    int currentState;
    /* Index for the next dequeue or peek. */
    int first;            
    /* Index for the next enqueue. */
    int last;
    /* Array for storing the buffer data. */
    private T[] rb;
    private int fillCount;
    private int cap = 100;

    protected T get(int i) {
        return rb[i];
    }

    protected int fillCount() {
        return fillCount;
    }

    protected int capacity() {
        return rb.length;
    }

    protected boolean isFull() {
        if (fillCount() == cap) {
            return true;
        } 
        return false;
    }

    protected boolean isEmpty() {
        if (fillCount() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    protected UndoRedoBuffer() {
        rb = (T[]) new Object[cap];
        fillCount = 0;
        first = 0;
        last = 0; 
        currentState = 0;
    }

    private void fillCountHelper() {
        if (fillCount == 100) {
            // do nothing
        } else {
            fillCount += 1;
        }
    }
    
    protected void push(T x) {
        if (isFull()) {
            dequeue();
        }
        enqueue(x);
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    private void enqueue(T x) {
        if (currentState != last) {
            int i = currentState;
            while (i != last) {
                rb[i] = null;
                fillCount -= 1;
                i = (i + 1) % cap;
            }
        }
        rb[currentState] = x;
        fillCountHelper();
        last = (last + 1) % cap;
        currentState = last;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    protected T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        T x = rb[first];
        rb[first] = null;
        first = (first + 1) % cap;
        fillCount--;
        if (isEmpty()) { 
            first = last;
        }
        return x;
    }

    protected T undo() {
        if (currentState == 0) {
            currentState = cap - 1;
        }
        currentState = (currentState - 1) % cap;
        return rb[currentState];
    }

    protected boolean canRedo() {
        return rb[currentState] != null;
    }

    protected T redo() {
        if (rb[currentState] != null) {
            currentState = (currentState + 1) % cap;
            return rb[currentState - 1];
        }
        return null;
    }

}
