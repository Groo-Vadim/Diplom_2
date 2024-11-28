import user.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class UserLoginTest {

    // Создаем экземпляр userActionsSetup
    private final UserActionsSetup userActionsSetup = new UserActionsSetup();
    // Инициализируем accessToken
    private String accessToken;


    @After
    public void deleteUser() {
        // Пропустить выполнение метода, если флаг установлен
        if (accessToken != null) {
            Response deleteResponse = userActionsSetup.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Test: user can login")
    public void userCanLogin() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Логин пользователя
        Response loginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));

        // Проверяем статус-код и accessToken пользователя
        loginResponse.then().assertThat().statusCode(SC_OK);
        String idString = loginResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);
    }

    @Test
    @DisplayName("Test: login with invalid Login")
    public void loginInvalidLogin() {
        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Пытаемся выполнить логин с неверным логином, и с верным паролем
        Response loginResponse = userActionsSetup.loginUser(new User("resu@btr11.ru", "123456"));

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"));

        // Получаем accessToken пользователя для удаления
        Response successfulLoginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));
        String idString = successfulLoginResponse.jsonPath().getString("accessToken");
        accessToken = idString;
    }

    @Test
    @DisplayName("Test: login with invalid Password")
    public void loginInvalidPassword() {
        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Пытаемся выполнить логин с верным логином, и неверным паролем
        Response loginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "12345"));

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"));

        // Получаем accessToken пользователя для удаления
        Response successfulLoginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));
        String idString = successfulLoginResponse.jsonPath().getString("accessToken");
        accessToken = idString;
    }

    @Test
    @DisplayName("Test: login without Login")
    public void loginWithoutLogin() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // логин без Логина (передаем пустую строку вместо логина)
        Response loginResponse = userActionsSetup.loginUser(new User("", "123456"));

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"));

        // Получаем accessToken пользователя для удаления
        Response successfulLoginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));
        String idString = successfulLoginResponse.jsonPath().getString("accessToken");
        accessToken = idString;

    }

    @Test
    @DisplayName("Test: login without Password")
    public void loginWithoutPassword() {
        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Пытаемся выполнить логин с логином, но без пароля
        Response loginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", ""));

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"));

        // Получаем accessToken пользователя для удаления
        Response successfulLoginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));
        String idString = successfulLoginResponse.jsonPath().getString("accessToken");
        accessToken = idString;
    }

    @Test
    @DisplayName("Test: login without Login and Password")
    public void loginWithoutLoginAndPassword() {
        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Пытаемся выполнить логин без логина и без пароля
        Response loginResponse = userActionsSetup.loginUser(new User("", ""));

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"));

        // Получаем accessToken пользователя для удаления
        Response successfulLoginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));
        String idString = successfulLoginResponse.jsonPath().getString("accessToken");
        accessToken = idString;
    }

    @Test
    @DisplayName("Test: login as a non-existent user")
    public void loginNullLogin() {

        // Пытаемся выполнить логин с несуществующим логином
        Response loginResponse = userActionsSetup.loginUser(new User("resu12345@btr12.ru", "1234562222"));
        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"));
    }
}