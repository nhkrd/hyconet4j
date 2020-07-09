//
// Test for HCEX HTTPRequestAPI
//
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.io.IOException;
import java.io.*;

import jp.or.nhk.rd.hyconet4j.TVRCStatus;
import jp.or.nhk.rd.hyconet4j.TVRCDevinfo;
import jp.or.nhk.rd.hyconet4j.SSDPCP;
import jp.or.nhk.rd.hyconet4j.TVRC;
import jp.or.nhk.rd.hyconet4j.ActHCEX;
import jp.or.nhk.rd.hyconet4j.HCListener;

// TestCaseCheck
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

public class TestHCEXHTTPRequest {



	// TODO

	// テスト対象の設定devinfo（スタブで用意する）: DirectSelct実行

	// 各APIのスタブテスト

	/*
	// testHTTPRequstMethod
	// デバイスAPIのリクエストのためのHTTPMethodのテスト
	// パラメター: http_method/body
	// テスト内容: ステータスコードがリクエストの結果通りに入るかどうか
	*/
	@Test
	public void testHTTPRequestGetOK() throws Exception {
		ActHCEX hcex = new ActHCEX();
		TVRCDevinfo tvdev = new TVRCDevinfo("HCXPGenericTVRC", "example.com","");
		final String requrl = "http://example.com";
		TVRCStatus stobj = (TVRCStatus) this.doPrivateMethod(
			hcex, "HTTPRequest", new Class[]{String.class, TVRCDevinfo.class, String.class, String.class},new Object[]{"GET", tvdev, requrl, ""});
		//System.out.println(stobj.body);
		//System.out.println(stobj.err);
		assertThat(stobj.status , is(200));
		assertThat(stobj.body, is(not("")));
		assertThat(stobj.err, is(""));
	}

	@Test
	public void testHTTPRequestGet404() throws Exception {

		ActHCEX hcex = new ActHCEX();
		TVRCDevinfo tvdev = new TVRCDevinfo("HCXPGenericTVRC", "example.com","");
		final String requrl = "http://example.com/not_found";
			TVRCStatus stobj = (TVRCStatus) this.doPrivateMethod(
				hcex, "HTTPRequest", new Class[]{String.class, TVRCDevinfo.class, String.class, String.class},new Object[]{"GET", tvdev, requrl, ""});
		System.out.println(stobj.body);
		//System.out.println(stobj.err);
		assertThat(stobj.status , is(404));
		assertThat(stobj.err, is(not("")));
	}

	/**
     * 非staticメソッド呼び出し.
     *
     * @param obj テスト対象オブジェクト
     * @param name テスト対象メソッド名
     * @param types テスト対象メソッドの引数の型
     * @param args テスト対象メソッドの引数
     * @return 非staticメソッドの戻り値
     */

	 private Object doPrivateMethod (
		 Object obj, String name, Class[] parameterTypes, Object[] args) throws Exception {
			 Method method = obj.getClass().getDeclaredMethod(name, parameterTypes);
			 method.setAccessible(true);
			 Object objst = method.invoke(obj, args);

			 //System.out.println("doPrivateMethod: " + name);
			 //System.out.println(objst.getClass().getDeclaredField("err").get(objst));

			 return objst;
		}




	/*
	// testGetMedia
	// 放送受信可能なメディアの可否の情報取得
	// スタブ設定: URLはテスト対象設定から取得
	// queryパラメターを数パターン用意
		// queryなし: 200 正しいスキーマ
		// media=: 400エラー
		// media=TD: 200 正しいスキーマ
	*/
	/*
	public void testHTTPRequestGetOK throw Exception {
		final String ipaddr = "192.168.1.1";
		final String appurl = "http://" + ipaddr;
		TVRCDevinfo tvdev = new TVRCDevinfo("HCEXProtocol", ipaddr, "", "" , appurl);
	*/

	/*
	// testGetReceiverStatus
	// 受信機の情報を取得するAPIのテスト
	// スタブ設定: テスト対象設定devinfo
	// パラメターなし
	// テスト内容: APIのリターンの成功時・失敗時のエラー判定
	*/



	private static boolean isListenerRegisted(TVRCDevinfo dev, String listenerName) throws Exception{
		boolean isRegisted = false;
		List<String> ListenerList = dev.getListenerList();
		for( int i=0; i < ListenerList.size(); i++ ) {
			String lisName = (String)ListenerList.get(i);
			System.out.print("Listener: " + i + "  Name: " + lisName + "\n");
			if(lisName.equals(listenerName)) isRegisted = true;
		}
		return isRegisted;
	}



}
