package com.ericcumbee.dlid;

import java.nio.charset.Charset;
import java.util.StringTokenizer;

/**
 * Created by ericcumbee on 12/23/14.
 */
public class parsev4 {
    public static DLicense parse(String data,String issuer)
    {
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));
        parser.dataRange range = parser.dataRangeV2(data);
        if(range.end >= data.length())
        {

        }
        String payload = new String(databytes,range.start,range.end);
        return parseDataV4(payload, issuer);


    }

    private static DLicense parseDataV4(String payload, String issuer) {
        DLicense license = new DLicense();
        if(!payload.startsWith("DL"))
        {
            //System.out.println("payload:"+payload);
            //return null;
        }
        else
        {

            String licenseData = payload.substring(2);
            String dateofBirth = null;
            String expiryDate = null;
            String issueDate = null;

            license.setIssuerId(issuer);
            license.setIssuerName(issuers.issuers.get(issuer));
            StringTokenizer t = new StringTokenizer(licenseData,"\\n");
            while(t.hasMoreTokens()) {
                String component = t.nextToken();
                String identifer = component.substring(0, 3);
                String data = component.substring(3).trim();
                switch (identifer)
                {
                    case "DCA":
                        license.setVehicleClass(data);
                        System.out.println(license.getVehicleClass());
                        break;
                    case "DCB":
                        license.setRestrictionCodes(data);
                        System.out.println(license.getRestrictionCodes());
                        break;
                    case "DCD":
                        license.setEndorsementCodes(data);
                        System.out.println(license.getEndorsementCodes());
                        break;
                    case "DCS":
                        license.setLastName(data);
                        System.out.println(license.getLastName());
                        break;
                    case "DCU":
                        license.setNameSuffix(data);
                        break;
                    case "DAC":
                        license.setFirstName(data);
                        break;
                    case "DAD":
                        license.setMiddleName(data.split(","));
                        break;
                    case "DCG":
                        license.setCountry(data);
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
                    case "DBA":
                        expiryDate = data;
                        break;
                    case "DBB":
                        dateofBirth = data;
                        break;
                    case "DBC":
                        switch (data) {
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
                    case "DBD":
                        issueDate = data;
                }
            }
            if(license.getCountry().equals("USA") && license.getPostal().length()>0)
            {
                if(license.getPostal().length()>5)
                {
                    String zip = license.getPostal().substring(0, 5);
                    String plus4 = license.getPostal().substring(5);

                    if (plus4.equals("0000")) {
                        license.setPostal(zip);
                        System.out.println(plus4);
                        System.out.println(zip);
                    } else {
                        license.setPostal(zip + "+" + plus4);
                    }
                }
            }
            if(license.getCountry().length()>0)
            {
                license.setDateOfBirth(parsev3.parseDateV3(dateofBirth,license.getCountry()));
                license.setExpiryDate(parsev3.parseDateV3(expiryDate,license.getCountry()));
                license.setIssueDate(parsev3.parseDateV3(issueDate,license.getCountry()));
            }

        }
        return license;
    }
}
