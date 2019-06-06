import io.github.vananos.sosedi.data.NEW_USER_WITHOUT_PROFILE
import io.github.vananos.sosedi.data.RegistrationFormData
import io.github.vananos.sosedi.steps.LOGIN_PAGE
import io.github.vananos.sosedi.steps.REGISTRATION_PAGE
import io.github.vananos.sosedi.steps.UserSteps
import io.github.vananos.sosedi.steps.getFullAddress
import io.github.vananos.sosedi.utils.DriverExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.WebElement
import java.util.function.Predicate
import java.util.stream.Stream


@ExtendWith(DriverExtension::class)
@DisplayName("Registration page tests")
class RegistrationPageTest(private val user: UserSteps) {

    @BeforeEach
    fun setUp() {
        user.apply {
            inAddressBar {
                open(LOGIN_PAGE)
            }
            stayingOnLoginPage {
                clickOnNotRegisteredButton()
            }
            waitFor(1, "redirect to registration page") {
                driver.currentUrl == getFullAddress(REGISTRATION_PAGE)
            }
        }
    }

    @Test
    @DisplayName("registration with valid parameters")
    fun registrationWithValidParameters() {
        val validRegistrationData = validRegistrationData()
        user.apply {
            stayingOnRegistrationPage {
                fillRegistrationForm(validRegistrationData)
                clickOnSubmitFormData()
            }
            waitFor(1, "success notification shown") {
                getNotifications().isNotEmpty()
            }

            assertThat(getNotifications().first())
                .`is`(
                    Condition(
                        Predicate { it.text.contains("${validRegistrationData.name}, мы выслали тебе на почту письмо с подтверждением.") },
                        "successful notification is Shown"
                    )
                )

        }
    }

    @Test
    @DisplayName("try to register already existing user")
    fun alreadyExistingUserTryToRegister() {
        val alreadyExistingEmail = NEW_USER_WITHOUT_PROFILE.username
        user.apply {
            stayingOnRegistrationPage {
                fillRegistrationForm(validRegistrationData().apply {
                    email = alreadyExistingEmail
                })
                clickOnSubmitFormData()

                waitFor(1, "error should appear") {
                    getErrors().isNotEmpty()
                }

                assertThat(getErrors())
                    .hasSize(1)
                    .first()
                    .extracting { it.text }
                    .matches { (it as String).contains("Пользователь с указанным email уже существует", true) }
            }

        }
    }

    @ParameterizedTest(name = "registration with invalid parameter: {1}")
    @MethodSource("invalidParametersProvider")
    fun shouldGetErrorWhenProvideInvalidParameters(
        invalidParameters: RegistrationFormData,
        description: String,
        checkErrors: (el: List<WebElement>) -> Unit
    ) {
        user.apply {
            stayingOnRegistrationPage {
                fillRegistrationForm(invalidParameters)
                clickOnSubmitFormData()
            }

            waitFor(1, "erros are shown") {
                getErrors().isNotEmpty()
            }

            checkErrors(getErrors())
        }
    }

    companion object {
        @JvmStatic
        fun invalidParametersProvider(): Stream<Arguments> {
            val unExistingEmail = "reg_${System.nanoTime()}@gmail.com"

            return Stream.of(
                Arguments.of(
                    validRegistrationData(unExistingEmail).apply {
                        name += "TOOOOOOOOOLOOOOONG"
                    }, "name",
                    getErrorMatcherForField("имя")
                ),
                Arguments.of(
                    validRegistrationData(unExistingEmail).apply {
                        surname += "TOOOOOOLOOOONG"
                    }, "surname",
                    getErrorMatcherForField("фамилия")
                ),
                Arguments.of(
                    validRegistrationData(unExistingEmail).apply {
                        email += "not.email!!?3c_!"
                    }, "email",
                    getErrorMatcherForField("email")
                ),
                Arguments.of(
                    validRegistrationData(unExistingEmail).apply {
                        password = "week"
                        passwordConfirmation = password
                    }, "password",
                    getErrorMatcherForField("пароль")
                ),
                Arguments.of(
                    validRegistrationData(unExistingEmail).apply {
                        passwordConfirmation = "wrong pass confirmation"
                    }, "passwordConfirmation",
                    getErrorMatcherForField("подтверждение пароля")
                ),

                Arguments.of(RegistrationFormData("", "", "", "", ""), "empty fields", { errors: List<WebElement> ->
                    assertThat(errors).hasSize(4)
                })
            )
        }
    }
}

fun validRegistrationData(email: String = "reg_2@gmail.com") = RegistrationFormData(
    "user_test_reg",
    "user_test_reg_1",
    email,
    "password_1P",
    "password_1P"
)

fun getErrorMatcherForField(fieldName: String) = { errors: List<WebElement> ->
    assertThat(errors)
        .hasSize(1)
        .has(
            Condition(
                Predicate { it[0].text.contains(fieldName, true) },
                "contains `$fieldName`"
            )
        )
}
