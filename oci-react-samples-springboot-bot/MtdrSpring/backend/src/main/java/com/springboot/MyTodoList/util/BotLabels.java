package com.springboot.MyTodoList.util;

public enum BotLabels {

	SHOW_MAIN_SCREEN("Pagina Principal"),
	HIDE_MAIN_SCREEN("Hide Main Screen"),
	MAIN_SCREEN("Pagina Principal"),
	LIST_ALL_ITEMS("Lista de Tareas"),
	LIST_ALL_PROJECTS("Lista de Proyectos"),
	ADD_NEW_ITEM("Agregar Tarea"),
	ADD_NEW_PROJECT("Agregar Proyecto"),
	DONE_TASK(" COMPLETADA"),
	UNDO_TASK(" Reactivar tarea"),
	DELETE_TASK(" Eliminar tarea"),
	DONE_PROJECT(" COMPLETADO"),
	UNDO_PROJECT(" Reactivar proyecto"),
	DELETE_PROJECT(" Eliminar proyecto"),
	MY_TODO_LIST("Mis Tareas de Hoy"),
	MY_PROJECT_LIST("Mis Proyectos"),
	// EMOJI_UNCOMPLETED("\uD83D\uDD34"),
	EMOJI_DELETE("\uD83D\uDDD1 "),
	EMOJI_DONE("\u2705 "),
	EMOJI_UNDO("\uD83D\uDD04 "),
	EMOJI_HOUSE("\uD83C\uDFE0 "),
	DASH("-");

	private String label;

	BotLabels(String enumLabel) {
		this.label = enumLabel;
	}

	public String getLabel() {
		return label;
	}

}
