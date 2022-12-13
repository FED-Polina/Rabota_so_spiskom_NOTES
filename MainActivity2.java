package fedulova.polina303.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fedulova.polina303.notes.databinding.ActivityMain2Binding;
import fedulova.polina303.notes.databinding.ActivityMainBinding;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding = null; //инициализируем объект привязки C Sharp в файле build.gradleModule

    int pos; //note array index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());//создание объекта привязки
        //setContentView(R.layout.activity_main2);//было
        setContentView(binding.getRoot()); //стало

        Intent i = getIntent(); //пелучение данных из намерения с 1й activity
        pos = i.getIntExtra("my-note-index", -1);
        binding.editTextTitle.setText(i.getStringExtra("my-note-title"));
        binding.editTextContent.setText(i.getStringExtra("my-note-content"));

        metodOtslejivaniaNajatia();//метод в код добавляем слушатели на кнопки
    }

    private void metodOtslejivaniaNajatia() {
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(); //создание намерение
                i.putExtra("my-note-index", pos); //prepare updated note data back to first activity
                i.putExtra("my-note-title", binding.editTextTitle.getText().toString());
                i.putExtra("my-note-content", binding.editTextContent.getText().toString());

                setResult(RESULT_OK, i); //return to first activity
                finish();
            }
        });
    }


}