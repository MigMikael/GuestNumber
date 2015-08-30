package com.cpsudevelopers.mig.guestnumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private TextView mGuessNumberTextView;
    private TextView mResultTextView;
    private TextView mRule;
    private EditText mInput;
    private Button mGuessButton;

    private Game mGame;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGuessNumberTextView = (TextView) findViewById(R.id.guess_number_text_view);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mRule = (TextView) findViewById(R.id.rule_textView);
        mInput = (EditText) findViewById(R.id.input);
        mGuessButton = (Button) findViewById(R.id.guess_button);

        Intent i = getIntent();
        level = i.getIntExtra("level", 0);
        newGame(level);

        mGuessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInput.getText().length() == 0) {
                    Toast.makeText(GameActivity.this, "Please Input Number", Toast.LENGTH_LONG).show();
                    return;
                } else if (level == 0 && mInput.getText().length() > 1) {
                    Toast.makeText(GameActivity.this, "Please Input One digit Number", Toast.LENGTH_LONG).show();
                    return;
                } else if (level == 1 && mInput.getText().length() > 2) {
                    Toast.makeText(GameActivity.this, "Please Input Two digit Number", Toast.LENGTH_LONG).show();
                }

                int guessNumber = Integer.valueOf(mInput.getText().toString());
                mGuessNumberTextView.setText(String.valueOf(guessNumber));

                checkAnswer(guessNumber);
                mInput.setText("");
            }
        });
    }

    private void newGame(int level) {

        mGame = new Game(level);

        if (level == 0) {
            mRule.setText("Guess Number Between 0 - 9");
            mGuessNumberTextView.setText("_");
        } else {
            mRule.setText("Guess Number Between 0 - 99");
            mGuessNumberTextView.setText("_ _");
        }
        mResultTextView.setText("");
        mGuessNumberTextView.setBackgroundResource(R.color.incorrect_guess);
    }

    private void checkAnswer(int guessNumber) {
        Game.CompareResult result = mGame.submitGuess(guessNumber);

        if (result == Game.CompareResult.EQUAL) {

            MediaPlayer player = MediaPlayer.create(this, R.raw.applause);
            player.start();

            mResultTextView.setText("CORRECT");
            mGuessNumberTextView.setBackgroundResource(R.color.correct_guess);

            String str = String.format("You Guess %d", mGame.getmTotalGuess());

            new AlertDialog.Builder(this)
                    .setTitle("Summary")
                    .setMessage(str)
                    .setCancelable(false)
                    .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(GameActivity.this)
                                    .setTitle("Choose Level")
                                    .setItems(
                                            new String[]{"Easy", "Hard"},
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    level = which;
                                                    newGame(level);
                                                }
                                            }
                                    )
                                    .show();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();

        } else if (result == Game.CompareResult.TOO_SMALL) {
            mResultTextView.setText("INCORRECT TOO SMALL");
            mGuessNumberTextView.setBackgroundResource(R.color.incorrect_guess);
        } else if (result == Game.CompareResult.TOO_BIG) {
            mResultTextView.setText("INCORRECT TOO BIG");
            mGuessNumberTextView.setBackgroundResource(R.color.incorrect_guess);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
