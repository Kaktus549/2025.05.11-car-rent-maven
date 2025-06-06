package com.comarch.szkolenia.car.rent.database;

import com.comarch.szkolenia.car.rent.model.User;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository implements IUserRepository {
    private final Map<String, User> users = new HashMap<>();
    private final String DB_FILE = "users.txt";

    public UserRepository() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                String[] parameter = line.split(";");
                User user = new User(parameter[0], parameter[1], User.Role.valueOf(parameter[2]));
                this.users.put(user.getLogin(), user);
            }
        } catch (IOException e) {
            System.out.println("baza nie dziala !!");
        }
    }

    @Override
    public User findUser(String login) {
        return this.users.get(login);
    }

    @Override
    public void persist() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE))) {
            for(User u : this.users.values()) {
                writer.write(u.convertToDatabaseLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Nie dziala zapisywanie !!");
        }
    }
}
