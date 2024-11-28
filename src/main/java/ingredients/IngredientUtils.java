package ingredients;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.apache.http.HttpStatus.SC_OK;

public class IngredientUtils {

    private static final String INGREDIENTS_API_URL = "https://stellarburgers.nomoreparties.site/api/ingredients";

    // Метод для получения случайного массива ID ингредиентов
    public static String[] getRandomIngredientIds(int count) {
        Response response = RestAssured.get(INGREDIENTS_API_URL);
        // Проверка статуса ответа
        response.then().assertThat().statusCode(SC_OK);

        List<String> ingredientIds = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject ingredient = dataArray.getJSONObject(i);
            ingredientIds.add(ingredient.getString("_id"));
        }
        // Перемешиваем список
        Collections.shuffle(ingredientIds);

        if (count > ingredientIds.size()) {
            count = ingredientIds.size();
        }

        // Преобразуем подсписок ingredientIds в массив и возвращаем его
        return ingredientIds.subList(0, count).toArray(new String[0]);
    }
}