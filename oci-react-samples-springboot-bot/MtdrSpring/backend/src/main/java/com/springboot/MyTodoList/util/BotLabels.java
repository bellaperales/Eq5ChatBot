package com.springboot.MyTodoList.util;

public enum BotLabels {
	SHOW_MAIN_SCREEN("Pagina Principal"),
	HIDE_MAIN_SCREEN("Hide Main Screen"),
	MAIN_SCREEN("Pagina Principal"),
	LIST_ALL_ITEMS("Lista de Tareas"),
	LIST_ALL_PROJECTS("Lista de Proyectos"),
	ADD_NEW_ITEM("Agregar Tarea"),
	ADD_NEW_PROJECT("Agregar Proyecto"),
	ADD_EMPLOYEE_TO_PROJECT("Agregar Empleado a Proyecto"),
	TASK_INDICATOR(" -. "),
	PROJECT_INFORMATION("Información del Proyecto"),
	DONE_TASK(" COMPLETAR"),
	UNDO_TASK(" DESHACER"),
	DELETE_TASK(" ELIMINAR"),
	DONE_PROJECT(" COMPLETADO"),
	UNDO_PROJECT(" REACTIVAR PROYECTO"),
	DELETE_PROJECT(" ELIMINAR PROYECTO"),
	MY_TODO_LIST("Mis Tareas (ordenadas por fecha límite)."),
	MY_PROJECT_LIST("Mis Proyectos"),
	LIST_EMPLOYEES("Lista de Empleados"),		
	TASKS_COMPLETED(" Tareas Completadas "),
	TASKS_UNCOMPLETED(" Tareas Pendientes "),
	LIST_ALL_EMPLOYEES_ITEMS("Tareas del Equipo de Desarrollo"),
	EMPLOYEE_TASKS("Todas las Tareas del Equipo."),
 	SELECT_TASK_MANAGER("Selecciona una tarea para ver más detalles. Selecciona un empleado para ver su información."),
	SELECT_TASK("Selecciona una tarea para ver más detalles."),
	EMOJI_ARROW(" \u2B07 "),
	EMOJI_DELETE("\uD83D\uDDD1 "),
	EMOJI_DONE("\u2705 "),
	EMOJI_UNDO("\uD83D\uDD04 "),
	EMOJI_HOUSE("\uD83C\uDFE0 "),
	EMOJI_PENDING("\uD83D\uDD34 "),
	EMOJI_WARNING("\u26A0\uFE0F "),
	EMOJI_ADD("\u2795 "),
	EMOJI_INFO("\u2139 "),

	DASH(" - "),;

	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
