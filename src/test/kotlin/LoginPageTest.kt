import io.github.vananos.sosedi.data.NEW_USER_WITHOUT_PROFILE
import io.github.vananos.sosedi.steps.LOGIN_PAGE
import io.github.vananos.sosedi.steps.UserSteps
import io.github.vananos.sosedi.utils.DriverExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.function.Predicate


@ExtendWith(DriverExtension::class)
@DisplayName("Login page test")
class LoginPageTest(private val user: UserSteps) {

    private val validUser = NEW_USER_WITHOUT_PROFILE

    @BeforeEach
    fun setUp() {
        user.inAddressBar {
            open(LOGIN_PAGE)
        }
    }

    @Test
    @DisplayName("login with new existing user")
    fun loginWithNewExistingUser() {
        user.stayingOnLoginPage {
            fillLoginForm(validUser.username, validUser.password)
            clickOnSubmitButton()
            waitForPopup(1)
        }
    }

    @Test
    @DisplayName("login with wrong credentials")
    fun loginWithWrongCredentials() {
        user.stayingOnLoginPage {
            fillLoginForm(validUser.username + "invalid", validUser.password + "invalid")
            clickOnSubmitButton()

            waitForErrors(1)

            val errors = getErrors()

            assertThat(errors)
                .hasSize(1)
                .has(Condition(Predicate { it[0].text == "Невеная пара логин/пароль" }, "should see error"))
        }
    }
}