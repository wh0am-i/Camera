package com.example.camera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Camera extends AppCompatActivity {
    ImageView foto;
    Button fotobtt, escolhebtt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        foto = findViewById(R.id.iv);
        fotobtt = findViewById(R.id.fotobtt);
        escolhebtt = findViewById(R.id.escolhebtt);
        fotobtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
        escolhebtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolheFoto();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //checa as próprias permissões, se a permissão de camera for diferente de garantida; o android.Manifest... é o camera, não o
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 0); //pede a permissão com e envia o código 0
        }
    }

    public void escolheFoto(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        abrirGaleria.launch(i);
    }

    public void tirarFoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, 1); não é mais usado por segurança
        abrirCamera.launch(i);
    }

    ActivityResultLauncher<Intent> abrirCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Bundle dado = data.getExtras();
            Bitmap imagem = (Bitmap) dado.get("data");
            foto.setImageBitmap(imagem);
            Toast.makeText(this, "Tá gostosa, hein!", Toast.LENGTH_SHORT).show();
        }
    }); //arrow function wtffff?
    ActivityResultLauncher<Intent> abrirGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri imagemSelecionada = data.getData();
            String[] localGaleria = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(imagemSelecionada, localGaleria, null, null, null); //query é o comando de select from * x
            c.moveToFirst();
            int coluna = c.getColumnIndex(localGaleria[0]);
            String caminhoFisico = c.getString(coluna);
            c.close();
            Bitmap imagem = BitmapFactory.decodeFile(caminhoFisico);
            foto.setImageBitmap(imagem);
            Toast.makeText(this, "Tá gostosa, hein!", Toast.LENGTH_SHORT).show();
        }
    }); //arrow function wtffff?
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //@Nullable pois pode receber nulo, caso abra a câmera e não tire foto
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle dado = data.getExtras();
            Bitmap imagem = (Bitmap) dado.get("data");
            foto.setImageBitmap(imagem);
            Toast.makeText(this, "Tá gostosa, hein!", Toast.LENGTH_SHORT).show();
        }
    }*/

}