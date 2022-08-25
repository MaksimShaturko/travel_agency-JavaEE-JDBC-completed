package by.epam.shaturko.controller.command.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.shaturko.bean.user.User;
import by.epam.shaturko.controller.Constant;
import by.epam.shaturko.controller.SessionAttribute;
import by.epam.shaturko.controller.command.Command;
import by.epam.shaturko.service.ServiceGettingData;
import by.epam.shaturko.service.ServiceProvider;
import by.epam.shaturko.service.exception.ServiceException;

public class GetAllUsers implements Command {
	private final static GetAllUsers INSTANCE = new GetAllUsers();
	private final static Logger logger = LogManager.getLogger();

	private GetAllUsers() {
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ServiceProvider provider = ServiceProvider.getInstance();
		ServiceGettingData gettingData = provider.getServiceGettingData();
		HttpSession session = request.getSession(true);
		String requestUrl = Constant.URL_TO_USERS_PAGE;
		try {
			List<User> listOfUsers = gettingData.getAllUsers();
			session.setAttribute(SessionAttribute.LIST_OF_USERS, listOfUsers);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Error while getting all users", e);
			session.setAttribute(SessionAttribute.ERROR_TYPE, Constant.ERROR_500);
			session.setAttribute(SessionAttribute.ERROR_MESSAGE, e);
			requestUrl = Constant.ERROR_COMMAND;
		}
		response.sendRedirect(requestUrl);
	}

	public static GetAllUsers getInstance() {
		return INSTANCE;
	}
}