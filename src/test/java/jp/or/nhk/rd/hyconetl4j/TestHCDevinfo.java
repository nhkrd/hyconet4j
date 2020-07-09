//
// Test for Devinfo
//
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.*;

import jp.or.nhk.rd.hyconet4j.TVRCStatus;
import jp.or.nhk.rd.hyconet4j.TVRCDevinfo;


import java.io.IOException;

import org.json.JSONjava.JSONObject;
import org.json.JSONjava.JSONArray;
import org.json.JSONjava.*;



import org.apache.commons.codec.binary.Base64; // Java7

import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;
// TestCaseCheck
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

public class TestHCDevinfo {

	@Test
    public void DevinfoExport() throws Exception {

		// given -- xUnit Naming TestFrame

		// まず通常通りDevinfo作成
		TVRCDevinfo tvDevInfo = new TVRCDevinfo("HCXPGenericTVRC", "192.168.0.99", "aaaa-bbbb-cccc-dddd", "http://192.168.0.99:8887/dd.xml", "http://192.168.0.99:8887/app/Hybridcast");

		// when -- xUnit Naming TestFrame

		// 外部保存用文字列出力
		String exportedStr = tvDevInfo.export();
		System.out.println("Devinfo JSON String: " + exportedStr);

		JSONObject devSession = new JSONObject(exportedStr);
		System.out.println("devSession JSON: " + devSession.toString());

		// Devinfo再作成
		TVRCDevinfo reGenDevInfo = new TVRCDevinfo(devSession);

		// then -- xUnit Naming TestFrame
		//// TestAssertion
		// Devinfoが再セットされているか検証(actual, expected)
		assertThat(exportedStr, is(devSession.toString()));
		assertThat(reGenDevInfo.ipaddr, is(tvDevInfo.ipaddr));
		assertThat(reGenDevInfo.uuid, is(tvDevInfo.uuid));
		assertThat(reGenDevInfo.location, is(tvDevInfo.location));
		assertThat(reGenDevInfo.applicationURL, is(tvDevInfo.applicationURL));
		assertThat(reGenDevInfo.sessInfo.appName, is(tvDevInfo.sessInfo.appName));
	}
	

	@Test
    public void DevinfoImortJsonString() throws Exception {

		// given -- xUnit Naming TestFrame

		// まず通常通りDevinfo作成
		TVRCDevinfo tvDevInfo = new TVRCDevinfo("HCXPGenericTVRC", "192.168.0.99", "aaaa-bbbb-cccc-dddd", "http://192.168.0.99:8887/dd.xml", "http://192.168.0.99:8887/app/Hybridcast");

		// when -- xUnit Naming TestFrame

		// 外部保存用文字列出力
		String exportedStr = tvDevInfo.export();
		System.out.println("Devinfo JSON String: " + exportedStr);

		JSONObject devSession = new JSONObject(exportedStr);
		System.out.println("devSession JSON: " + devSession.toString());

		// Devinfo再作成
		TVRCDevinfo reGenDevInfo = new TVRCDevinfo().load(devSession.toString());

		// then -- xUnit Naming TestFrame
		//// TestAssertion
		// Devinfoが再セットされているか検証(actual, expected)
		assertThat(exportedStr, is(devSession.toString()));
		assertThat(reGenDevInfo.ipaddr, is(tvDevInfo.ipaddr));
		assertThat(reGenDevInfo.uuid, is(tvDevInfo.uuid));
		assertThat(reGenDevInfo.location, is(tvDevInfo.location));
		assertThat(reGenDevInfo.applicationURL, is(tvDevInfo.applicationURL));
		assertThat(reGenDevInfo.sessInfo.appName, is(tvDevInfo.sessInfo.appName));

		System.out.println("devSession JSON params: " + reGenDevInfo.ipaddr);
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
			//System.out.println(objst.getClass());

			return objst;
	   }
}


