package networking.contact;

public class Constant {
	public static final int		SERVER_LISTENING_PORT	= 13935;
	public static final int		CLIENT_LISTENING_PORT	= 27365;
	
	/**/
	public static final int		RESPONSE_CODE_OK		= 123;
	public static final int		RESPONSE_CODE_ERROR		= 250;
	
	public static final char	PORT_START				= '[';
	public static final char	PORT_END				= ']';
	
	/*Command: Client to Server*/
	public static final String	CMD_GET_HELP			= "$HELP";
	public static final String	CMD_GET_ONLINE_USER		= "$GET_ONLINE_USER";
	public static final String	CMD_GET_CONN_TO_2_PRE	= "$CONNECT_TO[";
	public static final String	CMD_GET_CONN_TO_2_END	= "]";
	public static final String	CMD_GET_CONN_TO_2		= "$CONNECT_TO[XXX]";
	public static final String	DISCONNECT_MESSAGE		= "$BYE";
	public static final String	CMD_WHAT_IS_SESSION_ID	= "$WHAT_IS_MY_ID?";
	/*Response: Server to Client*/
	public static final String	BAD_CMD_ERROR			= "Command Error!!!";
	
	public static final String	ONLINE_USERS_HEADER		= "#ONLINE_USERS:";
	public static final String	ONLINE_USERS			= "#ONLINE_USERS:{User_code=AddressInfo[,User_code=AddressInfo]...}";
	public static final String	CONN_TO_2_RESULT_HEADER	= "#CONN_REQ_RES:";
	public static final String	CONN_TO_2_RESULT		= "#CONN_REQ_RES:(RESPONSE_CODE)[XXX]{Message}";
	public static final String	CONN_TO_2_OK			= "#CONN_REQ_RES:(123)[XXX]{Address Info}";
	public static final String	CONN_TO_2_ERROR			= "#CONN_REQ_RES:(250)[XXX]{Error Message}";
	public static final String	RES_SESSION_ID_HEADER	= "#YOU_ID_IS:";
	public static final String	RES_SESSION_ID			= "#YOU_ID_IS:[XXX]";
	
	/*Command: Server to Client*/
	public static final String	CMD_WHAT_IS_YOUR_PORT	= "$WHAT_IS_YOUR_PORT?";
	/*Response: Client to Server*/
	public static final String	RES_LISTEN_PORT_HEADER	= "#MY_PORT_IS:";
	public static final String	RES_LISTEN_PORT			= "#MY_PORT_IS:[XXX]";
	
	/*Command: Client to Client*/
	public static final String	CMD_WHAT_IS_YOUR_ID		= "$WHAT_IS_YOUR_ID?";
	/*Response: Client to Client*/
	public static final String	RES_MY_ID_HEADER		= "#MY_ID_IS:";
	public static final String	RES_MY_ID				= "#MY_ID_IS:[XXX]";
}
