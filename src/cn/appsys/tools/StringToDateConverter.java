package cn.appsys.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String,Date> {
		private String datePatten;
		public StringToDateConverter(String datePatten){
			System.out.println("StringToDateConverter convert:"+datePatten);
			this.datePatten=datePatten;
		}

		public Date convert(String s) {
			// TODO Auto-generated method stub
			Date date=null;
			try {
				date=new SimpleDateFormat(datePatten).parse(s);
				System.out.println("StringToDateConverter convert date:"+date);
			} catch (ParseException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return date;
		}
		
}
