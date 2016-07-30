package com.tiborschneider.hangin.mainGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.character.Direction;
import com.tiborschneider.hangin.character.Inventory;
import com.tiborschneider.hangin.character.MoveCommand;
import com.tiborschneider.hangin.character.NonPlayerCharacter;
import com.tiborschneider.hangin.character.Player;
import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.dialogue.DialoguePosition;
import com.tiborschneider.hangin.item.Item;
import com.tiborschneider.hangin.item.ItemType;
import com.tiborschneider.hangin.item.Lootbox;
import com.tiborschneider.hangin.item.SpecialItem;
import com.tiborschneider.hangin.scene.GameScene;
import com.tiborschneider.hangin.state.Quest;
import com.tiborschneider.hangin.state.QuestEntry;
import com.tiborschneider.hangin.state.QuestHandler;
import com.tiborschneider.hangin.state.StateHandler;
import com.tiborschneider.hangin.tile.TileForegroundType;
import com.tiborschneider.hangin.tile.TileType;
import com.tiborschneider.hangin.state.GameState;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Tibor Schneider on 20.06.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private GamePanel gamePanel;

    public static final String DATABASE_NAME     = "game.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCENE_NAME  = "db_scene";
    public static final String TABLE_SCENE_COL_ID = "id";
    public static final String TABLE_SCENE_COL_INDEX = "scene_index";
    public static final String TABLE_SCENE_COL_NAME = "name";

    public static final String TABLE_TILE_NAME   = "db_tile";
    public static final String TABLE_TILE_COL_ID  = "id";
    public static final String TABLE_TILE_COL_INDEX_SCENE  = "index_scene";
    public static final String TABLE_TILE_COL_TYPE  = "type";
    public static final String TABLE_TILE_COL_FOREGROUND  = "foreground";
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
    public static final String TABLE_DIAL_TEXT_COL_ACTION = "action";
    public static final String TABLE_DIAL_TEXT_COL_SET_VALUE = "set_value";
    public static final String TABLE_DIAL_TEXT_COL_SET_STATE = "set_state";

    public static final String TABLE_DIAL_REPLY_NAME = "db_dialogue_reply";
    public static final String TABLE_DIAL_REPLY_COL_ID = "id";
    public static final String TABLE_DIAL_REPLY_COL_DIALOGUE = "fs_dialogue_name";
    public static final String TABLE_DIAL_REPLY_COL_TEXT = "text";
    public static final String TABLE_DIAL_REPLY_COL_NEXT_DIALOGUE = "fs_next_dialogue_name";
    public static final String TABLE_DIAL_REPLY_COL_ACTION = "action";
    public static final String TABLE_DIAL_REPLY_COL_SET_STATE = "set_state";
    public static final String TABLE_DIAL_REPLY_COL_SET_VALUE = "set_value";
    public static final String TABLE_DIAL_REPLY_COL_CONDITION_STATE = "condition_state";
    public static final String TABLE_DIAL_REPLY_COL_CONDITION_VALUE = "condition_value";

    public static final String TABLE_LOOTBOX_NAME = "db_lootbox";
    public static final String TABLE_LOOTBOX_COL_ID = "id";
    public static final String TABLE_LOOTBOX_COL_SCENE = "fs_scene";
    public static final String TABLE_LOOTBOX_COL_X = "x";
    public static final String TABLE_LOOTBOX_COL_Y = "y";
    public static final String TABLE_LOOTBOX_COL_VISIBLE = "visible";
    public static final String TABLE_LOOTBOX_COL_ITEM1 = "item1";
    public static final String TABLE_LOOTBOX_COL_ITEM2 = "item2";
    public static final String TABLE_LOOTBOX_COL_ITEM3 = "item3";
    public static final String TABLE_LOOTBOX_COL_ITEM4 = "item4";
    public static final String TABLE_LOOTBOX_COL_ITEM5 = "item5";

    public static final String TABLE_STATE_NAME = "db_state";
    public static final String TABLE_STATE_COL_ID = "id";
    public static final String TABLE_STATE_COL_NAME = "name";
    public static final String TABLE_STATE_COL_VALUE = "value";
    public static final String TABLE_STATE_COL_SPECIAL = "special_state";

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
    public static final String TABLE_PLAYER_SAVE_COL_MUNCHIES = "munchies_meter";
    public static final String TABLE_PLAYER_SAVE_COL_STONED = "stoned_meter";

    public static final String TABLE_INVENTORY_NAME = "db_inventory";
    public static final String TABLE_INVENTORY_COL_ID = "id";
    public static final String TABLE_INVENTORY_COL_ITEM = "item_name";
    public static final String TABLE_INVENTORY_COL_NUM = "quantity";
    public static final String TABLE_INVENTORY_COL_USES = "special_item_uses";

    public static final String TABLE_NPC_MOVE_NAME = "db_npc_move";
    public static final String TABLE_NPC_MOVE_COL_ID = "id";
    public static final String TABLE_NPC_MOVE_COL_NPC_NAME = "fs_npc_name";
    public static final String TABLE_NPC_MOVE_COL_ON_SCENE = "on_scene";
    public static final String TABLE_NPC_MOVE_COL_ON_X = "on_x";
    public static final String TABLE_NPC_MOVE_COL_ON_Y = "on_y";
    public static final String TABLE_NPC_MOVE_COL_START_DIRECTION = "direction";
    public static final String TABLE_NPC_MOVE_COL_CONDITION_STATE = "condition_state";
    public static final String TABLE_NPC_MOVE_COL_CONDITION_VALUE = "condition_value";
    public static final String TABLE_NPC_MOVE_COL_COMMAND = "command";

    public static final String TABLE_DIALOGUE_SAVE_NAME = "db_dialogue_save";
    public static final String TABLE_DIALOGUE_SAVE_COL_ID = "id";
    public static final String TABLE_DIALOGUE_SAVE_COL_DIALOGUE_NAME = "fs_dialogue_name";
    public static final String TABLE_DIALOGUE_SAVE_COL_CURRENT_TEXT = "current_text";

    public static final String TABLE_QUEST_NAME = "db_quest";
    public static final String TABLE_QUEST_COL_ID = "id";
    public static final String TABLE_QUEST_COL_NAME = "name";
    public static final String TABLE_QUEST_COL_FINAL_CONDITION_STATE = "final_condition_state";
    public static final String TABLE_QUEST_COL_FINAL_CONDITION_VALUE = "final_condition_value";

    public static final String TABLE_QUEST_ENTRY_NAME = "db_quest_entry";
    public static final String TABLE_QUEST_ENTRY_COL_ID = "id";
    public static final String TABLE_QUEST_ENTRY_COL_QUEST_NAME = "fs_quest_name";
    public static final String TABLE_QUEST_ENTRY_COL_TEXT = "text";
    public static final String TABLE_QUEST_ENTRY_COL_ORDER = "text_order";
    public static final String TABLE_QUEST_ENTRY_COL_CONDITION_STATE_1 = "condition_state1";
    public static final String TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_1 = "condition_value1";
    public static final String TABLE_QUEST_ENTRY_COL_CONDITION_STATE_2 = "condition_state2";
    public static final String TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_2 = "condition_value2";
    public static final String TABLE_QUEST_ENTRY_COL_CONDITION_STATE_3 = "condition_state3";
    public static final String TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_3 = "condition_value3";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NPC_MOVE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOGUE_SAVE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST_ENTRY_NAME);

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
                TABLE_TILE_COL_FOREGROUND + " STRING, " +
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
                TABLE_DIAL_TEXT_COL_ORDER + " INTEGER, " +
                TABLE_DIAL_TEXT_COL_ACTION + " STRING, " +
                TABLE_DIAL_TEXT_COL_SET_STATE + " STRING, " +
                TABLE_DIAL_TEXT_COL_SET_VALUE + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_DIAL_REPLY_NAME + " ( " +
                TABLE_DIAL_REPLY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_DIAL_REPLY_COL_DIALOGUE + " STRING, " +
                TABLE_DIAL_REPLY_COL_TEXT + " STRING, " +
                TABLE_DIAL_REPLY_COL_NEXT_DIALOGUE + " STRING, " +
                TABLE_DIAL_REPLY_COL_ACTION + " STRING, " +
                TABLE_DIAL_REPLY_COL_SET_STATE + " STRING, " +
                TABLE_DIAL_REPLY_COL_SET_VALUE + " INTEGER, " +
                TABLE_DIAL_REPLY_COL_CONDITION_STATE + " STRING, " +
                TABLE_DIAL_REPLY_COL_CONDITION_VALUE + " INTEGER);";
        db.execSQL(sqlQuery);

        System.out.println("create Database");

        sqlQuery = "CREATE TABLE " + TABLE_LOOTBOX_NAME + " ( " +
                TABLE_LOOTBOX_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_LOOTBOX_COL_SCENE + " INTEGER, " +
                TABLE_LOOTBOX_COL_X + " INTEGER, " +
                TABLE_LOOTBOX_COL_Y + " INTEGER, " +
                TABLE_LOOTBOX_COL_VISIBLE + " INTEGER," +
                TABLE_LOOTBOX_COL_ITEM1 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM2 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM3 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM4 + " STRING, " +
                TABLE_LOOTBOX_COL_ITEM5 + " STRING);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_STATE_NAME + " ( " +
                TABLE_STATE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_STATE_COL_NAME + " STRING, " +
                TABLE_STATE_COL_VALUE + " INTEGER, " +
                TABLE_STATE_COL_SPECIAL + " INTEGER);";

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
                TABLE_PLAYER_SAVE_COL_DIRECTION + " STRING, " +
                TABLE_PLAYER_SAVE_COL_MUNCHIES + " INTEGER, " +
                TABLE_PLAYER_SAVE_COL_STONED + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_INVENTORY_NAME + " ( " +
                TABLE_INVENTORY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_INVENTORY_COL_ITEM + " STRING, " +
                TABLE_INVENTORY_COL_NUM + " INTEGER, " +
                TABLE_INVENTORY_COL_USES + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_NPC_MOVE_NAME + " ( " +
                TABLE_NPC_MOVE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_NPC_MOVE_COL_NPC_NAME + " STRING, " +
                TABLE_NPC_MOVE_COL_ON_SCENE + " INTEGER, " +
                TABLE_NPC_MOVE_COL_ON_X + " INTEGER, " +
                TABLE_NPC_MOVE_COL_ON_Y + " INTEGER, " +
                TABLE_NPC_MOVE_COL_START_DIRECTION + " STRING, " +
                TABLE_NPC_MOVE_COL_CONDITION_STATE + " STRING, " +
                TABLE_NPC_MOVE_COL_CONDITION_VALUE + " INTEGER, " +
                TABLE_NPC_MOVE_COL_COMMAND + " STRING);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_DIALOGUE_SAVE_NAME + " ( " +
                TABLE_DIALOGUE_SAVE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_DIALOGUE_SAVE_COL_DIALOGUE_NAME + " String, " +
                TABLE_DIALOGUE_SAVE_COL_CURRENT_TEXT + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_QUEST_NAME + " ( " +
                TABLE_QUEST_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_QUEST_COL_NAME + " STRING, " +
                TABLE_QUEST_COL_FINAL_CONDITION_STATE + " STRING, " +
                TABLE_QUEST_COL_FINAL_CONDITION_VALUE + " INTEGER);";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_QUEST_ENTRY_NAME + " ( " +
                TABLE_QUEST_ENTRY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_QUEST_ENTRY_COL_QUEST_NAME + " STRING, " +
                TABLE_QUEST_ENTRY_COL_TEXT + " STRING, " +
                TABLE_QUEST_ENTRY_COL_ORDER + " INTEGER, " +
                TABLE_QUEST_ENTRY_COL_CONDITION_STATE_1 + " STRING, " +
                TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_1 + " INTEGER, " +
                TABLE_QUEST_ENTRY_COL_CONDITION_STATE_2 + " STRING, " +
                TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_2 + " INTEGER, " +
                TABLE_QUEST_ENTRY_COL_CONDITION_STATE_3 + " STRING, " +
                TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_3 + " INTEGER);";
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
        readNpcMoveFromCSV(db);
        readQuestsFromCSV(db);
    }


    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
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
            contentValues.put(TABLE_TILE_COL_FOREGROUND, row[2]);
            contentValues.put(TABLE_TILE_COL_X, Integer.parseInt(row[3]));
            contentValues.put(TABLE_TILE_COL_Y, Integer.parseInt(row[4]));
            String interaction;
            if (row[5] == "null") {
                interaction = "";
            } else {
                interaction = row[5];
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
            contentValues.put(TABLE_DIAL_TEXT_COL_ACTION, row[4]);
            contentValues.put(TABLE_DIAL_TEXT_COL_SET_STATE, row[5]);
            contentValues.put(TABLE_DIAL_TEXT_COL_SET_VALUE, Integer.parseInt(row[6]));
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
            contentValues.put(TABLE_DIAL_REPLY_COL_CONDITION_STATE, row[6]);
            contentValues.put(TABLE_DIAL_REPLY_COL_CONDITION_VALUE, Integer.parseInt(row[7]));
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

            int visible = 0;
            if (row[3].equals("TRUE")) visible = 1;
            contentValues.put(TABLE_LOOTBOX_COL_VISIBLE, visible);

            String item = "";
            if (!row[4].equals("null")) item = row[4];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM1, item);
            item = "";
            if (!row[5].equals("null")) item = row[5];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM2, item);
            item = "";
            if (!row[6].equals("null")) item = row[6];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM3, item);
            item = "";
            if (!row[7].equals("null")) item = row[7];
            contentValues.put(TABLE_LOOTBOX_COL_ITEM4, item);
            item = "";
            if (!row[8].equals("null")) item = row[8];
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
            int isSpecial = 0;
            if (StateHandler.isSpecialState(row[0]))
                isSpecial = 1;
            contentValues.put(TABLE_STATE_COL_SPECIAL, isSpecial);
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


    private void readNpcMoveFromCSV(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NPC_MOVE_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.npc_command);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> npcMoveList = csv.read();

        ContentValues contentValues;

        for (String[] row : npcMoveList) {
            contentValues = new ContentValues();
            contentValues.put(TABLE_NPC_MOVE_COL_NPC_NAME, row[0]);
            contentValues.put(TABLE_NPC_MOVE_COL_ON_SCENE, Integer.parseInt(row[1]));
            contentValues.put(TABLE_NPC_MOVE_COL_ON_X, Integer.parseInt(row[2]));
            contentValues.put(TABLE_NPC_MOVE_COL_ON_Y, Integer.parseInt(row[3]));
            contentValues.put(TABLE_NPC_MOVE_COL_START_DIRECTION, row[4]);
            contentValues.put(TABLE_NPC_MOVE_COL_CONDITION_STATE, row[5]);
            contentValues.put(TABLE_NPC_MOVE_COL_CONDITION_VALUE, Integer.parseInt(row[6]));
            contentValues.put(TABLE_NPC_MOVE_COL_COMMAND, row[7]);
            db.insert(TABLE_NPC_MOVE_NAME, null, contentValues);
        }
    }


    public void readQuestsFromCSV(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_QUEST_NAME);
        db.execSQL("DELETE FROM " + TABLE_QUEST_ENTRY_NAME);

        InputStream inputStream = context.getResources().openRawResource(R.raw.quests);
        CSVReader csv = new CSVReader(inputStream);
        List<String[]> questList = csv.read();

        ContentValues contentValues;

        for (String[] row : questList) {
            System.out.println("Insert Data");
            contentValues = new ContentValues();
            contentValues.put(TABLE_QUEST_COL_NAME, row[0]);
            contentValues.put(TABLE_QUEST_COL_FINAL_CONDITION_STATE, row[1]);
            contentValues.put(TABLE_QUEST_COL_FINAL_CONDITION_VALUE, Integer.parseInt(row[2]));
            db.insert(TABLE_QUEST_NAME, null, contentValues);

            //add special State for Quest
            addState(db, Quest.getQuestStateName(row[0]), 0);
            addState(db, Quest.getLastShownStateName(row[0]), 0);
        }

        inputStream = context.getResources().openRawResource(R.raw.quest_entries);
        csv = new CSVReader(inputStream);
        List<String[]> questEntryList = csv.read();

        for (String[] row : questEntryList) {
            System.out.println("insert Data 2");
            contentValues = new ContentValues();
            contentValues.put(TABLE_QUEST_ENTRY_COL_QUEST_NAME, row[0]);
            contentValues.put(TABLE_QUEST_ENTRY_COL_TEXT, row[1]);
            contentValues.put(TABLE_QUEST_ENTRY_COL_ORDER, Integer.parseInt(row[2]));
            contentValues.put(TABLE_QUEST_ENTRY_COL_CONDITION_STATE_1, row[3]);
            contentValues.put(TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_1, Integer.parseInt(row[4]));
            contentValues.put(TABLE_QUEST_ENTRY_COL_CONDITION_STATE_2, row[5]);
            contentValues.put(TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_2, Integer.parseInt(row[6]));
            contentValues.put(TABLE_QUEST_ENTRY_COL_CONDITION_STATE_3, row[7]);
            contentValues.put(TABLE_QUEST_ENTRY_COL_CONDITION_VALUE_3, Integer.parseInt(row[8]));
            db.insert(TABLE_QUEST_ENTRY_NAME, null, contentValues);
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
                        boolean isInteractive = (!allTiles.getString(6).equals("null"));
                        gameScene[sceneIndex].createNewTile(allTiles.getInt(4), allTiles.getInt(5), TileType.valueOf(allTiles.getString(2)), TileForegroundType.NULL.valueOf(allTiles.getString(3)), isInteractive);
                        if (isInteractive) {
                            gameScene[sceneIndex].addDialogueToTile(allTiles.getInt(4), allTiles.getInt(5), allTiles.getString(6));

                        }
                    }
                }
                else
                    System.out.println("Error while getting Tiles from db from Scene " + sceneIndex);

                allTiles.close();

                //get Jumps
                allJumps = getAllJumpsOfSceneCursor(sceneIndex);
                if (allJumps.getCount() != 0)
                    while (allJumps.moveToNext())
                        gameScene[sceneIndex].addNewJump(allJumps.getInt(4), allJumps.getInt(2), allJumps.getInt(3), allJumps.getInt(5), allJumps.getInt(6));
                else
                    System.out.println("Error while getting Jumps from DB from Scene " + sceneIndex);

                allJumps.close();

                //get Lootboxes
                allLootboxes = getLootboxCursor(sceneIndex);
                if (allLootboxes.getCount() != 0) {
                    while (allLootboxes.moveToNext()) {
                        Lootbox lootbox = new Lootbox(context, allLootboxes.getInt(2), allLootboxes.getInt(3));
                        lootbox.setVisible(allLootboxes.getInt(4) != 0);
                        for (int i = 5; i < 10; i++) {
                            if (!allLootboxes.getString(i).equals(""))
                                lootbox.addItem(ItemType.valueOf(allLootboxes.getString(i)));
                        }
                        gameScene[sceneIndex].addLootbox(lootbox);
                    }
                }

                allLootboxes.close();

            }
        } else {
            System.out.println("Error while getting Scenes from db");
        }

        allScenes.close();
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
        cursorNpc.close();
    }

    public void getAllQuestsFromDB() {
        QuestHandler questHandler = gamePanel.getQuestHandler();
        Cursor cursorQuests = getQuestCursor();
        if (cursorQuests.getCount() > 0) {
            while (cursorQuests.moveToNext()) {
                GameState finalCondition = gamePanel.getStateHandler().getState(cursorQuests.getString(2));
                finalCondition.value = cursorQuests.getInt(3);
                Quest quest = new Quest(gamePanel.getStateHandler(), cursorQuests.getString(1), finalCondition);
                Cursor cursorQuestEntry = getQuestEntryCursor(cursorQuests.getString(1));
                if (cursorQuestEntry.getCount() > 0) {
                    while (cursorQuestEntry.moveToNext()) {
                        QuestEntry questEntry = new QuestEntry(gamePanel.getStateHandler(), cursorQuestEntry.getString(2), cursorQuestEntry.getInt(3), cursorQuestEntry.getString(4), cursorQuestEntry.getInt(5));
                        questEntry.addCondition(cursorQuestEntry.getString(6), cursorQuestEntry.getInt(7));
                        questEntry.addCondition(cursorQuestEntry.getString(8), cursorQuestEntry.getInt(9));
                        quest.addQuestEntry(questEntry);
                    }
                }
                questHandler.addQuest(quest);
            }
        }
        cursorQuests.close();
    }

    public Dialogue getDialogueFromDB(String aDialogue)
    {
        Cursor cursorDialogue = getDialogueCursor(aDialogue);
        if (cursorDialogue.getCount() == 1) {
            cursorDialogue.moveToFirst();
            Dialogue dialogue = new Dialogue(gamePanel, DialoguePosition.valueOf(cursorDialogue.getString(2)), aDialogue);

            Cursor cursorDialogueText = getDialogueTextCursor(aDialogue);
            if (cursorDialogueText.getCount() != 0) {
                //add Dialogue Text
                while (cursorDialogueText.moveToNext())
                    dialogue.addTextElement(cursorDialogueText.getString(2), cursorDialogueText.getString(3), cursorDialogueText.getString(5), cursorDialogueText.getString(6), cursorDialogueText.getInt(7));

                //add Dialogue Replies
                Cursor cursorDialogueReply = getDialogueReplyCursor(aDialogue);
                if (cursorDialogueReply.getCount() != 0) {
                    while (cursorDialogueReply.moveToNext()) {
                        GameState state = gamePanel.getStateHandler().getState(cursorDialogueReply.getString(7));
                        //System.out.println("Create Reply: " + cursorDialogueReply.getString(2) + " Check state: " + state.name + " should be: " + cursorDialogueReply.getInt(8) + " and is: " + state.value);
                        if (gamePanel.getStateHandler().getState(cursorDialogueReply.getString(7)).value == cursorDialogueReply.getInt(8)) {
                            Dialogue followingDialogue = getDialogueFromDB(cursorDialogueReply.getString(3));
                            dialogue.addReply(cursorDialogueReply.getString(2), followingDialogue, cursorDialogueReply.getString(4), cursorDialogueReply.getString(5), cursorDialogueReply.getInt(6));
                        }
                    }
                }
                cursorDialogueReply.close();
                cursorDialogueText.close();
                cursorDialogue.close();

                return dialogue;
            }
            cursorDialogue.close();
        } else if (cursorDialogue.getCount() > 1) {
            System.out.println("FATAL ERROR: multiple Dialogues with the same name");
        }
        cursorDialogue.close();
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
            return new GameState(cursorState.getString(1), cursorState.getInt(2), (cursorState.getInt(3) == 1));
        }
        cursorState.close();
        return new GameState("NULL", 0);
    }

    private void addState(SQLiteDatabase db, String name, int initialValue) {
        ContentValues contentValues= new ContentValues();
        contentValues.put(TABLE_STATE_COL_NAME, name);
        contentValues.put(TABLE_STATE_COL_VALUE, initialValue);
        contentValues.put(TABLE_STATE_COL_SPECIAL, 0);
        db.insert(TABLE_STATE_NAME, null, contentValues);
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
                if (gamePanel.getStateHandler().getState(cursor.getString(3)).value == cursor.getInt(4) && gamePanel.getStateHandler().getState(cursor.getString(5)).value == cursor.getInt(6) && gamePanel.getStateHandler().getState(cursor.getString(7)).value == cursor.getInt(8)) {
                    String ret = cursor.getString(2);
                    cursor.close();
                    return ret;
                }
            }
        }
        cursor.close();
        return "npcDoesNotInteract";
    }

    public boolean initSavedPlayer()
    {
        Player player = gamePanel.getPlayer();
        Cursor playerSave = getPlayerSaveCursor();
        if (playerSave.getCount() == 1) {
            //System.out.println("Reload Player State.");
            playerSave.moveToFirst();
            player.teleport(playerSave.getInt(2), playerSave.getInt(3), Direction.valueOf(playerSave.getString(4)));
            player.setMunchiesMeter(playerSave.getInt(5));
            player.setStonedMeter(playerSave.getInt(6));
            gamePanel.setCurrentScene(playerSave.getInt(1));
            initInventory();
            playerSave.close();
            return true;
        }
        playerSave.close();
        return false;
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
                TABLE_PLAYER_SAVE_COL_DIRECTION  + ", " +
                TABLE_PLAYER_SAVE_COL_MUNCHIES + ", " +
                TABLE_PLAYER_SAVE_COL_STONED + ") VALUES (" +
                gamePanel.getCurrentScene() + ", " +
                player.getX() + ", " +
                player.getY() + ", '" +
                player.getDirection().name() + "', " +
                player.getMunchiesMeter() + ", " +
                player.getStonedMeter() + ");";
        db.execSQL(insertSaveQuery);
    }

    public void initInventory()
    {
        //System.out.println("Reload Inventory");
        Inventory inventory = gamePanel.getPlayer().getInventory();
        Cursor savedInventory = getInventoryCursor();
        if (savedInventory.getCount() > 0) {
            while (savedInventory.moveToNext()) {
                for (int i = 0; i < savedInventory.getInt(2); i++) {
                    Item newItem = Item.createNewItem(context, ItemType.valueOf(savedInventory.getString(1)));
                    inventory.addItem(newItem, false);
                    if (SpecialItem.isSpecialItem(newItem.getItemType())) {
                        newItem.setCount(savedInventory.getInt(3));
                    }
                }
            }
        }
        savedInventory.close();
    }

    public void deleteInventoryItem(String aItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteItemQuery = "DELETE FROM " + TABLE_INVENTORY_NAME + " WHERE " + TABLE_INVENTORY_COL_ITEM + " = '" + aItem + "';";
        db.execSQL(deleteItemQuery);
    }

    public void setInventoryItemCount(String aItem, int aCount, int aNumUses)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateItemQuery = "UPDATE " + TABLE_INVENTORY_NAME + " SET " + TABLE_INVENTORY_COL_NUM + " = " + aCount + " WHERE " + TABLE_INVENTORY_COL_ITEM + " = '" + aItem + "';";
        db.execSQL(updateItemQuery);
        if (SpecialItem.isSpecialItem(ItemType.valueOf(aItem))) {
            updateItemQuery = "UPDATE " + TABLE_INVENTORY_NAME + " SET " + TABLE_INVENTORY_COL_USES + " = " + aNumUses + " WHERE " + TABLE_INVENTORY_COL_ITEM + " = '" + aItem + "';";
        }
        db.execSQL(updateItemQuery);
    }

    public void addNewItemToInventory(String aItem, int aCount, int aNumUses)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertItemQuery = "INSERT INTO " + TABLE_INVENTORY_NAME + " (" + TABLE_INVENTORY_COL_ITEM + ", " + TABLE_INVENTORY_COL_NUM + ", " + TABLE_INVENTORY_COL_USES + ") VALUES ('" + aItem + "', " + aCount + ", " + aNumUses + ");";
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

    public boolean getCommandFromDatabase(NonPlayerCharacter npc) {
        Cursor command = getCommandCursor(npc.getName(), npc.getSceneIndex(), npc.getX(), npc.getY());
        if (command.getCount() > 0) {
            while (command.moveToNext()) {
                //check State
                if (gamePanel.getStateHandler().getState(command.getString(6)).value == command.getInt(7)) {

                    //ensure that the player is looking in the correct direction
                    if (npc.getDirection() != Direction.valueOf(command.getString(5))) {
                        npc.queueMovement(new MoveCommand(Direction.valueOf(command.getString(5))));
                    }

                    String[] commandString = command.getString(8).split(",");
                    for (int i = 0; i < commandString.length; i++) {
                        if (commandString[i].equals("J")) {
                            npc.queueMovement(new MoveCommand(Integer.parseInt(commandString[++i]), Integer.parseInt(commandString[++i]), Integer.parseInt(commandString[++i])));
                        } else {
                            switch (commandString[i]) {
                                case "U":
                                    npc.queueMovement(new MoveCommand(Direction.UP));
                                    break;
                                case "D":
                                    npc.queueMovement(new MoveCommand(Direction.DOWN));
                                    break;
                                case "L":
                                    npc.queueMovement(new MoveCommand(Direction.LEFT));
                                    break;
                                case "R":
                                    npc.queueMovement(new MoveCommand(Direction.RIGHT));
                                    break;
                            }
                        }
                    }
                    command.close();
                    return true;
                }
            }
        }
        command.close();
        return false;
    }


    public void updateNpcPosition(NonPlayerCharacter npc, int scene, int x, int y, Direction direction) {
        Cursor cursor = getNpcCursor(npc.getName());
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TABLE_NPC_COL_NAME, cursor.getString(1));
            contentValues.put(TABLE_NPC_COL_IMAGE_NAME, cursor.getString(2));
            contentValues.put(TABLE_NPC_COL_SCENE_INDEX, scene);
            contentValues.put(TABLE_NPC_COL_INIT_X, x);
            contentValues.put(TABLE_NPC_COL_INIT_Y, y);
            contentValues.put(TABLE_NPC_COL_INIT_DIRECTION, direction.name());
            contentValues.put(TABLE_NPC_COL_SPEED, cursor.getInt(7));
            db.insert(TABLE_NPC_NAME, null, contentValues);
            db.execSQL("DELETE FROM " + TABLE_NPC_NAME + " WHERE " + TABLE_NPC_COL_ID + " = " + cursor.getInt(0) + ";");
        }
        cursor.close();
    }


    public void saveDialogue(Dialogue dialogue) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NPC_MOVE_NAME);

        ContentValues cv = new ContentValues();
        cv.put(TABLE_DIALOGUE_SAVE_COL_DIALOGUE_NAME, dialogue.getName());
        cv.put(TABLE_DIALOGUE_SAVE_COL_CURRENT_TEXT, dialogue.getCurrentText());
        db.insert(TABLE_DIALOGUE_SAVE_NAME, null, cv);
    }

    public Dialogue getSavedDialogue() {
        Cursor cursor = getSavedDialogueCursor();
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Dialogue dialogue = gamePanel.getInteractionHandler().getDialogueFromDatabase(cursor.getString(1));
        dialogue.setCurrentText(cursor.getInt(2)-1);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DIALOGUE_SAVE_NAME + ";");

        cursor.close();
        return dialogue;
    }

    public boolean isDialogueSaved() {
        Cursor cursor = getSavedDialogueCursor();
        int count = cursor.getCount();
        cursor.close();
        if (count == 1)
            return true;
        return false;
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
        String selectScenesQuery = "SELECT * FROM " + TABLE_TILE_NAME + " WHERE " + TABLE_TILE_COL_INDEX_SCENE + " = " + aSceneIndex + ";";
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

    private Cursor getNpcCursor(String npcName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectNpcQuery = "SELECT * FROM " + TABLE_NPC_NAME + " WHERE " + TABLE_NPC_COL_NAME + " = '" + npcName + "';";
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

    private Cursor getCommandCursor(String aNpc, int startScene, int startX, int startY) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectCommandQuery = "SELECT * FROM " + TABLE_NPC_MOVE_NAME + " WHERE ( " +
                TABLE_NPC_MOVE_COL_ON_SCENE + " = " + startScene + " AND " +
                TABLE_NPC_MOVE_COL_ON_X + " = " + startX + " AND " +
                TABLE_NPC_MOVE_COL_ON_Y + " = " + startY + " );";
        return db.rawQuery(selectCommandQuery, null);
    }

    private Cursor getSavedDialogueCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_DIALOGUE_SAVE_NAME + ";";
        return db.rawQuery(selectQuery, null);
    }

    private Cursor getQuestCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_QUEST_NAME + ";";
        return db.rawQuery(selectQuery, null);
    }

    private Cursor getQuestEntryCursor(String questName){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_QUEST_ENTRY_NAME + " WHERE " + TABLE_QUEST_ENTRY_COL_QUEST_NAME + " = '" + questName + "';";
        return db.rawQuery(selectQuery, null);
    }

    private int getIntFromString(String aString)
    {
        if (aString == null || aString == "")
            return 0;
        else
            return Integer.parseInt(aString);
    }


}
