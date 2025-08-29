package com.mehmetalican.shoppingbook.screens

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.shadow.ShadowContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.mehmetalican.shoppingbook.model.Item
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest


@Composable
fun AddItemScreen(saveFunciton: (item : Item) -> Unit){

    val itemName = remember {
        mutableStateOf("")
    }

    val storageName = remember {
        mutableStateOf("")
    }

    val price = remember {
        mutableStateOf("")
    }

    val selectedImageUri by remember{
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    Box (modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            ImagePicker(onImageSelected = {uri ->


            })


            TextField(value = itemName.value,
                placeholder = {
                    Text("Enter Item Name")
                }, onValueChange = {
                    itemName.value=it
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

            TextField(value = storageName.value,
                placeholder = {
                    Text("Enter Item Name")
                }, onValueChange = {
                    storageName.value=it
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

            TextField(value = price.value,
                placeholder = {
                    Text("Enter Item Name")
                }, onValueChange = {
                    price.value=it
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

            Button(onClick = {

                val imageByteArray = selectedImageUri?.let {
                    resizeImage(context,it,600,400)

                }?: ByteArray(0)

                val itemToInsert = Item(itemName.value,storageName.value,price.value, image = ByteArray(1))
                saveFunciton(itemToInsert)
            }) {
                Text("Save")
            }


        }
    }
}
@Composable
fun ImagePicker(onImageSelected: (Uri?) -> Unit){
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current

    val permission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    }else{
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {granted ->
        if(granted){
            galleryLauncher.launch("image/*")
        }else
        {
            Toast.makeText(context,"Permission denied !", Toast.LENGTH_SHORT).show()
        }

    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                "Selected Image",
                modifier = Modifier.size(300.dp, 200.dp)
                    .padding(16.dp)
            )

            onImageSelected(it)

        } ?: Image(
                painter = painterResource(com.mehmetalican.shoppingbook.R.drawable.selectimage),
            contentDescription = "Selected Image",
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .size(300.dp,200.dp)
                .clickable{

                    if(ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED){
                        galleryLauncher.launch("image/*")
                    }else
                    {
                        permissionLauncher.launch(permission)
                    }
                }
            )
    }
}

fun resizeImage(context: Context, uri: Uri, maxWidth : Int , maxHeight : Int) : ByteArray?{

    return try{
        val inputStream = context.contentResolver.openInputStream(uri)
        val orijinalBitmap = BitmapFactory.decodeStream(inputStream)

        val ratio = orijinalBitmap.width.toFloat() / orijinalBitmap.height.toFloat()

        var width = maxWidth
        var height = (width / ratio).toInt()

        if(height > maxHeight){
            height = maxHeight
            width = (height * ratio).toInt()
        }
        val resizeBitmap = Bitmap.createScaledBitmap(orijinalBitmap,width,height,false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        resizeBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
        byteArrayOutputStream.toByteArray()

    } catch (e : Exception){
        e.printStackTrace()
        null
    }




}

