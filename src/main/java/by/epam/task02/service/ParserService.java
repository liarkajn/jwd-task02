package by.epam.task02.service;

import by.epam.task02.entity.Entity;
import by.epam.task02.service.impl.exception.ServiceException;

public interface ParserService {

    Entity parse() throws ServiceException;

}
