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
        user.setId((long) 12345); // Set a user ID

        // Create a test Chat
        Chat chat = new Chat();
        chat.setId(123456789L); // The chat ID
        chat.setType("private");
        chat.setId(user.getId()); // Associate the User with the Chat

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

    // Add more tests for other bot commands and logic
    // Example for processing the "/todolist" command:
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
        when(bot.execute(any(SendMessage.class))).thenReturn(null); // Mock successful execution of SendMessage

        // Create a test update
        Update testUpdate = new Update();
        
        // Create a test User
        User user = new User();
        user.setId((long) 12345);

        // Create a test Chat
        Chat chat = new Chat();
        chat.setId(123456789L); 
        chat.setType("private");
        chat.setId(user.getId()); 

        // Create a test Message
        Message testMessage = new Message();
        testMessage.setText(BotCommands.TODO_LIST.getCommand());
        testMessage.setChat(chat);

        // Set the Message to the Update
        testUpdate.setMessage(testMessage); 

        // Set the user role to 1 (developer)
        bot.userRole = 1; 

        // When the bot receives the update
        bot.onUpdateReceived(testUpdate);

        // Verify that the bot sends a message with the todo list 
        verify(telegramBotMock, times(1)).execute(any(SendMessage.class)); 

        // Verify that the correct methods from your services are called
        //verify(toDoItemServiceMock).findByEmployeeidOrderByDatelimitAsc(anyInt());
        // ... (verify other service methods if needed)
    }
}