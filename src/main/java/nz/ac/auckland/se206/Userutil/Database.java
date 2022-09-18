package nz.ac.auckland.se206.Userutil;

import com.google.gson.Gson;
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
   * @param user
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
}
