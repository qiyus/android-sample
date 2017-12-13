package com.vigor.apitester;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.PopupMenu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button popupbtn = (Button) findViewById(R.id.button);
        popupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(MainActivity.this, popupbtn);
                menu.getMenuInflater().inflate(R.menu.menu_popupmenu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this, R.string.popupmenu_message ,Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
                menu.getMenu().add(Menu.NONE, Menu.FIRST + 6, 6, R.string.popupmenu_message);
                menu.show();
            }
        });
    }
}
