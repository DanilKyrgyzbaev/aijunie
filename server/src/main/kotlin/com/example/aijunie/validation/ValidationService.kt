package com.example.aijunie.validation

/**
 * Service for validating user input
 */
class ValidationService {
    companion object {
        /**
         * Validates an email address
         * @param email The email address to validate
         * @return A list of validation errors, empty if valid
         */
        fun validateEmail(email: String): List<String> {
            val errors = mutableListOf<String>()
            
            if (email.isBlank()) {
                errors.add("Email is required")
                return errors
            }
            
            // Simple regex for email validation
            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            if (!emailRegex.matches(email)) {
                errors.add("Invalid email format")
            }
            
            return errors
        }
        
        /**
         * Validates a password
         * @param password The password to validate
         * @return A list of validation errors, empty if valid
         */
        fun validatePassword(password: String): List<String> {
            val errors = mutableListOf<String>()
            
            if (password.isBlank()) {
                errors.add("Password is required")
                return errors
            }
            
            if (password.length < 6) {
                errors.add("Password must be at least 6 characters long")
            }
            
            if (!password.any { it.isDigit() }) {
                errors.add("Password must contain at least one digit")
            }
            
            if (!password.any { it.isLetter() }) {
                errors.add("Password must contain at least one letter")
            }
            
            return errors
        }
        
        /**
         * Validates a registration request
         * @param name User's name
         * @param lastName User's last name
         * @param email User's email
         * @param password User's password
         * @return A map of field names to validation errors, empty if all valid
         */
        fun validateRegistration(name: String, lastName: String, email: String, password: String): Map<String, List<String>> {
            val errors = mutableMapOf<String, List<String>>()
            
            if (name.isBlank()) {
                errors["name"] = listOf("Name is required")
            }
            
            if (lastName.isBlank()) {
                errors["lastName"] = listOf("Last name is required")
            }
            
            val emailErrors = validateEmail(email)
            if (emailErrors.isNotEmpty()) {
                errors["email"] = emailErrors
            }
            
            val passwordErrors = validatePassword(password)
            if (passwordErrors.isNotEmpty()) {
                errors["password"] = passwordErrors
            }
            
            return errors
        }
        
        /**
         * Validates a login request
         * @param email User's email
         * @param password User's password
         * @return A map of field names to validation errors, empty if all valid
         */
        fun validateLogin(email: String, password: String): Map<String, List<String>> {
            val errors = mutableMapOf<String, List<String>>()
            
            val emailErrors = validateEmail(email)
            if (emailErrors.isNotEmpty()) {
                errors["email"] = emailErrors
            }
            
            if (password.isBlank()) {
                errors["password"] = listOf("Password is required")
            }
            
            return errors
        }
        
        /**
         * Validates a change password request
         * @param currentPassword User's current password
         * @param newPassword User's new password
         * @return A map of field names to validation errors, empty if all valid
         */
        fun validateChangePassword(currentPassword: String, newPassword: String): Map<String, List<String>> {
            val errors = mutableMapOf<String, List<String>>()
            
            if (currentPassword.isBlank()) {
                errors["currentPassword"] = listOf("Current password is required")
            }
            
            val newPasswordErrors = validatePassword(newPassword)
            if (newPasswordErrors.isNotEmpty()) {
                errors["newPassword"] = newPasswordErrors
            }
            
            return errors
        }
    }
}