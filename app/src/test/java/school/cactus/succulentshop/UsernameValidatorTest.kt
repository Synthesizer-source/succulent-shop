package school.cactus.succulentshop

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import school.cactus.succulentshop.auth.validation.UsernameValidator

class UsernameValidatorTest {

    private lateinit var usernameValidator: UsernameValidator

    @Before
    fun setup() {
        usernameValidator = UsernameValidator()
    }

    @Test
    fun `given username contains lowercase and number and underscore, when isUsername called, then should return true`() {

        // Given
        val field = "hasan_01"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contains lowercase and number, when isUsername called, then should return true`() {

        // Given
        val field = "hasan01"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contains lowercase and underscore, when isUsername called, then should return true`() {

        // Given
        val field = "_hasan_"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contains number and underscore, when isUsername called, then should return true`() {

        // Given
        val field = "01_26"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contain just number, when isUsername called, then should return true`() {

        // Given
        val field = "0126"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contain just lowercase, when isUsername called, then should return true`() {

        // Given
        val field = "hasan"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contain just underscore, when isUsername called, then should return true`() {

        // Given
        val field = "_____"
        val expectedResult = true

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contain uppercase, when isUsername called, then should return true`() {

        // Given
        val field = "Hasan"
        val expectedResult = false

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given username contain special, when isUsername called, then should return true`() {

        // Given
        val field = "01*26"
        val expectedResult = false

        // When
        val actualResult = usernameValidator.isUsername(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }
}