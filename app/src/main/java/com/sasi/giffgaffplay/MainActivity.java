package com.sasi.giffgaffplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sasi.giffgaffplay.ggblogs.BlogsActivity;
import com.sasi.giffgaffplay.ggblogs.HTMLTextActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Toast.makeText(this, "Size: " + getData().size(), Toast.LENGTH_LONG).show();

        adapter = new MainAdapter(this, getData());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
                        break;

                    case 1:
                        startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
                        break;

                    case 2:
                        startActivity(new Intent(MainActivity.this, TabbedActivity.class));
                        break;

                    case 3:
                        startActivity(new Intent(MainActivity.this, TabWithImages.class));
                        break;

                    case 4:
                        startActivity(new Intent(MainActivity.this, TabWithImages2.class));
                        break;

                    case 5:
                        startActivity(new Intent(MainActivity.this, NavigationTabActivity.class));
                        break;

                    case 6:
                        startActivity(new Intent(MainActivity.this, TestCarrier.class));
                        break;

                    case 7:
                        startActivity(new Intent(MainActivity.this, TextViewAnimator.class));
                        break;

                    case 8:
                        startActivity(new Intent(MainActivity.this, ToggleButtonCustom.class));
                        break;

                    case 9:
                        startActivity(new Intent(MainActivity.this, KeystoreActivity.class));
                        break;

                    case 10:
                        startActivity(new Intent(MainActivity.this, HTMLTextActivity.class));
                        break;

                    case 11:
                        startActivity(new Intent(MainActivity.this, BlogsActivity.class));
                        break;

                    default:
                        break;
                }
            }
        }));
    }

    private ArrayList<RowItems> getData() {
        ArrayList<RowItems> itemsList = new ArrayList<RowItems>();

        RowItems row1 = new RowItems();
        row1.setTitle("Material Story board");
        itemsList.add(row1);

        RowItems row2 = new RowItems();
        row2.setTitle("Navigation Drawer");
        itemsList.add(row2);

        RowItems row3 = new RowItems();
        row3.setTitle("Tabbed - Pager");
        itemsList.add(row3);

        RowItems row4 = new RowItems();
        row4.setTitle("Tab with Images");
        itemsList.add(row4);

        RowItems row5 = new RowItems();
        row5.setTitle("App bar + Tab with Images");
        itemsList.add(row5);

        RowItems row6 = new RowItems();
        row6.setTitle("Navigation + Tabs");
        itemsList.add(row6);

        RowItems row7 = new RowItems();
        row7.setTitle("Telephony / Carrier info");
        itemsList.add(row7);

        RowItems row8 = new RowItems();
        row8.setTitle("TextView Increment animate");
        itemsList.add(row8);

        RowItems row9 = new RowItems();
        row9.setTitle("Toggle Button custom");
        itemsList.add(row9);

        RowItems row10 = new RowItems();
        row10.setTitle("Keystore");
        itemsList.add(row10);

        RowItems row11 = new RowItems();
        row11.setTitle("HTML WebView");
        itemsList.add(row11);

        RowItems row12 = new RowItems();
        row12.setTitle("gg blogs");
        itemsList.add(row12);

        RowItems row13 = new RowItems();
        row13.setTitle("Next row13");
        itemsList.add(row13);

        return itemsList;
    }
}
