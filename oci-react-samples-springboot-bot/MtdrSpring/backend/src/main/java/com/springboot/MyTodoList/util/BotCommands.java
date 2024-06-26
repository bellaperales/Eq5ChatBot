package com.springboot.MyTodoList.util;

public enum BotCommands {

	START_COMMAND("/start"),
	HIDE_COMMAND("/hide"),
	TODO_LIST("/todolist"),
	ADD_ITEM("/additem"),
	ADD_PROJECT("/addproject"),
	ADD_EMPLOYEE_TO_PROJECT("/addemployee"),
	LIST_EMPLOYEES_ITEMS("/allemployeetasks"),
	LIST_EMPLOYEES("/allemployees"),
	PROJECT_LIST("/projectlist"),
	START("/iniciar"),
	BACK_COMMAND("/mainscreen");

	private String command;

	BotCommands(String enumCommand) {
		this.command = enumCommand;
	}

	public String getCommand() {
		return command;
	}
}
