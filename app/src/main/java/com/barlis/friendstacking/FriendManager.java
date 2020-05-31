package com.barlis.friendstacking;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class FriendManager {

    public static FriendManager instance;
    private static int i = 0;
    private Context context;
    private ArrayList<Friend> friends = new ArrayList<>();

    static final String FILE_NAME = "friends.dat";

    private FriendManager(Context context){
        this.context = context;

        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            friends = (ArrayList<Friend>)ois.readObject();

            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static FriendManager getInstance(Context context) {
        if(instance == null)
        {
            instance = new FriendManager(context);
        }
        return instance;
    }

    public Friend getFriend(int position)
    {
        if(position < friends.size())
        {
            return friends.get(position);
        }
        return null;
    }

    public void addFriend(Friend friend)
    {
        friends.add(friend);
        saveFriends();
    }

    public void removeFriend(int position)
    {
        if(position < friends.size())
        {
            friends.remove(position);
        }
        saveFriends();
    }

    public void swapFriends(int position, int target)
    {
        Collections.swap(friends, position, target);
        saveFriends();
    }

    public void undoFriend(int position)
    {
        Friend friend = friends.get(position);
        removeFriend(position);
        friends.add(position, friend);
        saveFriends();
    }

    private void saveFriends()
    {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(friends);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }
}

