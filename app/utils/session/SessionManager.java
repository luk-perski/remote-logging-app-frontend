package utils.session;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import controllers.AuthenticationController;
import models.db.user.Role;
import models.db.user.User;
import play.mvc.Http;
import play.mvc.Http.Session;
import pt.iscte_iul.gdsi.utils.TypeUtils;
import utils.session.exception.InvalidIDSessionParameterException;
import utils.session.exception.InvalidRoleDataException;

/**
 * Helper class for dealing with session elements
 * 
 * @author alsl
 * 
 */
@Singleton
public class SessionManager {

	// Session parameters
	public static final String ID = "id";
	private static final String ADMIN_USER = "au";

	// Default values
	private static final String EMPTY_VALUE = "";

	@Inject
	private TypeUtils type_utils;

	/**
	 * Check if user is authenticated (by checking if there's a valid session ID)
	 * 
	 * @param request
	 *            The associated request
	 * @return true if authenticated, false if not
	 */
	public boolean isAuthenticated(Http.RequestHeader request) {
		String auth_user_id = getAuthenticatedUserID(request);
		return auth_user_id != null && !auth_user_id.isEmpty();
	}

	/**
	 * Get the authenticated user ID
	 * 
	 * @param request
	 *            The associated request
	 * @return the authenticated user ID (if set), or null otherwise
	 */
	public String getAuthenticatedUserID(Http.RequestHeader request) {
		return getAuthenticatedUserSessionParameter(request.session(), ID);
	}

	public User getAuthenticatedUser(Http.RequestHeader request) {
		return User.getByID(this.type_utils.getLongValue(this.getAuthenticatedUserID(request)));
	}

	public Role getAuthenticatedRole(Http.RequestHeader request) {
		return Role.getByID(this.type_utils.getIntegerValue(this.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY)));
	}

	/**
	 * Get the authenticated user's session parameter
	 * 
	 * @param request
	 *            The associated request
	 * @param parameter
	 *            The key of the session parameter
	 * @return A string with the user's session parameter value. Null if no user is currently in session or the parameter doesn't exist in session
	 */
	public String getAuthenticatedUserSessionParameter(Http.RequestHeader request, String parameter) {
		return getAuthenticatedUserSessionParameter(request.session(), parameter);
	}

	/**
	 * Get the authenticated user's session parameter
	 * 
	 * @param session
	 *            The associated session
	 * @param parameter
	 *            The key of the session parameter
	 * @return A string with the user's session parameter value. Null if no user is currently in session or the parameter doesn't exist in session
	 */
	public String getAuthenticatedUserSessionParameter(Session session, String parameter) {
		return session.get(parameter).orElse(null);
	}

	/**
	 * Set the value for a session parameter of the authenticated user
	 * 
	 * @param request
	 *            The associated request
	 * @param parameter_key
	 *            The key of the session parameter
	 * @param parameter_value
	 *            The value of the session parameter
	 * @return A new session with parameter added
	 */
	public Http.Session setAuthenticatedUserSessionParameter(Http.Request request, String parameter_key, String parameter_value) {
		return setAuthenticatedUserSessionParameter(request.session(), parameter_key, parameter_value);
	}

	/**
	 * Set the value for a session parameter of the authenticated user
	 * 
	 * @param session
	 *            The associated session
	 * @param parameter_key
	 *            The key of the session parameter
	 * @param parameter_value
	 *            The value of the session parameter
	 * @return A new session with parameter added
	 */
	public Http.Session setAuthenticatedUserSessionParameter(Session session, String parameter_key, String parameter_value) {
		if (parameter_key != null && parameter_value != null && !parameter_key.trim().isEmpty() && !parameter_value.trim().isEmpty()) {
			return session.removing(parameter_key).adding(parameter_key, parameter_value);
		}
		return session;
	}

	/**
	 * Resets the session, equivalent to logout
	 * 
	 * @param session
	 *            The previously set session
	 * @return A new empty session
	 */
	public Session resetSession(Http.Session session) {
		// Create a new session to start from scratch
		Http.Session new_session = new Http.Session(new HashMap<String, String>());

		return new_session;
	}

	/**
	 * Load a new user session
	 * 
	 * @param session_parameters
	 *            The session parameters to be loaded
	 * @param previous_session
	 *            The associated previous session (if any)
	 * @throws InvalidIDSessionParameterException
	 *             When there's no ID session parameter in the list of session parameters
	 */
	public Http.Session loadUserSession(Map<String, String> session_parameters, Http.Session previous_session) throws InvalidIDSessionParameterException {

		// save the admin user's ID (if any) to be set afterwards
		String admin_id = getAdminUser(previous_session);

		// Create a new session to start from scratch
		Http.Session new_session = new Http.Session(new HashMap<String, String>());

		// Load the previously set admin_user (if any)
		if (admin_id != null) {
			new_session = setAdminUser(admin_id, new_session);
		}

		// Set the remaining user session parameters
		if (session_parameters != null) {
			// Check if the mandatory ID parameter is in the list
			if (!sessionIDParameterIsValid(session_parameters)) {
				// If not throw corresponding exception
				throw new InvalidIDSessionParameterException();
			}

			// Iterate through all session parameters and set them
			for (String parameter : session_parameters.keySet()) {
				String value = session_parameters.get(parameter);
				// Check if value of the session parameter is null...
				if (value != null) {
					new_session = new_session.adding(parameter, value);
				} else {
					// ...because if it is, set to a specific string representing the null value
					new_session = new_session.adding(parameter, EMPTY_VALUE);
				}
			}
		}

		return new_session;
	}

	// Util function to check if the session parameters include a valid ID parameter
	private boolean sessionIDParameterIsValid(Map<String, String> session_parameters) {
		if (session_parameters != null) {
			if (session_parameters.containsKey(ID)) {
				String value = session_parameters.get(ID);
				return value != null && !value.trim().isEmpty();
			}
		}
		return false;
	}

	/**
	 * Load a user session from an Administrator account
	 * 
	 * @param admin_id
	 *            The ID of the administrator performing the access
	 * @param session_parameters
	 *            The remaining session parameters to be set for the user
	 * @param request
	 *            The associated request
	 * @throws InvalidIDSessionParameterException
	 *             When any of the provided session parameters are invalid
	 */
	public Http.Session loadUserSessionAsAdmin(String admin_id, Map<String, String> session_parameters, Http.Request request) throws InvalidIDSessionParameterException {
		// First set admin user
		Http.Session session = setAdminUser(admin_id, request.session());
		// Then, load user session
		return loadUserSession(session_parameters, session);
	}

	/**
	 * Check if user is authenticated with a certain role
	 * 
	 * @param role_key
	 *            The session key used to store the role
	 * @param role_id
	 *            The role to be checked
	 * @param request
	 *            The associated request
	 * @return true if the current session parameter for the role is equal to the role provided, false otherwise
	 * @throws InvalidRoleDataException
	 *             If the provided arguments for role are null
	 */
	public boolean isAuthenticatedAs(String role_key, String role_id, Http.Request request) throws InvalidRoleDataException {
		if (request != null) {
			return isAuthenticatedAs(role_key, role_id, request.session());
		}
		throw new InvalidRoleDataException();
	}

	public boolean isAuthenticatedAs(String role_key, String role_id, Session session) throws InvalidRoleDataException {
		if (role_key == null || role_id == null) {
			throw new InvalidRoleDataException();
		}

		String role = getAuthenticatedUserSessionParameter(session, role_key);
		if (role == null) {
			return false;
		}

		return role.equals(role_id);
	}

	/**
	 * Check if user in session is in Super User Mode (if is Administrator)
	 * 
	 * @return true if Yes, false if not
	 */
	public boolean isInAdminUserMode(Http.RequestHeader request) {
		String admin_user = getAuthenticatedUserSessionParameter(request, ADMIN_USER);
		return admin_user != null && !admin_user.isEmpty();
	}

	/**
	 * Reset the session back to the Admin user
	 * 
	 * @param session_parameters
	 *            The session parameters to load back the session for Admin user
	 * @throws InvalidIDSessionParameterException
	 *             inherited from loadUserSession
	 */
	public Http.Session resetAdminUserMode(Map<String, String> session_parameters) throws InvalidIDSessionParameterException {
		// Force reset of session to clear everything
		Http.Session new_session = new Http.Session(new HashMap<String, String>());
		// Load user as Admin
		return loadUserSession(session_parameters, new_session);
	}

	/**
	 * Get the Admin user's ID currently in session
	 * 
	 * @param session
	 *            The current session
	 * @return The Admin user's ID currently in session
	 */
	public String getAdminUser(Http.Session session) {
		return session.get(ADMIN_USER).orElse(null);
	}

	/**
	 * Set the Admin user
	 * 
	 * @param admin_id
	 *            The admin user's ID to be set
	 * @param session
	 *            The current session
	 * @return the session with the admin parameter added
	 * @throws InvalidIDSessionParameterException
	 *             If the Admin user's ID is null
	 */
	private Http.Session setAdminUser(String admin_id, Http.Session session) throws InvalidIDSessionParameterException {

		if (admin_id == null) {
			throw new InvalidIDSessionParameterException();
		}

		return session.adding(ADMIN_USER, admin_id.toString());
	}
}