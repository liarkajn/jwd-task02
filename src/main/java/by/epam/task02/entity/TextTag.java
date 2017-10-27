package by.epam.task02.entity;

import java.io.Serializable;

public class TextTag extends Entity implements Serializable {

    private String body = "";

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int hashCode() {
        return 107 * (body.hashCode());
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
        TextTag textTag = (TextTag) obj;
        if (!super.equals(textTag)) {
            return false;
        }
        return body.equals(textTag.body);
    }

    @Override
    public String toString() {
        return String.format("%" + getLevel() + "s%s\n", "", body);
    }

}
