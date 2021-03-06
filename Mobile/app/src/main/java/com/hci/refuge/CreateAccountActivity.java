package com.hci.refuge;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateAccountActivity extends AppCompatActivity implements DialogInterface.OnClickListener, ViewPager.OnPageChangeListener {

    /**
     * Built from an Android Studio template Activity (as opposed to sample code)
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static UserInfo userInfo;
    static PackageManager pm;

    private static boolean createdFlag = false;

    /**
     * CreateAccountActivity is presented to users who want to start using Refuge.
     * They must input personally identifying information, but the use of it is always explained
     * (mainly, the information will help aid organizations identify them in order to give them aid)
     * Users also create a username and password for our service,
     * and all their information is stored in a file that can be read in at a later point
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.containerCreate);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsCreate);
        tabLayout.setupWithViewPager(mViewPager);

        pm = getPackageManager();

        userInfo = new UserInfo();
    }

    /**
     * If the user pressed back, make sure they want to abandon creating an account
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Exit Create Account?")
                .setNegativeButton("No", null).setPositiveButton("Yes", this).show();
    }

    /**
     * Listens to the result of the popup menu that appears when the user presses back
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onBackPressed();
    }

    /**
     * Based on a StackOverflow answer, this may be necessary to let Fragments listen to Activity Results
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * If account creation is abandoned, delete any photos taken by the user
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!createdFlag) {
            if (userInfo.profilePicPath != null) new File(userInfo.profilePicPath).delete();
            for(String path : userInfo.docs.values()) {
                new File(path).delete();
            }
        }
        userInfo = null;
    }

    /**
     * If the user scrolls while the keyboard is open, close keyboard to prevent weird layout formatting
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if(v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.clearFocus();
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ProfilePicFragment allows a user to upload or take a new profile picture, which is then saved
     */
    public static class ProfilePicFragment extends Fragment implements View.OnClickListener {

        ImageButton takePhoto, loadPhoto;
        ImageView proPic;
        View rootView;
        TextView picHint, picReason;
        int picW, picH;

        public ProfilePicFragment() {
        }

        public static ProfilePicFragment newInstance() {
            ProfilePicFragment fragment = new ProfilePicFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * If a photo was already taken, make sure it's displayed in this new Fragment instance
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_picture, container, false);

            takePhoto = (ImageButton) rootView.findViewById(R.id.buttonTakePropic);
            takePhoto.setOnClickListener(this);

            loadPhoto = (ImageButton) rootView.findViewById(R.id.buttonUploadPropic);
            loadPhoto.setOnClickListener(this);

            picHint = (TextView) rootView.findViewById(R.id.labelPicHint);
            picReason = (TextView) rootView.findViewById(R.id.labelPicReason);

            proPic = (ImageView) rootView.findViewById(R.id.imageProPic);
            if(userInfo.propic != null) {
                picHint.setText("");
                picReason.setText("");
                proPic.setImageBitmap(userInfo.propic);
            }

            return rootView;
        }

        static final int REQUEST_TAKE_PHOTO = 1, REQUEST_LOAD_PHOTO = 2;

        /**
         * If the Take Propic button was selected, the user is sent to the camera interface
         * If the Upload Propic button was selected, the user is sent to the file selection interface
         */
        @Override
        public void onClick(View v) {
            picW = rootView.getWidth();
            picH = rootView.findViewById(R.id.wrapperPropicFrame).getHeight();

            if (v.getId() == R.id.buttonTakePropic) {
                //take a new photo
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(pm) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            } else {
                //load an existing photo
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state) ||
                        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, REQUEST_LOAD_PHOTO);
                } else {
                    Toast.makeText(getContext(), "Cannot Load Images", Toast.LENGTH_LONG).show();
                }

            }
        }

        String _currentPhotoPath = null;

        /**
         * If a new profile picture is being taken, generate a unique file to store the picture in
         */
        private File createImageFile() throws IOException {
            // Create an image file name
            String fname = "profile_" + (new SimpleDateFormat("yy_MM_dd_hh_mm_ss").format(new Date()));
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File image = File.createTempFile(fname, ".jpeg", dir);

            // Save a file: path for use with ACTION_VIEW intents
            _currentPhotoPath = image.getAbsolutePath();
            return image;
        }

        /**
         * If an image has been uploaded, make a copy of it so that we can load it freely in this app
         */
        private String copyLoadedImage(Bitmap pic) {
            // Save a loaded image in app-specific memory, so it can be loaded whenever
            try {
                File image = createImageFile();

                FileOutputStream fos = new FileOutputStream(image);
                // Copy the loaded Bitmap into a .jpeg
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                // Make sure the system knows that the new file exists
                MediaScannerConnection.scanFile(getContext(), new String[]{image.toString()}, null, null);
                return image.getAbsolutePath();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to read uploaded profile picture"
                        ,Toast.LENGTH_LONG).show();
                return null;
            }
        }

        /**
         * If the user took a photo, display it.
         * If the user uploaded a photo, copy it and then display it
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                // If the picture was successfully taken, show it
                setPic();
            } else if (requestCode == REQUEST_LOAD_PHOTO && resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    _currentPhotoPath = copyLoadedImage(bitmap.copy(Bitmap.Config.ARGB_8888, false));
                    if (_currentPhotoPath != null) setPic();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Unable to read loaded image", Toast.LENGTH_LONG).show();
                    if (_currentPhotoPath != null) {
                        new File(_currentPhotoPath).delete();
                        _currentPhotoPath = null;
                    }
                }
            }
        }

        /**
         * Loads in an image from a specified path, and displays it in the Fragment.
         * Makes sure that the dimensions of the image fit on the screen by scaling it as necessary.
         * The first time the user provides a profile picture, give them instructions on how to advance
         */
        public void setPic() {
            if (userInfo.profilePicPath == null) {
                Toast.makeText(getContext(), "Swipe left to continue creating your account", Toast.LENGTH_LONG).show();
            }

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(_currentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            bmOptions.inJustDecodeBounds = false;
            Bitmap pic = BitmapFactory.decodeFile(_currentPhotoPath, bmOptions);
            Bitmap scaled;
            if(photoW > photoH) {
                scaled = Bitmap.createScaledBitmap(pic, picW, (int) (photoH * (((double) picW) / photoW)), false);
            } else {
                scaled = Bitmap.createScaledBitmap(pic, (int) (photoW * (((double) picH) / photoH)), picH, false);
            }

            String prev = null;

            if (userInfo.profilePicPath != null) {
                prev = userInfo.profilePicPath;
            }

            userInfo.profilePicPath = _currentPhotoPath;
            picHint.setText("");
            picReason.setText("");
            proPic.setImageBitmap(scaled);
            userInfo.propic = scaled.copy(Bitmap.Config.ARGB_8888, false);

            if(prev != null) new File(prev).delete();
        }
    }

    /**
     * BasicInfoFragment is where a user inputs all of their required personally identifying info,
     * as well as a username, password, and max travel distance for use in this app
     */
    public static class BasicInfoFragment extends Fragment implements View.OnClickListener,
            DatePickerDialog.OnDateSetListener, TextWatcher, View.OnFocusChangeListener,
            AdapterView.OnItemSelectedListener {

        EditText fieldBirthDate, name, username, password, distance;
        private int _day, _month, _year, focused = -1;
        ImageButton explainTravel;
        Spinner countrySpinner;

        public BasicInfoFragment() {
            GregorianCalendar gc = new GregorianCalendar();
            _year = gc.get(Calendar.YEAR);
            _month = gc.get(Calendar.MONTH);
            _day = gc.get(Calendar.DAY_OF_MONTH);
        }

        public static BasicInfoFragment newInstance() {
            BasicInfoFragment fragment = new BasicInfoFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * No need to re-propagate the user info in these fields:
         * from testing, this only happens when you scroll 3 fragments to either side,
         * which you can't do as this one's 1 away from the left and 2 away from the right
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_info, container, false);

            fieldBirthDate = (EditText) rootView.findViewById(R.id.fieldBirthDate);
            fieldBirthDate.setOnClickListener(this);
            fieldBirthDate.setOnFocusChangeListener(this);

            name = (EditText) rootView.findViewById(R.id.fieldName);
            name.addTextChangedListener(this);
            name.setOnFocusChangeListener(this);

            username = (EditText) rootView.findViewById(R.id.fieldUsername);
            username.addTextChangedListener(this);
            username.setOnFocusChangeListener(this);

            password = (EditText) rootView.findViewById(R.id.fieldPassword);
            password.addTextChangedListener(this);
            password.setOnFocusChangeListener(this);

            distance = (EditText) rootView.findViewById(R.id.fieldTravel);
            distance.addTextChangedListener(this);
            distance.setOnFocusChangeListener(this);

            explainTravel = (ImageButton) rootView.findViewById(R.id.buttonExplainTravel);
            explainTravel.setOnClickListener(this);

            countrySpinner = (Spinner) rootView.findViewById(R.id.fieldCountry);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.country_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            countrySpinner.setAdapter(adapter);
            countrySpinner.setOnFocusChangeListener(this);
            countrySpinner.setOnItemSelectedListener(this);

            rootView.setOnFocusChangeListener(this);

            return rootView;
        }

        /**
         * Listens to result of DatePicker used to set birth date
         */
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }

        /**
         * If the user selects the birthday, opens up a DatePickerDialog for them to choose the date with.
         * If the user selects the ? button next to Maximum Travel Distance,
         * a popup appears to explain the meaning and need for the field
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fieldBirthDate:
                    DatePickerDialog dialog = new DatePickerDialog(getContext(), this, _year, _month, _day);
                    dialog.show();
                    break;
                case R.id.buttonExplainTravel:
                    new AlertDialog.Builder(getContext()).setTitle("What does this mean?")
                            .setMessage(R.string.explain_travel).setNegativeButton("Okay", null).show();
            }
        }

        /**
         * Updates the date in the birth date EditText, unless an invalid (future) date was submitted.
         * In that case, an error message is given
         */
        private void updateDisplay() {
            Calendar now = Calendar.getInstance();
            Calendar then = Calendar.getInstance();
            then.set(_year, _month, _day);

            if(!now.after(then)) {
                userInfo.birthday = 0;
                userInfo.birthmonth = 0;
                userInfo.birthyear = 0;
                fieldBirthDate.setText("");
                Toast.makeText(getContext(), "Entered a future date", Toast.LENGTH_SHORT).show();
            } else {
                fieldBirthDate.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(_month + 1).append("/").append(_day).append("/").append(_year));
                userInfo.birthday = _day;
                userInfo.birthmonth = _month;
                userInfo.birthyear = _year;
            }
        }

        /**
         * Used to keep track of which EditText is being typed into, if anything is typed
         */
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int viewTag = -1;
            switch (v.getId()) {
                case R.id.fieldName:
                    viewTag = 0; break;
                case R.id.fieldUsername:
                    viewTag = 1; break;
                case R.id.fieldPassword:
                    viewTag = 2; break;
                case R.id.fieldBirthDate:
                    viewTag = 3; break;
                case R.id.fieldTravel:
                    viewTag = 4; break;
            }
            if (viewTag == focused && !hasFocus) focused = -1;
            else if (hasFocus) focused = viewTag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        /**
         * If something is typed, check what it was typed into and update the appropriate UserInfo field
         */
        @Override
        public void afterTextChanged(Editable s) {
            switch (focused) {
                case 0: userInfo.name = s.toString(); break;
                case 1: userInfo.username = s.toString(); break;
                case 2: userInfo.password = s.toString(); break;
                case 4:
                    if(s.toString().length() > 0) {
                        try {
                            userInfo.travelDistance = Double.parseDouble(s.toString());
                        } catch(Exception e) {
                            userInfo.travelDistance = 0.0;
                        }
                    } break;
            }
        }

        /**
         * Listens to the Country of Origin Spinner
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0: userInfo.origin = Country.Blank; break;
                case 1: userInfo.origin = Country.USA; break;
                case 2: userInfo.origin = Country.Syria; break;
                case 3: userInfo.origin = Country.Afghanistan; break;
                case 4: userInfo.origin = Country.Iraq; break;
                case 5: userInfo.origin = Country.Kosovo; break;
                case 6: userInfo.origin = Country.Albania; break;
                case 7: userInfo.origin = Country.Pakistan; break;
                case 8: userInfo.origin = Country.Eritrea; break;
                case 9: userInfo.origin = Country.Nigeria; break;
                case 10: userInfo.origin = Country.Iran; break;
                case 11: userInfo.origin = Country.Ukraine; break;
                case 12: userInfo.origin = Country.Other; break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}

    }

    /**
     * DocsFragment is for users to upload any additional documents they have,
     * specifically those that aid organizations might want to see before handing out aid
     */
    public static class DocsFragment extends Fragment implements View.OnClickListener {

        private final int TAKE_ID_FLAG = 1, TAKE_REG_FLAG = 2, TAKE_PASS_FLAG = 3,
                UPLOAD_ID_FLAG = 4, UPLOAD_REG_FLAG = 5, UPLOAD_PASS_FLAG = 6;
        ImageButton takeID, uploadID, delID, takeReg, uploadReg, delReg, takePass, uploadPass, delPass;
        CheckBox idCheck, regCheck, passCheck;

        public DocsFragment() {
        }

        public static DocsFragment newInstance() {
            DocsFragment fragment = new DocsFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Like BasicInfoFragment, no need ot re-propagate these fields ever
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_docs, container, false);

            takeID = (ImageButton) rootView.findViewById(R.id.buttonTakePhotoID);
            takeID.setOnClickListener(this);
            uploadID = (ImageButton) rootView.findViewById(R.id.buttonUploadPhotoID);
            uploadID.setOnClickListener(this);
            delID = (ImageButton) rootView.findViewById(R.id.buttonDeletePhotoID);
            delID.setOnClickListener(this);
            idCheck = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoID);
            if(!idCheck.isChecked()) delID.setClickable(false);

            takeReg = (ImageButton) rootView.findViewById(R.id.buttonTakeRegistration);
            takeReg.setOnClickListener(this);
            uploadReg = (ImageButton) rootView.findViewById(R.id.buttonUploadRegistration);
            uploadReg.setOnClickListener(this);
            delReg = (ImageButton) rootView.findViewById(R.id.buttonDeleteRegistration);
            delReg.setOnClickListener(this);
            regCheck = (CheckBox) rootView.findViewById(R.id.checkBoxRegistration);
            if(!regCheck.isChecked()) delReg.setClickable(false);

            takePass = (ImageButton) rootView.findViewById(R.id.buttonTakePassport);
            takePass.setOnClickListener(this);
            uploadPass = (ImageButton) rootView.findViewById(R.id.buttonUploadPassport);
            uploadPass.setOnClickListener(this);
            delPass = (ImageButton) rootView.findViewById(R.id.buttonDeletePassport);
            delPass.setOnClickListener(this);
            passCheck = (CheckBox) rootView.findViewById(R.id.checkBoxPassport);
            if(!passCheck.isChecked()) delPass.setClickable(false);

            return rootView;
        }

        /**
         * There are 3 buttons for each type of document:
         * 1) Take picture, which opens the camera interface
         * 2) Upload picture, which opens the file selection interface
         * 3) Delete picture, which deletes a previously submitted photo
         */
        @Override
        public void onClick(View v) {
            String prev;
            switch(v.getId()) {
                case R.id.buttonTakePhotoID: takePic(TAKE_ID_FLAG); break;
                case R.id.buttonTakeRegistration: takePic(TAKE_REG_FLAG); break;
                case R.id.buttonTakePassport: takePic(TAKE_PASS_FLAG); break;
                case R.id.buttonUploadPhotoID: uploadPic(UPLOAD_ID_FLAG); break;
                case R.id.buttonUploadRegistration: uploadPic(UPLOAD_REG_FLAG); break;
                case R.id.buttonUploadPassport: uploadPic(UPLOAD_PASS_FLAG); break;
                case R.id.buttonDeletePhotoID:
                    prev = userInfo.docs.remove("ID");
                    if (prev != null) new File(prev).delete();

                    idCheck.setChecked(false);
                    delID.setClickable(false);
                    break;
                case R.id.buttonDeleteRegistration:
                    prev = userInfo.docs.remove("Registration");
                    if (prev != null) new File(prev).delete();

                    regCheck.setChecked(false);
                    delReg.setClickable(false);
                    break;
                case R.id.buttonDeletePassport:
                    prev = userInfo.docs.remove("Passport");
                    if (prev != null) new File(prev).delete();

                    passCheck.setChecked(false);
                    delPass.setClickable(false);
                    break;
            }
        }

        /**
         * Sets up everything so that a user can take a photo of any of the 3 documents
         */
        private void takePic(int code) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(pm) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile(code);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, code);
                }
            }
        }

        /**
         * Sets up everything so that a user can upload a photo of any of the 3 documents
         */
        private void uploadPic(int code) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, code);
            } else {
                Toast.makeText(getContext(), "Cannot Load Images", Toast.LENGTH_LONG).show();
            }
        }

        String _currentPhotoPath = null;

        /**
         * Creates a new file for any picture that the user submits.
         * Makes note of what kind of document was submitted
         */
        private File createImageFile(int code) throws IOException {
            // Create an image file name
            String fname = "";
            switch (code) {
                case 1: fname += "id_"; break;
                case 2: fname += "reg_"; break;
                case 3: fname += "pass_"; break;
            }

            fname += (new SimpleDateFormat("yy_MM_dd_hh_mm_ss").format(new Date()));
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File image = File.createTempFile(fname, ".jpeg", dir);

            // Save a file: path for use with ACTION_VIEW intents
            _currentPhotoPath = image.getAbsolutePath();
            return image;
        }

        /**
         * Same setup as ProfilePicFragment.onActivityResult, but with 6 different results:
         * 3 documents (ID, registration, passport) * 2 intents (take, upload)
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                if (requestCode > 0 && requestCode < 4) {
                    savePic(requestCode);
                } else if (data != null && requestCode > 3 && requestCode < 7) {
                    try {
                        int code = requestCode - 3;
                        Uri imageUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        _currentPhotoPath = copyLoadedImage(bitmap.copy(Bitmap.Config.ARGB_8888, false), code);
                        if (_currentPhotoPath != null) savePic(code);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Unable to read loaded image", Toast.LENGTH_LONG).show();
                        if (_currentPhotoPath != null) {
                            new File(_currentPhotoPath).delete();
                            _currentPhotoPath = null;
                        }
                    }
                }
            }
        }

        /**
         * Adds a photo to the UserInfo, checks off that it has been included, and makes the doc deletable
         */
        private void savePic(int code) {
            if (code == TAKE_ID_FLAG) {
                String prev = userInfo.docs.put("ID", _currentPhotoPath);
                if (prev != null) new File(prev).delete();

                delID.setClickable(true);
                idCheck.setChecked(true);
            } else if (code == TAKE_REG_FLAG) {
                String prev = userInfo.docs.put("Registration", _currentPhotoPath);
                if (prev != null) new File(prev).delete();

                delReg.setClickable(true);
                regCheck.setChecked(true);
            } else if (code == TAKE_PASS_FLAG) {
                String prev = userInfo.docs.put("Passport", _currentPhotoPath);
                if (prev != null) new File(prev).delete();

                delPass.setClickable(true);
                passCheck.setChecked(true);
            }
            _currentPhotoPath = null;
        }

        /**
         * Saves a copy of an uploaded pic for local retrieval
         */
        private String copyLoadedImage(Bitmap pic, int code) {
            // Save a loaded image in app-specific memory, so it can be loaded whenever
            try {
                File image = createImageFile(code);

                FileOutputStream fos = new FileOutputStream(image);
                // Copy the loaded Bitmap into a .jpeg
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                // Make sure the system knows that the new file exists
                MediaScannerConnection.scanFile(getContext(), new String[]{image.toString()}, null, null);
                return image.getAbsolutePath();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to read uploaded picture",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        }

    }

    /**
     * CreateFragment just has a button for creating the account.
     * It exists to decrease clutter, rather than having the button at the bottom of another page
     */
    public static class CreateFragment extends Fragment implements View.OnClickListener {

        Button create;

        public CreateFragment() {
        }

        public static CreateFragment newInstance() {
            CreateFragment fragment = new CreateFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create, container, false);

            create = (Button) rootView.findViewById(R.id.buttonCreate);
            create.setOnClickListener(this);
            return rootView;
        }

        /**
         * If the user tries to make a account, make sure that:
         * 1) The username doesn't exist, and
         * 2) All required fields have been filled
         * If not, an error message is given.
         * If so, the user's info is stored into username.txt for later retrieval
         */
        @Override
        public void onClick(View v) {
            String ready = userInfo.readyToCreate();
            if(ready == null) {
                //save the user info to a file in internal storage
                File pro_file = new File(getContext().getFilesDir(), userInfo.username + ".txt");

                if (pro_file.exists()) {
                    Toast.makeText(getContext(), "Username already taken.", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pro_file);
                    fos.write(userInfo.fileStyle().getBytes());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Unable to store user information",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(getContext(), SearchAidActivity.class);

                SearchAidActivity.userInfo = new UserInfo(userInfo);
                intent.putExtra("FROM","Create");
                startActivity(intent);
            } else Toast.makeText(getContext(), ready, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    return ProfilePicFragment.newInstance();
                case 1:
                    return BasicInfoFragment.newInstance();
                case 2:
                    return DocsFragment.newInstance();
                case 3:
                    return CreateFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Picture";
                case 1:
                    return "Info";
                case 2:
                    return "Docs";
                case 3:
                    return "Create";
            }
            return null;
        }
    }
}