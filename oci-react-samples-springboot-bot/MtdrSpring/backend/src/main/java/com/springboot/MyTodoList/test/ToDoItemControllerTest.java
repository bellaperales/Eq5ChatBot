package com.springboot.MyTodoList.test;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static org.mockito.Mockito.*;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

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
        when(bot.execute(any(SendMessage.class))).thenReturn(null); // Mock successful execution of SendMessage

        // Create a test update
        Update testUpdate = new Update(); 
        
        // Create a test User
        User user = new User();
        user.setId((long) USER_ID);

        // Create a test Chat
        Chat chat = new Chat();
        chat.setId(CHAT_ID);
        chat.setType("private");
        //chat.setChatId(user.getId()); 

        // Create a test Message
        Message testMessage = new Message();
        testMessage.setText(BotCommands.START.getCommand());
        testMessage.setChat(chat);

        // Set the Message to the Update
        testUpdate.setMessage(testMessage); 

        // When the bot receives the update
        bot.onUpdateReceived(testUpdate);

        // Verify that the bot sends a welcome message
        verify(telegramBotMock, times(1)).execute(any(SendMessage.class)); 

        // Verify that other methods of the bot are called (if needed)
        // ...
    }

    // ... (rest of your test methods)
}