package software.ehsan.newsfeed.util

class Validator {

    companion object {
        fun isValidEmail(email: String): Boolean {
            return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
        }

        fun isValidPassword(password: String): Boolean {
            return password.isNotEmpty()
        }
    }
}