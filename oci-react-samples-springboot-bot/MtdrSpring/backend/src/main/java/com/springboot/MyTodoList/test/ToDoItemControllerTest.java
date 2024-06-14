package com.springboot.MyTodoList.test;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.springboot.MyTodoList.controller.ToDoItemBotController;
import com.springboot.MyTodoList.service.EmployeeItemService;
import com.springboot.MyTodoList.service.ProjectItemService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;

class ToDoItemBotControllerTest {

    private static final long CHAT_ID = 123456789L; 
    private static final int USER_ID = 12345; 

    @Test
    void shouldProcessStartCommand() throws TelegramApiException {
        // Mock dependencies
        ToDoItemService toDoItemServiceMock = mock(ToDoItemService.class);
        ProjectItemService projectItemServiceMock = mock(ProjectItemService.class);
        EmployeeItemService employeeItemServiceMock = mock(EmployeeItemService.class);

        // Create the bot instance
        ToDoItemBotController bot = new ToDoItemBotController("7198383080:AAF1YvgOUUDbv7fmclRmEFn3DXGaoJGA3rM", "a00815371_bot", toDoItemServiceMock, projectItemServiceMock, employeeItemServiceMock);

        // Mock the Telegram API 
        TelegramLongPollingBot telegramBotMock = mock(TelegramLongPollingBot.class);
        when(telegramBotMock.execute(any(SendMessage.class))).thenReturn(new Message()); // Mock successful execution of SendMessage

        // Create a test User
        User user = new User();
        user.setId((long) USER_ID); 

        // Create a test Chat
        Chat chat = new Chat();
        chat.setId(CHAT_ID); 
        chat.setType("private"); 

        // Create a test Message
        Message testMessage = new Message();
        testMessage.setText(BotCommands.START.getCommand());
        testMessage.setChat(chat);

        // Create a test Update
        Update testUpdate = new Update();
        testUpdate.setMessage(testMessage);

        // When the bot receives the update
        bot.onUpdateReceived(testUpdate);

        // Verify that the bot sends a welcome message
        verify(telegramBotMock, times(1)).execute(any(SendMessage.class)); 
    }

    // Add more tests for other bot commands and logic
    @Test
    void shouldProcessTodoListCommandForDeveloper() throws TelegramApiException {
        // Mock dependencies
        ToDoItemService toDoItemServiceMock = mock(ToDoItemService.class);
        ProjectItemService projectItemServiceMock = mock(ProjectItemService.class);
        EmployeeItemService employeeItemServiceMock = mock(EmployeeItemService.class);

        // Create the bot instance
        ToDoItemBotController bot = new ToDoItemBotController("7198383080:AAF1YvgOUUDbv7fmclRmEFn3DXGaoJGA3rM", "a00815371_bot", toDoItemServiceMock, projectItemServiceMock, employeeItemServiceMock);

        // Mock the Telegram API 
        TelegramLongPollingBot telegramBotMock = mock(TelegramLongPollingBot.class);
        when(telegramBotMock.execute(any(SendMessage.class))).thenReturn(new Message()); // Mock successful execution of SendMessage

        // Create a test User
        User user = new User();
        user.setId((long) USER_ID);

        // Create a test Chat
        Chat chat = new Chat();
        chat.setId(CHAT_ID); 
        chat.setType("private"); 

        // Create a test Message
        Message testMessage = new Message();
        testMessage.setChat(chat);
        testMessage.setText("/todolist");

        // Create a test Update
        Update testUpdate = new Update();
        testUpdate.setMessage(testMessage);

        // When the bot receives the update
        bot.onUpdateReceived(testUpdate);

        // Verify that the bot sends a message with the todo list 
        verify(telegramBotMock, times(1)).execute(any(SendMessage.class)); 

        // Verify that the correct methods from your services are called
        // verify(toDoItemServiceMock, times(1)).findByEmployeeIdOrderByDateLimitAsc(USER_ID);
        // ... (verify other service methods if needed)
    }
}
