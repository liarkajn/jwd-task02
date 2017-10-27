package by.epam.task02.service.impl;

import by.epam.task02.dao.DAOFactory;
import by.epam.task02.dao.ParserDAO;
import by.epam.task02.dao.impl.exception.DAOException;
import by.epam.task02.entity.Entity;
import by.epam.task02.service.ParserService;
import by.epam.task02.service.impl.exception.ServiceException;

public class ParserServiceImpl implements ParserService {

    public Entity parse() throws ServiceException{
        DAOFactory daoFactory = DAOFactory.getInstance();
        ParserDAO parserDAO = daoFactory.getParserDAO();
        Entity entity;
        try {
            entity = parserDAO.parse();
        } catch (DAOException ex) {
            throw new ServiceException(ex.getMessage());
        }
        return entity;
    }

}
