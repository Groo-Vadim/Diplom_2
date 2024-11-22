import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderSetup;
import org.junit.Test;
import user.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class OrderCreateTest {

    private String accessToken;
    private final UserActionsSetup userActionsSetup = new UserActionsSetup();

    @Test
    @DisplayName("Test: creating an order with ingredients without authorization")
    public void CreateOrderWithIngredientsNotAuthorized() {

        // Создание заказа
        Response createResponse = OrderSetup.createOrderNotAuthorized(new Order(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72"}));
        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test: creating an order with an invalid ingredient hash without authorization")
    public void CreateOrderInvalidHashIngredientNotAuthorized() {

        // Создание заказа
        Response createResponse = OrderSetup.createOrderNotAuthorized(new Order(new String[]{"61c0c5a71d1f82001bdaaa6d12", "6цc0c5a71d1f82001bdaaa72ц"}));
        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
        //createResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test: creating an order without ingredients without authorization")
    public void CreateOrderWithoutIngredientsNotAuthorized() {

        // Создание заказа
        Response createResponse = OrderSetup.createOrderNotAuthorized(new Order(null));
        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_BAD_REQUEST);
        createResponse.then().assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Test: Creating an order with ingredients with authorization")
    public void CreateOrderWithIngredientsAuthorized() {

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

        // Удаление пользователя
        Response deleteResponse = userActionsSetup.deleteUser(accessToken);
        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(SC_ACCEPTED);
        // Проверяем, что ответ содержит поле success : true
        deleteResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test: creating an order with an invalid ingredient hash with authorization")
    public void CreateOrderInvalidHashIngredientWithAuthorized() {

        // Данные для пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));
        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));
        // Сохраняем accessToken из тела ответа
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;

        // Создание заказа
        Response createOrderResponse = OrderSetup.createOrderAuthorized(new Order(new String[]{"61c0c5a71d1f82001bdaaa6d12", "6цc0c5a71d1f82001bdaaa72ц"}), accessToken);
        // Проверяем статус-код
        createOrderResponse.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);

        // Удаление пользователя
        Response deleteResponse = userActionsSetup.deleteUser(accessToken);
        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(SC_ACCEPTED);
        // Проверяем, что ответ содержит поле success : true
        deleteResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test: creating an order without ingredients with authorization")
    public void CreateOrderWithoutIngredientsWithAuthorized() {

        // Данные для пользователя
        Response createResponse = userActionsSetup.createUser(new User("resu@btr1.ru", "123456", "resu"));
        // Проверяем статус-код
        createResponse.then().assertThat().statusCode(SC_OK);
        createResponse.then().assertThat().body("success", equalTo(true));
        // Сохраняем accessToken из тела ответа
        String idString = createResponse.jsonPath().getString("accessToken");
        accessToken = idString;

        // Создание заказа
        Response createOrderResponse = OrderSetup.createOrderAuthorized(new Order(null), accessToken);
        // Проверяем статус-код
        createOrderResponse.then().assertThat().statusCode(SC_BAD_REQUEST);
        createOrderResponse.then().assertThat().body("message", equalTo("Ingredient ids must be provided"));

        // Удаление пользователя
        Response deleteResponse = userActionsSetup.deleteUser(accessToken);
        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(SC_ACCEPTED);
        // Проверяем, что ответ содержит поле success : true
        deleteResponse.then().assertThat().body("success", equalTo(true));
    }
}