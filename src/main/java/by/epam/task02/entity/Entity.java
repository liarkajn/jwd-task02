package by.epam.task02.entity;

abstract public class Entity {

    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    abstract public String toString();

}
