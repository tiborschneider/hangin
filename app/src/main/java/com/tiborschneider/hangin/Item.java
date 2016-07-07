package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Tibor Schneider on 24.06.2016.
 */
public class Item {
    protected ItemType itemType;
    protected Bitmap image;
    protected Context context;
    protected int munchiesChange = 0;
    protected int stonedChange = 0;
    protected boolean isSpecial = false;
    protected boolean needsFire = false;

    public Item(Context aContext, ItemType aType)
    {
        itemType = aType;
        context = aContext;
        image = BitmapFactory.decodeResource(context.getResources(), getItemId());
        image = InterfaceElement.resizeImage(image, InterfaceElement.itemSize, InterfaceElement.itemSize);
        switch (itemType) {
            case ALCOHOL:
                break;
            case APPLE:
                munchiesChange = -5;
                break;
            case BACON:
                munchiesChange = -20;
                break;
            case BANANA:
                munchiesChange = -5;
                break;
            case BEER:
                break;
            case BURGER:
                munchiesChange = -40;
                break;
            case CARROT:
                munchiesChange = -5;
                break;
            case CHEESE:
                munchiesChange = -15;
                break;
            case RIBS:
                munchiesChange = -15;
                break;
            case SEED:
                break;
            case STEAK:
                munchiesChange = -40;
                break;
            case VEGAN_JUICE:
                break;
            case JOINT:
                stonedChange = 30;
                needsFire = true;
                break;
            case BIG_JOINT:
                stonedChange = 60;
                needsFire = true;
                break;
            case UGLY_JOINT:
                stonedChange = 20;
                needsFire = true;
                break;
        }

    }

    protected int getItemId()
    {
        String imageName = "item_" + itemType.toString().toLowerCase();
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public Bitmap getImage()
    {
        return image;
    }

    public String getText()
    {
        switch (itemType) {
            case ALCOHOL:
                return "damn strong alcohol";
            case APPLE:
                return "apple";
            case BACON:
                return "fucking tasty bacon";
            case BANANA:
                return "banana";
            case BEER:
                return "beer";
            case BURGER:
                return "burger";
            case CARROT:
                return "carrot";
            case CHEESE:
                return "stinky cheese";
            case RIBS:
                return "ribs";
            case SEED:
                return "weed seeds";
            case STEAK:
                return "steak";
            case VEGAN_JUICE:
                return "vegan juice. stupid vegans...";
            case JOINT:
                return "normal joint";
            case BIG_JOINT:
                return "really big joint";
            case UGLY_JOINT:
                return "ugly joint";
        }
        return "none";
    }

    public ItemType getItemType()
    {
        return itemType;
    }

    public void use(InteractionHandler interactionHandler)
    {
        String dialogueName;
        switch (itemType) {
            case ALCOHOL:
                dialogueName = "useAlcohol";
                break;
            case APPLE:
                dialogueName = "useApple";
                break;
            case BACON:
                dialogueName = "useBacon";
                break;
            case BANANA:
                dialogueName = "useBanana";
                break;
            case BEER:
                dialogueName = "useBeer";
                break;
            case BURGER:
                dialogueName = "useBurger";
                break;
            case CARROT:
                dialogueName = "useCarrot";
                break;
            case CHEESE:
                dialogueName = "useCheese";
                break;
            case RIBS:
                dialogueName = "useRibs";
                break;
            case SEED:
                dialogueName = "useSeeds";
                break;
            case STEAK:
                dialogueName = "useSteak";
                break;
            case VEGAN_JUICE:
                dialogueName = "useVeganJuice";
                break;
            case JOINT:
                dialogueName = "smokeJoint";
                break;
            case BIG_JOINT:
                dialogueName = "smokeBigJoint";
                break;
            case UGLY_JOINT:
                dialogueName = "smokeUglyJoint";
                break;
            default:
                return;
        }
        interactionHandler.createDialogue(interactionHandler.getDialogueFromDatabase(dialogueName));
    }

    public int getMunchiesChange()
    {
        return munchiesChange;
    }

    public int getStonedChange()
    {
        return stonedChange;
    }

    public static Item createNewItem(Context aContext, ItemType aType)
    {
        if (SpecialItem.isSpecialItem(aType))
            return new SpecialItem(aContext, aType);
        else
            return new Item(aContext, aType);
    }

    public boolean useOnce()
    {
        return false;
    }

    public void setCount(int count)
    {
        //do nothing;
    }

    public int getCount()
    {
        return 0;
    }

    public int getNumUses()
    {
        return 1;
    }

    public int getMaxNumUses()
    {
        return 1;
    }

    public boolean needsfire()
    {
        return needsFire;
    }
}
