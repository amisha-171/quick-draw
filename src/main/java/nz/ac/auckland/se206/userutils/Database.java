package nz.ac.auckland.se206.userutils;

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
    // Create a json file for the new profile
    FileWriter file = new FileWriter("users/" + user.getUserName() + ".json");
    // Use Gson to write the user object to the new json file we have created
    Gson currentUser = new Gson();
    String userData = currentUser.toJson(user);
    // Write json representation of the user to the json file
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
    // Get string representation in json format of a user file
    String currString = new String(Files.readAllBytes(Paths.get("users/" + user + ".json")));
    Gson currentUser = new Gson();
    // Convert the json string into its java object representation to return user of type User
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
    //Open the users directory as a file and create it if it doesn't exist
    File userDir = new File("users");
    File[] files;
    if (!userDir.exists()) {
      userDir.mkdir();
    }
    files = userDir.listFiles(); //get all the user jsons from the user directory
    // Since each file contains the username of the user + ".json" we make a string with the same
    // format
    String checkUser = user + ".json";
    //return false if the users directory doesn't exist (in which case files will be null)
    if (files == null) {
      return false;
    }
    // Run a for each loop to loop through existing usernames and compare them against the input
    // username and return boolean true or false
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
    // Return false if not found
    return false;
  }

  /**
   * This method will return an array containing all current registered users.
   *
   * @return Array containing object User
   * @throws IOException
   */
  public User[] getAllUsers() throws IOException {
    // Initialize an array of type User, and ensure it's empty so that it's returned if users directory doesn't exist
    User[] users = {};
    // Obtain all the currently registered user profiles
    File userDir = new File("users");
    File[] allUserFiles;
    //create the users directory if it doesn't exist
    if (!userDir.exists()) {
      userDir.mkdir();
    }
    allUserFiles = userDir.listFiles(); //obtain all the files from the user directory
    users = new User[allUserFiles.length];
    // Loop through all the current existing users and add them to our users array
    for (int i = 0; i < users.length; i++) {
      users[i] = read(allUserFiles[i].getName().replace(".json", ""));
    }
    return users;
  }
}
