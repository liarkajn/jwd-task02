package by.epam.task02.main;

import by.epam.task02.service.ServiceFactory;
import by.epam.task02.service.ParserService;
import by.epam.task02.service.impl.exception.ServiceException;

public class Main {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ParserService parserService = serviceFactory.getParserService();
        try {
            PrintInfo.print(parserService.parse());
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
        }
    }

}