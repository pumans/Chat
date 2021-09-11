package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Runnable loadChat;      // загрузка данных из удаленного ресурса
    private String serverResponse;  // тело ответа сервера
    private Runnable showResponse;  // отображение ответа сервера

    private TextView tvChat;        // ссылка на виджет вывода чата

    private ArrayList<Message> messages;
    private Runnable parseResponse; // разбор ответа, формирование коллекции

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // получение ссылок на интерфейсные элементы
        tvChat = findViewById(R.id.tvChat);
        // добавляем прокрутку
        tvChat.setMovementMethod(new ScrollingMovementMethod());
        // демонстрация работы
        new Thread(loadChat).start();
    }

    /**
     * конструктор (в отличии onCreate запускается один раз)
     * в нем делаем все начальные присваивания
     */
    public ChatActivity() {
        loadChat = () -> {
            // извлекаем адрес из ресурсного файла
            String chatURL = getString(R.string.serverURL);
            // создаем объект удаленного подключения
            try {
                URL url = new URL("https://chat.momentfor.fun");
                // открываем подключение, получаем данные
                InputStream reader = url.openStream();
                // сохраняем данные в буфере,
                StringBuilder builder = new StringBuilder();
                int sym;
                while ((sym = reader.read()) != -1) {
                    builder.append((char) sym);

                }
                // после полцчения данных освобождаем ресурсы
                reader.close();
                //корректируем кодировку, сохраняем результат в serverResponse
                serverResponse = new String(
                        builder.toString()                          //собранное тело ответа сервера
                                .getBytes(StandardCharsets.ISO_8859_1), // стандарт HTTP
                        StandardCharsets.UTF_8                      // отображаемая кодировка в нашем приложении
                );
                /*
                // запускаем их отображение в потоке интерфейса
                MainActivity.this.runOnUiThread(parseResponse);
                */
                // запускаем парсер ответа (разбор JSON)
                new Thread(parseResponse).start();
            } catch (IOException e) {

                // логируем исключение для отладки
                Log.e("Chat", e.getMessage());
            }
        };

        showResponse = () -> {
            // проходим циклом по коллекции  сообщений, выводим их в tvChat
            if (messages == null) return;
            int count = messages.size();
            // вывод сообщений
            for(int i = 0; i < count; i++){
                if(i != 0){
                    tvChat.append(messages.get(i).toString());
                } else{
                    tvChat.setText(messages.get(i).toString());
                }
            }
        };

        parseResponse = () -> {
            // ответ от сервера имеет структуру {status:1, data: [{},...,{}]}
            // В целом ответ - объект JSON
            try {
                JSONObject result = new JSONObject(serverResponse);
                // проверяем статус объекта, нормальный - 1
                int status = result.getInt("status");
                if (status != 1) {
                    Log.e("Chat", "Response status" + status);
                    return; // логируем ошибку и останавливаем
                }
                // извлекаем сообщения - это массив под именем data
                JSONArray data = result.getJSONArray("data");
                // проходим циклом по массиву, конвертируем в ORM Message
                // создаем коллекцию
                messages = new ArrayList<>();
                int len = data.length();
                for (int i = 0; i < len; i++) {
                    messages.add(new Message(data.getJSONObject(i)));
                }
                ChatActivity.this.runOnUiThread(showResponse);
            } catch (JSONException e) {
                Log.e("Chat", e.getMessage());
            }
        };
    }
}