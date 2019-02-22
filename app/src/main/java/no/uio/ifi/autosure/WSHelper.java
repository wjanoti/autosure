package no.uio.ifi.autosure;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

class WSHelper {

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

    static int login(String username, String password) throws Exception {
        final String METHOD_NAME = "login";

        return Integer.parseInt(makeRequest(METHOD_NAME, username, password));
    }

}
