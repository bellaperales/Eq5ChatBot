package com.springboot.MyTodoList.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.EmployeeItemService;
import com.springboot.MyTodoList.service.ProjectItemService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotHelper;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private ProjectItemService projectItemService;
	private EmployeeItemService employeeItemService;
	private String botName;
	private Boolean isProject = false;
	private Boolean isTask = false;
	private boolean isWaitingForRole = false;
	private boolean isWaitingForTask = false;
	private boolean isWaitingForEmployees = false;
	private int userRole = 0;
	private EmployeeItem currentEmployee;

	private String nameTask;
	private String descriptionTask;
	private Timestamp datelimit;
	private String type;
	private int step = 0;

	public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService,
			ProjectItemService projectItemService, EmployeeItemService employeeItemService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.projectItemService = projectItemService;
		this.employeeItemService = employeeItemService;
		this.botName = botName;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			if (messageTextFromTelegram.equals(BotCommands.START.getCommand())) {
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.INITIAL_MESSAGE.getMessage());
				isWaitingForRole = true;
				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForRole) {
				try {
					int mynumber = Integer.parseInt(messageTextFromTelegram);
					Optional<EmployeeItem> employee = employeeItemService.getEmployeeItemByMynumber(mynumber);

					currentEmployee = employee.get();
					boolean isManager = currentEmployee.getManager();

					if (isManager) {
						userRole = 2;
						isWaitingForRole = false;
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(
								"Bienvenido gerente al Chat Bot de Oracle. Selecciona /start para comenzar.");
						execute(messageToTelegram);
					} else {
						userRole = 1;
						isWaitingForRole = false;
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(
								"Bienvenido desarrollador al Chat Bot de Oracle. Selecciona /start para comenzar.");
						execute(messageToTelegram);
					}
				} catch (NumberFormatException e) {
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText("Formato inválido.");
					try {
						execute(messageToTelegram);
					} catch (TelegramApiException ex) {
						logger.error(ex.getLocalizedMessage(), ex);
					}
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				} catch (Exception e) {
					// Handle any other exceptions, e.g., if the mynumber is not found
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText("Usuario no encontrado.");
					try {
						execute(messageToTelegram);
					} catch (TelegramApiException ex) {
						logger.error(ex.getLocalizedMessage(), ex);
					}
				}
			}

			else if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand()) && userRole == 1) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first row
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
				// Add the first row to the keyboard
				keyboard.add(row);

				// Set the keyboard
				keyboardMarkup.setKeyboard(keyboard);

				// Add the keyboard markup
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand()) && userRole == 2) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first row
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_PROJECTS.getLabel());
				row.add(BotLabels.ADD_NEW_PROJECT.getLabel());
				keyboard.add(row);

				row = new KeyboardRow();
				row.add(BotLabels.LIST_EMPLOYEES.getLabel());
				row.add(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel());

				keyboard.add(row);

				// Set the keyboard
				keyboardMarkup.setKeyboard(keyboard);

				// Add the keyboard markup
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (userRole == 1 && messageTextFromTelegram.equals(BotCommands.BACK_COMMAND.getCommand())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())
					|| userRole == 1 && messageTextFromTelegram
							.equals(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel())) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.MAIN_SCREEN.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first row
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
				// Add the first row to the keyboard
				keyboard.add(row);
				// Set the keyboard
				keyboardMarkup.setKeyboard(keyboard);

				// Add the keyboard markup
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (userRole == 2 && messageTextFromTelegram.equals(BotCommands.BACK_COMMAND.getCommand())
					|| userRole == 2 && messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())
					|| userRole == 2 && messageTextFromTelegram
							.equals(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel())) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.MAIN_SCREEN.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first row
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_PROJECTS.getLabel());
				row.add(BotLabels.ADD_NEW_PROJECT.getLabel());
				keyboard.add(row);

				row = new KeyboardRow();
				row.add(BotLabels.LIST_EMPLOYEES.getLabel());
				row.add(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel());

				keyboard.add(row);

				// Set the keyboard
				keyboardMarkup.setKeyboard(keyboard);

				// Add the keyboard markup
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (messageTextFromTelegram.indexOf(BotLabels.DONE_TASK.getLabel()) != -1) {
				StringBuilder stringBuilder = new StringBuilder();
				String done = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DONE_TASK.getLabel()));

				for (char c : done.toCharArray()) {
					if (Character.isDigit(c)) {
						stringBuilder.append(c);
					}
				}

				String done1 = stringBuilder.toString();
				Integer id = Integer.valueOf(done1);

				try {
					ToDoItem item = getToDoItemById(id).getBody();
					item.setStatus(1);
					updateToDoItem(item, id);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO_TASK.getLabel()) != -1) {
				StringBuilder stringBuilder = new StringBuilder();
				String undo = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.UNDO_TASK.getLabel()));

				for (char c : undo.toCharArray()) {
					if (Character.isDigit(c)) {
						stringBuilder.append(c);
					}
				}

				String undo1 = stringBuilder.toString();
				Integer id = Integer.valueOf(undo1);
				try {

					ToDoItem item = getToDoItemById(id).getBody();
					item.setStatus(0);
					updateToDoItem(item, id);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE_TASK.getLabel()) != -1) {
				StringBuilder stringBuilder = new StringBuilder();
				String delete = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DELETE_TASK.getLabel()));

				for (char c : delete.toCharArray()) {
					if (Character.isDigit(c)) {
						stringBuilder.append(c);
					}
				}

				String delete1 = stringBuilder.toString();
				Integer id = Integer.valueOf(delete1);

				try {
					ToDoItem item = getToDoItemById(id).getBody();
					item.setStatus(2);
					updateToDoItem(item, id);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.DONE_PROJECT.getLabel()) != -1) {
				StringBuilder stringBuilder = new StringBuilder();
				String done = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DONE_PROJECT.getLabel()));

				for (char c : done.toCharArray()) {
					if (Character.isDigit(c)) {
						stringBuilder.append(c);
					}
				}

				String done1 = stringBuilder.toString();
				Integer id = Integer.valueOf(done1);

				try {
					ProjectItem project = getProjectItemById(id).getBody();
					project.setStatus(true);
					updateProjectItem(project, id);
					List<ToDoItem> allItems = getAllToDoItemsbyProjectid(id);
					List<ToDoItem> activeItems = allItems.stream()
							.filter(item -> item.getProjectID() == id)
							.collect(Collectors.toList());
					for (ToDoItem item : activeItems) {
						item.setStatus(1);
						updateToDoItem(item, item.getID());
					}
					BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_DONE.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO_PROJECT.getLabel()) != -1) {
				StringBuilder stringBuilder = new StringBuilder();
				String undo = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.UNDO_PROJECT.getLabel()));

				for (char c : undo.toCharArray()) {
					if (Character.isDigit(c)) {
						stringBuilder.append(c);
					}
				}

				String undo1 = stringBuilder.toString();
				Integer id = Integer.valueOf(undo1);

				try {
					ProjectItem project = getProjectItemById(id).getBody();
					project.setStatus(false);
					updateProjectItem(project, id);
					List<ToDoItem> allItems = getAllToDoItemsbyProjectid(id);
					List<ToDoItem> activeItems = allItems.stream()
							.filter(item -> item.getProjectID() == id)
							.collect(Collectors.toList());
					for (ToDoItem item : activeItems) {
						item.setStatus(0);
						updateToDoItem(item, item.getID());
					}
					BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_UNDONE.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE_PROJECT.getLabel()) != -1) {
				StringBuilder stringBuilder = new StringBuilder();
				String delete = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DELETE_PROJECT.getLabel()));

				for (char c : delete.toCharArray()) {
					if (Character.isDigit(c)) {
						stringBuilder.append(c);
					}
				}

				String delete1 = stringBuilder.toString();
				Integer id = Integer.valueOf(delete1);

				try {
					deleteProjectItem(id).getBody();
					BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_DELETED.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

			} else if (userRole == 1 && messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {
				List<ToDoItem> allItems = toDoItemService.findByEmployeeidOrderByDatelimitDesc(currentEmployee.getID());
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// command back to main screen
				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				KeyboardRow firstRow = new KeyboardRow();
				firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
				keyboard.add(firstRow);

				List<ToDoItem> activeItems = allItems.stream()
						.filter(item -> item.getStatus() == 0)
						.collect(Collectors.toList());

				for (ToDoItem item : activeItems) {

					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getDescription());
					currentRow.add(item.getDateLimit().toString());
					currentRow.add(BotLabels.EMOJI_DONE.getLabel() + item.getID() + BotLabels.DONE_TASK.getLabel());
					keyboard.add(currentRow);
				}

				List<ToDoItem> doneItems = allItems.stream()
						.filter(item -> item.getStatus() == 1)
						.collect(Collectors.toList());

				for (ToDoItem item : doneItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getDescription());
					currentRow.add(BotLabels.EMOJI_UNDO.getLabel() + item.getID() + BotLabels.UNDO_TASK.getLabel());
					currentRow.add(BotLabels.EMOJI_DELETE.getLabel() + item.getID() + BotLabels.DELETE_TASK.getLabel());
					keyboard.add(currentRow);
				} 
				// command back to main screen
				keyboardMarkup.setKeyboard(keyboard);

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			 else if (userRole == 1 && messageTextFromTelegram.contains(BotLabels.TASK_INDICATOR.getLabel())) {
				String[] parts = messageTextFromTelegram.split(BotLabels.TASK_INDICATOR.getLabel(), 2);
				Integer id;
				id = Integer.valueOf(parts[0].trim());
				ToDoItem item = getToDoItemById(id).getBody();
				if (item != null) {
					String message = "Información de la tarea: " + "\n" +
							"Nombre de la tarea: " + item.getName() + "\n" +
							"Descripción: " + item.getDescription() + "\n" +
							"Fecha Limite: " + item.getDateLimit() + "\n" +
							"Tipo de Tarea: " + item.getType();
					BotHelper.sendMessageToTelegram(chatId, message, this);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_INFORMATION_BACK.getMessage(), this);

				} else {
					BotHelper.sendMessageToTelegram(chatId, "Error.", this);
				}

			}else if (userRole == 2 && messageTextFromTelegram.equals(BotCommands.PROJECT_LIST.getCommand())
					|| userRole == 2 && messageTextFromTelegram.equals(BotLabels.LIST_ALL_PROJECTS.getLabel())) {

				List<ProjectItem> allProjects = getAllProjectItems();
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// command back to main screen
				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				List<ProjectItem> activeProjects = allProjects.stream().filter(item -> item.getStatus() == false)
						.collect(Collectors.toList());

				for (ProjectItem item : activeProjects) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getName());
					currentRow.add(BotLabels.EMOJI_DONE.getLabel() + item.getID() + BotLabels.DONE_PROJECT.getLabel());
					keyboard.add(currentRow);
				}

				List<ProjectItem> doneProjects = allProjects.stream().filter(item -> item.getStatus() == true)
						.collect(Collectors.toList());

				for (ProjectItem item : doneProjects) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getName());
					currentRow.add(BotLabels.EMOJI_UNDO.getLabel() + item.getID() + BotLabels.UNDO_PROJECT.getLabel());
					currentRow.add(
							BotLabels.EMOJI_DELETE.getLabel() + item.getID() + BotLabels.DELETE_PROJECT.getLabel());
					keyboard.add(currentRow);
				}

				// command back to main screen
				keyboardMarkup.setKeyboard(keyboard);

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotLabels.MY_PROJECT_LIST.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (userRole == 1 && messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel())) {
				try {
					isWaitingForTask = true;
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());
					ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
					messageToTelegram.setReplyMarkup(keyboardMarkup);

					// send message
					execute(messageToTelegram);
					step = 2;

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			}

			else if (userRole == 2 && messageTextFromTelegram.equals(BotCommands.ADD_PROJECT.getCommand())
					|| userRole == 2 && messageTextFromTelegram.equals(BotLabels.ADD_NEW_PROJECT.getLabel())) {
				try {
					if (currentEmployee.getProjectid() == 0) {
						isProject = true;
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(BotMessages.TYPE_NEW_PROJECT.getMessage());
						// hide keyboard
						ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
						messageToTelegram.setReplyMarkup(keyboardMarkup);

						// send message
						execute(messageToTelegram);
					} else {
						BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_ADD_ERROR.getMessage(), this);
					}

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			}

			else if (userRole == 2 && messageTextFromTelegram.equals(BotLabels.LIST_EMPLOYEES.getLabel())) {
				if (currentEmployee.getProjectid() == 0) {
					BotHelper.sendMessageToTelegram(chatId, BotMessages.LIST_EMPLOYEES_ERROR.getMessage(), this);
				}

				else {
					List<EmployeeItem> allEmployees = findByProjectid(currentEmployee.getProjectid());
					allEmployees = allEmployees.stream().filter(item -> item.getID() != currentEmployee.getID()).collect(Collectors.toList());
					ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					List<KeyboardRow> keyboard = new ArrayList<>();

					// command back to main screen
					KeyboardRow mainScreenRowTop = new KeyboardRow();
					mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
					keyboard.add(mainScreenRowTop);

					for (EmployeeItem item : allEmployees) {
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(item.getName() + " " + item.getLastname());
						keyboard.add(currentRow);
					}

					// command back to main screen
					keyboardMarkup.setKeyboard(keyboard);

					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotLabels.LIST_EMPLOYEES.getLabel());
					messageToTelegram.setReplyMarkup(keyboardMarkup);

					try {
						execute(messageToTelegram);
					} catch (TelegramApiException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}

			}

			else if (userRole == 2 && messageTextFromTelegram.equals(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel()) || userRole == 2 && messageTextFromTelegram.equals(BotCommands.LIST_EMPLOYEES_ITEMS.getCommand()) ) {

				if (currentEmployee.getProjectid() == 0) {
					BotHelper.sendMessageToTelegram(chatId, BotMessages.LIST_EMPLOYEES_ITEMS_ERROR.getMessage(), this);
				}

				else {
					List<ToDoItem> allTodoItems = toDoItemService.findByProjectidOrderByDatelimitDesc(currentEmployee.getProjectid());
					ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					List<KeyboardRow> keyboard = new ArrayList<>();

					// command back to main screen
					KeyboardRow mainScreenRowTop = new KeyboardRow();
					mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
					keyboard.add(mainScreenRowTop);

					KeyboardRow secondScreenRowTop = new KeyboardRow();
					secondScreenRowTop.add(BotLabels.EMOJI_PENDING.getLabel()+ BotLabels.TASKS_UNCOMPLETED.getLabel());
					keyboard.add(secondScreenRowTop);

					List<ToDoItem> activeItems = allTodoItems.stream().filter(item -> item.getStatus() == 0).collect(Collectors.toList());
					for (ToDoItem item : activeItems) {
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getDescription());
						//EmployeeItem employee = employeeItemService.getEmployeeItemByToDoItem(item);
						//currentRow.add(employee.getName() + " " + employee.getLastname());
						keyboard.add(currentRow);
					}

					KeyboardRow middleRowTop = new KeyboardRow();
					middleRowTop.add(BotLabels.EMOJI_DONE.getLabel() + BotLabels.TASKS_COMPLETED.getLabel());
					keyboard.add(middleRowTop);

					List<ToDoItem> doneItems = allTodoItems.stream().filter(item -> item.getStatus() == 1).collect(Collectors.toList());
					for (ToDoItem item : doneItems) {
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getDescription());
						//EmployeeItem employee = employeeItemService.getEmployeeItemByToDoItem(item);
						//currentRow.add(employee.getName() + " " + employee.getLastname());
						keyboard.add(currentRow);
					}

					// command back to main screen
					keyboardMarkup.setKeyboard(keyboard);

					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel());
					messageToTelegram.setReplyMarkup(keyboardMarkup);

					try {
						execute(messageToTelegram);
					} catch (TelegramApiException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}

			else if(userRole == 2 && messageTextFromTelegram.contains(BotLabels.TASK_INDICATOR.getLabel())) {
				String[] parts = messageTextFromTelegram.split(BotLabels.TASK_INDICATOR.getLabel(), 2);
				Integer id;
				id = Integer.valueOf(parts[0].trim());
				ToDoItem item = getToDoItemById(id).getBody();
				if (item != null) {
					String message = "Información de la tarea: " + "\n" +
							"Nombre de la tarea: " + item.getName() + "\n" +
							"Descripción: " + item.getDescription() + "\n" +
							"Fecha Limite: " + item.getDateLimit() + "\n" +
							"Tipo de Tarea: " + item.getType();
					BotHelper.sendMessageToTelegram(chatId, message, this);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.TASK_INFORMATION_BACK_MANAGER.getMessage(), this);

				} else {
					BotHelper.sendMessageToTelegram(chatId, "Error.", this);
				}
			}

			else if (isWaitingForTask && step == 2) {
				try {
					// isWaitingForTask = false;
					nameTask = messageTextFromTelegram;
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_DESCRIPTION.getMessage());
					execute(messageToTelegram);
					step = 3;
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForTask && step == 3) {
				try {
					// isWaitingForTask = false;
					descriptionTask = messageTextFromTelegram;
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_DATE_LIMIT.getMessage());
					execute(messageToTelegram);
					step = 4;
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForTask && step == 4) {
				try {
					// isWaitingForTask = false;
					datelimit = Timestamp.valueOf(messageTextFromTelegram);
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_TYPE.getMessage());
					execute(messageToTelegram);
					step = 5;
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForTask && step == 5) {
				try {
					type = messageTextFromTelegram;
					step = 0;
					isWaitingForTask = false;
					// isTask = false;
					ToDoItem newItem = new ToDoItem();
					newItem.setName(nameTask);
					newItem.setDescription(descriptionTask);
					newItem.setDateCreated(new java.sql.Timestamp(System.currentTimeMillis()));
					newItem.setStatus(0);
					newItem.setDateLimit(datelimit);
					newItem.setType(type);
					newItem.setEmployeeID(currentEmployee.getID());
					newItem.setProjectID(currentEmployee.getProjectid());

					ResponseEntity entity = addToDoItem(newItem);

					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.NEW_ITEM_ADDED.getMessage()
							+ "\n\nLa siguiente tarea fue agregada: " + nameTask + "\n\ncon la siguiente descripción: "
							+ descriptionTask);

					execute(messageToTelegram);
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isProject) {
				try {
					isProject = false;
					ProjectItem newProject = new ProjectItem();
					newProject.setName(messageTextFromTelegram);
					newProject.setDateStart(new java.sql.Timestamp(System.currentTimeMillis()));
					newProject.setDateEnd(new java.sql.Timestamp(System.currentTimeMillis()));
					newProject.setStatus(false);
					newProject.setDepartamentID(currentEmployee.getDepartamentid());
					step = 2;

					currentEmployee.setProjectid(projectItemService.addProjectItem(newProject).getID());
					employeeItemService.updateEmployeeItem(currentEmployee.getID(), currentEmployee);

					ResponseEntity entity = addProjectItem(newProject);

					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.NEW_PROJECT_ADDED.getMessage()
							+ "\n\nEl siguiente proyecto fue agregado: " + messageTextFromTelegram);

					execute(messageToTelegram);

					
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
			
			else if (step == 2 && messageTextFromTelegram.equals(BotCommands.ADD_EMPLOYEE_TO_PROJECT.getCommand())) {
				List<EmployeeItem> allEmployees = findByDepartamentid(currentEmployee.getDepartamentid());
				//exclude current employe
				List<EmployeeItem> employeesNotInProject = allEmployees.stream().filter(item -> item.getID() != currentEmployee.getID() && item.getProjectid() != currentEmployee.getProjectid()).collect(Collectors.toList());		
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// command back to main screen
				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				for (EmployeeItem item : employeesNotInProject) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getName() + " " + item.getLastname());
					keyboard.add(currentRow);
				}

				// command back to main screen
				keyboardMarkup.setKeyboard(keyboard);

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotLabels.LIST_EMPLOYEES.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);	

				step = 3;

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (step == 3) {
				try {
					String[] parts = messageTextFromTelegram.split(" ");
					String name = parts[0];
					String lastname = parts[1];
					EmployeeItem employee = employeeItemService.getEmployeeItemByNameAndLastname(name, lastname);
					employee.setProjectid(currentEmployee.getProjectid());
					employeeItemService.updateEmployeeItem(employee.getID(), employee);
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.EMPLOYEE_ADDED.getMessage());
					execute(messageToTelegram);

					step = 2;
					
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else {
				try {
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.ERROR.getMessage());
					execute(messageToTelegram);
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	// GET /todolist
	public List<ToDoItem> getAllToDoItems(int employeeid) {
		return toDoItemService.findByEmployeeid(employeeid);
	}

	// GET /employee/{projectid}
    public List<ToDoItem> getAllToDoItemsbyProjectid(int projectid) {
        return toDoItemService.findByProjectid(projectid);
    }

	public List<EmployeeItem> findByProjectid(int projectid) {
		return employeeItemService.findByProjectid(projectid);
	}

	public List<EmployeeItem> findByDepartamentid(int departamentid) {
		return employeeItemService.findByDepartamentid(departamentid);
	}

	// GET BY ID /todolist/{id}
	public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
		try {
			ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
			return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// PUT /todolist
	public ResponseEntity addToDoItem(@RequestBody ToDoItem todoItem) throws Exception {
		ToDoItem td = toDoItemService.addToDoItem(todoItem);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", "" + td.getID());
		responseHeaders.set("Access-Control-Expose-Headers", "location");
		// URI location = URI.create(""+td.getID())

		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	// UPDATE /todolist/{id}
	public ResponseEntity updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
		try {
			ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
			System.out.println(toDoItem1.toString());
			return new ResponseEntity<>(toDoItem1, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// DELETE todolist/{id}
	public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id) {
		Boolean flag = false;
		try {
			flag = toDoItemService.deleteToDoItem(id);
			return new ResponseEntity<>(flag, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
		}
	}

	// GET /projectlist
	public List<ProjectItem> getAllProjectItems() {
		return projectItemService.findAll();
	}

	// GET BY ID /projectlist/{id}
	public ResponseEntity<ProjectItem> getProjectItemById(@PathVariable int id) {
		try {
			ResponseEntity<ProjectItem> responseEntity = projectItemService.getProjectItemById(id);
			return new ResponseEntity<ProjectItem>(responseEntity.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// PUT /projectlist
	public ResponseEntity addProjectItem(@RequestBody ProjectItem projectItem) throws Exception {
		ProjectItem td = projectItemService.addProjectItem(projectItem);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", "" + td.getID());
		responseHeaders.set("Access-Control-Expose-Headers", "location");
		// URI location = URI.create(""+td.getID())

		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	// UPDATE /todolist/{id}
	public ResponseEntity updateProjectItem(@RequestBody ProjectItem projectItem, @PathVariable int id) {
		try {
			ProjectItem projectItem1 = projectItemService.updateProjectItem(id, projectItem);
			System.out.println(projectItem1.toString());
			return new ResponseEntity<>(projectItem1, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// DELETE todolist/{id}
	public ResponseEntity<Boolean> deleteProjectItem(@PathVariable("id") int id) {
		Boolean flag = false;
		try {
			flag = projectItemService.deleteProjectItem(id);
			return new ResponseEntity<>(flag, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
		}
	}

}