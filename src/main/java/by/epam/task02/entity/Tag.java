package by.epam.task02.entity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tag extends Entity {

    private String name;
    private Map<String, String> params = new LinkedHashMap<>();
    private List<Entity> innerTags = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<Entity> getInnerTags() {
        return innerTags;
    }

    public Entity getInnerTags(int index) {
        if (index < innerTags.size()) {
            return innerTags.get(index);
        }
        return null;
    }

    public void setInnerTags(List<Entity> innerTags) {
        this.innerTags = innerTags;
    }

    public void setInnerTags(Tag tag) {
        innerTags.add(tag);
    }

    private boolean compareFields(Tag tag) {
        if (!name.equals(tag.name) || !params.equals(tag.params)
                || !innerTags.equals(tag.innerTags)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 107 * (name.hashCode() + params.hashCode() +
                innerTags.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Tag tag = (Tag) obj;
        if (!super.equals(tag)) {
            return false;
        }
        return compareFields(tag);
    }

    @Override
    public String toString() {
        String result = "";
        result += String.format("%" + getLevel() +"s<%s", "", name);
        for (Map.Entry entry : params.entrySet()) {
            result += String.format(" %s=%s", entry.getKey(), entry.getValue());
        }
        result += ">\n";
        for (Entity tag : innerTags) {
            result += tag.toString();
        }
        result += String.format("%" + getLevel() + "s</%s>\n", "", name);
        return result;
    }

}
