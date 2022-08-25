package by.epam.shaturko.controller.command.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.shaturko.controller.Constant;
import by.epam.shaturko.controller.PagePath;
import by.epam.shaturko.controller.SessionAttribute;
import by.epam.shaturko.controller.command.Command;
import by.epam.shaturko.entity.tour.Categories;
import by.epam.shaturko.entity.tour.TypeOfFood;
import by.epam.shaturko.entity.tour.TypeOfPlacement;
import by.epam.shaturko.entity.tour.TypeOfRoom;
import by.epam.shaturko.service.ServiceGettingData;
import by.epam.shaturko.service.ServiceProvider;
import by.epam.shaturko.service.exception.ServiceException;

public class GoToMainPage implements Command {
	private final static GoToMainPage INSTANCE = new GoToMainPage();
	private final static Logger logger = LogManager.getLogger();
	private final static String ANY = "any";

	private GoToMainPage() {
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		List<Integer> listOfDurations = new ArrayList<>();
		for (int i = 7; i < 31; i++) {
			listOfDurations.add(i);
		}
		Map<String, String> numbers = new HashMap<>();
		for (int i = 1; i < 5; i++) {
			numbers.put(Integer.toString(i), TypeOfPlacement.values()[i - 1].toString());
		}
		numbers.put(ANY, null);
		
		HttpSession session = request.getSession();
		ServiceProvider provider = ServiceProvider.getInstance();
		ServiceGettingData serviceGettingData = provider.getServiceGettingData();
		try {
			List<String> listOfCountriesNames = serviceGettingData.getListOfCountriesNames();
			session.setAttribute(SessionAttribute.REQUEST_URL, Constant.URL_TO_MAIN_PAGE);
			session.setAttribute(SessionAttribute.DURATIONS, listOfDurations);
			session.setAttribute(SessionAttribute.FOOD, TypeOfFood.values());
			session.setAttribute(SessionAttribute.PLACEMENTS, TypeOfPlacement.values());
			session.setAttribute(SessionAttribute.ROOMS, TypeOfRoom.values());
			session.setAttribute(SessionAttribute.CATEGORIES, Categories.values());
			session.setAttribute(SessionAttribute.COUNTRIES, listOfCountriesNames);
			session.setAttribute(SessionAttribute.NUMBERS, numbers);
			session.removeAttribute(SessionAttribute.TOURS_REQUEST);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Error while getting list of countries names", e);
			session.setAttribute(SessionAttribute.ERROR_TYPE, Constant.ERROR_500);
			session.setAttribute(SessionAttribute.ERROR_MESSAGE, e);
			response.sendRedirect(Constant.ERROR_COMMAND);
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PagePath.MAIN);
		requestDispatcher.forward(request, response);
	}

	public static GoToMainPage getInstance() {
		return INSTANCE;
	}
}
