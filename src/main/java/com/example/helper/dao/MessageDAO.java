package com.example.helper.dao;

import com.example.helper.models.Message;
import com.example.helper.models.Role;
import com.example.helper.models.Status;
import com.example.helper.models.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class MessageDAO {
    private static int MESSAGE_COUNT;

    private static final String URL = "jdbc:postgresql://localhost/database_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "100601Mg";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> search(String text) {
        List<Message> messages = new ArrayList<>();

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM message WHERE text_tsv @@ plainto_tsquery(?)");
//                    connection.prepareStatement("SELECT to_tsvector(text) FROM message");

            preparedStatement.setString(1, text);

            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;
            while (resultSet.next()) {
                System.out.println(count++);
                Message message = new Message();

                message.setId(resultSet.getLong("id"));
                message.setTag(resultSet.getString("tag"));
                message.setText(resultSet.getString("text"));

                message.setDate_create(resultSet.getTimestamp("date_create"));

                message.setActive(resultSet.getBoolean("active"));

                Long status_id = resultSet.getLong("status_id");
                Long user_id = resultSet.getLong("user_id");

                PreparedStatement preparedStatement1 =
                        connection.prepareStatement("SELECT * FROM status WHERE id=?");

                preparedStatement1.setLong(1, status_id);

                ResultSet resultSet1 = preparedStatement1.executeQuery();

                resultSet1.next();

                Status status = new Status();

                status.setId(resultSet1.getLong("id"));
                status.setName(resultSet1.getString("name"));
                status.setReaction_time(resultSet1.getLong("reaction_time"));

                message.setStatus(status);

                PreparedStatement preparedStatement2 =
                        connection.prepareStatement("SELECT * FROM usr WHERE id=?");

                preparedStatement2.setLong(1, user_id);

                ResultSet resultSet2 = preparedStatement2.executeQuery();

                resultSet2.next();

                User user = new User();

                user.setId(resultSet2.getLong("id"));
                user.setUsername(resultSet2.getString("username"));
                user.setPassword(resultSet2.getString("password"));
                user.setActivationCode(resultSet2.getString("activation_code"));
                user.setChat_id(resultSet2.getString("chat_id"));
                user.setActive(resultSet2.getBoolean("active"));

                PreparedStatement preparedStatement3 =
                        connection.prepareStatement("SELECT * FROM user_role WHERE user_id=?");

                preparedStatement3.setLong(1, user.getId());

                ResultSet resultSet3 = preparedStatement3.executeQuery();

                Set<Role> roles = new HashSet<>();

                while (resultSet3.next()) {
                    roles.add(Role.valueOf(resultSet3.getString("roles")));
                }

                user.setRoles(roles);

                message.setAuthor(user);

                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public List<Message> searchByStatus(String text, Status status1) {
        List<Message> messages = new ArrayList<>();

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM message WHERE text_tsv @@ plainto_tsquery(?) AND status_id=?");

            preparedStatement.setString(1, text);
            preparedStatement.setLong(2, status1.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;
            while (resultSet.next()) {
                System.out.println(count++);
                Message message = new Message();

                message.setId(resultSet.getLong("id"));
                message.setTag(resultSet.getString("tag"));
                message.setText(resultSet.getString("text"));

                message.setDate_create(resultSet.getTimestamp("date_create"));

                message.setActive(resultSet.getBoolean("active"));

                Long status_id = resultSet.getLong("status_id");
                Long user_id = resultSet.getLong("user_id");

                PreparedStatement preparedStatement1 =
                        connection.prepareStatement("SELECT * FROM status WHERE id=?");

                preparedStatement1.setLong(1, status_id);

                ResultSet resultSet1 = preparedStatement1.executeQuery();

                resultSet1.next();

                Status status = new Status();

                status.setId(resultSet1.getLong("id"));
                status.setName(resultSet1.getString("name"));
                status.setReaction_time(resultSet1.getLong("reaction_time"));

                message.setStatus(status);

                PreparedStatement preparedStatement2 =
                        connection.prepareStatement("SELECT * FROM usr WHERE id=?");

                preparedStatement2.setLong(1, user_id);

                ResultSet resultSet2 = preparedStatement2.executeQuery();

                resultSet2.next();

                User user = new User();

                user.setId(resultSet2.getLong("id"));
                user.setUsername(resultSet2.getString("username"));
                user.setPassword(resultSet2.getString("password"));
                user.setActivationCode(resultSet2.getString("activation_code"));
                user.setChat_id(resultSet2.getString("chat_id"));
                user.setActive(resultSet2.getBoolean("active"));

                PreparedStatement preparedStatement3 =
                        connection.prepareStatement("SELECT * FROM user_role WHERE user_id=?");

                preparedStatement3.setLong(1, user.getId());

                ResultSet resultSet3 = preparedStatement3.executeQuery();

                Set<Role> roles = new HashSet<>();

                while (resultSet3.next()) {
                    roles.add(Role.valueOf(resultSet3.getString("roles")));
                }

                user.setRoles(roles);

                message.setAuthor(user);

                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public void add_tsv_text(Long id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE message SET text_tsv = to_tsvector(tag) || to_tsvector(text) WHERE id=?");

            preparedStatement.setLong(1, id);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add_tsv_text_all() {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE message SET text_tsv = to_tsvector(tag) || to_tsvector(text)");

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
