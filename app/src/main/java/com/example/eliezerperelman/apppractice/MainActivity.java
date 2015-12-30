package com.example.eliezerperelman.apppractice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.concurrent.Semaphore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    // This buffer is accessed by both threads and is shared memory between the two threads
    ManageBuffer buffer;
    Semaphore fillcount = new Semaphore(0); // The Items produced in the buffer
    Semaphore emptyCount = new Semaphore(50); // The remaining space contained the buffer, by default start at 50

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        // Initialize the buffer to size 50. The
        buffer = new ManageBuffer(50);

        Consumer cons = new Consumer();

        Producer prod = new Producer();
        prod.execute();
        cons.execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

    // This is the code for the producer.

    private class Producer extends AsyncTask<Void,Integer,Integer>
    {
        Producer(){}
        @Override
        protected Integer doInBackground(Void... params)
        {

                // This should loop out when there are no more available spaces in the buffer
                for(int i = 0; i < 20; i++)// While there is space in the buffer
                {
                    System.out.println("The producer is running");

                    if(i == 10)
                    {
                        try
                        {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                    // Call the semaphore

                    /*
                    try {
                         // Since you are the producer you first call the aquire(down),to request an entry in the buffer,
                        This buffer is a limited resource.

                        emptyCount.acquire();
                        buffer.add(); // Add an item to the buffer
                        fillcount.release(); // Call the semaphore release, that indicates that the count in the buffer is incremented
                        publishProgress(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    */
                }
                return 5;
        }

        protected void onPostExecute(Integer result)
        {
            // Since this is called after we have completed the thread execution, we will

            TextView tv  = (TextView)findViewById(R.id.counter);
            tv.setText("Count is complete"  + result.toString());
        }

        protected void onProgressUpdate(Integer... progress)
        {
            TextView tv = (TextView)findViewById(R.id.counter);
            tv.setText(progress[0] + "Complete!");
        }
        
    }



    private class Consumer extends AsyncTask<Void,Integer,Integer>
    {

        Consumer() {}

        // This is where all the actaul thread work is done
        @Override
        protected Integer doInBackground(Void... params)
        {
            for(int i = 0; i < 20; i++)
            {
                System.out.println("The consumer thread is running");
               /* try
                {
                    fillcount.acquire(); // Consumer may potentially wait until buffer contains an entry,
                    buffer.remove(); // Remove an entry
                    emptyCount.release(); // Release the empty count, that will potentially unblock the producer
                    publishProgress(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                */
                //item = removeItemFromBuffer();
                //up(emptyCount);
                //consumeItem(item);
            }
            return 5;
        }
        // This will run when ever the publishProgress is called from the doInBackGround method
        protected void onProgressUpdate(Integer... progress)
        {
            TextView tv = (TextView)findViewById(R.id.counter);
            tv.setText(progress[0] + "Complete!");
        }
        // This method will run once the background thread will finish running
        protected void onPostExecute(Integer result)
        {
            // Always cast to the proper widget

            TextView tv  = (TextView)findViewById(R.id.counter);
            tv.setText("Count is complete"  + result.toString());
        }

    }

    // This class will manage a fixed size buffer
    private class ManageBuffer
    {
        int size; // The max size of the buffer
        int current_length; // The the current lenght of the buffer
        ArrayList <Integer> buffer;
        // Constructer to create buffer
        public ManageBuffer(int setSize)
        {
            size = setSize; // Fix the size of the buffer
            buffer = new ArrayList<Integer>(); // Create the array list
        }

        // This will add to the buffer
        public void add()
        {
            buffer.add(buffer.size()); // Add a new entry to the buffer.
        }

        // This will add a new entry to the buffer
        public void remove()
        {
            buffer.remove(buffer.size() - 1); // remove the last item in in the buffer. LIFO
        }

        /*This will determine if the the consumer has any avaiable entries in the buffer to consume
        / Return values: True - its not empty, False - empty
        */
        public boolean consumeBuffer()
        {
           return buffer.size() > 0;
        }
        /* This will determince if the producer can produce any more files*/
        public boolean produceBuffer()
        {
            return buffer.size() < size;
        }


    }


}
