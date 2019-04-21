package br.unifesp.maritaca.mobile.thread;

import static br.unifesp.maritaca.mobile.util.Constants.OAUTH_CLIENT_ID;
import static br.unifesp.maritaca.mobile.util.Constants.OAUTH_CLIENT_SECRET;
import static br.unifesp.maritaca.mobile.util.Constants.OAUTH_REDIRECT_URI;
import static br.unifesp.maritaca.mobile.util.Constants.OAUTH_RESPONSE_TYPE;
import static br.unifesp.maritaca.mobile.util.Constants.URI_ACCESSTOKEN_REQUEST;
import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.message.types.GrantType;
import net.smartam.leeloo.common.message.types.ResponseType;
import br.unifesp.maritaca.mobile.activities.LoginActivity;
import br.unifesp.maritaca.mobile.activities.OAuthWebViewClient;
import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import android.os.AsyncTask;
import android.util.Log;

public class OAuthClientThread extends AsyncTask<String, Integer, Boolean> {

	private static final String USER_PARAM = "user";
	
	@Override
	protected Boolean doInBackground(String... params) {
		try {
			if (params.length != 1) {
				Log.e(this.getClass().getName(), "Only one code is allowed");
				throw new RuntimeException("Only one code is allowed");
			}
			MaritacaHelper sqliteHelper = new MaritacaHelper(LoginActivity.getContext());
			String code = params[0];
			OAuthClientRequest request;
				request = OAuthClientRequest
						.tokenLocation(URI_ACCESSTOKEN_REQUEST)
						.setGrantType(GrantType.AUTHORIZATION_CODE)
						.setClientId(OAUTH_CLIENT_ID)
						.setClientSecret(OAUTH_CLIENT_SECRET)
						.setRedirectURI(OAUTH_REDIRECT_URI).setCode(code )
						.setParameter(OAUTH_RESPONSE_TYPE, ResponseType.TOKEN.toString())
						.buildBodyMessage();
	
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			
			Log.d(OAuthWebViewClient.class.getName(),
					"Retrieving access token from: "+request.getLocationUri());
			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient
					.accessToken(request, OAuthJSONAccessTokenResponse.class);
	
			Log.i(OAuthWebViewClient.class.getName(), 
					"Received token: " + oAuthResponse.getAccessToken()	+ 
					", expires in: "   + oAuthResponse.getExpiresIn() + 
					" seconds.");
			
			sqliteHelper.saveUserData(oAuthResponse.getParam(USER_PARAM), oAuthResponse.getAccessToken(), oAuthResponse.getRefreshToken(), Integer.parseInt(oAuthResponse.getExpiresIn()));
			sqliteHelper.onClose();
			return true;
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
}
