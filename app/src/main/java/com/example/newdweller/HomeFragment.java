package com.example.newdweller;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.Context;

public class HomeFragment extends Fragment {
    //List<Post>mData = new List<>();
    FirebaseAuth mAuth;

    FirebaseFirestore fStore;
    TextView fullName;
    String userID;
    ImageView popupPostImg;
    Button popupBtn;
    EditText popupTitle, popupDescription;
    ProgressBar popupProgressBar;
    Dialog popAddPost;
    FloatingActionButton drawer;
    RecyclerView recyclerView;
    List<Post> postArrayList;
    PostAdapter postAdapter;
    private Uri pickedImgUri = null;
    private final int GALLERY_REQ_CODE=1000;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Подбор данных");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.post_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        postArrayList = new ArrayList<Post>();

        postAdapter = new PostAdapter(getActivity(),postArrayList);
        recyclerView.setAdapter(postAdapter);

        eventChangeListener();
        iniPopup();


        //etupPoupImageClick();
        drawer = view.findViewById(R.id.clickPlus);
        drawer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 popAddPost.show();

             }

         });

        popupPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,GALLERY_REQ_CODE);

            }
        });


    }

    private void eventChangeListener() {
            fStore.collection("post").orderBy("id_post",Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null){
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Log.e("Ошибка считывания из Firestore",error.getMessage());
                                return;
                            }
                            for(DocumentChange dc:value.getDocumentChanges()){
                                if(dc.getType()== DocumentChange.Type.ADDED){
                                    postArrayList.add(dc.getDocument().toObject(Post.class));
                                }
                                postAdapter.notifyDataSetChanged();
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    });


    }


    //открыть изображение из галерии
   /*private void setupPoupImageClick() {

    }*/

   @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
              //  popupPostImg.setImageURI(data.getData());
                pickedImgUri =data.getData();
                popupPostImg.setImageURI(pickedImgUri);
            }
        }
   }
   /* private void checkAndRequestForPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getContext(),"pla accept",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PreqCode);
            }

        }else{
            openGallery();
        }
    }

    /*private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(galleryIntent);
        //startActivityForResult(galleryIntent,REQUESCODE);
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result){
                    if(result.getResultCode()==Activity.RESULT_OK&& result.getData()!=null){
                        Intent data = result.getData();
                    }
                }
            });
    /*@Override
    public void onActiviyForResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == Activity.RESULT_OK && requestCode == REQUESCODE &&data !=null){
                pickedImgUri = data.getData();

            }
        }*/

    private void iniPopup() {
        popAddPost = new Dialog(getContext());
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;


        popupPostImg = popAddPost.findViewById(R.id.imagePost);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupBtn=popAddPost.findViewById(R.id.popup_create);
        popupProgressBar = popAddPost.findViewById(R.id.popup_progressBar);
        fullName = popAddPost.findViewById(R.id.idFullname);
        userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText(documentSnapshot.getString("fullname"));
            }
        });
        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = popupTitle.getText().toString();
                String desc = popupDescription.getText().toString();
                popupBtn.setVisibility(View.INVISIBLE);
                popupProgressBar.setVisibility(View.VISIBLE);
                //проверяем на пустые значения
                if(!title.isEmpty() && title.length()<40
                &&!desc.isEmpty()&& desc.length()<250
                && pickedImgUri !=null){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("postImg");
                    StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgDownloadLink = uri.toString();
                                    //создание поста
                                    String title = popupTitle.getText().toString();
                                    String descr = popupDescription.getText().toString();
                                    DocumentReference db = fStore.collection("post").document(userID);
                                    /*Post posting = new Post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            imgDownloadLink,
                                            userID);*/

                                    Map<String,Object> post = new HashMap<>();
                                    post.put("id_post",getId());
                                    post.put("title",title);
                                    post.put("description", descr);
                                    post.put("img", imgDownloadLink);
                                    db.set(post).addOnSuccessListener((OnSuccessListener)(aVoid)->{
                                        Log.d(TAG,"Успешно создан" +userID);
                                        popupBtn.setVisibility(View.VISIBLE);
                                        popupProgressBar.setVisibility(View.INVISIBLE);
                                        popAddPost.dismiss();
                                    }).addOnFailureListener(new OnFailureListener(){
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"Неудачно: "+ e.toString());
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   //если фото не подгружено
                                   showMessage(e.getMessage());
                                    popupProgressBar.setVisibility(View.INVISIBLE);
                                    popupBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                }else{
                    showMessage("Пожалуйста, заполните все значения и выберите фото");
                    popupBtn.setVisibility(View.VISIBLE);
                    popupProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }



    private void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

    }

}

