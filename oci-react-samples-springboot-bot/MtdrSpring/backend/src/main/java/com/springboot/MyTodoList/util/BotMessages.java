package com.springboot.MyTodoList.util;

public enum BotMessages {

	BEFORE_START("¡Bienvenido al Chat Bot de Oracle! Para comenzar selecciona /iniciar y  sigue las instrucciones."),
	INITIAL_MESSAGE(
			"Ingresa tu numero de asosciado para comenzar."),
	HELLO_MYTODO_BOT(
			"Selecciona una opción a continuacion: "),
	BOT_REGISTERED_STARTED(" ¡Se registró correctamente el Bot!"),
	MAIN_SCREEN("Selecciona una opción a continuación:"),
	EMPLOYEE_PROJECT("Empleado agregado al proyecto."),
	EMPLOYEE_ADDED("¡Empleado agregado! Si deseas agregar a mas empleados selecciona /addemployee o selecciona /mainscreen para ir a la pantalla principal."),
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
	PROJECT_ADD_ERROR("Error. No puedes agregar un nuevo proyecto. Ya tienes un proyecto activo. Selecciona /mainscreen para ir a la pantalla principal."),
	TASK_INFORMATION_BACK("Selecciona /todolist para desplegar todas las tareas o selecciona /mainscreen para ir a la pantalla principal."),
	TASK_INFORMATION_BACK_MANAGER("Selecciona /allemployeetasks para desplegar todas las tareas o selecciona /mainscreen para ir a la pantalla principal."),
	LIST_EMPLOYEES_ERROR("Error. No puedes ver la lista de empleados. No tienes un proyecto activo. Selecciona /mainscreen para ir a la pantalla principal."),
	LIST_EMPLOYEES_ITEMS_ERROR("Error. No puedes ver la lista de tareas de los empleados. No tienes un proyecto activo. Selecciona /mainscreen para ir a la pantalla principal."),
	TYPE_NEW_TODO_ITEM("Escribe el nombre de la tarea y presiona el botón para agregarla a la lista de tareas."),
	TYPE_NEW_PROJECT("Escribe un nuevo proyecto y presiona el botón para agregarlo a la lista de proyectos."),
	NEW_ITEM_ADDED("¡Nueva tarea agregada! Selecciona /mainscreen para ir a la pantalla principal o /todolist para ver las tareas."),
	NEW_PROJECT_ADDED("¡Nuevo proyecto agregado! Selecciona /addemployee para agregar personas al proyecto."),
	ERROR("¡Ups! Algo salió mal. Por favor, inténtalo de nuevo. Seleccione /start para comenzar de nuevo."),
	TYPE_DESCRIPTION("Escribe una descripción para la tarea y presiona el botón para agregarla a la lista de tareas."),
	TYPE_DATE_LIMIT(
			"Escribe la fecha límite para la tarea en el formato (yyyy-mm-dd hh:mm:ss) y presiona el botón para agregarla a la lista de tareas."),
	TYPE_TYPE(
			"Escribe el tipo de tarea (capacitacion o desarrollo) y presiona el botón para agregarla a la lista de tareas."),
	MAIN_SCREEN_BACK("Selecciona /mainscreen para ir a la pantalla principal."),
	BYE("¡Hasta pronto! Selecciona /start para empezar de nuevo.");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
