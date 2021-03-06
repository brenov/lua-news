package br.edu.ufrn.brenov.luanews.controller.database.rss;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import br.edu.ufrn.brenov.luanews.model.RSSChannel;

public class RSSDatabase {

    private final static String FILE_NAME = "luanews_rss_database.json";
    private final static String CHANNELS = "channels";
    private final static String LINK = "link";
    private final static String NAME = "name";
    private final static String FAV = "fav";

    public final static int CHANNEL_ADDED = 0;
    public final static int CHANNEL_ALREADY_ADDED = 1;

    public final static int CHANNEL_REMOVED = 2;
    public final static int CHANNEL_NOT_ADDED = 3;

    public final static int CHANNEL_FAV_CHANGED = 4;
    public final static int CHANNEL_FAV_NO_CHANGED = 5;

    public final static int INVALID_CHANNEL = -1;

    private static void createDatabase(Context context) throws JSONException, IOException {
        // Create object
        JSONObject json = new JSONObject();
        json.put(CHANNELS, new JSONArray());
        // Write file
        OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(FILE_NAME,
                Context.MODE_PRIVATE));
        try (PrintWriter pw = new PrintWriter(out)) {
            pw.write(json.toString());
            pw.flush();
        }
    }

    public static List<RSSChannel> getRSSChannels(Context context) throws JSONException, IOException {
        // Open file
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(context.openFileInput(FILE_NAME));
        } catch (FileNotFoundException ex) {
            createDatabase(context);
            return getRSSChannels(context);
        }
        // Read file
        JSONObject json;
        try(Scanner scanner = new Scanner(in)) {
            StringBuilder sb = new StringBuilder();
            // Get next line
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                sb.append(line).append(System.lineSeparator());
            }
            json = new JSONObject(sb.toString());
        }
        // Check if the rss channel was registered
        if (json == null) {
            return null;
        } else {
            List<RSSChannel> channels = new ArrayList<>();
            JSONArray array = json.getJSONArray(CHANNELS);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                channels.add(new RSSChannel(jo.getString(LINK), jo.getString(NAME), jo.getBoolean(FAV)));
            }
            return channels;
        }
    }

    public static int add(RSSChannel rss, Context context) throws JSONException, IOException {
        // Check if the channel is invalid
        if (rss.getLink().equals("") || rss.getName().equals("")) {
            return INVALID_CHANNEL;
        }
        // Check if the channel was already added
        List<RSSChannel> database = getRSSChannels(context);
        boolean contains = false;
        for (RSSChannel channel : database) {
            if (channel.getLink().equals(rss.getLink())) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            // Read file
            JSONObject json;
            InputStreamReader in = new InputStreamReader(context.openFileInput(FILE_NAME));
            try(Scanner scanner = new Scanner(in)) {
                StringBuilder sb = new StringBuilder();
                // Get next line
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    sb.append(line).append(System.lineSeparator());
                }
                json = new JSONObject(sb.toString());
            }
            // Add new object
            JSONObject jo = new JSONObject();
            jo.put(LINK, rss.getLink());
            jo.put(NAME, rss.getName());
            jo.put(FAV, rss.isFav());
            json.getJSONArray(CHANNELS).put(jo);
            // Write file
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(FILE_NAME,
                    Context.MODE_PRIVATE));
            try (PrintWriter pw = new PrintWriter(out)) {
                pw.write(json.toString());
                pw.flush();
            }
            return CHANNEL_ADDED;
        } else {
            return CHANNEL_ALREADY_ADDED;
        }
    }

    public static int remove(RSSChannel rss, Context context) throws JSONException, IOException {
        // Check if the channel is invalid
        if (rss.getLink().equals("") || rss.getName().equals("")) {
            return INVALID_CHANNEL;
        }
        // Check if the channel was already added
        List<RSSChannel> database = getRSSChannels(context);
        boolean contains = false;
        for (RSSChannel channel : database) {
            if (channel.getLink().equals(rss.getLink())) {
                contains = true;
                break;
            }
        }
        if (contains) {
            // Read file
            JSONObject json;
            InputStreamReader in = new InputStreamReader(context.openFileInput(FILE_NAME));
            try(Scanner scanner = new Scanner(in)) {
                StringBuilder sb = new StringBuilder();
                // Get next line
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    sb.append(line).append(System.lineSeparator());
                }
                json = new JSONObject(sb.toString());
            }
            // Remove new object
            JSONArray array = json.getJSONArray(CHANNELS);
            int index = -1;
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                if (jo.get(LINK).equals(rss.getLink())) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                array.remove(index);
                // Write file
                OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(FILE_NAME,
                        Context.MODE_PRIVATE));
                try (PrintWriter pw = new PrintWriter(out)) {
                    pw.write(json.toString());
                    pw.flush();
                }
                return CHANNEL_REMOVED;
            }
            return CHANNEL_NOT_ADDED;
        } else {
            return CHANNEL_NOT_ADDED;
        }
    }

    public static int fav(RSSChannel rss, Context context) throws JSONException, IOException {
        // Check if the channel is invalid
        if (rss.getLink().equals("") || rss.getName().equals("")) {
            return INVALID_CHANNEL;
        }
        // Check if the channel was already added
        List<RSSChannel> database = getRSSChannels(context);
        boolean contains = false;
        for (RSSChannel channel : database) {
            if (channel.getLink().equals(rss.getLink())) {
                contains = true;
                break;
            }
        }
        if (contains) {
            // Read file
            JSONObject json;
            InputStreamReader in = new InputStreamReader(context.openFileInput(FILE_NAME));
            try(Scanner scanner = new Scanner(in)) {
                StringBuilder sb = new StringBuilder();
                // Get next line
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    sb.append(line).append(System.lineSeparator());
                    Log.i("rss", line);
                }
                json = new JSONObject(sb.toString());
            }
            // Overwrite object
            JSONArray array = json.getJSONArray(CHANNELS);
            int index = -1;
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                if (jo.getString(LINK).equals(rss.getLink())) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                array.getJSONObject(index).put(FAV, rss.isFav());
                // Write file
                OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(FILE_NAME,
                        Context.MODE_PRIVATE));
                try (PrintWriter pw = new PrintWriter(out)) {
                    pw.write(json.toString());
                    pw.flush();
                }
                return CHANNEL_FAV_CHANGED;
            } else {
                return CHANNEL_FAV_CHANGED;
            }
        }
        return CHANNEL_FAV_NO_CHANGED;
    }
}
