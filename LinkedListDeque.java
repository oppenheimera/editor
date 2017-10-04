package editor;


public class LinkedListDeque {
    protected class Node {
        protected Node prev; // back queue pointer
        protected String item; // value of current node
        protected Node next; // pointer to rest of queue

        protected Node(Node p, String i, Node n){
            prev = p;
            item = i;
            next = n;
        }
    }

    protected Node sentinel;
    protected Node cursor;
    protected int size;
    protected int lineNumber;

    // constructs empty list
    protected LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, "sentinel", null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    protected LinkedListDeque(String x) {
        size = 1;
        sentinel = new Node(null, null, null);
        sentinel.next = new Node(sentinel, x, sentinel);
        sentinel.prev = sentinel.next;
    }

    protected boolean isEmpty() {
        return size == 0;
    }

    protected int size() {
        return size;
    }

    protected void printDeque() {
        Node pointer = sentinel.next;
        while (pointer != sentinel) {
            System.out.println(pointer.item);
            pointer = pointer.next;
        }
    }

    @Override
    public String toString() {
        if (sentinel == null) {
            return " ";
        }
        Node pointer = sentinel.next;
        String s = "";
        while (pointer != sentinel) {
            s += pointer.item;
            pointer = pointer.next;
        }
        return s;
    }

    public String toFancyString() {
        if (sentinel == null) {
            return "";
        }
        Node pointer = sentinel.next;
        String s = "";
        while (pointer != sentinel) {
            if (pointer == cursor) {
                s += "\033[36;47m" + pointer.item + "\033[0m";
            } else {
                s += pointer.item;
            }
            pointer = pointer.next;
        }
        if (pointer == cursor) {
            s += "\033[36;47m" + "*" + "\033[0m";
        } else {
            s += "*";
        }
        return s;
    }

    protected String prettyPrint() {
        String s = "*";
        if (cursor == sentinel) {
            s += "|";
        }

        if (sentinel == null) {
            return "SENTINEL IS NULL";
        }

        Node pointer = sentinel.next;
        while (pointer != sentinel) {
            if (pointer == cursor) {
                s += pointer.item + "|";
            } else {
                s += pointer.item;
            }
            pointer = pointer.next;
        }
        return s;
    }

    protected Node get(int target) {
        if (target >= size){
            return null;
        }
        Node pointer = sentinel.next;
        int count = 0;
        while (count < target){
            pointer = pointer.next;
            count += 1;
        }
        return pointer;
    }

    protected int findCursorPos(Node target) {
        Node pointer = sentinel.next;
        int count = 0;
        while (pointer != target){
            pointer = pointer.next;
            count += 1;
        }
        return count;
    }
    //TODO case where cursor is lost in space between sentinel and first node
    protected void moveCursorLeft() {
        if (cursor == this.sentinel) {
            // do nothing
        } else if (cursor.prev == sentinel) {
            cursor = sentinel;
        } else {
            cursor = cursor.prev;
        }
    }

    protected void moveCursorRight() {
        if (cursor.next == sentinel) {
            // do nothing
        } else {
            cursor = cursor.next;
        }
    }

    protected void add(String x) { // size increments in 3 different places!!!!!
        if (size == 0) {
            addFirst(x);
        } else if (cursor == sentinel) {
            addFirst(x);
        } else if (cursor.next == sentinel) {
            addLast(x);
        } else {
            this.size += 1;
            Node oldBack = cursor;
            Node newNode = new Node(oldBack, x, cursor.next);
            cursor.next.prev = newNode;
            oldBack.next = newNode;
            cursor = newNode;
        }
    }

    protected void addFirst(String x) {
        this.size += 1;
        Node oldFront = sentinel.next;
        Node newNode = new Node(sentinel, x, oldFront);
        oldFront.prev = newNode;
        sentinel.next = newNode;
        cursor = newNode;
    }

    protected void addLast(String x) {
        this.size += 1;
        Node oldBack = sentinel.prev;
        Node newNode = new Node(oldBack, x, sentinel);
        sentinel.prev = newNode;
        oldBack.next = newNode;
        cursor = sentinel.prev;

    }
    

    protected void delete() {   // for deletions at the end of a line
        this.size -= 1;
        Node nodeToDelete = cursor;
        cursor.prev.next = cursor.next;
        cursor.next.prev = cursor.prev;
        cursor = cursor.prev;
        nodeToDelete = null;
    }

    /* TODO: handle edge case with deleting at beginning of line
     * USE WITH CAUTION: deletes at current cursor position,
     * if cursor is pointing at "u" in "bug", will become "bg" */
    protected void delete(Node current) {
        if (current == sentinel) {
            // do nothing
        } else if (current.prev == sentinel){
            current.prev.next = current.next;
            current.next.prev = current.prev;
            cursor = sentinel;
            current = null;
        } else if (current.next == sentinel) {
            delete();
        } else {
            size -= 1;
            Node nodeToDelete = current;
            current.prev.next = current.next;
            current.next.prev = current.prev;
            cursor = current.prev;
            nodeToDelete = null;
        }
    }

    protected Node findBeginning() {
        return sentinel.next;
    }

}
