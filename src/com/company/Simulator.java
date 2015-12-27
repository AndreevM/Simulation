package com.company;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.*;
import java.util.*;

/**
 * Created by Mikhail on 24.12.2015.
 */
public class Simulator {
    void Run()
    {
        Quotes.uploadQuotes();
        
        InvestmentClub club = new InvestmentClub();
        club.OnboardInvestor(startDate);

        for (Date _d:
             tradingCalendar) {
            if(!(_d.before(startDate) || _d.after(endDate)))
            {
                //simulate
                currentDate = _d;

                //club.OnboardInvestor(_d);
                club.update(_d);
                //System.out.println(currentDate + "  " + club.GetInvestmentClubAUM());
            }
            
        }
    }

    Date startDate;
    Date endDate;
    static Date currentDate;

    List<Date> tradingCalendar;

    Simulator() {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        try {
            startDate = format.parse("30.06.2000");
            endDate = format.parse("30.11.2015");
        }

        catch (ParseException e) {
            e.printStackTrace();
        }
        tradingCalendar = new ArrayList<Date>();
        uploadCalendar();
    }

    void uploadCalendar() {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        String fileName = "C:\\Users\\Mikhail\\IdeaProjects\\Simulation\\DailyPrices.csv";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String strLine = null;
            StringTokenizer st = null;
            int lineNumber = 0, tokenNumber = 0;

            while ((fileName = br.readLine()) != null) {
                lineNumber++;
                String[] result = fileName.split(";");
                //for (int x=0; x<result.length; x++) {
                //System.out.println(result[x]);
                if(!result[0].equals("Date"))
                 tradingCalendar.add(format.parse(result[0]));
                //}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
