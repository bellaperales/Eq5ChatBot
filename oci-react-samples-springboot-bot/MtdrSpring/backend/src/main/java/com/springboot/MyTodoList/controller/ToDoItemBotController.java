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
	private boolean isWaitingForRole = false;
	private boolean isWaitingForTask = false;
	private boolean isEmployeeList = false;
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
			logger.info("Message: "+messageTextFromTelegram+" "+chatId);

			if (messageTextFromTelegram.equals(BotCommands.START.getCommand())) {
				logger.info("Inicio de la conversación. Se envia mensaje de bienvenida para solicitar el número de empleado.");

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
				logger.info("Esperando por el rol del usuario. Se valida el número de empleado.");
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
					} else if (!isManager){
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
				logger.info("Deteccion de rol 1, siendo un desarrollador. Se envia mensaje de bienvenida.");
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
				keyboard.add(row);
				keyboardMarkup.setKeyboard(keyboard);
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand()) && userRole == 2) {
				logger.info("Deteccion de rol 2, siendo un manager. Se envia mensaje de bienvenida.");
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.PROJECT_INFORMATION.getLabel());
				row.add(BotLabels.ADD_NEW_PROJECT.getLabel());
				keyboard.add(row);

				row = new KeyboardRow();
				row.add(BotLabels.LIST_EMPLOYEES.getLabel());
				row.add(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel());
				keyboard.add(row);
				keyboardMarkup.setKeyboard(keyboard);
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
				logger.info("Regresar a la pagina principal de un desarrollador.");
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.MAIN_SCREEN.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
				keyboard.add(row);
				keyboardMarkup.setKeyboard(keyboard);
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
				logger.info("Regresar a la pagina principal de un manager.");
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.MAIN_SCREEN.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.PROJECT_INFORMATION.getLabel());
				row.add(BotLabels.ADD_NEW_PROJECT.getLabel());
				keyboard.add(row);
				row = new KeyboardRow();
				row.add(BotLabels.LIST_EMPLOYEES.getLabel());
				row.add(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel());
				keyboard.add(row);
				keyboardMarkup.setKeyboard(keyboard);
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (messageTextFromTelegram.indexOf(BotLabels.DONE_TASK.getLabel()) != -1) {
				logger.info("Tarea completada.");
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
				logger.info("Tarea deshecha.");
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
				logger.info("Tarea eliminada.");
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
				logger.info("Proyecto completado.");
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
					
					List<ToDoItem> activeItems = getAllToDoItemsbyProjectId(id);
					for (ToDoItem item : activeItems) {
						item.setStatus(1);
						updateToDoItem(item, item.getID());
					}
					BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_DONE.getMessage(), this);
			
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO_PROJECT.getLabel()) != -1) {
				logger.info("Proyecto deshecho.");
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
							//.filter(item -> item.getStatus() == 1)
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
				logger.info("Eliminar proyecto. No se utiliza.");
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
				logger.info("Ocultar pantalla principal. No se utiliza.");
				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

			} else if (userRole == 1 && messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {
				logger.info("Lista de tareas de un desarrollador, completas e incompletas. Vista de desarrollador.");
				List<ToDoItem> allItems = toDoItemService.findByEmployeeidOrderByDatelimitAsc(currentEmployee.getID());
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				KeyboardRow firstRow = new KeyboardRow();
				firstRow.add(BotLabels.EMOJI_ADD.getLabel() + BotLabels.ADD_NEW_ITEM.getLabel());
				keyboard.add(firstRow);

				List<ToDoItem> activeItems = allItems.stream()
						.filter(item -> item.getStatus() == 0)
						.collect(Collectors.toList());

				for (ToDoItem item : activeItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
					currentRow.add(BotLabels.EMOJI_DONE.getLabel() + item.getID() + BotLabels.DONE_TASK.getLabel());
					keyboard.add(currentRow);
				}

				List<ToDoItem> doneItems = allItems.stream()
						.filter(item -> item.getStatus() == 1)
						.collect(Collectors.toList());

				for (ToDoItem item : doneItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
					currentRow.add(BotLabels.EMOJI_UNDO.getLabel() + item.getID() + BotLabels.UNDO_TASK.getLabel());
					currentRow.add(BotLabels.EMOJI_DELETE.getLabel() + item.getID() + BotLabels.DELETE_TASK.getLabel());
					keyboard.add(currentRow);
				} 
				keyboardMarkup.setKeyboard(keyboard);

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel() + "\n"  + "\n" + BotLabels.EMOJI_WARNING.getLabel() + BotLabels.SELECT_TASK.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			 else if (userRole == 1 && messageTextFromTelegram.contains(BotLabels.TASK_INDICATOR.getLabel())) {
				logger.info("Información de la tarea de un desarrollador. Vista de desarrollador. Levanta las tareas disponibles.");
				String[] parts = messageTextFromTelegram.split(BotLabels.TASK_INDICATOR.getLabel(), 2);
				Integer id;
				id = Integer.valueOf(parts[0].trim());
				ToDoItem item1 = getToDoItemById(id).getBody();
				if (item1 != null) {
					String message = BotLabels.EMOJI_INFO.getLabel() + "*Información de la Tarea:* " + "\n" + "\n" + 
							"*Nombre de la Tarea:* " + item1.getName() + "\n" +
							"*Descripción:* " + item1.getDescription() + "\n" +
							"*Fecha Límite:* " + item1.getDateLimit() + "\n" +
							"*Tipo de Tarea:* " + item1.getType();
							SendMessage formattedMessage = new SendMessage();
							formattedMessage.setChatId(chatId);
							formattedMessage.setText(message);
							formattedMessage.setParseMode("Markdown");
						
							try {
								execute(formattedMessage);
							} catch (TelegramApiException e) {
								  logger.error(e.getLocalizedMessage(), e);
							} 

				} else {
					BotHelper.sendMessageToTelegram(chatId, "Error.", this);
				}

				List<ToDoItem> allItems = toDoItemService.findByEmployeeidOrderByDatelimitAsc(currentEmployee.getID());
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// command back to main screen
				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				KeyboardRow firstRow = new KeyboardRow();
				firstRow.add(BotLabels.EMOJI_ADD.getLabel() + BotLabels.ADD_NEW_ITEM.getLabel());
				keyboard.add(firstRow);

				List<ToDoItem> activeItems = allItems.stream()
						.filter(item -> item.getStatus() == 0)
						.collect(Collectors.toList());

				for (ToDoItem item : activeItems) {

					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
					currentRow.add(BotLabels.EMOJI_DONE.getLabel() + item.getID() + BotLabels.DONE_TASK.getLabel());
					keyboard.add(currentRow);
				}

				List<ToDoItem> doneItems = allItems.stream()
						.filter(item -> item.getStatus() == 1)
						.collect(Collectors.toList());

				for (ToDoItem item : doneItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
					currentRow.add(BotLabels.EMOJI_UNDO.getLabel() + item.getID() + BotLabels.UNDO_TASK.getLabel());
					currentRow.add(BotLabels.EMOJI_DELETE.getLabel() + item.getID() + BotLabels.DELETE_TASK.getLabel());
					keyboard.add(currentRow);
				} 
				keyboardMarkup.setKeyboard(keyboard);
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				//messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel() + "\n"  + "\n" + BotLabels.EMOJI_WARNING.getLabel() + BotLabels.SELECT_TASK.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);
				messageToTelegram.setParseMode("Markdown");

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (userRole == 2 && messageTextFromTelegram.equals(BotCommands.PROJECT_LIST.getCommand())
					|| userRole == 2 && messageTextFromTelegram.equals(BotLabels.LIST_ALL_PROJECTS.getLabel())) {
				logger.info("Lista de proyectos de un manager. Vista de manager. No se utiliza.");
				List<ProjectItem> allProjects = getAllProjectItems();
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

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

			else if (userRole == 2 && messageTextFromTelegram.equals(BotLabels.PROJECT_INFORMATION.getLabel())) {
				logger.info("Información del proyecto de un manager. Vista de manager.");
				if (currentEmployee.getProjectid() == 0){
					BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_INFORMATION_ERROR.getMessage(), this);
				} else {
					int projectId = currentEmployee.getProjectid();
					ProjectItem project = getProjectItemById(currentEmployee.getProjectid()).getBody();
					String message = BotLabels.EMOJI_INFO.getLabel() + "*Información del Proyecto: *" + "\n" + "\n" +
							"*Nombre del Proyecto: *" + project.getName() + "\n" +
							"*Fecha de Inicio: *" + project.getDateStart() + "\n" +
							"*Fecha de Fin: *" + project.getDateEnd();
					
					if (project.getStatus() == false) {
						ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
						List<KeyboardRow> keyboard = new ArrayList<>();
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
						keyboard.add(currentRow);
	
						KeyboardRow nextRow = new KeyboardRow();
						nextRow.add(BotLabels.EMOJI_DONE.getLabel() + String.valueOf(projectId) + BotLabels.DONE_PROJECT.getLabel());
						keyboard.add(nextRow);
						keyboardMarkup.setKeyboard(keyboard);
				
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(message);
						messageToTelegram.setReplyMarkup(keyboardMarkup);
						messageToTelegram.setParseMode("Markdown");

						try {
							execute(messageToTelegram);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					} else {
						ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
						List<KeyboardRow> keyboard = new ArrayList<>();
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
						keyboard.add(currentRow);
	
						KeyboardRow nextRow = new KeyboardRow();
						nextRow.add(BotLabels.EMOJI_UNDO.getLabel() + String.valueOf(projectId) + BotLabels.UNDO_PROJECT.getLabel());
						keyboard.add(nextRow);
						keyboardMarkup.setKeyboard(keyboard);
				
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(message);
						messageToTelegram.setReplyMarkup(keyboardMarkup);
						messageToTelegram.setParseMode("Markdown");

						try {
							execute(messageToTelegram);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					}
				}
			}

			else if (userRole == 1 && messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
					|| userRole == 1 && messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel()) || userRole == 1 && messageTextFromTelegram
					.equals(BotLabels.EMOJI_ADD.getLabel() + BotLabels.ADD_NEW_ITEM.getLabel())) {
				logger.info("Agregar tarea de un desarrollador. Vista de desarrollador.");
				try {
					isWaitingForTask = true;
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());
					ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
					messageToTelegram.setReplyMarkup(keyboardMarkup);
					execute(messageToTelegram);
					step = 2;
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (userRole == 2 && messageTextFromTelegram.equals(BotCommands.ADD_PROJECT.getCommand())
					|| userRole == 2 && messageTextFromTelegram.equals(BotLabels.ADD_NEW_PROJECT.getLabel())) {
				logger.info("Agregar proyecto de un manager. Vista de manager.");
				try {
					if (currentEmployee.getProjectid() == 0) {
						isProject = true;
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(BotMessages.TYPE_NEW_PROJECT.getMessage());
						ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
						messageToTelegram.setReplyMarkup(keyboardMarkup);
						execute(messageToTelegram);
					} else {
						BotHelper.sendMessageToTelegram(chatId, BotMessages.PROJECT_ADD_ERROR.getMessage(), this);
					}

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (userRole == 2 && messageTextFromTelegram.equals(BotLabels.LIST_EMPLOYEES.getLabel()) || userRole == 2 && messageTextFromTelegram.equals(BotCommands.LIST_EMPLOYEES.getCommand())) {
				logger.info("Lista de empleados que tiene un manager en su mismo proyecto. Vista de manager.");
				if (currentEmployee.getProjectid() == 0){
					BotHelper.sendMessageToTelegram(chatId, BotMessages.LIST_EMPLOYEES_ERROR.getMessage(), this);
				}

				else {
					List<EmployeeItem> allEmployees = findByProjectid(currentEmployee.getProjectid());
					allEmployees = allEmployees.stream().filter(item -> item.getID() != currentEmployee.getID()).collect(Collectors.toList());
					isEmployeeList = true;
					ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					List<KeyboardRow> keyboard = new ArrayList<>();

					KeyboardRow mainScreenRowTop = new KeyboardRow();
					mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
					keyboard.add(mainScreenRowTop);

					for (EmployeeItem item : allEmployees) {
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(item.getID() + " " + BotLabels.DASH.getLabel() + " " + item.getName() + " " + item.getLastname());
						keyboard.add(currentRow);
					}
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

			else if(isEmployeeList && userRole == 2 && messageTextFromTelegram.contains(BotLabels.DASH.getLabel())) {
				logger.info("Información de un empleado. Vista de manager. Levanta los empleados disponibles.");
				try {
					String[] parts = messageTextFromTelegram.split(BotLabels.DASH.getLabel(), 2);
					Integer id;
					id = Integer.valueOf(parts[0].trim());
					EmployeeItem employee = employeeItemService.getEmployeeItemById(id).getBody();	

					if (employee != null) {
						String message = BotLabels.EMOJI_INFO.getLabel() + "*Información del Empleado:* " + "\n" + "\n" + 
								"*Nombre del Empleado:* " + employee.getName() + "\n" +
								"*Apellido del Empleado:* " + employee.getLastname() + "\n" +
								"*Número de Empleado:* " + employee.getMynumber() + "\n" +
								"*Correo Electrónico:* " + employee.getMail() + "\n" +
								"*Celular:* " + employee.getCellphone();
						SendMessage formattedMessage = new SendMessage();
						formattedMessage.setChatId(chatId);
						formattedMessage.setText(message);
						formattedMessage.setParseMode("Markdown");
						ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
						formattedMessage.setReplyMarkup(keyboardMarkup);

						try {
							execute(formattedMessage);
						} catch (TelegramApiException e) {
							logger.error(e.getLocalizedMessage(), e);
						} 
					} else {
						BotHelper.sendMessageToTelegram(chatId, "Error.", this);
					}
					isEmployeeList = false;
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.EMPLOYEE_BACK.getMessage());
					//messageToTelegram.setReplyMarkup(keyboardMarkup);
					messageToTelegram.setParseMode("Markdown");

					try {
						execute(messageToTelegram);
					} catch (TelegramApiException e) {
						logger.error(e.getLocalizedMessage(), e);
					}

				}
				 catch (Exception e) {
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

			else if (userRole == 2 && messageTextFromTelegram.equals(BotLabels.LIST_ALL_EMPLOYEES_ITEMS.getLabel()) || userRole == 2 && messageTextFromTelegram.equals(BotCommands.LIST_EMPLOYEES_ITEMS.getCommand()) ) {
				logger.info("Lista de tareas de los empleados que tiene un manager en su mismo proyecto. Vista de manager.");
				if (currentEmployee.getProjectid() == 0){
					BotHelper.sendMessageToTelegram(chatId, BotMessages.LIST_EMPLOYEES_ITEMS_ERROR.getMessage(), this);
				}

				else {
					isEmployeeList = true;
					List<ToDoItem> allTodoItems = toDoItemService.findByProjectidOrderByDatelimitDesc(currentEmployee.getProjectid());
					ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
					List<KeyboardRow> keyboard = new ArrayList<>();

					KeyboardRow mainScreenRowTop = new KeyboardRow();
					mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
					keyboard.add(mainScreenRowTop);

					KeyboardRow secondScreenRowTop = new KeyboardRow();
					secondScreenRowTop.add(BotLabels.EMOJI_ARROW.getLabel() + "----------" + BotLabels.TASKS_UNCOMPLETED.getLabel()+ "----------" + BotLabels.EMOJI_ARROW.getLabel());
					keyboard.add(secondScreenRowTop);

					List<ToDoItem> activeItems = allTodoItems.stream().filter(item -> item.getStatus() == 0).collect(Collectors.toList());
					for (ToDoItem item : activeItems) {
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
						//EmployeeItem employee = item.getEmployeeID();
						//currentRow.add(employee.getName() + " " + employee.getLastname());
						//currentRow.add(employee.getID() + " " + BotLabels.DASH.getLabel() + " " + employee.getName() + " " + employee.getLastname());
						int employee = item.getEmployeeID();
						EmployeeItem employeeItem = getEmployeeItemById(employee).getBody();
						currentRow.add(employeeItem.getID() + " " + BotLabels.DASH.getLabel() + " " + employeeItem.getName() + " " + employeeItem.getLastname());
						keyboard.add(currentRow);
					}

					KeyboardRow middleRowTop = new KeyboardRow();
					middleRowTop.add(BotLabels.EMOJI_ARROW.getLabel() + "----------" + BotLabels.TASKS_COMPLETED.getLabel() + "----------" + BotLabels.EMOJI_ARROW.getLabel());
					keyboard.add(middleRowTop);

					List<ToDoItem> doneItems = allTodoItems.stream().filter(item -> item.getStatus() == 1).collect(Collectors.toList());
					for (ToDoItem item : doneItems) {
						KeyboardRow currentRow = new KeyboardRow();
						currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
						//EmployeeItem employee = item.getEmployeeID();
						//currentRow.add(employee.getID() + " " + BotLabels.DASH.getLabel() + " " + employee.getName() + " " + employee.getLastname());
						int employee = item.getEmployeeID();
						EmployeeItem employeeItem = getEmployeeItemById(employee).getBody();
						currentRow.add(employeeItem.getID() + " " + BotLabels.DASH.getLabel() + " " + employeeItem.getName() + " " + employeeItem.getLastname());
						keyboard.add(currentRow);
					}
					keyboardMarkup.setKeyboard(keyboard);

					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotLabels.EMPLOYEE_TASKS.getLabel() + "\n"  + "\n" + BotLabels.EMOJI_WARNING.getLabel() + BotLabels.SELECT_TASK_MANAGER.getLabel());
					messageToTelegram.setReplyMarkup(keyboardMarkup);
					messageToTelegram.setParseMode("Markdown");

					try {
						execute(messageToTelegram);
					} catch (TelegramApiException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}

			else if(userRole == 2 && messageTextFromTelegram.contains(BotLabels.TASK_INDICATOR.getLabel())) {
				logger.info("Información de la tarea de un empleado. Vista de manager. Levanta las tareas disponibles.");
				String[] parts = messageTextFromTelegram.split(BotLabels.TASK_INDICATOR.getLabel(), 2);
				Integer id;
				id = Integer.valueOf(parts[0].trim());

				ToDoItem item1 = getToDoItemById(id).getBody();
				if (item1 != null) {
					String message = BotLabels.EMOJI_INFO.getLabel() + "*Información de la Tarea:* " + "\n" + "\n" + 
							"*Nombre de la tarea:* " + item1.getName() + "\n" +
							"*Descripción:* " + item1.getDescription() + "\n" +
							"*Fecha Límite:* " + item1.getDateLimit() + "\n" +
							"*Tipo de Tarea:* " + item1.getType();
					SendMessage formattedMessage = new SendMessage();
					formattedMessage.setChatId(chatId);
					formattedMessage.setText(message);
					formattedMessage.setParseMode("Markdown");
				
					try {
						execute(formattedMessage);
					} catch (TelegramApiException e) {
					  	logger.error(e.getLocalizedMessage(), e);
					} 
				} else {
					BotHelper.sendMessageToTelegram(chatId, "Error.", this);
				}

				List<ToDoItem> allTodoItems = toDoItemService.findByProjectidOrderByDatelimitDesc(currentEmployee.getProjectid());
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				KeyboardRow secondScreenRowTop = new KeyboardRow();
				secondScreenRowTop.add(BotLabels.EMOJI_ARROW.getLabel() + "----------" + BotLabels.TASKS_UNCOMPLETED.getLabel()+ "----------" + BotLabels.EMOJI_ARROW.getLabel());
				keyboard.add(secondScreenRowTop);

				List<ToDoItem> activeItems = allTodoItems.stream().filter(item -> item.getStatus() == 0).collect(Collectors.toList());
				for (ToDoItem item : activeItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
					//get the employee asosiacte with that item id
					int employee = item.getEmployeeID();
					EmployeeItem employeeItem = getEmployeeItemById(employee).getBody();
					currentRow.add(employeeItem.getID() + " " + BotLabels.DASH.getLabel() + " " + employeeItem.getName() + " " + employeeItem.getLastname());
					keyboard.add(currentRow);
				}
				KeyboardRow middleRowTop = new KeyboardRow();
				middleRowTop.add(BotLabels.EMOJI_ARROW.getLabel() + "----------" + BotLabels.TASKS_COMPLETED.getLabel() + "----------" + BotLabels.EMOJI_ARROW.getLabel());
				keyboard.add(middleRowTop);

				List<ToDoItem> doneItems = allTodoItems.stream().filter(item -> item.getStatus() == 1).collect(Collectors.toList());
				for (ToDoItem item : doneItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getID() + BotLabels.TASK_INDICATOR.getLabel() + item.getName());
					//EmployeeItem employee = item.getEmployeeID();
					//currentRow.add(employee.getID() + " " + BotLabels.DASH.getLabel() + " " + employee.getName() + " " + employee.getLastname());
					int employee = item.getEmployeeID();
					EmployeeItem employeeItem = getEmployeeItemById(employee).getBody();
					currentRow.add(employeeItem.getID() + " " + BotLabels.DASH.getLabel() + " " + employeeItem.getName() + " " + employeeItem.getLastname());
					keyboard.add(currentRow);
				}
				keyboardMarkup.setKeyboard(keyboard);

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				//messageToTelegram.setText(BotLabels.EMPLOYEE_TASKS.getLabel() + "\n"  + "\n" + BotLabels.EMOJI_WARNING.getLabel() + BotLabels.SELECT_TASK_MANAGER.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);
				messageToTelegram.setParseMode("Markdown");

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForTask && step == 2) {
				logger.info("Segundo paso para recibir la informacion de la tarea. Captura el nombre de la tarea.");
				try {
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
				logger.info("Tercer paso para recibir la informacion de la tarea. Captura la descripcion de la tarea.");
				try {
					descriptionTask = messageTextFromTelegram;
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_DATE_LIMIT.getMessage() + "\n" + "\n" + BotLabels.EMOJI_WARNING.getLabel() + BotMessages.DISCLAIMER.getMessage());
					execute(messageToTelegram);
					step = 4;
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForTask && step == 4) {
				logger.info("Cuarto paso para recibir la informacion de la tarea. Captura la fecha limite de la tarea.");
				try {
					if (messageTextFromTelegram.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
						datelimit = Timestamp.valueOf(messageTextFromTelegram);
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(BotMessages.TYPE_TYPE.getMessage());
						execute(messageToTelegram);
						step = 5;
					} else {
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText("Formato invalido. Por favor, ingrese la fecha en el formato correcto: yyyy-mm-dd hh:mm:ss");
						execute(messageToTelegram);
					}
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isWaitingForTask && step == 5) {
				logger.info("Quinto paso para recibir la informacion de la tarea. Captura el tipo de la tarea.");
				try {
					type = messageTextFromTelegram;
					step = 0;
					isWaitingForTask = false;
					ToDoItem newItem = new ToDoItem();
					newItem.setName(nameTask);
					newItem.setDescription(descriptionTask);
					newItem.setDateCreated(new java.sql.Timestamp(System.currentTimeMillis()));
					newItem.setStatus(0);
					newItem.setDateLimit(datelimit);
					newItem.setType(type);
					/*newItem.setEmployeeID(currentEmployee);
					newItem.setProjectID(currentEmployee.getProjectid())*/
					newItem.setEmployeeID(currentEmployee.getID());
					newItem.setProjectID(currentEmployee.getProjectid());

					ResponseEntity entity = addToDoItem(newItem);


					String message = BotMessages.NEW_ITEM_ADDED.getMessage()+ "\n\n" +
							BotLabels.EMOJI_INFO.getLabel() + "*Información de la Tarea:* " + "\n" + "\n" +
							"*Nombre de la Tarea:* " + nameTask + "\n" +
							"*Descripción:* " + descriptionTask + "\n" +
							"*Fecha Límite:* " + datelimit + "\n" +
							"*Tipo de Tarea:* " + type;
					SendMessage formattedMessage = new SendMessage();
					formattedMessage.setChatId(chatId);
					formattedMessage.setText(message);
					formattedMessage.setParseMode("Markdown");
					formattedMessage.setText(message);

					try {
						execute(formattedMessage);
					} catch (TelegramApiException e) {
						  logger.error(e.getLocalizedMessage(), e);
					} 							
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}

			else if (isProject) {
				logger.info("Agregar proyecto. Vista de manager. Captura el nombre del proyecto.");
				try {
					isProject = false;
					ProjectItem newProject = new ProjectItem();
					newProject.setName(messageTextFromTelegram);
					newProject.setDateStart(new java.sql.Timestamp(System.currentTimeMillis()));
					newProject.setDateEnd(new java.sql.Timestamp(System.currentTimeMillis()));
					newProject.setStatus(false);
					newProject.setDepartamentID(currentEmployee.getDepartamentid());
					step = 2;

					ProjectItem savedProject = projectItemService.addProjectItem(newProject);
					//set the project id
					currentEmployee.setProjectid(savedProject.getID());
			
					employeeItemService.updateEmployeeItem(currentEmployee.getID(), currentEmployee);

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
				logger.info("Agregar empleado a un proyecto. Vista de manager.");
				List<EmployeeItem> allEmployees = findByDepartamentid(currentEmployee.getDepartamentid());
				allEmployees = allEmployees.stream().filter(item -> item.getID() != currentEmployee.getID() && (item.getProjectid() == 0 || item.getProjectid()!= currentEmployee.getProjectid()))
				.collect(Collectors.toList());		
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.EMOJI_HOUSE.getLabel() + BotLabels.MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				for (EmployeeItem item : allEmployees) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getName() + " " + item.getLastname());
					keyboard.add(currentRow);
				}
				keyboardMarkup.setKeyboard(keyboard);
				
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotLabels.ADD_EMPLOYEE_TO_PROJECT.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
				step = 3;
			}

			else if (step == 3) {
			logger.info("Agregar empleado a un proyecto. Vista de manager. Captura el nombre y apellido del empleado.");
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
				logger.info("Mensaje en caso de no reconocer el comando.");
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

	//getAllToDoItemsbyProjectId
	public List<ToDoItem> getAllToDoItemsbyProjectId(int projectid) {
		return toDoItemService.findByProjectid(projectid);
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

	//get employee item by id
	public ResponseEntity<EmployeeItem> getEmployeeItemById(@PathVariable int id) {
		try {
			ResponseEntity<EmployeeItem> responseEntity = employeeItemService.getEmployeeItemById(id);
			return new ResponseEntity<EmployeeItem>(responseEntity.getBody(), HttpStatus.OK);
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
			logger.error("340000000"+e.getLocalizedMessage(), e);
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
			logger.error("350000000"+e.getLocalizedMessage(), e);
			return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
		}
	}
}