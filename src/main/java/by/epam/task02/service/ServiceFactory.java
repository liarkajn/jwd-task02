package by.epam.task02.service;

import by.epam.task02.service.impl.ParserServiceImpl;

public final class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();
    private final ParserService parserService = new ParserServiceImpl();

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return instance;
    }

    public ParserService getParserService() {
        return parserService;
    }

}
