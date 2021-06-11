package school.cactus.succulentshop

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import school.cactus.succulentshop.auth.validation.EmailValidator

class EmailValidatorTest {

    private lateinit var emailValidator: EmailValidator

    @Before
    fun setup() {
        emailValidator = EmailValidator()
    }

    @Test
    fun `given email contains @ and dot, when isEmail called, then should return true`() {

        // Given
        val field = "abcde@gmail.com"
        val expectedResult = true

        // When
        val actualResult = emailValidator.isEmail(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given email contain @, when called isEmail, then should return false`() {

        // Given
        val field = "abcde@defg"
        val expectedResult = false

        // When
        val actualResult = emailValidator.isEmail(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given email contain dot, when isEmail called, then should return false`() {

        // Given
        val field = "abcde.defg"
        val expectedResult = false

        // When
        val actualResult = emailValidator.isEmail(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given email is not contain @ an dot, when isEmail called, then should return false`() {

        // Given
        val field = "something"
        val expectedResult = false

        // When
        val actualResult = emailValidator.isEmail(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }
}