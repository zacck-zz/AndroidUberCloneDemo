
package com.parse.moober;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {

  Switch mUserSwitch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    mUserSwitch = (Switch)findViewById(R.id.typeSwitch);
    //check if user is logged in if not lets create a new User
    if(ParseUser.getCurrentUser() == null)
    {
      ParseAnonymousUtils.logIn(new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(e == null)
          {
            Log.d(getPackageName(), "Anon user Logged in");
          }
          else
          {
            Log.d(getPackageName(), "Anon User login error "+e.getMessage());
          }
        }
      });
    }
    //check if user is rider or driver
    else
    {
      if(ParseUser.getCurrentUser().get("riderOrDriver") != null)
      {
        Log.i(getPackageName(), "Redirect User");
      }
    }

  }
  public  void getStarted(View view)
  {
    String riderDriver = "rider";
    if(mUserSwitch.isChecked())
    {
      //when the switch is on the user is a driver
      riderDriver = "driver";
    }

    //add a field to distinguish user from rider
    ParseUser.getCurrentUser().put("riderOrDriver", riderDriver);
    final String finalRiderDriver = riderDriver;
    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null)
        {
          Log.i(getPackageName(), finalRiderDriver+" signedup");
        }
      }
    });





  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
