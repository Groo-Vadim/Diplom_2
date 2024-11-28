package order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;


public class OrderSetup {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private static final String ORDER_ENDPOINT = "/api/orders";

    @Step("Создание заказа для неавторизованного пользователя")
    public static Response createOrderNotAuthorized(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(BASE_URL + ORDER_ENDPOINT);
    }

    @Step("Создание заказа для авторизованного пользователя")
    public static Response createOrderAuthorized(Order order, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post(BASE_URL + ORDER_ENDPOINT);
    }

    @Step("Получение заказов неавторизованного пользователя")
    public static Response getOrderNotAuthorized() {
        return given()
                .get(BASE_URL + ORDER_ENDPOINT);
    }

    @Step("Получение заказов авторизованного пользователя")
    public static Response getOrderAuthorized(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .get(BASE_URL + ORDER_ENDPOINT);
    }
}