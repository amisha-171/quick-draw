package nz.ac.auckland.se206.Userutil;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is used to read and write user data to a file in .json format, and also to read from a
 * .json format
 */
public class Database {
  /**
   * Takes input a User object and it then converts the user object into a json format and writes it
   * to its corresponding json file in /users we will use this to update user stats and to create
   * new users.
   *
   * @param user Takes a User object input
   * @throws IOException
   */
  public void write(User user) throws IOException {
    FileWriter file = new FileWriter("users/" + user.getUserName() + ".json");
    Gson currentUser = new Gson();
    String userData = currentUser.toJson(user);
    file.write(userData);
    file.flush();
  }

  /**
   * This method takes input the username of some user and it then converts that object from its
   * json format into its java representation and returns the User object
   *
   * @param user Takes input string for username
   * @return Returns a User object associated with the given username if it is valid
   * @throws IOException
   */
  public User read(String user) throws IOException {
    String currString = new String(Files.readAllBytes(Paths.get("users/" + user + ".json")));
    Gson currentUser = new Gson();
    return currentUser.fromJson(currString, User.class);
  }

  /**
   * This method checks if a username already exists in our system when a user is trying to create a
   * new profile
   *
   * @param user Takes input the wanted username input
   * @return returns boolean based on whether the username exists already (true if user already
   *     exists false otherwise)
   */
  public boolean userExists(String user, boolean login) {
    File[] files = new File("users").listFiles();
    String checkUser = user + ".json";
    assert files != null;
    for (File file : files) {
      if (!login) {
        if (checkUser.equalsIgnoreCase(file.getName())) {
          return true;
        }
      } else {
        if (checkUser.equals(file.getName())) {
          return true;
        }
      }
    }
    return false;
  }
}
