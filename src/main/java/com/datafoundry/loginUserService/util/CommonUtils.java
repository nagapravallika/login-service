package com.datafoundry.loginUserService.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datafoundry.loginUserService.repository.AuditEntityRepository;

@Component
public class CommonUtils {
	
    @Autowired
    AuditEntityRepository auditEntityRepository;
	
    public static String difference(String oldValue, String newValue)
	{
		String difference = ""; 
		String[] oldValueTokens = oldValue.split("~");
		String[] newValueTokens = newValue.split("~");
		
		for(int i=0; i<oldValueTokens.length && i<newValueTokens.length; i++)
		{
			if(!oldValueTokens[i].equals(newValueTokens[i]))
			{
				System.out.println( oldValueTokens[i]);
				System.out.println( newValueTokens[i]);
				String[] oldValueSplit = oldValueTokens[i].split("=");
				String[] newValueSplit = newValueTokens[i].split("=");
				difference += "\""+oldValueSplit[0].trim() +"\" :";
				
				difference += (oldValueSplit.length)>1?"{ \"oldValue\":\""+ oldValueSplit[1]+"\",":"{ \"oldValue\":\"\",";
				difference += (newValueSplit.length>1)?"\"newValue\":\""+newValueSplit[1]+"\"}," : "\"newValue\":\"\"},";
				
			}
		}
		
		//Remove the last occurrence of comma
		int ind = difference.lastIndexOf(",");
		if( ind>=0 )
			difference = new StringBuilder(difference).replace(ind, ind+1,"").toString();
		
		return "{"+difference+"}";
	}    
}
