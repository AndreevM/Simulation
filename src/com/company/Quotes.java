package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Mikhail on 24.12.2015.
 */
public abstract class Quotes {
    static Map<AssetType, Map<Date, Double>> quotes;

    static void uploadQuotes() {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        //String fileName = "C:\\Users\\Mikhail\\IdeaProjects\\Simulation\\DailyPrices.csv";
        String fileName = "C:\\Users\\myandreev\\IdeaProjects\\MyProject\\DailyPrices.csv";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String strLine = null;
            StringTokenizer st = null;
            int lineNumber = 0, tokenNumber = 0;

            while ((fileName = br.readLine()) != null) {
                lineNumber++;
                String[] result = fileName.split(";");
                for (int x=0; x<result.length; x++) {
                    //System.out.println(result[x]);
                    if(!result[0].equals("Date"))
                    {
                        Date _d = format.parse(result[0]);
                        quotes.get(AssetType.SP).put(_d, Double.parseDouble(result[1]));
                        quotes.get(AssetType.DOW).put(_d, Double.parseDouble(result[2]));
                        quotes.get(AssetType.VOL).put(_d, Double.parseDouble(result[3]));
                        quotes.get(AssetType.UsEq).put(_d, Double.parseDouble(result[4]));
                        quotes.get(AssetType.DevEq).put(_d, Double.parseDouble(result[5]));
                        quotes.get(AssetType.EMMkt).put(_d, Double.parseDouble(result[6]));
                        quotes.get(AssetType.US_REIT).put(_d, Double.parseDouble(result[7]));
                        quotes.get(AssetType.US_TIPS).put(_d, Double.parseDouble(result[8]));
                        quotes.get(AssetType.US_Bond).put(_d, Double.parseDouble(result[9]));
                    }
                    else
                    {
                        quotes = new HashMap<AssetType, Map<Date, Double>>();
                        quotes.put(AssetType.SP, new HashMap<Date, Double>());
                        quotes.put(AssetType.DOW, new HashMap<Date, Double>());
                        quotes.put(AssetType.VOL, new HashMap<Date, Double>());
                        quotes.put(AssetType.UsEq, new HashMap<Date, Double>());
                        quotes.put(AssetType.DevEq, new HashMap<Date, Double>());
                        quotes.put(AssetType.EMMkt, new HashMap<Date, Double>());
                        quotes.put(AssetType.US_REIT, new HashMap<Date, Double>());
                        quotes.put(AssetType.US_TIPS, new HashMap<Date, Double>());
                        quotes.put(AssetType.US_Bond, new HashMap<Date, Double>());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static double GetQuote(AssetType _type, Date _date)
    {return quotes.get(_type).get(_date);}
}
