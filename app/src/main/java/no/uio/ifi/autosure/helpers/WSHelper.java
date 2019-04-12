package no.uio.ifi.autosure.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import no.uio.ifi.autosure.models.Claim;
import no.uio.ifi.autosure.models.ClaimItem;
import no.uio.ifi.autosure.models.ClaimMessage;
import no.uio.ifi.autosure.models.Customer;

public class WSHelper {

    private static final String TAG = "WSHelper";
    private static final String NAMESPACE = "http://pt.ulisboa.tecnico.sise.autoinsure.ws/";
    private static final String URL = "http://10.0.2.2:8080/AutoInSureWS?WSDL";
    private static final String SERVICE_NAME = "AutoInsureWS";

    /**
     * Generic reusable method to make SOAP requests.
     *
     * @param method method name
     * @param args   argument list
     * @return response body
     * @throws Exception
     */
    private static String makeRequest(String method, String... args) throws Exception {
        // create the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // add request params
        SoapObject request = new SoapObject(NAMESPACE, method);
        int paramCounter = 0;
        for (String arg : args) {
            request.addProperty("arg" + paramCounter++, arg);
        }
        envelope.setOutputSoapObject(request);

        // perform the request
        int TIMEOUT = 4000;
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, TIMEOUT);
        String actionString = "\"" + NAMESPACE + SERVICE_NAME + "/" + method + "\"";
        androidHttpTransport.call(actionString, envelope);

        // obtain the result
        SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();

        return resultsRequestSOAP.toString();
    }

    /**
     * Logs an user in the app
     *
     * @param username name of the user to log in
     * @param password password of the user to log in
     * @return int session id
     * @throws Exception
     * @see no.uio.ifi.autosure.tasks.LoginTask
     */
    public static int login(String username, String password) throws Exception {
        return Integer.parseInt(makeRequest(WSEndpoints.LOGIN.getMethodName(), username, password));
    }

    /**
     * Logs an user out of the app
     *
     * @param sessionId session id of the logged in user
     * @return boolean indicating if the operation was successful or not
     * @throws Exception
     * @see no.uio.ifi.autosure.tasks.LogoutTask
     */
    public static boolean logout(int sessionId) throws Exception {
        String response = makeRequest(WSEndpoints.LOGOUT.getMethodName(), Integer.toString(sessionId));

        return response.equals("true");
    }

    /**
     * Fetches info about a single user
     *
     * @param sessionId session id of the user we want to fetch info from
     * @return Customer object containing customer info
     * @throws Exception
     * @see no.uio.ifi.autosure.tasks.CustomerInfoTask
     */
    public static Customer getCustomerInfo(int sessionId) throws Exception {
        String jsonResult = makeRequest(
            WSEndpoints.CUSTOMER_INFO.getMethodName(),
            Integer.toString(sessionId)
        );

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            String customerName = jsonRootObject.getString("name");
            if (customerName == null || customerName.equals("")) {
                return null;
            }

            String address = jsonRootObject.optString("address");
            String dateOfBirth = jsonRootObject.getString("dateOfBirth");
            int fiscalNumber = Integer.parseInt(jsonRootObject.getString("fiscalNumber"));
            int policyNumber = Integer.parseInt(jsonRootObject.optString("policyNumber"));

            return new Customer(customerName, address, dateOfBirth, fiscalNumber, policyNumber);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getCustomerInfo - JSONResult:" + jsonResult);
        }

        return null;
    }

    /**
     * List claims associated to a given user
     *
     * @param sessionId session id of the user
     * @return List of claims
     * @throws Exception
     */
    public static List<ClaimItem> getCustomerClaims(int sessionId) throws Exception {
        String jsonResult = makeRequest(
            WSEndpoints.CLAIM_LIST.getMethodName(),
            Integer.toString(sessionId)
        );

        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            ArrayList<ClaimItem> claimList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int claimId = Integer.parseInt(jsonObject.optString("claimId"));
                String claimTitle = jsonObject.optString("claimTitle");
                claimList.add(new ClaimItem(claimId, claimTitle));
            }

            return claimList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "listClaims - JSONResult:" + jsonResult);
        }

        return null;
    }

    /**
     * Get detailed info about a specific claim
     *
     * @param sessionId session id of the user who owns the claim
     * @param claimId id of the claim
     * @return Claim object containing claim data
     * @throws Exception
     */
    public static Claim getClaimInfo(int sessionId, int claimId) throws Exception {
        String jsonResult = makeRequest(
                WSEndpoints.CLAIM_INFO.getMethodName(),
                Integer.toString(sessionId),
                Integer.toString(claimId)
        );

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            int respClaimId = Integer.parseInt(jsonRootObject.getString("claimId"));
            String claimTitle = jsonRootObject.getString("claimTitle");
            String plate = jsonRootObject.optString("plate");
            String submissionDate = jsonRootObject.optString("submissionDate");
            String description = jsonRootObject.optString("description");
            String status = jsonRootObject.optString("status");

            return new Claim(respClaimId, claimTitle, submissionDate, plate, description, status);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getClaimInfo - JSONResult:" + jsonResult);
        }

        return null;
    }

    /**
     * Fetches list of plates associated to an user
     *
     * @param sessionId session id of the user
     * @return list of plates
     * @throws Exception
     */
    public static List<String> listPlates(int sessionId) throws Exception {
        String jsonResult = makeRequest(
                WSEndpoints.PLATE_LIST.getMethodName(),
                Integer.toString(sessionId)
        );
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            ArrayList<String> plateList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String plate = jsonArray.getString(i);
                plateList.add(plate);
            }

            return plateList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "listPlates - JSONResult:" + jsonResult);
        }

        return null;
    }

    /**
     * Submits a new claim from the user
     *
     * @param sessionId session id of the user
     * @param claimTitle title of the claim
     * @param occurrenceDate date of the occurrence
     * @param plate plate associated with the claim
     * @param claimDescription description
     * @return boolean indicating success or failure
     * @throws Exception
     */
    public static boolean submitNewClaim(int sessionId, String claimTitle, String occurrenceDate,
                                         String plate, String claimDescription) throws Exception {
        String res = makeRequest(WSEndpoints.NEW_CLAIM.getMethodName(), Integer.toString(sessionId),
                claimTitle, occurrenceDate, plate, claimDescription);

        return res.equals("true");
    }

    /**
     * List messages regarding a given claims
     *
     * @param sessionId session id of the user
     * @param claimId id of the claim
     * @return List of claim messages
     * @throws Exception
     */
    public static List<ClaimMessage> getClaimMessages(int sessionId, int claimId) throws Exception {
        String jsonResult = makeRequest(
                WSEndpoints.CLAIM_MESSAGES.getMethodName(),
                Integer.toString(sessionId),
                Integer.toString(claimId)
        );

        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            ArrayList<ClaimMessage> claimList = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                claimList.add(
                    new ClaimMessage(
                        jsonObject.optString("sender"),
                        jsonObject.optString("msg"),
                        jsonObject.optString("date")
                    )
                );
            }

            return claimList;
        }  catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "listClaimMessages - JSONResult:" + jsonResult);
        }

        return null;
    }

    /**
     * Sends a message regarding a claim
     *
     * @param sessionId session id of the user
     * @param claimId id of the claim
     * @param message message content
     * @return bool
     * @throws Exception
     */
    public static boolean sendClaimMessage(int sessionId, int claimId, String message) throws Exception {
        String jsonResult = makeRequest(
                WSEndpoints.NEW_MESSAGE.getMethodName(),
                Integer.toString(sessionId),
                Integer.toString(claimId),
                message
        );

        return jsonResult.equals("true");
    }

}
