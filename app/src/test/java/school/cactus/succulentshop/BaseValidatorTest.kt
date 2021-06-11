package school.cactus.succulentshop

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import school.cactus.succulentshop.auth.validation.Validator

class BaseValidatorTest {

    private lateinit var validator: Validator

    @Before
    fun setup() {
        val MIN = 0
        val MAX = Int.MAX_VALUE
        validator = object : Validator() {
            override fun isValid(field: String): Boolean {
                return isLonger(MIN, field) && isShorter(MAX, field) && isNotEmpty(field)
            }


        }
    }

    @Test
    fun `given longer field than minLen, when isLonger called, then should return true`() {

        // Given
        val field = "very_long_field"
        val minLen = 5
        val expectedResult = true

        // When
        val actualResult = validator.isLonger(minLen = minLen, field = field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given field is greater than or equal minLen, when isLonger called, then should return false`() {

        // Given
        val field = "field"
        val minLen = 5
        val expectedResult = false

        // When
        val actualResult = validator.isLonger(minLen = minLen, field = field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given field is shorter maxLen, when isShorter called, then should return true`() {

        // Given
        val field = "short_field"
        val maxLen = 15
        val expectedResult = true

        // When
        val actualResult = validator.isShorter(maxLen = maxLen, field = field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given field is shorter than maxLen, when isShorter called, then should return false`() {

        // Given
        val field = "very_long_field"
        val maxLen = 15
        val expectedResult = false

        // When
        val actualResult = validator.isShorter(maxLen = maxLen, field = field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given field is not empty, when isNotEmpty called, then should return true`() {

        // Given
        val field = "something"
        val expectedResult = true

        // When
        val actualResult = validator.isNotEmpty(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `given field is empty, when isNotEmpty called, then should return false`() {

        // Given
        val field = ""
        val expectedResult = false

        // When
        val actualResult = validator.isNotEmpty(field)

        // Then
        assertThat(actualResult).isEqualTo(expectedResult)
    }
}