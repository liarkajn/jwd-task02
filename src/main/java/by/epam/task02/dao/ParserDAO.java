package by.epam.task02.dao;

import by.epam.task02.dao.impl.exception.DAOException;
import by.epam.task02.entity.Entity;

public interface ParserDAO {

    Entity parse() throws DAOException;

}
