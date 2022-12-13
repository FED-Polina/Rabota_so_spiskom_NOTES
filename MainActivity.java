package fedulova.polina303.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fedulova.polina303.notes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null; //инициализируем объект привязки C Sharp в файле build.gradleModule
    private Integer selectedNotePosition = -1;
    private ArrayAdapter<Note> adp;//инициализация адаптера note

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());//создание объекта привязки
        //setContentView(R.layout.activity_main); было
        setContentView(binding.getRoot()); //стало

        adp = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1);// create note array

        binding.listNotes.setAdapter(adp); //установить адаптер на листбокс
        binding.listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() /* отслеживание нажатия на элемент листа заметок*/{
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedNotePosition = position; //запись позиции нажатия на элемент
                binding.buttonEdit.setEnabled(true);
                binding.buttonDelete.setEnabled(true);
            }
        });
        metodOtslejivaniaNajatia();//метод в код добавляем слушатели на кнопки
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) /*метод для отслеживания какие данные приходят со второй активности*/ {
        if (data != null && resultCode == RESULT_OK) { //получение переданных данных
            int pos = data.getIntExtra("my-note-index", -1);
            String title = data.getStringExtra("my-note-title");
            String content = data.getStringExtra("my-note-content");

            if (pos == -1) { //для новой записи
                Note n = new Note();
                n.title = title;
                n.content = content;
                adp.add(n);
            } else {
                Note n = adp.getItem(pos);
                n.title = title;
                n.content = content;
            }
            adp.notifyDataSetChanged(); //update list box
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void metodOtslejivaniaNajatia() {
        binding.buttonNew.setOnClickListener(new View.OnClickListener() /*установить слушатель нажатий на buttonNew*/ {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MainActivity2.class); //создание намерения перехода на 2ю activity

                startActivityForResult(i, 12345); //метод для запуска 2й activity
            }
        });

        binding.buttonEdit.setOnClickListener(new View.OnClickListener() /*установить слушатель нажатий на buttonEdit*/ {
            @Override
            public void onClick(View view) { //передаем данные на 2ю activity
                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                i.putExtra("my-note-index", selectedNotePosition/*передача индакса выбранного элемента*/); //share note data with new activity
                i.putExtra("my-note-title", adp.getItem(selectedNotePosition).title);
                i.putExtra("my-note-content", adp.getItem(selectedNotePosition).content);

                startActivityForResult(i, 12345); //show note editing activity
            }
        });

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() /*установить слушатель нажатий на buttonDelete*/ {
            @Override
            public void onClick(View view) {
                createDialog().show();
            }
        });

    }


    //Метод для вывода DialogWindow
    @NonNull
    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Точно ли Вы хотите удалить запись?")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        adp.remove(adp.getItem(selectedNotePosition));
                        adp.notifyDataSetChanged(); //update list box
                        dialog.cancel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}