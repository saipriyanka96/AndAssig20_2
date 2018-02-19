package com.example.layout.assig20_2;
//Package objects contain version information about the implementation and specification of a Java package
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //public keyword is used in the declaration of a class,method or field;public classes,method and fields can be accessed by the members of any class.
//extends is for extending a class. implements is for implementing an interface
//AppCompatActivity is a class from e v7 appcompat library. This is a compatibility library that back ports some features of recent versions of
// Android to older devices.
    private final static int PERMISSION_ALL = 1;

    private Button insertContactBtn;
    private Button updateContactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Variables, methods, and constructors, which are declared protected in a superclass can be accessed only by the subclasses
        // in other package or any class within the package of the protected members class.
        //void is a Java keyword.  Used at method declaration and definition to specify that the method does not return any type,
        // the method returns void.
        //onCreate Called when the activity is first created. This is where you should do all of your normal static set up: create views,
        // bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
        // if there was one.Always followed by onStart().
        //Bundle is most often used for passing data through various Activities.
// This callback is called only when there is a saved instance previously saved using onSaveInstanceState().
// We restore some state in onCreate() while we can optionally restore other state here, possibly usable after onStart() has
// completed.The savedInstanceState Bundle is same as the one used in onCreate().
        // call the super class onCreate to complete the creation of activity like the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //R means Resource
        //layout means design
        //  main is the xml you have created under res->layout->main.xml
        //  Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        // The other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        // the design
        ///findViewById:A user interface element that displays text to the user.

        insertContactBtn = (Button) findViewById(R.id.insert_contact);
        updateContactBtn = (Button) findViewById(R.id.update_contact);

        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            //Requests permissions to be granted to this application.
            /**Parameters
             activity	Activity: The target activity.
             permissions	String: The requested permissions. Must me non-null and not empty.
             requestCode	int: Application specific request code to match with a result reported to onRequestPermissionsResult(int, String[], int[]). Should be >= 0.**/

        }else{
            initialize();
        }
    }
    public void initialize(){
        insertContactBtn.setOnClickListener(new View.OnClickListener() {
            //here we insert the contacts on click of the button
            /**Register a callback to be invoked when this view is clicked. If this view is not clickable, it becomes clickable.

             Parameters
             l	View.OnClickListener: The callback that will run
             This value may be null.
             onClick-Called when a view has been clicked.

             Parameters
             v	View: The view that was clicked.**/
            @Override
            public void onClick(View view) {
                insertContact();//method for insert
            }
        });
        updateContactBtn.setOnClickListener(new View.OnClickListener() {
            //here we update the contacts on click of the button
            @Override
            public void onClick(View view) {
                updateContact();//method for update
            }
        });
    }

    private void updateContact() {
//Creates a builder for an alert dialog that uses the default alert dialog theme.
        //context:the parent
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        //Instantiates a layout XML file into its corresponding View objects.
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.contact_main, null);

        final EditText name = view.findViewById(R.id.input_contact_name);
        final EditText phone = view.findViewById(R.id.input_contact_phone);

        builder.setView(view).setTitle(R.string.update_title).setPositiveButton(R.string.save_contact, new DialogInterface.OnClickListener() {
            //Sets a custom view to be the contents of the alert dialog.
            //set the title and set positive button to save the contact
            //Interface used to allow the creator of a dialog to run some code when an item on the dialog is clicked
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String contactName = name.getText().toString();
                        String contactPhone = phone.getText().toString();

                        int saveStatus = 0;
                        //at the time of update
                        try {
                            saveStatus = ContactsHelper.updateContact(getContentResolver(), contactName, contactPhone);
                            //contacthelper will help us to update the contact
                            //getContentResolver-Return a ContentResolver instance for your application's package.

                        } catch (RemoteException e) {
                            //Parent exception for all Binder remote-invocation errors
                            e.printStackTrace();

                        } catch (OperationApplicationException e) {
                            //Thrown when an application of a ContentProviderOperation fails due the specified constraints.
                            //It prints a stack trace for this Throwable object on the error output stream that is the value of the field System.err.
                            e.printStackTrace();
                        }

                        if (saveStatus == 1) {
                            //if status is equal to 1 then update the contact\
                            /**Toast id like pop up message
                             * Make a standard toast that just contains a text view with the text from a resource.

                             Parameters
                             context	Context: The context to use. Usually your Application or Activity object.
                             resId	int: The resource id of the string resource to use. Can be formatted text.
                             duration	int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG**/
                            Toast.makeText(getApplicationContext(), "Updated successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to update.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_contact, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        //creates the dialog and show the dialog

        dialog.show();
    }

    private void insertContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.contact_main, null);

        final EditText name = view.findViewById(R.id.input_contact_name);
        final EditText phone = view.findViewById(R.id.input_contact_phone);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save_contact, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String contactName = name.getText().toString();
                        String contactPhone = phone.getText().toString();

                        boolean saveStatus = ContactsHelper.insertContact(getContentResolver(), contactName, contactPhone);
//here we insert the contacts with contacthelper
                        if (saveStatus) {
                            Toast.makeText(getApplicationContext(), "Saved successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to save.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_contact, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Callback for the result from requesting permissions.
        /**Parameters
         requestCode	int: The request code passed in requestPermissions(android.app.Activity, String[], int)
         permissions	String: The requested permissions. Never null.
         grantResults	int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
         **/
        switch (requestCode) {
            case PERMISSION_ALL : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted,  Do the
                    // contacts-related task you need to do.
                    // Since reading contacts takes more time, let's run it on a separate thread.
                    initialize();
                } else {

                    // permission denied,  Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "You've denied the required permission.", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        //here we are saying it has permission to add the contacts based on version
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                //once string permission is given then it need to check the permission and then access need to be granted
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}