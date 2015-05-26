package com.ericcumbee.dlid;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ericcumbee on 12/18/14.
 */
public final class parseV1 {
    public static final String ColoradoIssuerId = "636020";
    public static final String ConnecticutIssuerId = "636006";
    public static final String IllinoisIssuerId = "636035";
    public static final String MassachusettsIssuerId = "636002";
    public static final String SouthCarolinaIssuerId = "636005";
    public static final String TennesseeIssuerId = "636053";

    public static DLicense parse(String data,String issuer)
    {
        parser.dataRange range = dataRangeV1(data);
        // Illinois are the worst offenders so far in terms of mangling the DLID
        // spec.  They store name, licence number, expiry date and date of birth
        // as expected, but then go all-out crazy and encrypt everything else.
        // This means that the data range exceeds the size of the licence data
        // string.  We have to treat Illinois as a special case.
        if(issuer.equals(IllinoisIssuerId))
        {
            range.end = data.length() -1;
        }

        if(range.end >= data.length())
        {
            System.out.println("Range end greater than or = data length");
        }
        byte[] payloadarray = data.getBytes(Charset.forName("UTF-8"));
        String payload = new String(payloadarray,range.start,range.end);

        return parseDataV1(payload,issuer);

    }

    private static DLicense parseDataV1(String licenceData, String issuer) {
        // Version 1 of the DLID card spec was published in 2000.  As of 2012, it is
        // the version used in Colorado.

        // We want to strip off the "DL" chunk identifier, but every other state has
        // managed to screw this up too.  Rather than handle this on a
        // state-by-state basis, we'll check to see what's at the target location
        // and handle it appropriately.

        if(licenceData.startsWith("DL")){
            //It's right
            licenceData = licenceData.substring(2);
        }
        else if(licenceData.startsWith("L"))
        {
            // Either the guys in South Carolina can't count or they don't consider
            // the "DL" header part of the licence data.  In either case, their
            // offset is off by one.
            licenceData = licenceData.substring(1);
        }


        DLicense license = new DLicense();
        license.setIssuerId(issuer);
        license.setIssuerName(issuers.issuers.get(issuer));
        //for v1 always USA
        license.setCountry("USA");
        //System.out.println(licenceData);
        StringTokenizer t = new StringTokenizer(licenceData,"\\n");
        while(t.hasMoreTokens())
        {
            String component = t.nextToken();
            if(component.length() < 3)
            {
                continue;
            }
            String identifer = component.substring(0,3);
            String data = component.substring(3).trim();
            System.out.println(identifer+":"+data);
            switch (identifer)
            {
                case "DAR":
                    license.setVehicleClass(data);
                    break;
                case "DAS":
                    license.setRestrictionCodes(data);
                    break;
                case "DAT":
                    license.setEndorsementCodes(data);
                    break;
                case "DAA":
                    //Early Versions of colorado messed up the delimiter with a space instead of a comma
                    String separator = " ";
                    if(!data.contains(separator))
                    {
                        separator = ",";
                    }
                    String[] names = data.split(separator);
                    if(issuer.equals(ColoradoIssuerId)||issuer.equals(TennesseeIssuerId))
                    {
                        license.setFirstName(names[0]);
                         int namecount = names.length;
                        if(names.length > 2)
                        {

                            license.setMiddleName(Arrays.copyOfRange(names,1,names.length-1));
                            license.setLastName(names[names.length-1]);
                        }
                        else if(names.length > 1)
                        {
                            license.setLastName(names[1]);
                        }
                    }
                    else{
                        license.setLastName(names[0]);
                        if(names.length > 1)
                        {
                            license.setFirstName(names[1]);
                            if(names.length > 2)
                            {
                                license.setMiddleName(Arrays.copyOfRange(names,2,names.length));
                            }
                        }
                    }
                    break;
                case "DAE":
                    license.setNameSuffix(data);
                    break;
                case "DAL":
                    // Colorado screws up again: they omit the *required* DAG field and
                    // substitute the optional DAL field in older licences.
                    license.setStreet(data);
                    break;
                case "DAG":
                    license.setStreet(data);
                    break;
                case "DAN":
                    license.setCity(data);
                    break;
                case "DAI":
                    license.setCity(data);
                    break;
                case "DAO":
                    //colorado again....
                    license.setState(data);
                    break;
                case "DAJ":
                    license.setState(data);
                    break;
                case "DAP":
                    //colorado again
                    license.setPostal(data.trim());
                    break;
                case "DAK":
                    license.setPostal(data.trim());
                    break;
                case "DAQ":
                    license.setCustomerId(data);
                    break;
                case "DBA":
                    license.setExpiryDate(parseDateV1(data));
                    break;
                case "DBB":
                    license.setDateOfBirth(parseDateV1(data));
                    break;
                case "DBC":
                    switch(data)
                    {
                        case "M":
                            license.setSex(DLicense.Sex.MALE);
                            break;
                        case "1":
                            license.setSex(DLicense.Sex.MALE);
                            break;
                        case "F":
                            license.setSex(DLicense.Sex.FEMALE);
                            break;
                        case "2":
                            license.setSex(DLicense.Sex.FEMALE);
                            break;
                        default:
                            license.setSex(DLicense.Sex.NONE);
                    }
                    break;


            }
        }
        String[] components = licenceData.split("\\n");
        System.out.println(licenceData.indexOf("\\n",17));
        System.out.println(components.length);
        return license;
    }


    private static Date parseDateV1(String data) {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        try {
            return date.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static parser.dataRange dataRangeV1(String data)
    {
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));
        int start=0;
        int end=0;
        ArrayList<String> errors;
        String test = new String(databytes,36,4);
        //System.out.println("TESTStart:"+test);
        String test1 = new String(databytes,40,4);
        //System.out.println("TESTEND:"+test1);
        end = Integer.parseInt(test);
        start = Integer.parseInt(test1);
        System.out.println(start);
        //System.out.println(data.substring(25,26));
        System.out.println(end);

        return new parser.dataRange(start,end,null);
    }
}
