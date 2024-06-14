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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.controller.ToDoItemBotController;
import com.springboot.MyTodoList.service.EmployeeItemService;
import com.springboot.MyTodoList.service.ProjectItemService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;

/**
 * Tests for {@link ToDoItemBotController}.
 */
class ToDoItemBotControllerTest {

    private static final long CHAT_ID = 123456789L;
    private static final int USER_ID = 12345;

    @Test
    void shouldProcessStartCommand() throws TelegramApiException {
        ToDoItemService toDoService = mock(ToDoItemService.class);
        ProjectItemService projectService = mock(ProjectItemService.class);
        EmployeeItemService employeeService = mock(EmployeeItemService.class);

        ToDoItemBotController bot = new ToDoItemBotController(
            "7198383080:AAF-YvgOUUDbv7fmclRmEFn3DXGaoJGA3rM",
            "a00815371_bot", toDoService, projectService, employeeService);

        TelegramLongPollingBot telegramBot = mock(TelegramLongPollingBot.class);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(new Message());

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

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
}
