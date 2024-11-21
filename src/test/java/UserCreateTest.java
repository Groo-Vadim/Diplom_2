import user.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class UserCreateTest {

    private String accessToken;
    // Создаем экземпляр userActionsSetup
    private final UserActionsSetup userActionsSetup = new UserActionsSetup();

    @Test
    @DisplayName("Test: user can be created")
    public void createLoginAndDeleteUser() {
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));
        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Логин пользователя
        Response loginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));

        // Проверяем статус-код логина
        loginResponse.then().assertThat().statusCode(SC_OK);

        // Сохраняем accessToken из тела ответа
        String idString = loginResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Удаление пользователя
        Response deleteResponse = userActionsSetup.deleteUser(accessToken);

        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(SC_ACCEPTED);
        // Проверяем, что ответ содержит поле success : true
        deleteResponse.then().assertThat().body("success", equalTo(true));
    }


    @Test
    @DisplayName("Test: create two identical users")
    public void createDuplicateUser() {
        // Данные для пользователя
        Response createResponse1 = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа первого пользователя
        createResponse1.then().assertThat().statusCode(SC_OK);
        createResponse1.then().assertThat().body("success", equalTo(true));

        // Логин под данными первого пользователя, чтобы убедиться, что он создан
        Response loginResponse1 = userActionsSetup.loginUser(new User("resu@btr1.ru", "123456"));

        // Проверяем статус-код логина
        loginResponse1.then().assertThat().statusCode(SC_OK);
        // Сохраняем accessToken пользователя
        String idString = loginResponse1.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Пытаемся создать второго пользователя с теми же данными
        Response createResponse2 = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));
        // Проверяем статус-код и ожидание ошибки
        createResponse2.then().assertThat().statusCode(SC_FORBIDDEN);
        createResponse2.then().assertThat().body("success", equalTo(false));

        // Удаление пользователя
        Response deleteResponse = userActionsSetup.deleteUser(accessToken);
        deleteResponse.then().assertThat().statusCode(SC_ACCEPTED);
        deleteResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test: create user without Login")
    public void createUserWithoutLogin() {
        // Создаем тело запроса без логина
        Response createResponse = userActionsSetup.createUser(new User("", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_FORBIDDEN);
        createResponse.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test: create user without Password")
    public void createUserWithoutPassword() {
        // Создаем тело запроса без пароля
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_FORBIDDEN);
        createResponse.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test: create user without Name")
    public void createUserWithoutName() {
        // Создаем тело запроса без имени
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", ""));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_FORBIDDEN);
        createResponse.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Test: create user without Login and Password")
    public void createUserWithoutLoginAndPassword() {
        // Создаем тело запроса без логина и пароля
        Response createResponse = userActionsSetup.createUser(new User("", "", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_FORBIDDEN);
        createResponse.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}