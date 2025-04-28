package com.arnold.myapplication.data.repositories

import com.arnold.myapplication.data.local.dao.UserDao
import com.arnold.myapplication.data.local.entities.User
// Import a suitable password hashing library (e.g., BCrypt)
// import org.mindrot.jbcrypt.BCrypt // Example BCrypt import

/**
 * Repository for handling User data operations.
 *
 * @property userDao The Data Access Object for User entities.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Registers a new user in the database.
     *
     * @param email The user's email address.
     * @param password The user's plain text password (will be hashed).
     * @param name The user's full name.
     * @return The row ID of the newly inserted user record, or potentially an error indicator.
     */
    suspend fun registerUser(email: String, password: String, name: String): Long {
        // **IMPORTANT:** Implement strong password hashing here!
        val hashedPassword = hashPassword(password)
        // Create a User object using the correct field name 'fullName'
        val user = User(
            email = email,
            password = hashedPassword, // Store the hashed password
            fullName = name // Corrected: Use 'fullName' to match the entity
            // Other fields will use default values defined in the User entity
        )
        // Insert the user record into the database via the DAO.
        return userDao.insertUser(user)
    }

    /**
     * Attempts to log in a user with the given email and password.
     *
     * @param email The user's email address.
     * @param password The user's plain text password.
     * @return The User object if login is successful, null otherwise.
     */
    suspend fun login(email: String, password: String): User? {
        // Retrieve the user by email from the database.
        val user = userDao.getUserByEmail(email)
        // Check if the user exists and if the provided password matches the stored hash.
        // **IMPORTANT:** Implement password verification matching your hashing method.
        return if (user != null && verifyPassword(password, user.password)) {
            user // Return the user object on successful verification
        } else {
            null // Return null if user not found or password doesn't match
        }
    }
}

// --- Password Hashing Placeholder Functions ---
// **SECURITY WARNING:** These are placeholders. Replace with a robust library like BCrypt.

/**
 * Hashes a plain text password.
 * **DO NOT USE IN PRODUCTION.** Implement using a strong hashing algorithm (e.g., BCrypt).
 *
 * @param password The plain text password.
 * @return A hashed representation of the password.
 */
fun hashPassword(password: String): String {
    // Placeholder implementation. Replace with actual hashing.
    // Example using BCrypt (add the dependency first):
    // return BCrypt.hashpw(password, BCrypt.gensalt())
    println("WARNING: Using insecure placeholder password hashing!")
    return "hashed_$password" // Replace this line
}

/**
 * Verifies a plain text password against a stored hash.
 * **DO NOT USE IN PRODUCTION.** Implement using the same library as hashPassword.
 *
 * @param password The plain text password to check.
 * @param hashedPassword The stored password hash.
 * @return True if the password matches the hash, false otherwise.
 */
fun verifyPassword(password: String, hashedPassword: String): Boolean {
    // Placeholder implementation. Replace with actual verification.
    // Example using BCrypt:
    // return BCrypt.checkpw(password, hashedPassword)
    println("WARNING: Using insecure placeholder password verification!")
    return hashedPassword == "hashed_$password" // Replace this line
}
