package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;



// Класс для работы с API пользователя
public class UserActionsSetup {
    // Базовый URL
    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    // Ручки API
    // Регистрация пользователя
    private static final String CREATE_USER_ENDPOINT = "/api/auth/register";
    // Авторизация пользователя
    private static final String LOGIN_USER_ENDPOINT = "/api/auth/login";
    // Удаление пользователя
    private static final String DELETE_USER_ENDPOINT = "/api/auth/user";
    // Получение информации о пользователе
    private static final String GET_USER_DATA = "/api/auth/user";
    // Обновление информации о пользователе
    private static final String UPDATE_USER_DATA = "/api/auth/user";

    @Step("Метод для создания пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + CREATE_USER_ENDPOINT);
    }

    @Step("Метод для логина пользователя")
    public Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + LOGIN_USER_ENDPOINT);
    }

    @Step("Метод для удаления пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_URL + DELETE_USER_ENDPOINT);
    }

    @Step("Метод обновления информации о пользователе без авторизации")
    public Response updateUserNotAuthorization(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .patch(BASE_URL + UPDATE_USER_DATA);
    }

    @Step("Метод обновления информации о пользователе c авторизацией")
    public Response updateUserAuthorization(User user, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(BASE_URL + UPDATE_USER_DATA);
    }

    @Step("Получение информации о пользователе")
        public Response getUserData(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(BASE_URL + GET_USER_DATA);
    }
}