package my.cart.common;

/**
 * Created by shahid Akhtar on 10-02-2017.
 */

public class Utility {
    public static final String BASE_URL="https://cart.coderswebsites.com/";
    public static final String PHONE_NUMBER="PHONE_NUMBER";

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
  /*
  Basic authentication should be present in every request.
Username: abstract Password: anonymous

1.  Registration
     POST https://cart.coderswebsites.com/registration/
     Parameters: phone (phone no with country code)
2.  Verification
     POST https://cart.coderswebsites.com/verification/
     Parameters: phone, code (code received through sms)
3.  Create cart for registered member.
     POST https://cart.coderswebsites.com/carts/
     Parameters: name, ownerId(the memberId of one who creates the cart)
4.  Get carts associated with a member
     GET https://cart.coderswebsites.com/members/(memberId)/carts/?page=(pageno)

Please find the attached screenshots. Let me know for any concerns.
  * */

}
