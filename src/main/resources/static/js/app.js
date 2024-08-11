document.addEventListener("DOMContentLoaded", () => {
    const todoList = document.getElementById("todo-list");
    const addTodoButton = document.getElementById("add-todo-button");

    // Fetch and display all todos
    fetchTodos();

    addTodoButton.addEventListener("click", () => {
        const title = document.getElementById("todo-title").value;
        const description = document.getElementById("todo-description").value;
        if (title && description) {
            addTodo(title, description);
        }
    });

    async function fetchTodos() {
        const response = await fetch("/api/todos");
        const todos = await response.json();
        todoList.innerHTML = "";
        todos.forEach(todo => {
            const todoItem = createTodoElement(todo);
            todoList.appendChild(todoItem);
        });
    }

    async function addTodo(title, description) {
        const response = await fetch("/api/todos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ title, description, status: "pending" })
        });

        if (response.ok) {
            const newTodo = await response.json();
            const todoItem = createTodoElement(newTodo);
            todoList.appendChild(todoItem);
            clearInputs();
        }
    }

    function createTodoElement(todo) {
        const li = document.createElement("li");

        const todoText = document.createElement("span");
        todoText.textContent = `${todo.title} - ${todo.description} [${todo.status}]`;

        const statusSelect = document.createElement("select");
        ["pending", "in_progress", "completed"].forEach(status => {
            const option = document.createElement("option");
            option.value = status;
            option.textContent = status.charAt(0).toUpperCase() + status.slice(1).replace('_', ' ');
            if (status === todo.status) {
                option.selected = true;
            }
            statusSelect.appendChild(option);
        });
        statusSelect.addEventListener("change", () => updateTodoStatus(todo.id, statusSelect.value, todoText));

        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.classList.add("delete-todo");
        deleteButton.addEventListener("click", () => deleteTodo(todo.id, li));

        li.appendChild(todoText);
        li.appendChild(statusSelect);
        li.appendChild(deleteButton);

        return li;
    }

    async function updateTodoStatus(id, status, todoText) {
        const response = await fetch(`/api/todos/${id}/status`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ status })
        });

        if (response.ok) {
            const updatedTodo = await response.json();
            todoText.textContent = `${updatedTodo.title} - ${updatedTodo.description} [${updatedTodo.status}]`;
        }
    }

    async function deleteTodo(id, todoElement) {
        const response = await fetch(`/api/todos/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            todoElement.remove();
        }
    }

    function clearInputs() {
        document.getElementById("todo-title").value = "";
        document.getElementById("todo-description").value = "";
    }
});
