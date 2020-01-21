package ru.evillcat.server.authorization;

import ru.evillcat.server.user.User;
import ru.evillcat.server.user.UserConnectionRepo;

import java.util.Set;

public class Authorization {

    private final UserConnectionRepo userConnectionRepo;

    public Authorization(UserConnectionRepo userConnectionRepo) {
        this.userConnectionRepo = userConnectionRepo;
    }

    public boolean authorize(String name) {
        Set<User> users = userConnectionRepo.getUsers();
        for (User user : users) {
            if (user.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
