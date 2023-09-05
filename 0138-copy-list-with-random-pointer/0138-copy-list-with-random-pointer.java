/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

class Solution {
    public Node copyRandomList(Node head) {
        if (head == null) {
        return null;
    }

    // Step 1: Duplicate each node and insert it after the original node
    Node current = head;
    while (current != null) {
        Node newNode = new Node(current.val);
        newNode.next = current.next;
        current.next = newNode;
        current = current.next.next;
    }

    // Step 2: Assign random pointers to the duplicated nodes
    current = head;
    while (current != null) {
        if (current.random != null) {
            current.next.random = current.random.next;
        }
        current = current.next.next;
    }

    // Step 3: Separate the original list and the copied list
    Node newHead = head.next;
    Node newCurrent = newHead;
    current = head;

    while (current != null) {
        current.next = current.next.next;
        if (newCurrent.next != null) {
            newCurrent.next = newCurrent.next.next;
        }
        current = current.next;
        newCurrent = newCurrent.next;
    }

    return newHead;
    }
}