package com.sfu.cmpt276.coopachievement;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sfu.cmpt276.coopachievement.model.GamePlayed;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestGamePlayed {
    private ArrayList<Integer> testArray;
    private GamePlayed testGame;
    private ArrayList<Integer> testBounds = new ArrayList<>();
    private final String[] testAchievements = {
            "Lowly Leech",
            "Horrendous Hagfish",
            "Bogus Blowfish",
            "Terrible Trolls",
            "Goofy Goblins",
            "Dastardly Dragons",
            "Awesome Alligators",
            "Epic Elephants",
            "Fabulous Fairies"
    };

    @Test
    public void testNumPlayers(){
        testGame = new GamePlayed();
        testGame.setNumPlayers(1);

        assertEquals(1,testGame.getNumPlayers());

        testGame.setNumPlayers(0);
        assertEquals(0,testGame.getNumPlayers());
    }

    @Test
    public void testInvalidNumPlayers(){
        testGame = new GamePlayed();
        assertThrows(IllegalArgumentException.class, ()->testGame.setNumPlayers(-1));
    }

    @Test
    public void testDifficulty(){
        testGame = new GamePlayed();

        testGame.setDifficulty(0);
        assertEquals("Easy", testGame.getDifficultyAsString());

        testGame.setDifficulty(1);
        assertEquals("Medium", testGame.getDifficultyAsString());

        testGame.setDifficulty(2);
        assertEquals("Hard", testGame.getDifficultyAsString());

    }
    @Test
    public void testDifficultyDefault(){
        testGame = new GamePlayed();

        testGame.setDifficulty(-1);
        assertEquals("Hard", testGame.getDifficultyAsString());

        testGame.setDifficulty(3);
        assertEquals("Hard", testGame.getDifficultyAsString());
    }

    @Test
    public void testTotalScore(){
        testArray = new ArrayList<>();
        testGame = new GamePlayed();
        testGame.setNumPlayers(10);
        for(int i = 0; i < 10; i++){
            testArray.add(i*10);
        }
        testGame.setTotalScore(testArray);
        assertEquals(450,testGame.getTotalScore());

        testArray.set(0,100);
        testGame.setTotalScore(testArray);
        assertEquals(550,testGame.getTotalScore());
    }

    @Test
    public void testInvalidTotalScore(){
        testArray = new ArrayList<>();
        testGame = new GamePlayed();
        testGame.setNumPlayers(10);
        for(int i = 0; i < 10; i++){
            testArray.add(i*10);
        }
        testArray.set(5,-1);
        testGame.setTotalScore(testArray);
        assertEquals(-1, testGame.getTotalScore());
    }

    @Test
    public void testSetAchievements(){
        setBounds();
        testArray = new ArrayList<>();
        testArray.add(40);
        testArray.add(80);

        testGame = new GamePlayed();
        testGame.setNumPlayers(2);
        testGame.setTotalScore(testArray);
        testGame.setAchievementLevel(testBounds, testAchievements);

        assertEquals(testAchievements[5], testGame.getAchievementName());
    }
    @Test
    public void testSetLowestAchievement(){
        setBounds();
        testArray = new ArrayList<>();
        testArray.add(0);
        testArray.add(0);

        testGame = new GamePlayed();
        testGame.setNumPlayers(2);
        testGame.setTotalScore(testArray);
        testGame.setAchievementLevel(testBounds, testAchievements);
        assertEquals(testAchievements[0], testGame.getAchievementName());
    }
    @Test
    public void testSetHighestAchievement(){
        setBounds();
        testArray = new ArrayList<>();
        testArray.add(100);
        testArray.add(300);

        testGame = new GamePlayed();
        testGame.setNumPlayers(2);
        testGame.setTotalScore(testArray);
        testGame.setAchievementLevel(testBounds,testAchievements);
        assertEquals(testAchievements[8],testGame.getAchievementName());
    }
    @Test
    public void testGamePlayedString(){
        setBounds();
        testArray = new ArrayList<>();
        testArray.add(30);
        testArray.add(50);
        testArray.add(70);

        testGame = new GamePlayed();
        testGame.setNumPlayers(3);
        testGame.setTotalScore(testArray);
        testGame.setAchievementLevel(testBounds, testAchievements);
        testGame.setDifficulty(1);

        String[] stringParam = testGame.getParamsArray();
        assertEquals("150", stringParam[0]);
        assertEquals("3", stringParam[1]);
        assertEquals("Medium", stringParam[2]);
        assertEquals(testAchievements[4], stringParam[3]);
    }

    private void setBounds(){
        testBounds.add(1);
        testBounds.add(13);
        testBounds.add(26);
        testBounds.add(39);
        testBounds.add(52);
        testBounds.add(64);
        testBounds.add(76);
        testBounds.add(88);
        testBounds.add(100);
    }

}