package com.example.demo.service;

import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    // Get all Todos
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Get a Todo by ID
    public Todo getTodoById(int id) {
        return todoRepository.findById(id);
    }

    // Create a new Todo
    public void createTodo(Todo todo) {
        todoRepository.save(todo);
    }

    // Update a Todo
    public Todo updateTodo(int id, Todo todo) {
        Todo existingTodo = todoRepository.findById(id);
        if (existingTodo != null) {
            existingTodo.setTitle(todo.getTitle());
            existingTodo.setDescription(todo.getDescription());
            existingTodo.setStatus(todo.getStatus());
            todoRepository.update(id, existingTodo);
            return existingTodo;
        }
        return null;
    }

    // Delete a Todo
    public boolean deleteTodo(int id) {
        int rowsAffected = todoRepository.delete(id);
        return rowsAffected > 0;
    }
}
