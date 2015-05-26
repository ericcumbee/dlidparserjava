package com.ericcumbee.dlid;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by ericcumbee on 12/22/14.
 */
public  class parsev3 {

    public static DLicense parse(String data, String issuer)
    {
        byte[] databytes = data.getBytes(Charset.forName("UTF-8"));
        parser.dataRange range = parser.dataRangeV2(data);
        if(range.end >= data.length())
        {
            System.out.println("Payload bad 1");
        }

        String payload = new String(databytes,range.start,range.end);
        DLicense license = parseDataV3(payload,issuer);
        System.out.println(data);
        System.out.println(payload);
        return null;
    }

    private static DLicense parseDataV3(String payload, String issuer) {
        if(!payload.startsWith("DL"))
        {
            System.out.println("payload:"+payload);
            return null;
        }
        else
        {
            String licenseData = payload.substring(2);
            System.out.println("payload:" + licenseData);
            String dateofBirth = null;
            String expiryDate = null;
            String issueDate = null;
            DLicense license = new DLicense();
            license.setIssuerId(issuer);
            license.setIssuerName(issuers.issuers.get(issuer));
            StringTokenizer t = new StringTokenizer(licenseData,"\\n");
            int i = 0;
            while(t.hasMoreTokens()) {
                String component = t.nextToken();
                String identifer = component.substring(0,3);
                String data = component.substring(3).trim();
                switch (identifer) {
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
                    case "DCG":
                        license.setCountry(data);
                        System.out.println(data);
                        System.out.println(license.getCountry());
                        break;
                    case "DCT":
                        // This field contains all of the licencee's names except last
                        // name.  The V3 spec doc doesn't specify how the names are
                        // separated and doesn't provide an example (unlike the 2000
                        // doc).  Wisconsin use a space and Virginia use a comma.  This
                        // is why standards have to be adequately documented.
                        String separator = " ";

                        if (!data.contains(separator)) {
                            separator = ",";
                        }
                        String[] names = data.split(separator);
                        license.setFirstName(names[0]);
                        System.out.println(license.getFirstName());
                        license.setMiddleName(Arrays.copyOfRange(names, 1, names.length));
                        System.out.println(Arrays.toString(license.getMiddleName()));
                        break;
                    case "DAG":
                        license.setStreet(data);
                        System.out.println(license.getStreet());

                        break;
                    case "DAI":
                        license.setCity(data);
                        System.out.println(license.getCity());
                        break;
                    case "DAJ":
                        license.setState(data);
                        System.out.println(license.getState());
                        break;
                    case "DAK":
                        license.setPostal(data);
                        System.out.println(license.getPostal());
                        break;
                    case "DAQ":
                        license.setCustomerId(data);
                        System.out.println(license.getCustomerId());
                        break;
                    case "DBA":
                        expiryDate = data;
                        System.out.println(expiryDate);
                        break;
                    case "DBB":
                        dateofBirth = data;
                        System.out.println(dateofBirth);
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
                        System.out.println(license.getSex());
                        break;
                    case "DBD":
                        issueDate = data;
                        System.out.println(issueDate);
                        break;
                    default:
                        break;

                }


            }

            if (license.getCountry().equals("USA") && license.getPostal().length() > 0) {
                // For some reason known only to themselves, the standards guys took
                // the V1 and 2 postal code standards (code padded to 11 characters with
                // spaces) and replaced the spaces with zeros if a) the country is "USA"
                // and b) if the trailing "+4" portion of the postal code is unknown.
                // Quite what happens to pad Canadian postal codes (they are always 6
                // alphanumeric characters, like British postal codes) is undocumented.
                //
                // We will extract the 5-digit zip and the +4 section.  If the +4 is all
                // zeros we can discard it.  The last two digits are always useless (the
                // next version of the spec shortens the field to 9 characters) so we
                // can ignore them.

                // Naturally, some Texas licences ignore the spec and just use 5
                // characters if they don't have a +4 section.
                if (license.getPostal().length() > 5) {
                    System.out.println(license.getPostal());
                    String zip = license.getPostal().substring(0, 5);
                    String plus4 = license.getPostal().substring(5);
                    System.out.println(zip.length());
                    System.out.println(plus4.length());
                    if (plus4.equals("0000")) {
                        license.setPostal(zip);
                        System.out.println(plus4);
                        System.out.println(zip);
                    } else {
                        license.setPostal(zip + "+" + plus4);
                    }
                }
            }
            if (license.getCountry().length() > 0) {
                if (dateofBirth != null)
                {
                    license.setDateOfBirth(parseDateV3(dateofBirth, license.getCountry()));
                    System.out.println(license.getDateOfBirth());
                }
                if (expiryDate != null)
                {
                    license.setExpiryDate(parseDateV3(expiryDate,license.getCountry()));
                    System.out.println(license.getExpiryDate());
                }
                if (issueDate != null)
                {
                    license.setIssueDate(parseDateV3(issueDate,license.getCountry()));
                    System.out.println(issueDate);
                    System.out.println(license.getIssueDate());
                }

                return license;
            }
        }
        return null;
    }
    public static Date parseDateV3(String dateString,String Country)
    {
        SimpleDateFormat datefm;

        if(Country.equals("US"))
        {

              datefm = new SimpleDateFormat("MMddyyyy");

        }
        else
        {
            datefm = new SimpleDateFormat("yyyyMMdd");
        }
        try {
            return datefm.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
