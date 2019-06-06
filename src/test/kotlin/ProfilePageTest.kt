import io.github.vananos.sosedi.data.ProfileFormData
import io.github.vananos.sosedi.data.USER_WITH_EXISTING_PROFILE
import io.github.vananos.sosedi.steps.LOGIN_PAGE
import io.github.vananos.sosedi.steps.NavLinks
import io.github.vananos.sosedi.steps.UserSteps
import io.github.vananos.sosedi.utils.DriverExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.WebElement
import java.util.stream.Stream

@ExtendWith(DriverExtension::class)
@DisplayName("Profile page test")
class ProfilePageTest(val user: UserSteps) {

    @BeforeEach
    fun setUp() {
        user.apply {
            inAddressBar { open(LOGIN_PAGE) }
            stayingOnLoginPage {
                fillLoginForm(USER_WITH_EXISTING_PROFILE.username, USER_WITH_EXISTING_PROFILE.password)
                clickOnSubmitButton()
            }
            clickOnNabBarToggle()
            waitForNavBarExpanded()
            clickOnNavItem(NavLinks.PROFILE)
        }
    }

    @Test
    @DisplayName("change user data")
    fun changeUserProfileData() {
        val validProfileData = validProfileData()

        user.apply {
            stayingOnProfilePage {
                fillProfileForm(validProfileData)
                clickOnSaveButton()
            }
            waitFor(1, "success notification should be shown") {
                getNotifications().any { it.text.contains("Данные успешно обновлены!") }
            }
        }
    }

    @ParameterizedTest(name = "update form with invalid parameter: {1}")
    @MethodSource("invalidParametersProvider")
    fun shouldGetErrorWhenProvideInvalidParameters(
        invalidParameters: ProfileFormData,
        description: String,
        checkInvalidParameterAssertion: (el: List<WebElement>) -> Unit
    ) {
        user.apply {
            stayingOnProfilePage {
                fillProfileForm(invalidParameters)
                clickOnSaveButton()
            }

            waitFor(1, "erros are shown") {
                getErrors().isNotEmpty()
            }

            checkInvalidParameterAssertion(getErrors())
        }
    }

    companion object {
        @JvmStatic
        fun invalidParametersProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    validProfileData().apply {
                        name += "TOOOOOOOOOLOOOOONG"
                    }, "name",
                    getErrorMatcherForField("имя")
                ),
                Arguments.of(
                    validProfileData().apply {
                        surname += "TOOOOOOLOOOONG"
                    }, "surname",
                    getErrorMatcherForField("фамилия")
                )
            )
        }
    }
}

fun validProfileData(): ProfileFormData {
    val timestamp = System.nanoTime().toString().drop(6)
    return ProfileFormData(
        "t$timestamp",
        "ts$timestamp",
        "19-12-1991",
        "79219823244",
        listOf("books", "tv"),
        "description $timestamp"
    )
}
