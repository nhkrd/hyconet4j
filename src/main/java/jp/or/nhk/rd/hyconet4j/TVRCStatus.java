package jp.or.nhk.rd.hyconet4j;

import org.json.JSONjava.JSONObject;

/**
 * APIが返す処理ステータス
 *
 */
public class TVRCStatus  {
	/**
	 * 通信(処理)ステータスコード
	 */
	public int status;

	/**
	 * 通信レスポンス
	 */
	public String body;

	/**
	 * 通信時のエラーコード・メッセージ
	 */
	public String err;

	/**
	 * ステータスコード
	 */
	public enum Status {
		/**
		 * 値：200、OK
		 */
		OK(200),
		/**
		 * 値：201、Created
		 */
		Created(201),

		/**
		 * 値：300、AlreadyActive
		 */
		AlreadyActive(300),

		/**
		 * 値：400、Bad Request
		 */
		BadRequest(400),
		/**
		 * 値：401、UnAuthorized
		 */
		UnAuthorized(401),
		/**
		 * 値：403、Forbidden
		 */
		Forbidden(403),

		/**
		 * 値：500、InternalError
		 */
		InternalError(500),
		/**
		 * 値：501、NotImplemented
		 */
		NotImplemented(501),
		/**
		 * 値：503、Service Unavailable
		 */
		ServiceUnavailable(503),

		/**
		 * 値：50200、OK (Internal Processing)
		 */
		OKInternalProcessing(50200),

		/**
		 * 値：50300、AlreadyActive (Internal Processing)
		 */
		AlreadyActiveInternalProcessing(50300),

		/**
		 * 値：50400、Bad Request (Internal Processing)
		 */
		BadRequestInternalProcessing(50400),

		/**
		 * 値：50500、Deny (Internal Processing)
		 */
		DenyInternalProcessing(50500)
		;

		private final int id;
		private Status(final int id) {
			this.id = id;
		}

		/**
		 * 値を取得する
		 *
		 * @return ステータスの値
		 */
		public int code() {
			return this.id;
		}
	}

	public TVRCStatus() {
		this.status = Status.BadRequest.code() ;	//default "Bad Request"
		this.body = "";
		this.err = "";
	}

	public void setStatus(int status, String body, String err ) {
		this.status = status;
		this.body = body;
		this.err = err;
	}

	public String toJSONString() {
		JSONObject res = new JSONObject();
		res.put("status", this.status);
		res.put("body",   this.body);
		res.put("error",  this.err);
		return res.toString();
	}
}
