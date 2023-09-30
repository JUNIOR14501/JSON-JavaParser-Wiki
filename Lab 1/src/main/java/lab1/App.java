package lab1;

//Murzin production
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONObject;

import com.google.gson.Gson;
import java.net.URL;
//import JSON.JSONObject;
import java.awt.Desktop;

public class App {
    public static void main(String arg[]){
        Scanner in = new Scanner(System.in, "UTF-8");
        String urlAricle = GetURL(in);
        String jsonStr = getJsonString(urlAricle);
        AllPage allPage = ConvertToClass(jsonStr);
        int selectedPoint = Menu(allPage, in);
        Desktop(allPage, selectedPoint);
        in.close();
    }
    
    public static String GetURL(Scanner in){
        System.out.print("Enter the article name (Only EN): ");

        String nameArticle = in.nextLine().toString();
        

        //System.out.printf(nameArticle);

        nameArticle = encodeValue(nameArticle);
        String urlAricle = "https://en.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=\"" + nameArticle + "\"";
        return urlAricle;
    }

    public static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
    
    public static String getJsonString(String oldUrl){
        try {
            URL url = new URL(oldUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }

            conn.disconnect();
            
            JSONObject json = new JSONObject(builder.toString());
            json.remove("batchcomplete");
            json.remove("continue");
            
            String jsonStr = json.toString();
            //System.out.println(jsonStr);
            
            return jsonStr;

            
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception e";
        }
    }

    public static AllPage ConvertToClass(String jsonStr){
            Gson g = new Gson();
            AllPage allPage = g.fromJson(jsonStr, AllPage.class);
            return allPage;
    }

    public static int Menu(AllPage allPage, Scanner in){
        if (allPage.query.search.size() == 0){
            throw new NullPointerException("There are no pages with a private name");
        }
        for (int i = 0; i < allPage.query.search.size(); i++){
            System.out.println("№ " + i); 
            System.out.println("Name of article: " + allPage.query.search.get(i).title);
            System.out.println("ID Page: " + allPage.query.search.get(i).pageid);
            System.out.println("Word count: " + allPage.query.search.get(i).wordcount + "\n");
            
        }

        System.out.print("Chose an article: ");
        
        int selectedPoint = in.nextInt();
        return selectedPoint;
    }

    public static void Desktop(AllPage allPage, int selectedPoint){
        try {
            int pageid = allPage.query.search.get(selectedPoint).pageid;

            URI uri = new URI("https://en.wikipedia.org/w/index.php?curid=" + pageid);
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                desktop.browse(uri);
            }

        } catch (URISyntaxException ex) {
            System.out.print("URISyntaxException");
        } catch (IOException ioe) {
        }
        
    }
}
