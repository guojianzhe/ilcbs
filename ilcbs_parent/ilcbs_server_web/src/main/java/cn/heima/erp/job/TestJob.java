package cn.heima.erp.job;

import java.text.ParseException;
import java.util.Date;

import cn.heima.utils.UtilFuns;

public class TestJob {

	public void showTime() throws ParseException {
		System.out.println(UtilFuns.dateTimeFormat(new Date(),"HH:mm:ss"));
	}
}
