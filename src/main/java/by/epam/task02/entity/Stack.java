package by.epam.task02.entity;

import java.util.LinkedList;

public class Stack<T> {

    private LinkedList linkedList;
    private int level;

    public Stack() {
        linkedList = new LinkedList();
    }

    public void push(T obj) {
        linkedList.addFirst(obj);
    }

    public T pop() {
        return (T)linkedList.pollFirst();
    }

    public int size() {
        return linkedList.size();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
