package edu.demo.savemytrip.tripbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import edu.demo.savemytrip.R;
import edu.demo.savemytrip.base.BaseActivity;
import edu.demo.savemytrip.utils.StorageUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TripBookActivity extends BaseActivity {

    //FOR DESIGN
    @BindView(R.id.trip_book_activity_external_choice)
    LinearLayout linearLayoutExternalChoice;
    @BindView(R.id.trip_book_activity_internal_choice) LinearLayout linearLayoutInternalChoice;
    @BindView(R.id.trip_book_activity_radio_external)
    RadioButton radioButtonExternalChoice;
    @BindView(R.id.trip_book_activity_radio_public) RadioButton radioButtonExternalPublicChoice;
    @BindView(R.id.trip_book_activity_radio_volatile) RadioButton radioButtonInternalVolatileChoice;
    @BindView(R.id.trip_book_activity_edit_text)
    EditText editText;

    // FOR DATA
    private static final String FILENAME = "tripBook.txt";
    private static final String FOLDERNAME = "bookTrip";
    private static final String AUTHORITY="edu.demo.savemytrip.fileprovider";

    // PERMISSION PURPOSE
    private static final int RC_STORAGE_WRITE_PERMS = 100;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_trip_book;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureToolbar();
        readFromStorage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                /*TODO*/
                shareFile();
                return true;
            case R.id.action_save:
                /*TODO*/
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // --------------------
    // ACTIONS
    // --------------------

    @OnCheckedChanged({R.id.trip_book_activity_radio_internal, R.id.trip_book_activity_radio_external,
            R.id.trip_book_activity_radio_private, R.id.trip_book_activity_radio_public,
            R.id.trip_book_activity_radio_normal, R.id.trip_book_activity_radio_volatile})
    public void onClickRadioButton(CompoundButton button, boolean isChecked){
        if (isChecked) {
            switch (button.getId()) {
                case R.id.trip_book_activity_radio_internal:
                    this.linearLayoutExternalChoice.setVisibility(View.GONE);
                    this.linearLayoutInternalChoice.setVisibility(View.VISIBLE);
                    break;
                case R.id.trip_book_activity_radio_external:
                    this.linearLayoutExternalChoice.setVisibility(View.VISIBLE);
                    this.linearLayoutInternalChoice.setVisibility(View.GONE);
                    break;
            }
        }
        readFromStorage();
    }

    private void save(){
        if (radioButtonExternalChoice.isChecked()){
            writeOnExternalStorage();
        } else {
            // TODO : Save on internal storage
            writeOnInternalStorage();
        }
    }

    // ------------------------
    // UTILS - STORAGE
    // ------------------------
    @AfterPermissionGranted(RC_STORAGE_WRITE_PERMS)
    private void readFromStorage(){
        // CHECK PERMISSION
        if (!EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)){
            EasyPermissions.requestPermissions(this, getString(R.string.title_permission),
                    RC_STORAGE_WRITE_PERMS, WRITE_EXTERNAL_STORAGE);
            return;
        }

        if (radioButtonExternalChoice.isChecked()){
            if (StorageUtils.isExternalStorageReadable()){
                // TODO :  READ FROM EXTERNAL STORAGE
                if (radioButtonExternalPublicChoice.isChecked()){
                    // External - Public
                    editText.setText(StorageUtils.getTextFromStorage(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),this,FILENAME, FOLDERNAME));
                } else {
                    // External - Private
                    editText.setText(StorageUtils.getTextFromStorage(
                            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), this, FILENAME, FOLDERNAME
                    ));
                }
            } else {
                // TODO : READ FROM INTERNAL STORAGE
                if (radioButtonInternalVolatileChoice.isChecked()){
                    // TODO : CACHE
                    editText.setText(StorageUtils.getTextFromStorage(
                            getCacheDir(), this, FILENAME, FOLDERNAME
                    ));
                } else {
                    // TODO : NORMAL
                    editText.setText(StorageUtils.getTextFromStorage(
                            getFilesDir(), this, FILENAME, FOLDERNAME
                    ));
                }
            }
        }
    }

    private void writeOnExternalStorage(){
        if (StorageUtils.isExternalStorageWritable()){
            if (radioButtonExternalPublicChoice.isChecked()){
                StorageUtils.setTextInStorage(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                        this, FILENAME, FOLDERNAME, editText.getText().toString());
            } else {
                StorageUtils.setTextInStorage(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                        this,FILENAME, FOLDERNAME, editText.getText().toString());
            }
        } else {
            Toast.makeText(this, getString(R.string.external_storage_impossible_create_file),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void writeOnInternalStorage(){
        if (radioButtonInternalVolatileChoice.isChecked()){
            StorageUtils.setTextInStorage(getCacheDir(), this, FILENAME,
                    FOLDERNAME, editText.getText().toString());
        } else {
            StorageUtils.setTextInStorage(getFilesDir(), this, FILENAME,
                    FOLDERNAME, editText.getText().toString());
        }
    }


    // -----------------
    // SHARE FILE
    // -----------------
    private void shareFile(){
        File internalFile = StorageUtils.getFileFromStorage(getFilesDir(), this, FILENAME, FOLDERNAME);
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), AUTHORITY, internalFile);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.trip_book_share)));
    }

}
