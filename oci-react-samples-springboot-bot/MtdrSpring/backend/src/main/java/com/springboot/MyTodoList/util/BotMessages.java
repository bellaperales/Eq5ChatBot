package com.springboot.MyTodoList.util;

public enum BotMessages {

	INITIAL_MESSAGE(
			"¡Bienvenido al Chat Bot de Oracle! Soy el Chat Bot de Oracle. Ingresa tu numero de asosciado para comenzar."),
	HELLO_MYTODO_BOT(
			"¡Bienvenido al Chat Bot de Oracle! Escribe una nueva tarea a continuación y envia el boton o selecciona una opción a continuacion:"),
	BOT_REGISTERED_STARTED(" ¡Se registró correctamente el Bot!"),
	MAIN_SCREEN("Selecciona una opción a continuación:"),
	ITEM_DONE(
			"¡Tarea lista! Selecciona /todolist para desplegar todas las tareas o selecciona /mainscreen para ir a la pantalla principal."),
	ITEM_UNDONE(
			"¡Tarea deshecha! Selecciona /todolist para desplegar todas las tareas o selecciona /mainscreen para ir a la pantalla principal."),
	ITEM_DELETED(
			"¡Tarea eliminada! Selecciona /todolist para desplegar todas las tareas o selecciona /mainscreen para ir a la pantalla principal."),
	PROJECT_DONE(
			"¡Proyecto listo! Selecciona /projectlist para desplegar todos los proyectos o selecciona /mainscreen para ir a la pantalla principal."),
	PROJECT_UNDONE(
			"¡Proyecto deshecho! Selecciona /projectlist para desplegar todos los proyectos o selecciona /mainscreen para ir a la pantalla principal."),
	PROJECT_DELETED(
			"¡Proyecto eliminado! Selecciona /projectlist para desplegar todos los proyectos o selecciona /mainscreen para ir a la pantalla principal."),
	TYPE_NEW_TODO_ITEM("Escribe una nueva tarea y presiona el botón para agregarla a la lista de tareas."),
	TYPE_NEW_PROJECT("Escribe un nuevo proyecto y presiona el botón para agregarlo a la lista de proyectos."),
	NEW_ITEM_ADDED("¡Nueva tarea agregada! Selecciona /mainscreen para ir a la pantalla principal."),
	NEW_PROJECT_ADDED("¡Nuevo proyecto agregado! Selecciona /mainscreen para ir a la pantalla principal."),
	ERROR("¡Ups! Algo salió mal. Por favor, inténtalo de nuevo. Seleccione /start para comenzar de nuevo."),
	BYE("¡Hasta pronto! Selecciona /start para empezar de nuevo.");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
