package by.epam.task02.dao;

import by.epam.task02.dao.impl.XmlParserDAOImpl;

public final class DAOFactory {

    private static final DAOFactory instance = new DAOFactory();
    private final ParserDAO parserDAO = new XmlParserDAOImpl();

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        return instance;
    }

    public ParserDAO getParserDAO() {
        return parserDAO;
    }

}
