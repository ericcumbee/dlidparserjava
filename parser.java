package com.ericcumbee.dlid;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ericcumbee on 12/17/14.
 */
public class parser {
    ArrayList<String> errs;
    public static class dataRange
    {
        int start;
        int end;
        ArrayList<String> Error;

        public dataRange(int start, int end, ArrayList<String> error) {
            this.start = start;
            this.end = end;
            Error = error;
        }
    }

    public DLicense parse(String data)
    {
        DLicense license = new DLicense();
        errs = new ArrayList<String>();
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));

        if(databytes.length < 15)
        {

            errs.add("Data does not contain expected header");
            license.setErrors(errs);
            return license;
        }
        if(!new String(databytes,0,3).contains("@\\n") ||
                !new String(databytes,7,2).contains("\\r")||
                (!new String(databytes,9,5).contains("ANSI ") && !new String(databytes,9,5).contains("AAMVA")))
        {

            errs.add("Data does not contain expected header");
            license.setErrors(errs);
            return license;
        }
        //System.out.println(data.indexOf("636020"));
        //System.out.println(new String(databytes,14,6));
        String issuer = new String(databytes,14,6);
        int version;
        try
        {

            version = Integer.parseInt(new String(databytes,20,2));
            //System.out.println(new String(databytes,20,2));
            //System.out.println("ver:"+version);



        }
        catch (NumberFormatException nfe)
        {
            errs.add("Data Does not contain a version number");
            license.setErrors(errs);
            return license;

        }
        if(version != -1)
        {
            System.out.println("Version:"+version);
            switch (version)
            {
                case 1:
                    return parseV1.parse(data,issuer);
                case 2:
                    return parsev2.parse(data,issuer);
                case 3:
                    return parsev3.parse(data,issuer);
                case 4:
                    return parsev4.parse(data,issuer);
                case 5:
                    return parsev4.parse(data,issuer);
                case 6:
                    return parsev4.parse(data,issuer);
                case 7:
                    return parsev4.parse(data,issuer);

            }

        }
        return null;
    }

    private static SimpleDateFormat parseDateV2(String data) {
        SimpleDateFormat date = new SimpleDateFormat("MMddyyyy");
        try {
            date.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static parser.dataRange dataRangeV2(String data)
    {
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));
        int start;
        int end;
        String test = new String(databytes,28,4);
        System.out.println("TESTStart:"+test);
        String test1 = new String(databytes,32,4);
        System.out.println("TESTEND:"+test1);
        start = Integer.parseInt(test)+5;
        end = databytes.length-start;

        return new parser.dataRange(start,end,null);
    }
}

