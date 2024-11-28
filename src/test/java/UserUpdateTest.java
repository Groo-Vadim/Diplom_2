import user.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class UserUpdateTest {

    // Создаем экземпляр userActionsSetup
    private final UserActionsSetup userActionsSetup = new UserActionsSetup();
    // Инициализируем accessToken
    private String accessToken;

  @After
    public void deleteUser() {
      Response deleteResponse = userActionsSetup.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Test: change authorized user data - email")
    public void userAuthorizedUpdateEmail() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserAuthorization(new User("resu1@btr1.ru", "123456", "resu") , accessToken);
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_OK);
        updateResponse.then().assertThat().body("success", equalTo(true));

        // Получение обновленной информации о пользователе
        Response getUpdateResponse = userActionsSetup.getUserData(accessToken);
        // Проверяем статус-код и содержимое ответа
        getUpdateResponse.then().assertThat().statusCode(SC_OK);
        getUpdateResponse.then().assertThat().body("user.email", equalTo("resu1@btr1.ru"));
    }

    @Test
    @DisplayName("Test: change authorized user data - password")
    public void userAuthorizedUpdatePassword() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserAuthorization(new User("resu@btr1.ru", "1234567", "resu") , accessToken);
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_OK);
        updateResponse.then().assertThat().body("success", equalTo(true));

        // Логин пользователя
        Response loginResponse = userActionsSetup.loginUser(new User("resu@btr1.ru", "1234567"));

        // Проверяем статус-код и accessToken пользователя
        loginResponse.then().assertThat().statusCode(SC_OK);

        // Получение обновленной информации о пользователе
        String idString1 = loginResponse.jsonPath().getString("accessToken");
        accessToken = idString1;
        System.out.println("accessToken: " + accessToken);
    }

    @Test
    @DisplayName("Test: change authorized user data - name")
    public void userAuthorizedUpdateName() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserAuthorization(new User("resu@btr1.ru", "123456", "sure") , accessToken);
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_OK);
        updateResponse.then().assertThat().body("success", equalTo(true));

        // Получение обновленной информации о пользователе
        Response getUpdateResponse = userActionsSetup.getUserData(accessToken);
        // Проверяем статус-код и содержимое ответа
        getUpdateResponse.then().assertThat().statusCode(SC_OK);
        getUpdateResponse.then().assertThat().body("user.name", equalTo("sure"));
    }

    @Test
    @DisplayName("Test: change authorized user data - email duplicate")
    public void userAuthorizedUpdateEmailDuplicate() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserAuthorization(new User("test-data@yandex.ru", "123456", "resu"), accessToken);
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_FORBIDDEN);
        updateResponse.then().assertThat().body("message", equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("Test: change not authorized user data - email")
    public void userNotAuthorizedUpdateEmail() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserNotAuthorization(new User("resu1@btr1.ru", "123456", "resu"));
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        updateResponse.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Test: change not authorized user data - password")
    public void userNotAuthorizedUpdatePassword() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        //Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserNotAuthorization(new User("resu@btr1.ru", "1234567", "resu"));
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        updateResponse.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Test: change not authorized user data - name")
    public void userNotAuthorizedUpdateName() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserNotAuthorization(new User("resu@btr1.ru", "123456", "sure"));
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        updateResponse.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Test: change not authorized user data - email duplicate")
    public void userNotAuthorizedUpdateEmailDuplicate() {

        // Создаем тело запроса для создания пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));

        // Получаем accessToken пользователя
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;
        System.out.println("accessToken: " + accessToken);

        // Обновление информации пользователя
        Response updateResponse = userActionsSetup.updateUserNotAuthorization(new User("test-data@yandex.ru", "123456", "resu"));
        // Проверяем статус-код и содержимое ответа
        updateResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        updateResponse.then().assertThat().body("message", equalTo("You should be authorised"));
    }
}