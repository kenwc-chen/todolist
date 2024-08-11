package com.example.demo.repository;

import com.example.demo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TodoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for mapping rows of a ResultSet to a Todo object
    private static final class TodoRowMapper implements RowMapper<Todo> {
        @Override
        public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Todo todo = new Todo();
            todo.setId(rs.getInt("id"));
            todo.setTitle(rs.getString("title"));
            todo.setDescription(rs.getString("description"));
            todo.setStatus(rs.getString("status"));
            todo.setCreatedAt(rs.getTimestamp("created_at"));
            todo.setUpdatedAt(rs.getTimestamp("updated_at"));
            return todo;
        }
    }

    // Create a new Todo
    public int save(Todo todo) {
        String sql = "INSERT INTO todos (title, description, status, created_at, updated_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        return jdbcTemplate.update(sql, todo.getTitle(), todo.getDescription(), todo.getStatus());
    }

    // Get all Todos
    public List<Todo> findAll() {
        String sql = "SELECT * FROM todos";
        return jdbcTemplate.query(sql, new TodoRowMapper());
    }

    // Get a Todo by ID
    public Todo findById(int id) {
        String sql = "SELECT * FROM todos WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new TodoRowMapper(), id);
    }

    // Update a Todo
    public int update(int id, Todo todo) {
        String sql = "UPDATE todos SET title = ?, description = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        return jdbcTemplate.update(sql, todo.getTitle(), todo.getDescription(), todo.getStatus(), id);
    }

    // Delete a Todo
    public int delete(int id) {
        String sql = "DELETE FROM todos WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
