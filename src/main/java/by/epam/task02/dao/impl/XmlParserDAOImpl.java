package by.epam.task02.dao.impl;

import by.epam.task02.dao.ParserDAO;
import by.epam.task02.dao.impl.exception.DAOException;
import by.epam.task02.dao.impl.exception.RootElementException;
import by.epam.task02.dao.impl.exception.TagFormatException;
import by.epam.task02.entity.Entity;
import by.epam.task02.entity.Stack;
import by.epam.task02.entity.Tag;
import by.epam.task02.entity.TextTag;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParserDAOImpl implements ParserDAO {

    private static final String FILENAME = "task02.xml";

    private static final String TAG_RECOGNIZER = "(<.*?>)|([^<]+)";
    private static final String OPEN_TAG_STYLE_CHECKER = "<([a-zA-Z][-_\\w]+)((\\s+[a-zA-Z][-_\\w]+=\"[^\"]+\")*)(\\s?/?)>";
    private static final String CLOSED_TAG_STYLE_CHECKER = "<(/\\s*[a-zA-Z][-_\\w]+)>";

    private Pattern tagRecognizer = Pattern.compile(TAG_RECOGNIZER);
    private Pattern openTagStyleChecker = Pattern.compile(OPEN_TAG_STYLE_CHECKER);

    private Stack<Entity> stack = new Stack<>();

    public Entity parse() throws DAOException {
        File path = new File(getResourcePath());
        processFile(path);
        try {
            if (stack.size() > 1) {
                throw new RootElementException("Too much root elements");
            }
        } catch (RootElementException ex) {
            throw new DAOException(ex.getMessage());
        }
        return stack.pop();
    }

    private String getResourcePath() throws DAOException{
        URL url = getClass().getClassLoader().getResource(FILENAME);
        if (url == null) {
            throw new DAOException("File not found");
        }
        return url.getPath();
    }

    private void processFile(File path) throws DAOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    processLine(line);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new DAOException(ex.getMessage());
        } catch (IOException ex) {
            throw new DAOException(ex.getMessage());
        }
    }

    private void processLine(String line) throws DAOException {
        Matcher matcher = tagRecognizer.matcher(line);
        while (matcher.find()) {
            try {
                processTag(matcher.group());
            } catch (TagFormatException ex) {
                throw new DAOException(ex.getMessage());
            }
        }
    }

    private void processTag(String tag) throws TagFormatException {
        if (Pattern.matches("^<.*>", tag)) {
            determineTag(tag);
        } else {
            processText(tag);
        }
    }

    private void determineTag(String tag) throws TagFormatException {
        if (Pattern.matches(OPEN_TAG_STYLE_CHECKER, tag)) {
            processOpenTag(tag);
        } else if (Pattern.matches(CLOSED_TAG_STYLE_CHECKER, tag)) {
            processClosedTag(tag);
        } else {
            throw new TagFormatException("Invalid tag format");
        }
    }

    private void processOpenTag(String tag) {
        Matcher matcher = openTagStyleChecker.matcher(tag);
        matcher.reset(tag);
        while (matcher.find()) {
            boolean isFinalTag = matcher.group(4) != null && !matcher.group(4).isEmpty();
            addTagToStack(matcher.group(1), matcher.group(2), isFinalTag);
        }
    }

    private void addTagToStack(String name, String params, boolean isFinal) {
        int nestedLevel = stack.getLevel();
        nestedLevel++;
        Entity entity = createTag(name, params, nestedLevel);
        stack.push(entity);
        if (isFinal) {
            nestedLevel--;
        }
        stack.setLevel(nestedLevel);
    }

    private Tag createTag(String name, String params, int nestedLevel) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setLevel(nestedLevel);
        if (params != null && !params.isEmpty()) {
            tag.setParams(processParams(params));
        }
        return tag;
    }

    private Map<String, String> processParams(String paramsString) {
        Map<String, String> params = new LinkedHashMap<>();
        paramsString = paramsString.trim();
        String[] paramsArray = paramsString.split("\\s");
        for (String param : paramsArray) {
            String[] paramValue = param.split("=");
            params.put(paramValue[0], paramValue[1]);
        }
        return params;
    }

    private void processClosedTag(String tagString) throws TagFormatException {
        String tagName = tagString.substring(2, tagString.length() - 1);
        LinkedList<Entity> innerTags = new LinkedList<>();
        boolean isFound = false;
        Entity entity;
        while (!isFound && (entity = stack.pop()) != null) {
            if (entity.getLevel() > stack.getLevel()) {
                innerTags.addFirst(entity);
            } else {
                if (compareTags(tagName, entity)) {
                    Tag tag = (Tag) entity;
                    tag.setInnerTags(innerTags);
                    stack.push(tag);
                } else {
                    throw new TagFormatException("Different names of the opening and closing tags.");
                }
                isFound = true;
            }
        }
        stack.setLevel(stack.getLevel() - 1);
    }

    private boolean compareTags(String tagName, Entity entity) {
        if (entity instanceof Tag) {
            Tag tag = (Tag) entity;
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    private void processText(String tag) {
        int nestedLevel = stack.getLevel();
        nestedLevel++;
        TextTag textTag = createTextTag(tag, nestedLevel);
        nestedLevel--;
        stack.setLevel(nestedLevel);
        stack.push(textTag);
    }

    private TextTag createTextTag(String tag, int nestedLevel) {
        TextTag textTag;
        if (isLastTextTag()) {
            textTag = (TextTag) stack.pop();
            textTag.setBody(textTag.getBody() + " " + tag);
        } else {
            textTag = new TextTag();
            textTag.setLevel(nestedLevel);
            textTag.setBody(tag);
        }
        return textTag;
    }

    private boolean isLastTextTag() {
        boolean isLastText = true;
        Entity entity = stack.pop();
        if (!(entity instanceof TextTag)) {
            isLastText = false;
        }
        stack.push(entity);
        return isLastText;
    }

}
