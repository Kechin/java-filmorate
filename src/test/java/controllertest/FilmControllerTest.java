package controllertest;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//    целочисленный идентификатор — id;
//    электронная почта — email;
//    логин пользователя — login;
//    имя для отображения — name;
//    дата рождения — birthday.
public class FilmControllerTest {
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
    public void filmTest () throws IOException, InterruptedException {

        Film film0=new Film(0,null,null,null,0);//fail
        Film film1= new Film(1,"film1",MAXIMUM_LENGTH_STRING,LocalDate.of(1985,2,2),22);//OK
        Film film2= new Film(1,"film2",OVER_LENGTH_STRING,LocalDate.of(1985,2,2),22);//fail
        Film film3= new Film(1,"film3","Good film",MIN_DATE,9);//OK
        Film film4= new Film(1,"film4","Good film",MIN_DATE,9);//OK

        String json= gson.toJson(film0);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).header("Content-type", "application/json").build();
        HttpResponse<String> actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(false, actual.body().contains("null"));

        json= gson.toJson(film1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(actual.body());
        Assert.assertEquals(true, actual.body().contains("film1"));

        json= gson.toJson(film2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(false, actual.body().contains("film2"));

        json= gson.toJson(film3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals(true, actual.body().contains("film3"));
        json= gson.toJson(film4);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body)
                .header("Content-type", "application/json").build();
        actual = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(false, actual.body().contains("film4"));

    }

}