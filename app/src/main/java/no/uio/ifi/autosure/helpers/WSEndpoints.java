package no.uio.ifi.autosure.helpers;

/**
 * Simple enum to manage WS endpoints
 */
public enum WSEndpoints {

    LOGIN("login"),
    LOGOUT("logout"),
    CUSTOMER_INFO("getCustomerInfo"),
    CLAIM_LIST("listClaims"),
    CLAIM_INFO("getClaimInfo"),
    CLAIM_MESSAGES("listClaimMessages"),
    NEW_CLAIM("submitNewClaim"),
    NEW_MESSAGE("submitNewMessage"),
    PLATE_LIST("listPlates");

    private String methodName;

    WSEndpoints(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

}
