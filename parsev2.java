package com.ericcumbee.dlid;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by ericcumbee on 12/22/14.
 */
public class parsev2 {

    public static DLicense parse(String data, String issuer)
    {
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));
        parser.dataRange range = dataRangeV2(data);
        if(range.end >= data.length())
        {
            System.out.println("Payload bad 1");
        }
        String payload = new String(databytes,range.start,range.end);
        System.out.println(payload);
        return null;
    }
    public static DLicense parseDataV2(String licenceData, String issuer)
    {
        if(licenceData.startsWith("DL")){
            //It's right
            licenceData = licenceData.substring(2);
        }
        DLicense license = new DLicense();
        license.setIssuerId(issuer);
        license.setIssuerName(issuers.issuers.get(issuer));
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
            switch (identifer)
            {
                case "DCA":
                    license.setVehicleClass(data);
                    break;
                case "DCB":
                    license.setRestrictionCodes(data);
                    break;
                case "DCD":
                    license.setEndorsementCodes(data);
                    break;
                case "DCS":
                    license.setLastName(data);
                    break;
                case "DCT":
                    // This field contains all of the licencee's names except last
                    // name.  The V2 spec doc doesn't specify how the names are
                    // separated and doesn't provide an example (unlike the 2000
                    // doc).  Wisconsin use a space and Virginia use a comma.  This
                    // is why standards have to be adequately documented.
                    String separator = " ";
                    if(data.contains(separator))
                    {
                        separator = ",";
                    }
                    String[] names = data.split(separator);
                    license.setFirstName(names[0]);
                    license.setMiddleName(Arrays.copyOfRange(names,1, names.length));
                    break;
                case "DAG":
                    license.setStreet(data);
                    break;
                case "DAI":
                    license.setCity(data);
                    break;
                case "DAJ":
                    license.setState(data);
                    break;
                case "DAK":
                    license.setPostal(data);
                    break;
                case "DAQ":
                    license.setCustomerId(data);
                    break;
                case "DBB":
                    license.setDateOfBirth(parseDateV2(data));
                    break;
                case "DBC":
                    switch (data)
                    {
                        case "1":
                            license.setSex(DLicense.Sex.MALE);
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
        return license;

    }


    private static Date parseDateV2(String data) {
        SimpleDateFormat date = new SimpleDateFormat("MMddyyyy");
        try {
            return date.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static parser.dataRange dataRangeV2(String data)
    {
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));
        int start=0;
        int end=0;
        ArrayList<String> errors;
        String test = new String(databytes,24,4);
        System.out.println("TESTStart:"+test);
        String test1 = new String(databytes,27,4);
        System.out.println("TESTEND:"+test1);
        start = Integer.parseInt(test);
        end = Integer.parseInt(test1);
        System.out.println(start);
        //System.out.println(data.substring(25,26));
        System.out.println(end);

        return new parser.dataRange(start,end,null);
    }
}
