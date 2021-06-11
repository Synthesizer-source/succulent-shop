package school.cactus.succulentshop

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import school.cactus.succulentshop.auth.validation.PasswordValidator

class PasswordValidatorTest {

    private lateinit var passwordValidator: PasswordValidator

    @Before
    fun setup() {
        passwordValidator = PasswordValidator()
    }

    @Test
    fun `given password contains number, lowercase, uppercase and special, when isPassword called, then should return true`() {

        // Given
        val field = "01_Hasan_26"
        val expectedResult = true

        // When
        val actualResult = passwordValidator.isPassword(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given password contains number and lowercase and upperCase, when isPassword called, then should return false`() {

        // Given
        val field = "01Hasan26"
        val expectedResult = false

        // When
        val actualResult = passwordValidator.isPassword(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given password contains number and lowercase and special, when isPassword called, then should return false`() {

        // Given
        val field = "01_hasan_26"
        val expectedResult = false

        // When
        val actualResult = passwordValidator.isPassword(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given password contains number and uppercase and special, when isPassword called, then  should return false`() {

        // Given
        val field = "01_HASAN_26"
        val expectedResult = false

        // When
        val actualResult = passwordValidator.isPassword(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given password contains lowercase and uppercase and special, when isPassword called, then should return false`() {

        // Given
        val field = "_Hasan_"
        val expectedResult = false

        // When
        val actualResult = passwordValidator.isPassword(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }
}