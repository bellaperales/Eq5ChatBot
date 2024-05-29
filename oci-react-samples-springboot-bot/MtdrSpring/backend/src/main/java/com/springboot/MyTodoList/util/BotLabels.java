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
	DONE_TASK(" COMPLETADA"),
	TASK_INDICATOR(" "),
	UNDO_TASK(" Reactivar tarea"),
	DELETE_TASK(" Eliminar tarea"),
	DONE_PROJECT(" COMPLETADO"),
	UNDO_PROJECT(" Reactivar proyecto"),
	DELETE_PROJECT(" Eliminar proyecto"),
	MY_TODO_LIST("Mis Tareas de Hoy"),
	MY_PROJECT_LIST("Mis Proyectos"),
	LIST_EMPLOYEES("Lista de Empleados"),		
	TASKS_COMPLETED("Tareas Completadas "),
	TASKS_UNCOMPLETED("Tareas Pendientes "),
	LIST_ALL_EMPLOYEES_ITEMS("Todas las Tareas del Equipo"),
	// EMOJI_UNCOMPLETED("\uD83D\uDD34"),
	EMOJI_DELETE("\uD83D\uDDD1 "),
	EMOJI_DONE("\u2705 "),
	EMOJI_UNDO("\uD83D\uDD04 "),
	EMOJI_HOUSE("\uD83C\uDFE0 "),
	EMOJI_PENDING("\uD83D\uDD34 "),
	DASH("-");

	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
