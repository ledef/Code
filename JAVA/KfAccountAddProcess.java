package com.lanyuan.msg.wechat.send.client;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kuiren.common.easyui.RetData;
import com.lanyuan.msg.wechat.domain.send.resp.ISendRespMessage;
import com.lanyuan.msg.wechat.engine.Constants;
import com.lanyuan.msg.wechat.pubnum.ISendProcess;
import com.lanyuan.msg.wechat.send.util.WechatUtil;
import com.lanyuan.msg.wechat.util.AccessToken;
import com.lanyuan.msg.wechat.util.HttpUtil;
import com.lanyuan.msg.wechat.util.WeixinInterfaceUtil;

public class KfAccountAddProcess implements ISendProcess {
	protected final Log logger = LogFactory.getLog(getClass());

	

	@Override
	public String getRequestAddr() { 
		return WeixinInterfaceUtil.ADD_KFACCOUNT;
	}

	@Override
	public boolean validateParams(Map<String, Object> map) {
		boolean flag = false;
		// map中是否存在客服帐号数据
		boolean ifKfAccount = map.containsKey(Constants.KF_ACCOUNT);
		// map中是否存APPID
		boolean ifAppId = map.containsKey(Constants.APPID);
		// map中是否存在APPSECRET
		boolean ifAppSecret = map.containsKey(Constants.APPSECRET);
		
		if (ifKfAccount && ifAppId && ifAppSecret) {
			flag = true;
		} 
		
		return flag;
	}

	@Override
	public String convertReqData(Map<String, Object> map) {
		// 将map中客服帐号的json数据取出（含帐号，昵称和MD5加密的密码）
		return map.get(Constants.KF_ACCOUNT).toString();
	}

	@Override
	public Object send(Map<String, Object> map, String data) {
		String appid = map.get(Constants.APPID).toString();
		String appsecret = map.get(Constants.APPSECRET).toString();
		RetData retMsg = new RetData();
		boolean flag = false;
		String result = "";

		AccessToken accessToken = WechatUtil.getAccessToken(appid, appsecret);

		// 拼装添加客服帐号接口的url
		String url = getRequestAddr().replace("ACCESS_TOKEN", accessToken.getToken());
		// 调用接口添加客服帐号
		JSONObject jsonObject = HttpUtil.httpRequestJSONObject(url, "POST", data);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.get("errcode").toString();
				if (logger.isDebugEnabled()) {
					logger.debug("添加客服帐号失败,错误代码:" + result 
							+ ";错误信息:" + jsonObject.getString("errmsg"));
				}
			} else { 
				flag = true;
			}
		}
		retMsg.setData(result);
		retMsg.setSuccess(flag);

		return retMsg; 
	}

	@Override
	public ISendRespMessage convertResult(Map<String, Object> map, Object data) {
		// TODO Auto-generated method stub
		return null;
	}


}
