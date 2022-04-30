package zcu.kiv.pt;

/******************************************************************************
 * Instances of class DLinkedList are a bit crippled LinkedLists of double type
 * which only can add or remove nodes
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class DLinkedList {
    //== VARIABLE INSTANCE ATTRIBUTES ==========================================
    /** head of the list */
    private Link first;
    /** tail of the list */
    private Link last;

    //== PUBLIC METHODS OF INSTANCES ===========================================
    /**
     * adds new node with given value
     * @param value value of new node
     */
    public void add(double value) {
        Link newLink = new Link(value);
        if (first == null) {
            first = last = newLink;
        } else {
            last.next = newLink;
            last = newLink;
        }
    }

    /**
     * removes and returns value of head element of the list
     * @throws NullPointerException when list is empty
     * @return double value of head
     */
    public double remove() {
        if (first == null) {
            throw new NullPointerException("Linked list is empty");
        }
        double result = first.value;
        first = first.next;
        return result;
    }

    /**
     * class representing one element of the Linked list
     */
    static class Link {
        /** reference to the next Link */
        private Link next;
        /** value of the element */
        private final double value;

        /** simple constructor */
        public Link(double value) {
            this.value = value;
        }
    }
}
