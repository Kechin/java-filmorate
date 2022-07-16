package controllertest;

import com.google.gson.*;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//    целочисленный идентификатор — id;
//    электронная почта — email;
//    логин пользователя — login;
//    имя для отображения — name;
//    дата рождения — birthday.
public class UserControllerTest {
    private final LocalDate MIN_DATE=LocalDate.of(1895,12,28);
    private final String MAXIMUM_LENGTH_STRING =new String(new char[200]);
    private final String OVER_LENGTH_STRING =new String(new char[201]);
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
                    (JsonSerializer<LocalDate>) new JsonSerializer<LocalDate>() {

                        @Override
                        public JsonElement serialize(LocalDate t, java.lang.reflect.Type type,
                                                     JsonSerializationContext jsc) {
                            if (t != null) {
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                return new JsonPrimitive(t.format(dateTimeFormatter));
                            } else {
                                return null;
                            }
                        }
                    }).registerTypeAdapter(LocalDate.class,
                    (JsonDeserializer<LocalDate>) new JsonDeserializer<LocalDate>() {

                        @Override
                        public LocalDate deserialize(JsonElement je, java.lang.reflect.Type type,
                                                     JsonDeserializationContext jdc) {
                            if (je.getAsJsonPrimitive().getAsString() != null) {
                                return LocalDate.parse(
                                        je.getAsJsonPrimitive().getAsString().substring(0, 10).trim(),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            } else {
                                return null;
                            }
                        }
                    })
            .create();
    @Test
    public void userTest() throws IOException, InterruptedException {


        User user0 = new User(0,null,null,null,LocalDate.of(2001,1,1));
        User user1 = new User(1,"ya@ya.ru","Login1","",LocalDate.of(1988,1,1));
        User user2 = new User(1,"ya@ya","Login2","Nmae",LocalDate.of(2023,1,1));
        User user3 = new User(1,"ya@ya","Login3"," ",LocalDate.of(2023,1,1));
        String json= gson.toJson(user0);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).header("Content-type", "application/json").build();
        HttpResponse<String> actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(false, actual.body().contains("null"));

        json= gson.toJson(user1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(actual.body());
        Assert.assertEquals(true, actual.body().contains("name\":\"Login1"));

        json= gson.toJson(user2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(false, actual.body().contains("Login2"));

        json= gson.toJson(user3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(actual.body());
        Assert.assertEquals(false, actual.body().contains("name\":\" "));
    }
}

