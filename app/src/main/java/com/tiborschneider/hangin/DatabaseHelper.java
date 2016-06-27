package com.tiborschneider.hangin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Tibor Schneider on 20.06.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private GamePanel gamePanel;

    public static final String DATABASE_NAME     = "game.db";
    public static final int DATABASE_VERSION = 5;

    public static final String TABLE_SCENE_NAME  = "db_scene";
    public static final String TABLE_SCENE_COL_ID = "id";
    public static final String TABLE_SCENE_COL_INDEX = "scene_index";
    public static final String TABLE_SCENE_COL_NAME = "name";

    public static final String TABLE_TILE_NAME   = "db_tile";
    public static final String TABLE_TILE_COL_ID  = "id";
    public static final String TABLE_TILE_COL_INDEX_SCENE  = "index_scene";
    public static final String TABLE_TILE_COL_TYPE  = "type";
    public static final String TABLE_TILE_COL_X  = "x";
    public static final String TABLE_TILE_COL_Y  = "y";
    public static final String TABLE_TILE_COL_INTERACTIVE = "interaction";

    public static final String TABLE_JUMP_NAME = "db_jump";
    public static final String TABLE_JUMP_COL_ID = "id";
    public static final String TABLE_JUMP_COL_SCENE1 = "fs_scene1";
    public static final String TABLE_JUMP_COL_X1 = "x1";
    public static final String TABLE_JUMP_COL_Y1 = "y1";
    public static final String TABLE_JUMP_COL_SCENE2 = "fs_scene2";
    public static final String TABLE_JUMP_COL_X2 = "x2";
    public static final String TABLE_JUMP_COL_Y2 = "y2";

    public static final String TABLE_DIALOGUE_NAME = "db_dialogue";
    public static final String TABLE_DIALOGUE_COL_ID = "id";
    public static final String TABLE_DIALOGUE_COL_NAME = "name";
    public static final String TABLE_DIALOGUE_COL_POSITION = "position";

    public static final String TABLE_DIAL_TEXT_NAME = "db_dialogue_text";
    public static final String TABLE_DIAL_TEXT_COL_ID = "id";
    public static final String TABLE_DIAL_TEXT_COL_DIALOGUE = "fs_dialogue_name";
    public static final String TABLE_DIAL_TEXT_COL_TEXT1 = "text1";
    public static final String TABLE_DIAL_TEXT_COL_TEXT2 = "text2";
    public static final String TABLE_DIAL_TEXT_COL_ORDER = "order_number";

    public static final String TABLE_DIAL_REPLY_NAME = "db_dialogue_reply";
    public static final String TABLE_DIAL_REPLY_COL_ID = "id";
    public static final String TABLE_DIAL_REPLY_COL_DIALOGUE = "fs_dialogue_name";
    public static final String TABLE_DIAL_REPLY_COL_TEXT = "text";
    public static final String TABLE_DIAL_REPLY_COL_NEXT_DIALOGUE = "fs_next_dialogue_name";
    public static final String TABLE_DIAL_REPLY_COL_ACTION = "action";
    public static final String TABLE_DIAL_REPLY_COL_SET_STATE = "set_state";
    public static final String TABLE_DIAL_REPLY_COL_SET_VALUE = "set_value";

    public static final String TABLE_LOOTBOX_NAME = "db_lootbox";
    public static final String TABLE_LOOTBOX_COL_ID = "id";
    public static final String TABLE_LOOTBOX_COL_SCENE = "fs_scene";
    public static final String TABLE_LOOTBOX_COL_X = "x";
    public static final String TABLE_LOOTBOX_COL_Y = "y";
    public static final String TABLE_LOOTBOX_COL_ITEM1 = "item1";
    public static final String TABLE_LOOTBOX_COL_ITEM2 = "item2";
    public static final String TABLE_LOOTBOX_COL_ITEM3 = "item3";
    public static final String TABLE_LOOTBOX_COL_ITEM4 = "item4";
    public static final String TABLE_LOOTBOX_COL_ITEM5 = "item5";

    public static final String TABLE_STATE_NAME = "db_state";
    public static final String TABLE_STATE_COL_ID = "id";
    public static final String TABLE_STATE_COL_NAME = "name";
    public static final String TABLE_STATE_COL_VALUE = "value";

    public static final String TABLE_NPC_NAME = "db_npc";
    public static final String TABLE_NPC_COL_ID = "id";
    public static final String TABLE_NPC_COL_NAME = "name";
    public static final String TABLE_NPC_COL_IMAGE_NAME = "image_base_name";
    public static final String TABLE_NPC_COL_SCENE_INDEX = "fs_scene_index";
    public static final String TABLE_NPC_COL_INIT_X = "init_x";
    public static final String TABLE_NPC_COL_INIT_Y = "init_y";
    public static final String TABLE_NPC_COL_INIT_DIRECTION = "init_direction";
    public static final String TABLE_NPC_COL_SPEED = "speed";

    public static final String TABLE_NPC_DIALOGUE_NAME = "db_npc_dialogue";
    public static final String TABLE_NPC_DIALOGUE_COL_ID = "id";
    public static final String TABLE_NPC_DIALOGUE_COL_NPC_NAME = "npc_name";
    public static final String TABLE_NPC_DIALOGUE_COL_DIALOGUE_NAME = "dialogue_name";
    public static final String TABLE_NPC_DIALOGUE_COL_COND1_NAME = "condition1_name";
    public static final String TABLE_NPC_DIALOGUE_COL_COND1_VALUE = "condition1_value";
    public static final String TABLE_NPC_DIALOGUE_COL_COND2_NAME = "condition2_name";
    public static final String TABLE_NPC_DIALOGUE_COL_COND2_VALUE = "condition2_value";
    public static final String TABLE_NPC_DIALOGUE_COL_COND3_NAME = "condition3_name";
    public static final String TABLE_NPC_DIALOGUE_COL_COND3_VALUE = "condition3_value";

    public static final String TABLE_PLAYER_SAVE_NAME = "db_player_save";
    public static final String TABLE_PLAYER_SAVE_COL_ID = "id";
    public static final String TABLE_PLAYER_SAVE_COL_SCENE = "fs_scene_index";
    public static final String TABLE_PLAYER_SAVE_COL_X = "coord_x";
    public static final String TABLE_PLAYER_SAVE_COL_Y = "coord_y";
    public static final String TABLE_PLAYER_SAVE_COL_DIRECTION = "direction";

    public static final String TABLE_INVENTORY_NAME = "db_inventory";
    public static final String TABLE_INVENTORY_COL_ID = "id";
    public static final String TABLE_INVENTORY_COL_ITEM = "item_name";
    public static final String TABLE_INVENTORY_COL_NUM = "quantity";

    public DatabaseHelper(Context aContext, GamePanel aGamePanel)
    {
        super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        context = aContext;
        gamePanel = aGamePanel;
        //just used for checking
        //SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override public void onCreate(SQLiteDatabase db)
    {
        System.out.println("Create all Tables");
        String sqlQuery = "CREATE TABLE " + TABLE_SCENE_NAME + " ( " +
                TABLE_SCENE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_SCENE_COL_NAME + " STRING, " +
                TABLE_SCENE_COL_INDEX + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_TILE_NAME + " ( " +
                TABLE_TILE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_TILE_COL_INDEX_SCENE + " INTEGER, " +
                TABLE_TILE_COL_TYPE + " STRING, " +
                TABLE_TILE_COL_X + " INTEGER, " +
                TABLE_TILE_COL_Y + " INTEGER, " +
                TABLE_TILE_COL_INTERACTIVE + " STRING);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_JUMP_NAME + " ( " +
                TABLE_JUMP_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_JUMP_COL_SCENE1 + " INTEGER, " +
                TABLE_JUMP_COL_X1 + " INTEGER, " +
                TABLE_JUMP_COL_Y1 + " INTEGER, " +
                TABLE_JUMP_COL_SCENE2 + " INTEGER, " +
                TABLE_JUMP_COL_X2 + " INTEGER, " +
                TABLE_JUMP_COL_Y2 + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_DIALOGUE_NAME + " ( " +
                TABLE_DIALOGUE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_DIALOGUE_COL_NAME + " STRING, " +
                TABLE_DIALOGUE_COL_POSITION + " STRING);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_DIAL_TEXT_NAME + " ( " +
                TABLE_DIAL_TEXT_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_DIAL_TEXT_COL_DIALOGUE + " STRING, " +
                TABLE_DIAL_TEXT_COL_TEXT1 + " STRING, " +
                TABLE_DIAL_TEXT_COL_TEXT2 + " STRING, " +
                TABLE_DIAL_TEXT_COL_ORDER + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_DIAL_REPLY_NAME + " ( " +
                TABLE_DIAL_REPLY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_DIAL_REPLY_COL_DIALOGUE + " STRING, " +
                TABLE_DIAL_REPLY_COL_TEXT + " STRING, " +
                TABLE_DIAL_REPLY_COL_NEXT_DIALOGUE + " STRING, " +
                TABLE_DIAL_REPLY_COL_ACTION + " STRING, " +
                TABLE_DIAL_REPLY_COL_SET_STATE + " STRING, " +
                TABLE_DIAL_REPLY_COL_SET_VALUE + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_LOOTBOX_NAME + " ( " +
                TABLE_LOOTBOX_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_LOOTBOX_COL_SCENE + " INTEGER, " +
                TABLE_LOOTBOX_COL_X + " INTEGER, " +
                TABLE_LOOTBOX_COL_Y + " INTEGER, " +
                TABLE_LOOTBOX_COL_ITEM1 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM2 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM3 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM4 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM5 + " STRING);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_STATE_NAME + " ( " +
                TABLE_STATE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_STATE_COL_NAME + " STRING, " +
                TABLE_STATE_COL_VALUE + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_NPC_NAME + " ( " +
                TABLE_NPC_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_NPC_COL_NAME + " STRING, " +
                TABLE_NPC_COL_IMAGE_NAME + " STRING, " +
                TABLE_NPC_COL_SCENE_INDEX + " INTEGER, " +
                TABLE_NPC_COL_INIT_X + " INTEGER, " +
                TABLE_NPC_COL_INIT_Y + " INTEGER, " +
                TABLE_NPC_COL_INIT_DIRECTION + " STRING, " +
                TABLE_NPC_COL_SPEED + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_NPC_DIALOGUE_NAME + " ( " +
                TABLE_NPC_DIALOGUE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_NPC_DIALOGUE_COL_NPC_NAME + " STRING, " +
                TABLE_NPC_DIALOGUE_COL_DIALOGUE_NAME + " STRING, " +
                TABLE_NPC_DIALOGUE_COL_COND1_NAME + " STRING, " +
                TABLE_NPC_DIALOGUE_COL_COND1_VALUE + " INTEGER, " +
                TABLE_NPC_DIALOGUE_COL_COND2_NAME + " STRING, " +
                TABLE_NPC_DIALOGUE_COL_COND2_VALUE + " INTEGER, " +
                TABLE_NPC_DIALOGUE_COL_COND3_NAME + " STRING, " +
                TABLE_NPC_DIALOGUE_COL_COND3_VALUE + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PLAYER_SAVE_NAME + " ( " +
                TABLE_PLAYER_SAVE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_PLAYER_SAVE_COL_SCENE + " INTEGER, " +
                TABLE_PLAYER_SAVE_COL_X + " INTEGER, " +
                TABLE_PLAYER_SAVE_COL_Y + " INTEGER, " +
                TABLE_PLAYER_SAVE_COL_DIRECTION + " STRING);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_INVENTORY_NAME + " ( " +
                TABLE_INVENTORY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_INVENTORY_COL_ITEM + " INTEGER, " +
                TABLE_INVENTORY_COL_NUM + " STRING);";
        db.execSQL(sqlQuery);


        System.out.println("Initialize all Tables with data from CSV files.");
        readScenesFromCSV(db);
        readTilesFromCSV(db);
        readJumpsFromCSV(db);
        readDialogueFromCSV(db);
        readLootboxesFromCSV(db);
        readStateFromCSV(db);
        readNpcDialogueFromCSV(db);
        readNpcFromCSV(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        System.out.println("Delete all Tables");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCENE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TILE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUMP_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOGUE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIAL_TEXT_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIAL_REPLY_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOTBOX_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NPC_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NPC_DIALOGUE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_SAVE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_NAME);
        onCreate(db);
    }

    public void readScenesFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_SCENE_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.scenes);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> sceneList = csv.read();

        ContentValues contentValues;

        for (String[] row : sceneList)
        {
            contentValues = new ContentValues();
            contentValues.put(TABLE_SCENE_COL_INDEX, Integer.parseInt(row[0]));
            contentValues.put(TABLE_SCENE_COL_NAME, row[1]);
            db.insert(TABLE_SCENE_NAME, null, contentValues);
        }
    }

    public void readTilesFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_TILE_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.tiles);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> tileList = csv.read();

        ContentValues contentValues;

        for (String[] row : tileList)
        {
            contentValues = new ContentValues();
            contentValues.put(TABLE_TILE_COL_INDEX_SCENE, Integer.parseInt(row[0]));
            contentValues.put(TABLE_TILE_COL_TYPE, row[1]);
            contentValues.put(TABLE_TILE_COL_X, Integer.parseInt(row[2]));
            contentValues.put(TABLE_TILE_COL_Y, Integer.parseInt(row[3]));
            String interaction;
            if (row[4] == "null") {
                interaction = "";
            } else {
                interaction = row[4];
            }
            contentValues.put(TABLE_TILE_COL_INTERACTIVE, interaction);
            db.insert(TABLE_TILE_NAME, null, contentValues);
        }
    }

    public void readJumpsFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_JUMP_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.jumps);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> jumpList = csv.read();

        ContentValues contentValues;

        for (String[] row : jumpList)
        {
            //add 2 Entries for each jump!
            contentValues = new ContentValues();
            contentValues.put(TABLE_JUMP_COL_SCENE1, Integer.parseInt(row[0]));
            contentValues.put(TABLE_JUMP_COL_X1, Integer.parseInt(row[1]));
            contentValues.put(TABLE_JUMP_COL_Y1, Integer.parseInt(row[2]));
            contentValues.put(TABLE_JUMP_COL_SCENE2, Integer.parseInt(row[3]));
            contentValues.put(TABLE_JUMP_COL_X2, Integer.parseInt(row[4]));
            contentValues.put(TABLE_JUMP_COL_Y2, Integer.parseInt(row[5]));
            db.insert(TABLE_JUMP_NAME, null, contentValues);

            contentValues = new ContentValues();
            contentValues.put(TABLE_JUMP_COL_SCENE1, Integer.parseInt(row[3]));
            contentValues.put(TABLE_JUMP_COL_X1, Integer.parseInt(row[4]));
            contentValues.put(TABLE_JUMP_COL_Y1, Integer.parseInt(row[5]));
            contentValues.put(TABLE_JUMP_COL_SCENE2, Integer.parseInt(row[0]));
            contentValues.put(TABLE_JUMP_COL_X2, Integer.parseInt(row[1]));
            contentValues.put(TABLE_JUMP_COL_Y2, Integer.parseInt(row[2]));
            db.insert(TABLE_JUMP_NAME, null, contentValues);
        }
    }

    public void readDialogueFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_DIALOGUE_NAME);
        db.execSQL("DELETE FROM " + TABLE_DIAL_REPLY_NAME);
        db.execSQL("DELETE FROM " + TABLE_DIAL_TEXT_NAME);

        ContentValues contentValues;

        InputStream inputStream = context.getResources().openRawResource(R.raw.dialogue);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> DialogueList = csv.read();

        for (String[] row : DialogueList) {
            contentValues = new ContentValues();
            contentValues.put(TABLE_DIALOGUE_COL_NAME, row[0]);
            contentValues.put(TABLE_DIALOGUE_COL_POSITION, row[1]);
            db.insert(TABLE_DIALOGUE_NAME, null, contentValues);
        }

        inputStream = context.getResources().openRawResource(R.raw.dialogue_text);
        csv = new CSVReader(inputStream);
        DialogueList = csv.read();

        for (String[] row : DialogueList) {
            contentValues = new ContentValues();
            contentValues.put(TABLE_DIAL_TEXT_COL_DIALOGUE, row[0]);
            contentValues.put(TABLE_DIAL_TEXT_COL_TEXT1, row[1]);
            contentValues.put(TABLE_DIAL_TEXT_COL_TEXT2, row[2]);
            contentValues.put(TABLE_DIAL_TEXT_COL_ORDER, Integer.parseInt(row[3]));
            db.insert(TABLE_DIAL_TEXT_NAME, null, contentValues);
        }

        inputStream = context.getResources().openRawResource(R.raw.dialogue_reply);
        csv = new CSVReader(inputStream);
        DialogueList = csv.read();

        for (String[] row : DialogueList) {
            contentValues = new ContentValues();
            contentValues.put(TABLE_DIAL_REPLY_COL_DIALOGUE, row[0]);
            contentValues.put(TABLE_DIAL_REPLY_COL_TEXT, row[1]);
            contentValues.put(TABLE_DIAL_REPLY_COL_NEXT_DIALOGUE, row[2]);
            contentValues.put(TABLE_DIAL_REPLY_COL_ACTION, row[3]);
            contentValues.put(TABLE_DIAL_REPLY_COL_SET_STATE, row[4]);
            contentValues.put(TABLE_DIAL_REPLY_COL_SET_VALUE, Integer.parseInt(row[5]));
            db.insert(TABLE_DIAL_REPLY_NAME, null, contentValues);
        }
    }

    public void readLootboxesFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_LOOTBOX_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.lootboxes);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> lootboxList = csv.read();

        ContentValues contentValues;

        for (String[] row : lootboxList)
        {
            contentValues = new ContentValues();
            contentValues.put(TABLE_LOOTBOX_COL_SCENE, Integer.parseInt(row[0]));
            contentValues.put(TABLE_LOOTBOX_COL_X, Integer.parseInt(row[1]));
            contentValues.put(TABLE_LOOTBOX_COL_Y, Integer.parseInt(row[2]));
            String item = "";
            if (!row[3].equals("null")) item = row[3];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM1, item);
            item = "";
            if (!row[4].equals("null")) item = row[4];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM2, item);
            item = "";
            if (!row[5].equals("null")) item = row[5];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM3, item);
            item = "";
            if (!row[6].equals("null")) item = row[6];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM4, item);
            item = "";
            if (!row[7].equals("null")) item = row[7];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM5, item);
            db.insert(TABLE_LOOTBOX_NAME, null, contentValues);
        }
    }

    private void readStateFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_STATE_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.state);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> stateList = csv.read();

        ContentValues contentValues;

        for (String[] row : stateList)
        {
            contentValues = new ContentValues();
            contentValues.put(TABLE_STATE_COL_NAME, row[0]);
            contentValues.put(TABLE_STATE_COL_VALUE, Integer.parseInt(row[1]));
            db.insert(TABLE_STATE_NAME, null, contentValues);
        }
    }

    private void readNpcDialogueFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_NPC_DIALOGUE_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.npc_dialogue);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> npcDialogueList = csv.read();

        ContentValues contentValues;

        for (String[] row : npcDialogueList)
        {
            contentValues = new ContentValues();
            contentValues.put(TABLE_NPC_DIALOGUE_COL_NPC_NAME, row[0]);
            contentValues.put(TABLE_NPC_DIALOGUE_COL_DIALOGUE_NAME, row[1]);
            contentValues.put(TABLE_NPC_DIALOGUE_COL_COND1_NAME, row[2]);
            contentValues.put(TABLE_NPC_DIALOGUE_COL_COND1_VALUE, getIntFromString(row[3]));
            contentValues.put(TABLE_NPC_DIALOGUE_COL_COND2_NAME, row[4]);
            contentValues.put(TABLE_NPC_DIALOGUE_COL_COND2_VALUE, getIntFromString(row[5]));
            contentValues.put(TABLE_NPC_DIALOGUE_COL_COND3_NAME, row[6]);
            contentValues.put(TABLE_NPC_DIALOGUE_COL_COND3_VALUE, getIntFromString(row[7]));
            db.insert(TABLE_NPC_DIALOGUE_NAME, null, contentValues);
        }
    }

    private void readNpcFromCSV(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_NPC_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.npc);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> npcList = csv.read();

        ContentValues contentValues;

        for (String[] row : npcList)
        {
            contentValues = new ContentValues();
            contentValues.put(TABLE_NPC_COL_NAME, row[0]);
            contentValues.put(TABLE_NPC_COL_IMAGE_NAME, row[1]);
            contentValues.put(TABLE_NPC_COL_SCENE_INDEX, Integer.parseInt(row[2]));
            contentValues.put(TABLE_NPC_COL_INIT_X, Integer.parseInt(row[3]));
            contentValues.put(TABLE_NPC_COL_INIT_Y, Integer.parseInt(row[4]));
            contentValues.put(TABLE_NPC_COL_INIT_DIRECTION, row[5]);
            contentValues.put(TABLE_NPC_COL_SPEED, Integer.parseInt(row[6]));
            db.insert(TABLE_NPC_NAME, null, contentValues);
        }
    }

    public GameScene[] getGameScenes()
    {
        Cursor allScenes = getAllScenesCursor();
        Cursor allTiles;
        Cursor allJumps;
        Cursor allLootboxes;
        GameScene[] gameScene = new GameScene[GamePanel.numScenes];
        if (allScenes.getCount() != 0)
        {
            while (allScenes.moveToNext())
            {
                int sceneIndex = allScenes.getInt(2);
                gameScene[sceneIndex] = new GameScene(gamePanel, context);

                //get Tiles
                allTiles = getAllTilesOfSceneCursor(sceneIndex);
                if (allTiles.getCount() != 0) {
                    while (allTiles.moveToNext()) {
                        boolean isInteractive = (!allTiles.getString(5).equals("null"));
                        gameScene[sceneIndex].createNewTile(allTiles.getInt(3), allTiles.getInt(4), TileType.valueOf(allTiles.getString(2)), isInteractive);
                        if (isInteractive) {
                            gameScene[sceneIndex].addDialogueToTile(allTiles.getInt(3), allTiles.getInt(4), getDialogueFromDB(allTiles.getString(5)));

                        }
                    }
                }
                else
                    System.out.println("Error while getting Tiles from db from Scene " + sceneIndex);

                //get Jumps
                allJumps = getAllJumpsOfSceneCursor(sceneIndex);
                if (allJumps.getCount() != 0)
                    while (allJumps.moveToNext())
                        gameScene[sceneIndex].addNewJump(allJumps.getInt(4), allJumps.getInt(2), allJumps.getInt(3), allJumps.getInt(5), allJumps.getInt(6));
                else
                    System.out.println("Error while getting Jumps from DB from Scene " + sceneIndex);

                //get Lootboxes
                allLootboxes = getLootboxCursor(sceneIndex);
                if (allLootboxes.getCount() != 0) {
                    while (allLootboxes.moveToNext()) {
                        Lootbox lootbox = new Lootbox(context, allLootboxes.getInt(2), allLootboxes.getInt(3));
                        for (int i = 4; i < 9; i++)
                            if (!allLootboxes.getString(i).equals(""))
                                lootbox.addItem(ItemType.valueOf(allLootboxes.getString(i)));
                        gameScene[sceneIndex].addLootbox(lootbox);
                    }
                }

            }
        } else {
            System.out.println("Error while getting Scenes from db");
        }
        return gameScene;
    }

    public void getAllNpc()
    {
        Cursor cursorNpc = getNpcCursor();
        if (cursorNpc.getCount() > 0) {
            while (cursorNpc.moveToNext()) {
                gamePanel.newNpc(cursorNpc.getString(1), cursorNpc.getString(2), Direction.valueOf(cursorNpc.getString(6)), cursorNpc.getInt(4), cursorNpc.getInt(5), cursorNpc.getInt(3), cursorNpc.getInt(7));
            }
        }
    }

    public Dialogue getDialogueFromDB(String aDialogue)
    {
        Cursor cursorDialogue = getDialogueCursor(aDialogue);
        if (cursorDialogue.getCount() == 1) {
            cursorDialogue.moveToFirst();
            Dialogue dialogue = new Dialogue(gamePanel, DialoguePosition.valueOf(cursorDialogue.getString(2)));

            Cursor cursorDialogueText = getDialogueTextCursor(aDialogue);
            if (cursorDialogueText.getCount() != 0) {
                //add Dialogue Text
                while (cursorDialogueText.moveToNext())
                    dialogue.addTextElement(cursorDialogueText.getString(2), cursorDialogueText.getString(3));

                //add Dialogue Replies
                Cursor cursorDialogueReply = getDialogueReplyCursor(aDialogue);
                if (cursorDialogueReply.getCount() != 0) {
                    while (cursorDialogueReply.moveToNext()) {
                        Dialogue followingDialogue = getDialogueFromDB(cursorDialogueReply.getString(3));
                        dialogue.addReply(cursorDialogueReply.getString(2), followingDialogue, cursorDialogueReply.getString(4), cursorDialogueReply.getString(5), cursorDialogueReply.getInt(6));
                    }
                }
                return dialogue;
            }
        } else if (cursorDialogue.getCount() > 1) {
            System.out.println("FATAL ERROR: multiple Dialogues with the same name");
        }
        System.out.println("FATAL ERROR: Could not create Dialogue");
        return null;
    }

    public GameState getState(String aState)
    {
        if (aState == null || aState == "")
            return new GameState("NULL", 0);
        Cursor cursorState = getStateCursor(aState);
        if (cursorState.getCount() == 1)
        {
            cursorState.moveToFirst();
            return new GameState(cursorState.getString(1), cursorState.getInt(2));
        }
        return new GameState("NULL", 0);
    }

    public void setState(GameState state)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String updateQuery = "UPDATE " + TABLE_STATE_NAME + " SET " + TABLE_STATE_COL_VALUE + " = " + state.value + " WHERE " + TABLE_STATE_COL_NAME + " = '" + state.name + "';";
        db.execSQL(updateQuery);
    }

    public String getNpcDialogueName(String aNpc)
    {
        Cursor cursor = getNpcDialogueCursor(aNpc);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                if (getState(cursor.getString(3)).value == cursor.getInt(4) && getState(cursor.getString(5)).value == cursor.getInt(6) && getState(cursor.getString(7)).value == cursor.getInt(8))
                    return cursor.getString(2);
            }
        }
        return "npcDoesNotInteract";
    }

    public void initSavedPlayer()
    {
        Player player = gamePanel.getPlayer();
        Cursor playerSave = getPlayerSaveCursor();
        if (playerSave.getCount() == 1) {
            System.out.println("Reload Player State.");
            playerSave.moveToFirst();
            player.teleport(playerSave.getInt(2), playerSave.getInt(3), Direction.valueOf(playerSave.getString(4)));
            gamePanel.setCurrentScene(playerSave.getInt(1));
            initInventory();
        }
    }

    public void savePlayerStatus()
    {
        Player player = gamePanel.getPlayer();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PLAYER_SAVE_NAME);
        String insertSaveQuery = "INSERT INTO " + TABLE_PLAYER_SAVE_NAME + " (" +
                TABLE_PLAYER_SAVE_COL_SCENE + ", " +
                TABLE_PLAYER_SAVE_COL_X + ", " +
                TABLE_PLAYER_SAVE_COL_Y + ", " +
                TABLE_PLAYER_SAVE_COL_DIRECTION + ") VALUES (" +
                gamePanel.getCurrentScene() + ", " +
                player.getX() + ", " +
                player.getY() + ", '" +
                player.getDirection().name() + "');";
        db.execSQL(insertSaveQuery);
    }

    public void initInventory()
    {
        System.out.println("Reload Inventory");
        Inventory inventory = gamePanel.getPlayer().getInventory();
        Cursor savedInventory = getInventoryCursor();
        if (savedInventory.getCount() > 0) {
            while (savedInventory.moveToNext()) {
                for (int i = 0; i < savedInventory.getInt(2); i++) {
                    inventory.addItem(new Item(context, ItemType.valueOf(savedInventory.getString(1))));
                }
            }
        }
    }

    public void deleteInventoryItem(String aItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteItemQuery = "DELETE FROM " + TABLE_INVENTORY_NAME + " WHERE " + TABLE_INVENTORY_COL_ITEM + " = '" + aItem + "';";
        db.execSQL(deleteItemQuery);
    }

    public void setInventoryItemCount(String aItem, int aCount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateItemQuery = "UPDATE " + TABLE_INVENTORY_NAME + " SET " + TABLE_INVENTORY_COL_NUM + " = " + aCount + " WHERE " + TABLE_INVENTORY_COL_ITEM + " = '" + aItem + "';";
        db.rawQuery(updateItemQuery, null);
    }

    public void addNewItemToInventory(String aItem, int aCount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertItemQuery = "INSERT INTO " + TABLE_INVENTORY_NAME + " (" + TABLE_INVENTORY_COL_ITEM + ", " + TABLE_INVENTORY_COL_NUM + ") VALUES ('" + aItem + "', " + aCount + ");";
        db.execSQL(insertItemQuery);
    }

    public void deleteLootbox(int aX, int aY)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteLootboxQuery = "DELETE FROM " + TABLE_LOOTBOX_NAME + " WHERE ( " +
                TABLE_LOOTBOX_COL_SCENE + " = " + gamePanel.getCurrentScene() + " AND " +
                TABLE_LOOTBOX_COL_X + " = " + aX + " AND " +
                TABLE_LOOTBOX_COL_Y + " = " + aY + " );";
        db.execSQL(deleteLootboxQuery);
    }

    //Cursors:

    private Cursor getAllScenesCursor()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectScenesQuery = "SELECT * FROM " + TABLE_SCENE_NAME + ";";
        Cursor result = db.rawQuery(selectScenesQuery, null);
        return result;
    }

    private Cursor getAllTilesOfSceneCursor(int aSceneIndex)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectScenesQuery = "SELECT * FROM " + TABLE_TILE_NAME + " WHERE " + TABLE_TILE_COL_INDEX_SCENE + " = " + Integer.toString(aSceneIndex) + ";";
        Cursor result = db.rawQuery(selectScenesQuery, null);
        return result;
    }

    private Cursor getAllJumpsOfSceneCursor(int aSceneIndex)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectScenesQuery = "SELECT * FROM " + TABLE_JUMP_NAME + " WHERE " + TABLE_JUMP_COL_SCENE1 + " = " + Integer.toString(aSceneIndex) + ";";
        Cursor result = db.rawQuery(selectScenesQuery, null);
        return result;
    }

    private Cursor getDialogueCursor(String aDialogue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectDialogueQuery = "SELECT * FROM " + TABLE_DIALOGUE_NAME + " WHERE (" + TABLE_DIALOGUE_COL_NAME + " = '" + aDialogue + "');";
        Cursor result = db.rawQuery(selectDialogueQuery, null);
        return result;
    }

    private Cursor getDialogueTextCursor(String aDialogue)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectDialogueTextQuery = "SELECT * FROM " + TABLE_DIAL_TEXT_NAME + " WHERE " + TABLE_DIAL_TEXT_COL_DIALOGUE + " = '" + aDialogue +  "' ORDER BY " + TABLE_DIAL_TEXT_COL_ORDER + " ASC;";
        Cursor result = db.rawQuery(selectDialogueTextQuery, null);
        return result;
    }

    private Cursor getDialogueReplyCursor(String aDialogue)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectDialogueReplyQuery = "SELECT * FROM " + TABLE_DIAL_REPLY_NAME + " WHERE " + TABLE_DIAL_REPLY_COL_DIALOGUE + " = '" + aDialogue + "';";
        return db.rawQuery(selectDialogueReplyQuery, null);
    }

    private Cursor getLootboxCursor(int aScene)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectLootboxQuery = "SELECT * FROM " + TABLE_LOOTBOX_NAME + " WHERE " + TABLE_LOOTBOX_COL_SCENE + " = " + aScene + ";";
        return db.rawQuery(selectLootboxQuery, null);
    }

    private Cursor getStateCursor(String aState)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectStateQuery = "SELECT * FROM " + TABLE_STATE_NAME + " WHERE " + TABLE_STATE_COL_NAME + " = '" + aState + "';";
        return db.rawQuery(selectStateQuery, null);
    }

    private Cursor getNpcDialogueCursor(String aNpc)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectNpcDialogueQuery = "SELECT * FROM " + TABLE_NPC_DIALOGUE_NAME + " WHERE " + TABLE_NPC_DIALOGUE_COL_NPC_NAME + " = '" + aNpc + "';";
        return db.rawQuery(selectNpcDialogueQuery, null);
    }

    private Cursor getNpcCursor()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectNpcQuery = "SELECT * FROM " + TABLE_NPC_NAME + ";";
        return db.rawQuery(selectNpcQuery, null);
    }

    private Cursor getPlayerSaveCursor()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectPlayerSaveQuery = "SELECT * FROM " + TABLE_PLAYER_SAVE_NAME +";";
        return db.rawQuery(selectPlayerSaveQuery, null);
    }

    private Cursor getInventoryCursor()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectInventoryQuery = "SELECT * FROM " + TABLE_INVENTORY_NAME + ";";
        return db.rawQuery(selectInventoryQuery, null);
    }

    private int getIntFromString(String aString)
    {
        if (aString == null || aString == "")
            return 0;
        else
            return Integer.parseInt(aString);
    }

}
