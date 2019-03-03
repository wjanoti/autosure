package no.uio.ifi.autosure;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
     * @param args argument list
     *
     * @return response body
     *
     * @throws Exception
     */
    private static String makeRequest(String method, String... args) throws Exception{
        // create the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        SoapObject request = new SoapObject(NAMESPACE, method);
        int paramCounter = 0;
        for(String arg : args){
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

    public static int login(String username, String password) throws Exception {
        final String METHOD_NAME = "login";

        return Integer.parseInt(makeRequest(METHOD_NAME, username, password));
    }

    public static boolean logout(int sessionId) throws Exception {
        final String METHOD_NAME = "logout";

        String response = makeRequest(METHOD_NAME, Integer.toString(sessionId));

        return response.equals("true");
    }

    public static Customer getCustomerInfo(int sessionId) throws Exception {
        final String METHOD_NAME = "getCustomerInfo";
        String jsonResult = makeRequest(METHOD_NAME, Integer.toString(sessionId));
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            String customerName = jsonRootObject.getString("name");
            if (customerName == null || customerName.equals("")) return null;
            String userName = jsonRootObject.getString("username");
            String address      = jsonRootObject.optString("address");
            String dateOfBirth  = jsonRootObject.getString("dateOfBirth");
            int fiscalNumber    = Integer.parseInt(jsonRootObject.getString("fiscalNumber"));
            int policyNumber    = Integer.parseInt(jsonRootObject.optString("policyNumber"));

            return new Customer(userName, customerName, address, dateOfBirth, fiscalNumber, policyNumber);
        }  catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getCustomerInfo - JSONResult:" + jsonResult);
        }
        return null;
    }
}
