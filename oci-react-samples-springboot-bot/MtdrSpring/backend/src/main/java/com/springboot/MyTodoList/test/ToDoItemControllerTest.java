package com.springboot.MyTodoList.test;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

/**
 * Tests for {@link ToDoItemBotController}.
 */
class ToDoItemBotControllerTest {

    /**
     * Chat ID for testing.
     */
    private static final long CHAT_ID = 123456789L;

    /**
     * User ID for testing.
     */
    private static final int USER_ID = 12345;

    /**
     * Tests start command processing.
     */
    @Test
    void shouldProcessStartCommand() throws TelegramApiException {
        ToDoItemService toDoItemServiceMock = mock(ToDoItemService.class);
        ProjectItemService projectItemServiceMock = mock(ProjectItemService.class);
        EmployeeItemService employeeItemServiceMock = mock(EmployeeItemService.class);

        ToDoItemBotController bot = new ToDoItemBotController(
            "7198383080:AAF1YvgOUUDbv7fmclRmEFn3DXGaoJGA3rM",
            "a00815371_bot", toDoItemServiceMock,
            projectItemServiceMock, employeeItemServiceMock);

        TelegramLongPollingBot telegramBotMock = mock(TelegramLongPollingBot.class);
        when(telegramBotMock.execute(any(SendMessage.class)))
            .thenReturn(new Message());

        User user = new User();
        user.setId((long) USER_ID);

        Chat chat = new Chat();
        chat.setId(CHAT_ID);
        chat.setType("private");

        Message testMessage = new Message();
        testMessage.setText(BotCommands.START.getCommand());
        testMessage.setChat(chat);

        Update testUpdate = new Update();
        testUpdate.setMessage(testMessage);

        bot.onUpdateReceived(testUpdate);

        verify(telegramBotMock, times(1)).execute(any(SendMessage.class));
    }

    /**
     * Tests todo list command processing for a developer.
     */
    @Test
    void shouldProcessTodoListCommandForDeveloper() throws TelegramApiException {
        ToDoItemService toDoItemServiceMock = mock(ToDoItemService.class);
        ProjectItemService projectItemServiceMock = mock(ProjectItemService.class);
        EmployeeItemService employeeItemServiceMock = mock(EmployeeItemService.class);

        ToDoItemBotController bot = new ToDoItemBotController(
            "7198383080:AAF1YvgOUUDbv7fmclRmEFn3DXGaoJGA3rM",
            "a00815371_bot", toDoItemServiceMock,
            projectItemServiceMock, employeeItemServiceMock);

        TelegramLongPollingBot telegramBotMock = mock(TelegramLongPollingBot.class);
        when(telegramBotMock.execute(any(SendMessage.class)))
            .thenReturn(new Message());

        User user = new User();
        user.setId((long) USER_ID);

        Chat chat = new Chat();
        chat.setId(CHAT_ID);
        chat.setType("private");

        Message testMessage = new Message();
        testMessage.setChat(chat);
        testMessage.setText("/todolist");

        Update testUpdate = new Update();
        testUpdate.setMessage(testMessage);

        bot.onUpdateReceived(testUpdate);

        verify(telegramBotMock, times(1)).execute(any(SendMessage.class));
    }
}
