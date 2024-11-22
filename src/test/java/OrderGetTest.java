import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.*;
import org.junit.Test;
import user.User;
import user.UserActionsSetup;
import java.util.Arrays;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class OrderGetTest {


    private String accessToken;
    private final UserActionsSetup userActionsSetup = new UserActionsSetup();

    @Test
    @DisplayName("Test: get order with ingredients without authorization")
    public void GetOrderNotAuthorized() {
        // Получение заказа
        Response getOrderResponse = OrderSetup.getOrderNotAuthorized();
        // Проверяем статус-код
        getOrderResponse.then().assertThat().statusCode(SC_UNAUTHORIZED);
        getOrderResponse.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Test: get order with ingredients with authorization")
    public void GetOrderAuthorized() {
        // Данные для пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));

        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));
        // Сохраняем accessToken из тела ответа
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;

        // Создание заказа
        Response createOrderResponse = OrderSetup.createOrderAuthorized(new Order(new String[]{"61c0c5a71d1f82001bdaaa7a", "61c0c5a71d1f82001bdaaa71"}), accessToken);
        // Проверяем статус-код
        createOrderResponse.then().assertThat().statusCode(SC_OK);
        createOrderResponse.then().assertThat().body("success", equalTo(true));

        // Получение заказа
        Response getOrderResponse = OrderSetup.getOrderAuthorized(accessToken);
        // Проверяем статус-код
        getOrderResponse.then().assertThat().statusCode(SC_OK);
        getOrderResponse.then().assertThat().body("orders[0].ingredients", equalTo(Arrays.asList("61c0c5a71d1f82001bdaaa7a", "61c0c5a71d1f82001bdaaa71")));

        // Удаление пользователя
        Response deleteResponse = userActionsSetup.deleteUser(accessToken);
        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(SC_ACCEPTED);
        // Проверяем, что ответ содержит поле success : true
        deleteResponse.then().assertThat().body("success", equalTo(true));
    }
}